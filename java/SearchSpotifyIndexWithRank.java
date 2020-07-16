package cl.uchile.pmd;

import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.fieldValueFactorFunction;

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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.search.SearchHit;

/**
 * Main method to search songs using Elasticsearch.
 *
 */
public class SearchWikiIndexWithRank {
	
	public enum FieldNames {
		ARTIST, SONG, RANKING
	}

	// used to assign higher/lower ranking
	// weight to different document fields
	public static final HashMap<String, Float> BOOSTS = new HashMap<String, Float>();
	static {
		BOOSTS.put(FieldNames.ARTIST.name(), 1f);
		BOOSTS.put(FieldNames.SONG.name(), 1f);
	}

	public static final int DOCS_PER_PAGE = 20;

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
						FilterFunctionBuilder[] functions = {
						        new FunctionScoreQueryBuilder.FilterFunctionBuilder(
						        		QueryBuilders.multiMatchQuery(line,
												FieldNames.ARTIST.name(), FieldNames.SONG.name()).fields(BOOSTS),
						        		fieldValueFactorFunction(FieldNames.RANKING.name())) //<< This does the trick
						};
						
						QueryBuilder qb = QueryBuilders.functionScoreQuery(QueryBuilders.multiMatchQuery(line,
								FieldNames.ARTIST.name(), FieldNames.SONG.name()).fields(BOOSTS),functions);
						
						// here we run the search, specifying how many results
						// we want per "page" of results
						SearchResponse response = client.prepareSearch(indexName).setQuery(qb) // Query
								.setSize(DOCS_PER_PAGE).setExplain(true).get();
						
						// for each document in the results ...
						for (SearchHit hit : response.getHits().getHits()) {
							// get the JSON data per field
							Map<String, Object> json = hit.getSourceAsMap();
							String artist = (String) json.get(FieldNames.ARTIST.name());
							String song = (String) json.get(FieldNames.SONG.name()); 
							
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