/**
 * 
 */
package iit.flink.ct.userDefinedFunctions;

import java.util.ArrayList;

import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.util.Collector;

/**
 * @author Georgios Kechagias
 *
 */
public class GatherIDs implements
		GroupReduceFunction<Tuple5<String, String, String, Long, Long>, Tuple4<String, String, String, Long[]>> {

	private static final long serialVersionUID = 1L;

	private final ArrayList<Long> ids = new ArrayList<Long>();

	public void reduce(Iterable<Tuple5<String, String, String, Long, Long>> values,
			Collector<Tuple4<String, String, String, Long[]>> out) throws Exception {

		ids.clear();
		String window = " ";
		String community = " ";
		String winPLUScom = " ";

		// it's the same functionality with hashset - union

		for (Tuple5<String, String, String, Long, Long> tuple : values) {
			window = tuple.f0;
			community = tuple.f1;
			winPLUScom = tuple.f2;

			if (!ids.contains(tuple.f3)) {
				ids.add(tuple.f3);
			}
			if (!ids.contains(tuple.f4)) {
				ids.add(tuple.f4);
			}
		}

		out.collect(new Tuple4<String, String, String, Long[]>(window, community, winPLUScom,
				ids.toArray(new Long[ids.size()])));

	}

}
