/**
 * 
 */
package iit.flink.ct.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author Georgios Kechagias
 *
 */

public class ListUtils {

	// creates the union of the two inputed lists
	public static <T> List<T> union(List<T> list1, List<T> list2) {
		Set<T> set = new HashSet<T>();

		set.addAll(list1);
		set.addAll(list2);

		return new ArrayList<T>(set);
	}

	// creates the intersection of the two inputed lists
	public static <T> List<T> intersection(List<T> list1, List<T> list2) {
		List<T> list = new ArrayList<T>();

		for (T t : list1) {
			if (list2.contains(t)) {
				list.add(t);
			}
		}

		return list;
	}

}
