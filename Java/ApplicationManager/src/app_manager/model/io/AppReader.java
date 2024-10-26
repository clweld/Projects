package app_manager.model.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import app_manager.model.application.Application;

/**
 * Reads and processes Application records from a text file into an ArrayList of
 * Applications.
 * 
 * @author Christine Weld
 */
public class AppReader {

	/**
	 * Reads Application records from a file into a String object, calls
	 * processApp() to return a single Application from the String, and generates a
	 * list of Applications. If the file to read cannot be found or the permissions
	 * are incorrect, an IllegalArgumentException is thrown.
	 * 
	 * @param fileName file to read Application records from
	 * @return a list of valid Applications
	 * @throws IllegalArgumentException "Unable to load file." if the file cannot be
	 *                                  found or read
	 */
	public static ArrayList<Application> readAppsFromFile(String fileName) {
		ArrayList<Application> apps = new ArrayList<Application>();
		try {
			Scanner fileReader = new Scanner(new FileInputStream(fileName));
			String fileText = "";

			while (fileReader.hasNextLine()) {
				fileText += fileReader.nextLine() + "\n";
			}

			Scanner lineScanner = new Scanner(fileText);
			lineScanner.useDelimiter("\\r?\\n?[*]");

			while (lineScanner.hasNext()) {
				Application app = processApp(lineScanner.next());
				apps.add(app);
			}

			lineScanner.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Unable to load file.");
		}
		return apps;
	}

	/**
	 * Processes a String object of the lines from the text file representing a
	 * single application.
	 * 
	 * @param appString String object of all lines from input file
	 * @return Application constructed from processes data from input file
	 * @throws IllegalArgumentException if Application cannot be constructed.
	 */
	private static Application processApp(String appString) {
		Application app = null;
		try {
			Scanner lineScanner = new Scanner(appString);
			String line = lineScanner.nextLine();

			Scanner tokenScanner = new Scanner(line);
			tokenScanner.useDelimiter(",");

			int id = Integer.parseInt(tokenScanner.next());
			String state = tokenScanner.next();
			String appType = tokenScanner.next();
			String summary = tokenScanner.next();
			String reviewer = tokenScanner.next();
			boolean processPaperwork = Boolean.parseBoolean(tokenScanner.next());
			String resolution = "";
			ArrayList<String> notes = new ArrayList<String>();

			if (tokenScanner.hasNext()) {
				resolution = tokenScanner.next();
			}

			String noteElement = "";
			while (lineScanner.hasNext()) {
				String firstLine = lineScanner.nextLine();
				if (firstLine.startsWith("-")) {
					firstLine = firstLine.replaceFirst("-", "");
					noteElement += firstLine;
				}

				while (lineScanner.hasNext()) {
					String nextLine = lineScanner.nextLine();
					if (nextLine.startsWith("-")) {
						notes.add(noteElement);
						nextLine = nextLine.replaceFirst("-", "");
						noteElement = nextLine;
					} else {
						noteElement += "\n" + nextLine;
					}
				}
				notes.add(noteElement);
			}

			if (tokenScanner.hasNext()) {
				tokenScanner.close();
				lineScanner.close();
				throw new IllegalArgumentException();
			}
			tokenScanner.close();
			lineScanner.close();

			app = new Application(id, state, appType, summary, reviewer, processPaperwork, resolution, notes);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Unable to load file.");
		}
		return app;
	}
}
