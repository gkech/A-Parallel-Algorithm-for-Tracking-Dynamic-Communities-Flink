/**
 * 
 */
package iit.flink.ct.statistics;

import java.util.List;

/**
 * @author Georgios Kechagias
 *
 */
public class TimeframeStats {

	private String timeFrameID;

	private Integer numberOfCommunities;

	// all vertices (some may)
	private Integer numberOfVertices;

	// divide(numberOfVertices,numberOfCommunities)
	private Double verticesFraction;

	/**
	 * @return the verticesFraction
	 */
	public Double getVerticesFraction() {
		return verticesFraction;
	}

	/**
	 * @param verticesFraction
	 *            the verticesFraction to set
	 */
	public void setVerticesFraction(Double verticesFraction) {
		this.verticesFraction = verticesFraction;
	}

	private List<CommunityStats> communities;

	/**
	 * @return the timeFrameID
	 */
	public String getTimeFrameID() {
		return timeFrameID;
	}

	/**
	 * @param timeFrameID
	 *            the timeFrameID to set
	 */
	public void setTimeFrameID(String timeFrameID) {
		this.timeFrameID = timeFrameID;
	}

	/**
	 * @return the communities
	 */
	public List<CommunityStats> getCommunities() {
		return communities;
	}

	/**
	 * @param communities
	 *            the communities to set
	 */
	public void setCommunities(List<CommunityStats> communities) {
		this.communities = communities;
	}

	/**
	 * @return the numberOfCommunities
	 */
	public Integer getNumberOfCommunities() {
		return numberOfCommunities;
	}

	/**
	 * @param numberOfCommunities
	 *            the numberOfCommunities to set
	 */
	public void setNumberOfCommunities(Integer numberOfCommunities) {
		this.numberOfCommunities = numberOfCommunities;
	}

	/**
	 * @return the numberOfVertices
	 */
	public Integer getNumberOfVertices() {
		return numberOfVertices;
	}

	/**
	 * @param numberOfVertices
	 *            the numberOfVertices to set
	 */
	public void setNumberOfVertices(Integer numberOfVertices) {
		this.numberOfVertices = numberOfVertices;
	}

}
