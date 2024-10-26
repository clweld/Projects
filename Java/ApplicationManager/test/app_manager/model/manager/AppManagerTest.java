package app_manager.model.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import app_manager.model.application.Application;
import app_manager.model.application.Application.AppType;
import app_manager.model.command.Command;
import app_manager.model.command.Command.CommandValue;
import app_manager.model.command.Command.Resolution;

/**
 * Tests the AppManager class. Individual tests for getters and setters are
 * omitted and are tested within other test methods.
 * 
 * @author Christine Weld
 */
public class AppManagerTest {

	/** Instance of AppManager */
	private AppManager manager;
	/** Valid app file 1 */
	private final String validFileApp1 = "test-files/app1.txt";
	/** Invalid app file 3 */
	private final String invalidFileApp3 = "test-files/app3.txt";

	/**
	 * Sets up the AppManager and clears the data.
	 * 
	 * @throws Exception if error
	 */
	@BeforeEach
	public void setUp() throws Exception {
		manager = AppManager.getInstance();
		manager.createNewAppList();
	}

	/**
	 * Tests AppManager constructor.
	 */
	@Test
	public void testAppManager() {
		assertEquals(0, manager.getAppList().getApps().size());
	}

	/**
	 * Tests AppManager.loadAppsFromFile()
	 */
	@Test
	public void testLoadAppsFromFile() {
		// test valid app records
		manager.loadAppsFromFile(validFileApp1);
		assertEquals(6, manager.getAppList().getApps().size());

		// test invalid app records
		manager.createNewAppList();
		Exception e1 = assertThrows(IllegalArgumentException.class,
				() -> manager.loadAppsFromFile(invalidFileApp3));
		assertEquals("Unable to load file.", e1.getMessage());

		// test FileNotFoundException
		Exception e2 = assertThrows(IllegalArgumentException.class,
				() -> manager.loadAppsFromFile("test-files/nonexistentApp.txt"));
		assertEquals("Unable to load file.", e2.getMessage());
	}
	
	/**
	 * Tests AppManager.saveAppsToFile()
	 */
	@Test
	public void testSaveAppsToFile() {
		// test saving valid app records
		manager.loadAppsFromFile(validFileApp1);
		manager.saveAppsToFile("test-files/act_app1_sorted.txt");
		checkFiles("test-files/exp_app1_sorted.txt", "test-files/act_app1_sorted.txt");

		// test IOException
		Exception e = assertThrows(IllegalArgumentException.class,
				() -> manager.saveAppsToFile("/home/sesmith5/actual_app1.txt"));
		assertEquals("Unable to save file.", e.getMessage());
	}

	/**
	 * Tests AppManager.createNewAppList()
	 */
	@Test
	public void testCreateNewAppList() {
		manager.createNewAppList();
		assertEquals(0, manager.getAppList().getApps().size());
	}

	/**
	 * Tests AppManager.getAppListAsArray()
	 */
	@Test
	public void testGetAppListAsArray() {
		manager.loadAppsFromFile(validFileApp1);
		Object[][] appArray = manager.getAppListAsArray();

		// Row 1
		assertEquals(1, appArray[0][0]);
		assertEquals("Review", appArray[0][1]);
		assertEquals("New", appArray[0][2]);
		assertEquals("Application summary", appArray[0][3]);
		// Row 2
		assertEquals(3, appArray[1][0]);
		assertEquals("Interview", appArray[1][1]);
		assertEquals("Old", appArray[1][2]);
		assertEquals("Application summary", appArray[1][3]);
		// Row 3
		assertEquals(7, appArray[2][0]);
		assertEquals("RefCheck", appArray[2][1]);
		assertEquals("Old", appArray[2][2]);
		assertEquals("Application summary", appArray[2][3]);
		// Row 4
		assertEquals(14, appArray[3][0]);
		assertEquals("Waitlist", appArray[3][1]);
		assertEquals("New", appArray[3][2]);
		assertEquals("Application summary", appArray[3][3]);
	}

	/**
	 * Tests AppManager.getAppListAsArrayByType()
	 */
	@Test
	public void testGetAppListAsArrayByType() {
		manager.loadAppsFromFile(validFileApp1);
		Object[][] appArray = manager.getAppListAsArrayByAppType("New");

		// Row 1
		assertEquals(1, appArray[0][0]);
		assertEquals("Review", appArray[0][1]);
		assertEquals("New", appArray[0][2]);
		assertEquals("Application summary", appArray[0][3]);
		// Row 2
		assertEquals(14, appArray[1][0]);
		assertEquals("Waitlist", appArray[1][1]);
		assertEquals("New", appArray[1][2]);
		assertEquals("Application summary", appArray[1][3]);
	}

	/**
	 * Tests AppManager.getAppById()
	 */
	@Test
	public void testGetAppById() {
		manager.loadAppsFromFile(validFileApp1);
		
		Application app = manager.getAppById(1);
		assertEquals(1, app.getAppId());
		assertEquals("Review", app.getStateName());
		assertEquals("New", app.getAppType());
		assertEquals("Application summary", app.getSummary());
		assertEquals(null, app.getReviewer());
		assertEquals(false, app.isProcessed());
		assertEquals("-[Review] Note 1\n", app.getNotesString());	
	}

	/**
	 * Tests AppManager.executeCommand()
	 */
	@Test
	public void testExecuteCommand() {
		manager.addAppToList(AppType.NEW, "summary", "Note 1");
		int id = 1;
		Application app = manager.getAppById(id);
		
		Command acceptReview = new Command(CommandValue.ACCEPT, "reviewer review test", Resolution.REVCOMPLETED,
				"note review test");
		manager.executeCommand(id, acceptReview);
		assertEquals("Interview", app.getStateName());
		assertEquals("Old", app.getAppType());
		
		Command standbyInterview = new Command(CommandValue.STANDBY, "reviewer interview test", Resolution.INTCOMPLETED,
				"note interview test");
		manager.executeCommand(id, standbyInterview);
		assertEquals("Waitlist", app.getStateName());
		assertEquals("InterviewCompleted", app.getResolution());
		
		Command reopenWaitlist = new Command(CommandValue.REOPEN, "reviewer waitlist test", Resolution.INTCOMPLETED,
				"note waitlist test");
		manager.executeCommand(id, reopenWaitlist);
		assertEquals("RefCheck", app.getStateName());
		assertEquals("reviewer waitlist test", app.getReviewer());
		
		Command acceptRefCheck = new Command(CommandValue.ACCEPT, "reviewer refcheck test", Resolution.REFCHKCOMPLETED,
				"note refcheck test");
		manager.executeCommand(id, acceptRefCheck);
		assertEquals("Offer", app.getStateName());
		assertEquals("reviewer refcheck test", app.getReviewer());
	}

	/**
	 * Tests AppManager.deleteAppById()
	 */
	@Test
	public void testDeleteAppById() {
		manager.loadAppsFromFile(validFileApp1);
		manager.deleteAppById(1); 
		assertEquals(5, manager.getAppList().getApps().size());
		assertEquals(manager.getAppList().getApps().get(0), manager.getAppList().getAppById(3));
	}

	/**
	 * Tests AppManager.addAppToList()
	 */
	@Test
	public void testAddAppToList() {
		manager.addAppToList(AppType.NEW, "summary", "Note 1");
		assertEquals(1, manager.getAppList().getApps().size());
	}

	/**
	 * Helper method to compare two files for the same contents
	 * 
	 * @param expFile expected output
	 * @param actFile actual output
	 */
	private void checkFiles(String expFile, String actFile) {
		try {
			Scanner expScanner = new Scanner(new FileInputStream(expFile));
			Scanner actScanner = new Scanner(new FileInputStream(actFile));

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
