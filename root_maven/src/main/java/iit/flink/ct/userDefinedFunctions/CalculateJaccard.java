/**
 * 
 */
package iit.flink.ct.userDefinedFunctions;

import java.util.Arrays;
import java.util.List;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple5;

import iit.flink.ct.utils.ListUtils;

/**
 * @author Georgios Kechagias
 *
 */
public final class CalculateJaccard implements
		MapFunction<Tuple2<Tuple4<String, String, String, Long[]>, Tuple4<String, String, String, Long[]>>, Tuple5<String, String, String, String, Double>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Long> tuple1Elements, tuple2Elements;

	private double union, intersection, jaccardCoefficient;

	public Tuple5<String, String, String, String, Double> map(
			Tuple2<Tuple4<String, String, String, Long[]>, Tuple4<String, String, String, Long[]>> value)
			throws Exception {

		tuple1Elements = Arrays.asList(value.f0.f3);
		tuple2Elements = Arrays.asList(value.f1.f3);

		intersection = ListUtils.intersection(tuple1Elements, tuple2Elements).size();
		union = ListUtils.union(tuple1Elements, tuple2Elements).size();

		jaccardCoefficient = (double) intersection / union;

		if (jaccardCoefficient > 0.2) {

		}

		return new Tuple5<String, String, String, String, Double>(value.f0.f0, value.f0.f1, value.f1.f0, value.f1.f1,
				jaccardCoefficient);
	}

}
