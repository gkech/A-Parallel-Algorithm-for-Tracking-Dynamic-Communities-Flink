/**
 * 
 */
package iit.flink.ct.statistics;

import java.util.List;

/**
 * @author Georgios Kechagias
 *
 */
public class DatasetJSON {

	private String datasetName;

	private Integer numberOfTimeframes;

	private Integer totalConnections;

	private List<TimeframeStats> timeframes;

	/**
	 * @return the datasetName
	 */
	public String getDatasetName() {
		return datasetName;
	}

	/**
	 * @param datasetName
	 *            the datasetName to set
	 */
	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	/**
	 * @return the numberOfTimeframes
	 */
	public Integer getNumberOfTimeframes() {
		return numberOfTimeframes;
	}

	/**
	 * @param numberOfTimeframes
	 *            the numberOfTimeframes to set
	 */
	public void setNumberOfTimeframes(Integer numberOfTimeframes) {
		this.numberOfTimeframes = numberOfTimeframes;
	}

	/**
	 * @return the totalConnections
	 */
	public Integer getTotalConnections() {
		return totalConnections;
	}

	/**
	 * @param totalConnections
	 *            the totalConnections to set
	 */
	public void setTotalConnections(Integer totalConnections) {
		this.totalConnections = totalConnections;
	}

	/**
	 * @return the timeframes
	 */
	public List<TimeframeStats> getTimeframes() {
		return timeframes;
	}

	/**
	 * @param timeframes
	 *            the timeframes to set
	 */
	public void setTimeframes(List<TimeframeStats> timeframes) {
		this.timeframes = timeframes;
	}

}
