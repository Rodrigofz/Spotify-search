package cl.uchile.pmd;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.AlreadyBoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;

/**
 * Main method to index artist names and songs from Spotify
 *
 */
public class BuildSpotifyIndexBulk {

	public enum FieldNames {
		ARTIST, SONG, RANKING
	}
	public static int TICKS = 10000;

	public static int BATCH_SIZE = 250;

	public static void main(String args[]) throws IOException, ClassNotFoundException, AlreadyBoundException,
	InstantiationException, IllegalAccessException {
		Option inO = new Option("i", "input file");
		inO.setArgs(1);
		inO.setRequired(true);

		Option ingzO = new Option("igz", "input file is GZipped");
		ingzO.setArgs(0);

		Option outO = new Option("o", "output elasticsearch index name");
		outO.setArgs(1);
		outO.setRequired(true);

		Option helpO = new Option("h", "print help");

		Options options = new Options();
		options.addOption(inO);
		options.addOption(ingzO);
		options.addOption(outO);
		options.addOption(helpO);

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

		String indexName = cmd.getOptionValue("o");
		System.err.println("Indexing at  " + indexName);

		String in = cmd.getOptionValue(inO.getOpt());
		System.err.println("Opening input at  " + in);
		InputStream is = new FileInputStream(in);
		if (cmd.hasOption(ingzO.getOpt())) {
			is = new GZIPInputStream(is);
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));

		TransportClient client = ElasticsearchCluster.getTransportClient();

		indexTitleAndAbstract(br, client, indexName);

		br.close();

		client.close();
	}

	public static void indexTitleAndAbstract(BufferedReader input, TransportClient client, String indexName) throws IOException {
		String line = null;
		int read = 0;

		BulkRequestBuilder bulkJson = client.prepareBulk();
		// read each line from input (tab separated)
		// first element: Artist
		// second element: Song
		// third element: Ranking
		while ((line = input.readLine()) != null) {
			read++;
			line = line.trim();
			if (!line.isEmpty()) {
				String[] tabs = line.split("\t");
				// we store the JSON document as a map from
				// strings (keys) to objects (values)
				Map<String, Object> json = new HashMap<String, Object>();

				json.put(FieldNames.ARTIST.name(), tabs[0]);
				json.put(FieldNames.SONG.name(), tabs[1]);
				json.put(FieldNames.RANKING.name(), Double.parseDouble(tabs[2]));
				// add to bulk request
				bulkJson.add(client.prepareIndex(indexName, "_doc").setSource(json, XContentType.JSON));

				// if we have read a multiple of batch size
				if(read % BATCH_SIZE == 0) {
					// send the request (i.e., get the unused response of the request)
					bulkJson.get();
					bulkJson = client.prepareBulk();
				}
			}

			if (read % TICKS == 0) {
				System.err.println("... read " + read);
			}

		}

		// Try to indicate that the ranking is a double (I think this doesnt actually work)
		Map<String, Object> ranking_type = new HashMap<>();
		ranking_type.put("type", "double");
		Map<String, Object> properties = new HashMap<>();
		properties.put(FieldNames.RANKING.name(), ranking_type);
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("properties", properties);
		bulkJson.add(client.prepareIndex(indexName, "_doc").setSource(json, XContentType.JSON));

		// if we have something left in the batch to send ...
		if(bulkJson.numberOfActions() > 0) {
			// send the request (i.e., get the unused response of the request)
			bulkJson.get();
		}
		client.close();
	}
}