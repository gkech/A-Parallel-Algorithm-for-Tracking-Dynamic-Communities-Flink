/**
 * 
 */
package iit.flink.ct.root;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.core.fs.FileSystem.WriteMode;

import iit.flink.ct.userDefinedFunctions.CalculateJaccard;
import iit.flink.ct.userDefinedFunctions.FilterTimeFrame;
import iit.flink.ct.userDefinedFunctions.GatherIDs;
import iit.flink.ct.utils.Communities;
import iit.flink.ct.utils.ReadJSONDataSet;
import iit.flink.ct.utils.TupleDataset;

/**
 * @author Georgios Kechagias
 *
 */
public class CommunityTrackingExperiment {

	/**
	 * This method is the main experiment, and combines all the appropriate Apache Flink tasks required to successfully
	 * calculate the similarities of dynamic communities between consecutive timeframes. After calculating the
	 * similarities, the results are saved in a csv file which will be located in the specified output location.
	 * 
	 * @param jsonDatasetInput
	 * @param outputLocation
	 * @param timeframeCoeff
	 * @return execution time to complete the experiment
	 * @throws Exception
	 */
	public static Long execute(String jsonDatasetInput, String outputLocation, int timeframeCoeff) throws Exception {

		// much needed initializations
		List<DataSet<Tuple4<String, String, String, Long[]>>> timeframes = new ArrayList<DataSet<Tuple4<String, String, String, Long[]>>>();
		List<DataSet<Tuple5<String, String, String, String, Double>>> timeframesCross = new ArrayList<DataSet<Tuple5<String, String, String, String, Double>>>();
		List<String> timeframeNames;

		// flink's execution environment
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		/*
		 * Data Source Section
		 */

		// read Json Dataset
		Communities[] data = ReadJSONDataSet.readJSONDataSet(jsonDatasetInput, timeframeCoeff);

		// get the name for each timeframe as a List
		timeframeNames = TupleDataset.getTimeFrameNames(data);

		// the full dataset, 20 timeframes
		DataSet<Tuple5<String, String, String, Long, Long>> tupleDataset = TupleDataset.getDefaultDataSet(env, data);

		/*
		 * Transformations Section
		 */

		// group vertices for each community in an array (using the 3rd element-String of the tuple)
		DataSet<Tuple4<String, String, String, Long[]>> arrayTupleDataset = tupleDataset.groupBy(2)
				.reduceGroup(new GatherIDs());

		// filter grouped social network dataset using timeframe names
		DataSet<Tuple4<String, String, String, Long[]>> timeframe;
		for (String name : timeframeNames) {
			timeframe = arrayTupleDataset.filter(new FilterTimeFrame(name));

			// store filtering results in a list
			timeframes.add(timeframe);
		}

		// cartesian product of consecutive timeframes, applying jaccard similarity
		DataSet<Tuple5<String, String, String, String, Double>> result;
		for (int timeframeIndex = 0; timeframeIndex < timeframes.size() - 1; timeframeIndex++) {
			result = timeframes.get(timeframeIndex).cross(timeframes.get(timeframeIndex + 1))
					.map(new CalculateJaccard());

			// store cross jaccard results in a list
			timeframesCross.add(result);
		}

		// start unions by merging the first two product results
		DataSet<Tuple5<String, String, String, String, Double>> resultUnion = timeframesCross.get(0)
				.union(timeframesCross.get(1));

		// then proceed with the remaining products
		for (int crossIndex = 2; crossIndex < timeframesCross.size(); crossIndex++) {
			resultUnion = timeframesCross.get(crossIndex).union(resultUnion);
		}

		/*
		 * Data Sink Section
		 */

		// starting point for internal execution duration
		LocalTime time = LocalTime.now();
		
		resultUnion.writeAsCsv(outputLocation, WriteMode.OVERWRITE).setParallelism(1);
		env.execute();

		// to print results on console (stdout), uncomment the line 101 and comment lines 96&97
		// arrayTupleDataset.print();

		// calculate execution time until this method returns
		return ChronoUnit.MILLIS.between(time, LocalTime.now());

	}

}
