/**
 * 
 */
package iit.flink.ct.statistics;

/**
 * @author Georgios Kechagias
 *
 */
public class CommunityStats {

	private String communityID;

	private Integer numberOfEdges;

	private Integer numberOfVertices;

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

	/**
	 * @return the communityID
	 */
	public String getCommunityID() {
		return communityID;
	}

	/**
	 * @param communityID
	 *            the communityID to set
	 */
	public void setCommunityID(String communityID) {
		this.communityID = communityID;
	}

	/**
	 * @return the numberOfEdges
	 */
	public Integer getNumberOfEdges() {
		return numberOfEdges;
	}

	/**
	 * @param numberOfEdges
	 *            the numberOfEdges to set
	 */
	public void setNumberOfEdges(Integer numberOfEdges) {
		this.numberOfEdges = numberOfEdges;
	}

}
