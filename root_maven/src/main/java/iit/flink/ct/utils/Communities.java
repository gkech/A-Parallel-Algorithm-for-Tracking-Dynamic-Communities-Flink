/**
 * 
 */
package iit.flink.ct.utils;

import java.util.Arrays;

/**
 * @author Georgios Kechagias
 *
 */
public class Communities {

	private long[][][] communities;

	/**
	 * @return the communities
	 */
	public long[][][] getCommunities() {
		return communities;
	}

	/**
	 * @param communities
	 *            the communities to set
	 */
	public void setCommunities(long[][][] communities) {
		this.communities = communities;
	}

	@Override
	public String toString() {
		return "Communities [communities=" + Arrays.toString(communities) + ", toString()=" + super.toString() + "]";
	}

}
