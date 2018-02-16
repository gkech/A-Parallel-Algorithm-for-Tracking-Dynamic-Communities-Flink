/**
 * 
 */
package iit.flink.ct.userDefinedFunctions;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.tuple.Tuple4;

/**
 * @author Georgios Kechagias
 *
 */
public class FilterTimeFrame implements FilterFunction<Tuple4<String, String, String, Long[]>> {
	private static final long serialVersionUID = 1L;

	private String timeFrameName;

	public FilterTimeFrame(String timeFrameName) {
		this.timeFrameName = timeFrameName;
	}

	public boolean filter(Tuple4<String, String, String, Long[]> value) throws Exception {
		return value.f0.equals(timeFrameName);

	}

}
