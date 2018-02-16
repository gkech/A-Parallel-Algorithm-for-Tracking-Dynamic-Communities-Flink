/**
 * 
 */
package iit.flink.ct.root;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import iit.flink.ct.utils.CSVUtils;
import iit.flink.ct.utils.ReadJSONDataSet.InvalidInputException;

/**
 * This class contains the main method which is the root of this project. Its main objective is to orchistrate the
 * execution of the parallel algorithm, considering a variety of mandatory and optional inputs such as dataset to
 * process, output location, repeats of each experiments etc. The tuning of this algorithm is based on Apache Commons
 * CLI.
 * 
 * @author Georgios Kechagias
 *
 */
public class CommunityTrackingIterative {

	// optional + default initializations
	private static String datasetName = "default";
	private static int iterations = 1;
	private static int englare = 1;
	private static boolean deleteSims = true;

	public static void main(String[] args) throws Exception {

		Options options = new Options();

		/* == EXECUTION INPUTS == */

		// required, the full path to dataset
		Option datasetPath = new Option("i", "dataset", true, "Full path to the dataset");
		datasetPath.setRequired(true);
		options.addOption(datasetPath);

		// required, path to store results (outputs)
		Option outPath = new Option("o", "sink-path", true, "Results sink path");
		outPath.setRequired(true);
		options.addOption(outPath);

		// optional, how many times the experiment will run
		Option iterationsCLI = new Option("t", "repeat", true, "Repeat experiment <t> times ");
		options.addOption(iterationsCLI);

		// optional, what's the dataset name, used for output file naming
		Option whichDatasetCLI = new Option("d", "datasetName", true, "Keyword to use for output file naming");
		options.addOption(whichDatasetCLI);

		// optional, enlarge dataset up to 60 timeframes (up to 3 times)
		Option timeframesCoeff = new Option("f", "englarge", true,
				"Englare a 20 timeframes dataset up to 3 times (i.e. 60 timeframes)");
		options.addOption(timeframesCoeff);

		// optional, similarity results output file is large. Delete it to save space when only evaluating speedup
		Option ged_results = new Option("r", "del-simil", false,
				"Delete similarity results. Use to save memory when studying only the speedup");
		options.addOption(ged_results);

		/* == END == */

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();

		try {

			CommandLine cmd = parser.parse(options, args);

			// mandatory arguments
			String dataset = cmd.getOptionValue("i");
			String output = cmd.getOptionValue("o");

			// experiment repeats
			if (cmd.hasOption("t")) {
				iterations = numAgentToInteger(cmd.getOptionValue("t"));

				// cant be less than one
				if (iterations < 1) {
					throw new InvalidInputException("An experiments has to be executed at least one time!");
				}

			}

			// result's prefix of the name
			if (cmd.hasOption("d")) {
				datasetName = cmd.getOptionValue("d");
			}

			// enlarge datasets
			if (cmd.hasOption("f")) {
				englare = numAgentToInteger(cmd.getOptionValue("f"));

				// cant be smaller than one or bigger than 3
				if (englare < 1 || englare > 3) {
					throw new InvalidInputException(
							"The maximum size of the dataset has to be 60 timeframes and the minimum 20!");
				}

			}

			// delete similarities
			if (cmd.hasOption("r")) {
				deleteSims = cmd.hasOption("r");
			}

			// start experiments
			repeatablExperiment(dataset, output, datasetName, iterations, englare, deleteSims);

		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("utility-name", options);
			System.exit(1);
			return;
		}

	}

	/**
	 * 
	 * As java arguments are inserted as Strings, we need to convert them into integers. In case of exception, the
	 * program terminates.
	 * 
	 * @param arg
	 * @return
	 */
	private static Integer numAgentToInteger(String arg) {
		try {
			return Integer.parseInt(arg);
		} catch (NumberFormatException nfe) {
			System.out.println(arg + " Argument is Invalid");
			System.exit(1);
		}
		return 0;
	}

	/**
	 * 
	 * Repeat experiments method. The user needs to specify how many times each experiment is going to be performed. The
	 * aim is statistical significance.
	 * 
	 * @param inputDataLocation
	 * @param outputDataLocation
	 * @param executionDataLocation
	 * @param datasetName
	 * @param iterations
	 * @param timeframeCoeff
	 * @param sink
	 * @throws Exception
	 */
	private static void repeatablExperiment(String inputDataLocation, String executionDataLocation, String datasetName,
			int iterations, int timeframeCoeff, boolean sink) throws Exception {

		List<String> executionTimes = new ArrayList<>();
		List<String> csv_titles = new ArrayList<>();

		Long totalDuration = 0l;

		for (int i = 0; i < iterations; i++) {
			// as the results are always the same, we overwrite the output file
			Long duration = CommunityTrackingExperiment.execute(inputDataLocation,
					executionDataLocation + datasetName + "_similarities" + ".csv", timeframeCoeff);

			executionTimes.add(Long.toString(duration));
			csv_titles.add("iteration" + i);
			totalDuration += duration;
		}

		// last into the list is the raw average
		executionTimes.add(Long.toString(totalDuration / iterations));
		csv_titles.add("average");

		try (FileWriter file = new FileWriter(executionDataLocation + datasetName + "_durations.csv")) {
			// insert titles into the csv
			CSVUtils.writeLine(file, csv_titles);
			// insert experiment results(execution durations in milliseconds)
			CSVUtils.writeLine(file, executionTimes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// delete the similarity results to save space
		if (!sink) {
			Path path = Paths.get(executionDataLocation + datasetName + "_iteration" + "_default" + ".csv");
			try {
				Files.delete(path);
			} catch (NoSuchFileException x) {
				System.err.format("%s: no such" + " file or directory%n", path);
			} catch (DirectoryNotEmptyException x) {
				System.err.format("%s not empty%n", path);
			} catch (IOException x) {
				// File permission problems are caught here.
				System.err.println(x);
			}
		}

		// execution time results
		System.out.println("List of Execution Durations (avg is the last element of the array): \n" + executionTimes);
		System.out.println("Average Execution Duration: \n" + totalDuration / iterations);

	}

}
