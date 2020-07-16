package cl.uchile.pmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.AlreadyBoundException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

/**
 * Main method to search articles using Elasticsearch.
 * 
 * @author Aidan, Alberto
 */
public class SearchSpotifyIndex {
	
	// used to keep track of the names of fields
	// that we are using
	public enum FieldNames {
		ARTIST, SONG
	}

	public static final int DOCS_PER_PAGE = 10;

	public static void main(String args[]) throws IOException, ClassNotFoundException, AlreadyBoundException,
			InstantiationException, IllegalAccessException {
		Option inO = new Option("i", "input elasticsearch index name");
		inO.setArgs(1);
		inO.setRequired(true);

		Options options = new Options();
		options.addOption(inO);

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println("***ERROR: " + e.getClass() + ": " + e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("parameters:", options);
			return;
		}

		// print help options and return
		if (cmd.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("parameters:", options);
			return;
		}

		TransportClient client = ElasticsearchCluster.getTransportClient();
		
		String indexName = cmd.getOptionValue(inO.getOpt());
		System.err.println("Querying index at  " + indexName);

		startSearchApp(client,indexName);
		
		client.close();
	}

	/**
	 * 
	 * @param inDirectory : the location of the index directory
	 * @throws IOException
	 */
	public static void startSearchApp(TransportClient client, String indexName) throws IOException {
		// we open a UTF-8 reader over std-in
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "utf-8"));

		while (true) {
			System.out.println("Enter a keyword search phrase:");

			// read keyword search from user
			String line = br.readLine();
			if (line != null) {
				line = line.trim();
				if (!line.isEmpty()) {
					try {
						// we will use a multi-match query builder that
						// will allow us to match multiple document fields
						// (e.g., search over title and abstract)
						MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(line,
								FieldNames.SONG.name(), FieldNames.ARTIST.name());

						multiMatchQueryBuilder.field(FieldNames.SONG.name());
						multiMatchQueryBuilder.field(FieldNames.ARTIST.name());

						// here we run the search, specifying how many results
						// we want per "page" of results
						SearchResponse response = client.prepareSearch(indexName).setQuery(multiMatchQueryBuilder) // Query
								.setSize(DOCS_PER_PAGE).setExplain(true).get();
						
						// for each document in the results ...
						for (SearchHit hit : response.getHits().getHits()) {
							// get the JSON data per field
							Map<String, Object> json = hit.getSourceAsMap();
							String artist = (String) json.get(FieldNames.ARTIST.name());
							String song = (String) json.get(FieldNames.SONG.name());

							// print the details of the doc (title, url, abstract) to standard out
							System.out.println(artist + "\t" + song);
						}
					} catch (Exception e) {
						System.err.println("Error with query '" + line + "'");
						e.printStackTrace();
					}
				}
			}
		}
	}
}