/**
 * 
 */
package iit.flink.ct.statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import iit.flink.ct.utils.Communities;
import iit.flink.ct.utils.ReadJSONDataSet;

/**
 * 
 * Receives a social network dataset, and calculates its statistics in terms of number of communities, number of edges
 * per community, number of vertices per community, for all timeframes. It outputs a .json file which contains all the
 * relevant information. As the output is .json, it is easy to read and further process.
 * 
 * @author Georgios Kechagias
 *
 */
public class CalculateStatistics {

	private static int count_Connections = 0;

	private static TimeframeStats timeframe;

	private static CommunityStats community;

	private static List<CommunityStats> communities;

	private static List<TimeframeStats> timeframes = new ArrayList<>();

	@SuppressWarnings("rawtypes")
	private static HashSet mySet = new HashSet<>();

	private static Integer verticesCount = 0;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws JsonIOException, JsonSyntaxException, IOException {

		// some GSON instanciation
		GsonBuilder builder = new GsonBuilder().setPrettyPrinting().serializeNulls();
		Gson gson = builder.create();

		// create the main dataset object
		DatasetJSON djson = new DatasetJSON();

		// set datasetName
		djson.setDatasetName(args[0]);

		// get main dataset as an input
		Communities[] data = ReadJSONDataSet.readJSONDataSet(args[0]);

		// set total timeframes to the target object
		djson.setNumberOfTimeframes(data.length);

		// for each timeframe
		for (int i = 0; i < data.length; i++) {

			timeframe = new TimeframeStats();

			timeframe.setTimeFrameID("timeframe" + i);
			timeframe.setNumberOfCommunities(data[i].getCommunities().length);

			communities = new ArrayList<>();

			// for each community of a timeframe
			for (int j = 0; j < data[i].getCommunities().length; j++) {

				community = new CommunityStats();

				// update communities list, count total communities, and continue
				community.setCommunityID("Community" + j);
				community.setNumberOfEdges(data[i].getCommunities()[j].length);

				count_Connections += data[i].getCommunities()[j].length;
				// for each connection (edge) of a community
				for (int k = 0; k < data[i].getCommunities()[j].length; k++) {
					mySet.add(data[i].getCommunities()[j][k][0]);
					mySet.add(data[i].getCommunities()[j][k][1]);
				}

				Integer temp_number_vertices = mySet.size();

				verticesCount += temp_number_vertices;

				community.setNumberOfVertices(temp_number_vertices);
				mySet.clear();
				communities.add(community);

			}

			// update timeframe list with current timeframe data
			timeframe.setCommunities(communities);
			timeframe.setNumberOfVertices(verticesCount);
			verticesCount = 0;
			timeframe
					.setVerticesFraction((double) timeframe.getNumberOfVertices() / timeframe.getNumberOfCommunities());
			timeframes.add(timeframe);

		}

		djson.setTotalConnections(count_Connections);
		djson.setTimeframes(timeframes);

		// try-with-resources statement based on post comment below :)
		try (FileWriter file = new FileWriter("/home/uheq/Desktop/ct_flink/runCTFlink/outputs/statistics.json")) {
			file.write(gson.toJson(djson));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
