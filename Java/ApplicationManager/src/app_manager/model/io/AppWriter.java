package app_manager.model.io;

import java.io.File;
import java.io.PrintStream;
import java.util.List;

import app_manager.model.application.Application;

/**
 * Writes a list of Applications to file.
 * 
 * @author Christine Weld
 */
public class AppWriter {

	/**
	 * Writes the given list of Applications to file.
	 * 
	 * @param fileName file to write schedule of Applications to
	 * @param appList  list of Applications to write
	 * @throws IllegalArgumentException "Unable to save file." if cannot write to
	 *                                  file
	 */
	public static void writeAppsToFile(String fileName, List<Application> appList) {
		try {
			PrintStream fileWriter = new PrintStream(new File(fileName));
			for (int i = 0; i < appList.size(); i++) {
				fileWriter.print(appList.get(i).toString());
			}
			fileWriter.close();
		} catch (Exception e) {
			throw new IllegalArgumentException("Unable to save file.");
		}
	}

}
