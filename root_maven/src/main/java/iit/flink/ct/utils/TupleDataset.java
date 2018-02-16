/**
 * 
 */
package iit.flink.ct.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple5;

/**
 * @author Georgios Kechagias
 *
 */
public class TupleDataset {

	public static DataSet<Tuple5<String, String, String, Long, Long>> getDefaultDataSet(ExecutionEnvironment env,
			Communities[] data) {

		Tuple5<String, String, String, Long, Long> aTuple = new Tuple5<String, String, String, Long, Long>();

		long[][] aCommunity;

		List<Tuple5<String, String, String, Long, Long>> dataSet = new ArrayList<Tuple5<String, String, String, Long, Long>>();

		for (int window = 0; window < data.length; window++) {

			for (int community = 0; community < data[window].getCommunities().length; community++) {
				aCommunity = data[window].getCommunities()[community];

				for (int connection = 0; connection < aCommunity.length; connection++) {

					aTuple = new Tuple5<String, String, String, Long, Long>("timeframe_" + window,
							"community_" + community, "Timeframe" + window + "_Community" + community,
							aCommunity[connection][0], aCommunity[connection][1]);

					dataSet.add(aTuple);

				}
			}
		}
		return env.fromCollection(dataSet);
	}

	/**
	 * @param env
	 * @param data
	 * @return
	 */
	public static DataSet<String> getTimeFrameNamesDataset(ExecutionEnvironment env, Communities[] data) {

		List<String> names = new ArrayList<String>();

		for (int window = 0; window < data.length; window++) {
			names.add("timeframe_" + window);
		}

		return env.fromCollection(names);
	}

	public static List<String> getTimeFrameNames(Communities[] data) {

		List<String> names = new ArrayList<String>();

		for (int window = 0; window < data.length; window++) {
			names.add("timeframe_" + window);
		}

		return names;
	}

}
