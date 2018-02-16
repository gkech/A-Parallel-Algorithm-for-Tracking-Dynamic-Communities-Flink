/**
 * 
 */
package iit.flink.ct.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.commons.lang.ArrayUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * @author Georgios Kechagias
 *
 */
public class ReadJSONDataSet {

	/**
	 * @throws InvalidInputException
	 * 
	 */
	public static Communities[] readJSONDataSet(String datasetPath, Integer loadCountData)
			throws JsonIOException, JsonSyntaxException, FileNotFoundException, InvalidInputException {

		JsonParser parser = new JsonParser();

		JsonObject element = (JsonObject) parser.parse(new FileReader(datasetPath));

		JsonElement responseWrapper = element.get("windows");

		Gson gson = new GsonBuilder().create();

		Communities[] data = gson.fromJson(responseWrapper, Communities[].class);

		Communities[] temp = data;
		for (int i = 1; i < loadCountData; i++) {
			data = (Communities[]) ArrayUtils.addAll(data, temp);
		}

		// uncomment below if you want a dataset subset
		// data = Arrays.copyOfRange(data, 0, 3);

		return data;
	}

	public static Communities[] readJSONDataSet(String datasetPath)
			throws JsonIOException, JsonSyntaxException, FileNotFoundException {

		JsonParser parser = new JsonParser();

		JsonObject element = (JsonObject) parser.parse(new FileReader(datasetPath));

		JsonElement responseWrapper = element.get("windows");

		Gson gson = new GsonBuilder().create();

		Communities[] data = gson.fromJson(responseWrapper, Communities[].class);

		return data;
	}

	public static class InvalidInputException extends Exception {
		private static final long serialVersionUID = 1L;

		public InvalidInputException(String message) {
			super(message);
		}

	}

}
