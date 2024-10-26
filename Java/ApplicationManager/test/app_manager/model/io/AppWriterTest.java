package app_manager.model.io;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import app_manager.model.application.Application;
import app_manager.model.application.Application.AppType;

/**
 * Tests the AppWriter class.
 * 
 * @author Christine Weld
 */
public class AppWriterTest {

	/**
	 * Tests AppWriter.writeAppsToFile()
	 */
	@Test
	public void testWriteApps() {
		ArrayList<Application> apps = new ArrayList<Application>();
		apps.add(new Application(1, AppType.NEW, "Application summary", "Note 1"));

		assertDoesNotThrow(() -> AppWriter.writeAppsToFile("test-files/act_app_test", apps));
		checkFiles("test-files/exp_app_test.txt", "test-files/act_app_test.txt");
	}

	/**
	 * Tests AppWriter.writeAppsToFile()
	 */
	@Test
	public void testWriteAppsNewReview() {
		ArrayList<Application> apps = AppReader.readAppsFromFile("test-files/exp_new_review.txt");
		
		assertDoesNotThrow(() -> AppWriter.writeAppsToFile("test-files/act_new_review.txt", apps));
		checkFiles("test-files/exp_new_review.txt", "test-files/act_new_review.txt");
	}

	/**
	 * Tests AppWriter.writeAppsToFile()
	 */
	@Test
	public void testWriteAppsReview() {
		ArrayList<Application> apps = AppReader.readAppsFromFile("test-files/exp_app_review.txt");
		
		assertDoesNotThrow(() -> AppWriter.writeAppsToFile("test-files/act_app_review.txt", apps));
		checkFiles("test-files/exp_app_review.txt", "test-files/act_app_review.txt");
	}

	/**
	 * Tests AppWriter.writeAppsToFile()
	 */
	@Test
	public void testWriteAppsInterview() {
		ArrayList<Application> apps = AppReader.readAppsFromFile("test-files/exp_app_interview.txt");
		
		assertDoesNotThrow(() -> AppWriter.writeAppsToFile("test-files/act_app_interview.txt", apps));
		checkFiles("test-files/exp_app_interview.txt", "test-files/act_app_interview.txt");
	}

	/**
	 * Tests AppWriter.writeAppsToFile()
	 */
	@Test
	public void testWriteAppsWaitlist() {
		ArrayList<Application> apps = AppReader.readAppsFromFile("test-files/exp_app_waitlist.txt");
		
		assertDoesNotThrow(() -> AppWriter.writeAppsToFile("test-files/act_app_waitlist.txt", apps));
		checkFiles("test-files/exp_app_waitlist.txt", "test-files/act_app_waitlist.txt");
	}

	/**
	 * Tests AppWriter.writeAppsToFile()
	 */
	@Test
	public void testWriteAppsRefCheck() {
		ArrayList<Application> apps = AppReader.readAppsFromFile("test-files/exp_app_refcheck.txt");
		
		assertDoesNotThrow(() -> AppWriter.writeAppsToFile("test-files/act_app_refcheck.txt", apps));
		checkFiles("test-files/exp_app_refcheck.txt", "test-files/act_app_refcheck.txt");
	}

	/**
	 * Tests AppWriter.writeAppsToFile()
	 */
	@Test
	public void testWriteAppsClosed() {
		ArrayList<Application> apps = AppReader.readAppsFromFile("test-files/exp_app_closed.txt");
		
		assertDoesNotThrow(() -> AppWriter.writeAppsToFile("test-files/act_app_closed.txt", apps));
		checkFiles("test-files/exp_app_closed.txt", "test-files/act_app_closed.txt");
	}

	/**
	 * Helper method to compare two files for the same contents
	 * 
	 * @param expFile expected output
	 * @param actFile actual output
	 */
	private void checkFiles(String expFile, String actFile) {
		try (Scanner expScanner = new Scanner(new File(expFile));
				Scanner actScanner = new Scanner(new File(actFile));) {

			while (expScanner.hasNextLine()) {
				assertEquals(expScanner.nextLine(), actScanner.nextLine());
			}

			expScanner.close();
			actScanner.close();
		} catch (IOException e) {
			fail("Error reading files.");
		}
	}

}
