/**
 * 
 */
package iit.flink.ct.utils;

import java.util.Arrays;

/**
 * @author Georgios Kechagias
 *
 */
public class Windows {

	private Communities[] windows;

	/**
	 * @return the windows
	 */
	public Communities[] getWindows() {
		return windows;
	}

	/**
	 * @param windows
	 *            the windows to set
	 */
	public void setWindows(Communities[] windows) {
		this.windows = windows;
	}

	@Override
	public String toString() {
		return "Windows [windows=" + Arrays.toString(windows) + "]";
	}

}
