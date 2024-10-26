package app_manager.model.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import app_manager.model.application.Application;
import app_manager.model.application.Application.AppType;
import app_manager.model.command.Command;
import app_manager.model.command.Command.CommandValue;
import app_manager.model.command.Command.Resolution;
import app_manager.model.io.AppReader;
import app_manager.model.io.AppWriter;

/**
 * Tests the AppList class. Individual tests for getters and setters are omitted
 * and are tested within other test methods.
 * 
 * @author Christine Weld
 */
public class AppListTest {
	
	/** Valid app file 1 */
	private final String validFileApp1 = "test-files/app1.txt";
	/** Valid app file 2 */
	private final String validFileApp2 = "test-files/app2.txt";

	/**
	 * Tests AppList constructor.
	 */
	@Test
	public void testAppList() {
		AppList appList = new AppList();
		assertEquals(0, appList.getApps().size());
	}

	/**
	 * Tests AppList.addApps() with empty list
	 */
	@Test
	public void testAddAppEmptyList() {
		AppList appList = new AppList();
		assertEquals(0, appList.getApps().size());
		
		appList.addApp(AppType.NEW, "summary", "Note 1");
		assertEquals(1, appList.getApps().size());
		
		Application lastApp = appList.getApps().get(appList.getApps().size() - 1);
		assertEquals(1, lastApp.getAppId());
		assertEquals("Review", lastApp.getStateName());
		assertEquals("New", lastApp.getAppType());
		assertEquals("summary", lastApp.getSummary());
		assertEquals(null, lastApp.getReviewer());
		assertEquals(false, lastApp.isProcessed());
		assertEquals(null, lastApp.getResolution());
		assertEquals("-[Review] Note 1\n", lastApp.getNotesString());
		
	}
	
	/**
	 * Tests AppList.addApps() with filled list from file app1.txt
	 */
	@Test
	public void testAddAppFilledList() {
		AppList appList = new AppList();
		assertEquals(0, appList.getApps().size());
		
		ArrayList<Application> apps = AppReader.readAppsFromFile(validFileApp1);
		appList.addApps(apps);
		
		appList.addApp(AppType.NEW, "summary", "Note 1");
		assertEquals(7, appList.getApps().size());
		
		Application lastApp = appList.getApps().get(appList.getApps().size() - 1);
		assertEquals(17, lastApp.getAppId());
		assertEquals("Review", lastApp.getStateName());
		assertEquals("New", lastApp.getAppType());
		assertEquals("summary", lastApp.getSummary());
		assertEquals(null, lastApp.getReviewer());
		assertEquals(false, lastApp.isProcessed());
		assertEquals(null, lastApp.getResolution());
		assertEquals("-[Review] Note 1\n", lastApp.getNotesString());
		
	}

	/**
	 * Tests AppList.addApps() with file app1.txt
	 */
	@Test
	public void testAddAppsFile1() {
		ArrayList<Application> apps = AppReader.readAppsFromFile(validFileApp1);

		AppList appList = new AppList();
		assertEquals(0, appList.getApps().size());
		
		appList.addApps(apps);
		assertEquals(6, appList.getApps().size());
		
		AppWriter.writeAppsToFile("test-files/act_app1_sorted.txt", appList.getApps());
		checkFiles("test-files/exp_app1_sorted.txt", "test-files/act_app1_sorted.txt");
	}
	
	/**
	 * Tests AppList.addApps() with file app2.txt
	 */
	@Test
	public void testAddAppsFile2() {
		ArrayList<Application> apps = AppReader.readAppsFromFile(validFileApp2);

		AppList appList = new AppList();
		assertEquals(0, appList.getApps().size());
		
		appList.addApps(apps);
		assertEquals(6, appList.getApps().size());
		
		AppWriter.writeAppsToFile("test-files/act_app2_sorted.txt", appList.getApps());
		checkFiles("test-files/exp_app2_sorted.txt", "test-files/act_app2_sorted.txt");
	}

	/**
	 * Tests AppList.getAppByType() by type = new from file app1.txt
	 */
	@Test
	public void testGetAppsByTypeNew() {
		ArrayList<Application> apps = AppReader.readAppsFromFile(validFileApp1);
		AppList appList = new AppList();
		appList.addApp(AppType.OLD, "summary", "Note 1");
		appList.addApps(apps);
		
		
		ArrayList<Application> newApps = appList.getAppsByType("New");
		assertEquals(2, newApps.size());
		assertEquals("New", newApps.get(0).getAppType());
		assertEquals("New", newApps.get(1).getAppType());
	}
	
	/**
	 * Tests AppList.getAppByType() by type = old from file app1.txt
	 */
	@Test
	public void testGetAppsByTypeOld() {
		ArrayList<Application> apps = AppReader.readAppsFromFile(validFileApp1);
		AppList appList = new AppList();
		appList.addApps(apps);
		
		ArrayList<Application> oldApps = appList.getAppsByType("Old");
		assertEquals(4, oldApps.size());
		assertEquals("Old", oldApps.get(0).getAppType());
		assertEquals("Old", oldApps.get(1).getAppType());
		assertEquals("Old", oldApps.get(2).getAppType());
		assertEquals("Old", oldApps.get(3).getAppType());
	}

	/**
	 * Tests AppList.getAppById() from file app1.txt
	 */
	@Test
	public void testGetAppById() {
		// empty list
		AppList appList = new AppList();
		assertEquals(null, appList.getAppById(1));
		
		//full list
		ArrayList<Application> apps = AppReader.readAppsFromFile(validFileApp1);
		appList.addApps(apps);
		assertEquals(appList.getApps().get(3), appList.getAppById(14));
	}
	
	/**
	 * Tests AppList.deleteAppById() with empty list
	 */
	@Test
	public void testDeleteAppByIdEmptyList() {
		AppList appList = new AppList();
		appList.addApp(AppType.NEW, "summary", "Note 1");
		assertEquals(1, appList.getApps().size());
		
		appList.deleteAppById(1);
		assertEquals(0, appList.getApps().size());
	}
	
	/**
	 * Tests AppList.deleteAppById() with filled list from file app1.txt
	 */
	@Test
	public void testDeleteAppByIdFilledList()  {
		
		// delete an app that doesnt exist
		
		AppList appList = new AppList();
		ArrayList<Application> apps = AppReader.readAppsFromFile(validFileApp1);
		appList.addApps(apps);
		assertEquals(6, appList.getApps().size());
		
		appList.deleteAppById(7);
		assertEquals(5, appList.getApps().size());
		assertEquals(14, appList.getApps().get(2).getAppId());
	}

	/**
	 * Tests AppList.executeCommand()
	 */
	@Test
	public void testExecuteCommand() {
		AppList appList = new AppList();
		appList.addApp(AppType.NEW, "summary", "Note 1");
		int id = 1;
		Application app = appList.getAppById(id);
		
		
		
		Command acceptReview = new Command(CommandValue.ACCEPT, "reviewer review test", Resolution.REVCOMPLETED,
				"note review test");
		appList.executeCommand(id, acceptReview);
		assertEquals("Interview", app.getStateName());
		assertEquals("Old", app.getAppType());
		
		Command standbyInterview = new Command(CommandValue.STANDBY, "reviewer interview test", Resolution.INTCOMPLETED,
				"note interview test");
		appList.executeCommand(id, standbyInterview);
		assertEquals("Waitlist", app.getStateName());
		assertEquals("InterviewCompleted", app.getResolution());
		
		Command reopenWaitlist = new Command(CommandValue.REOPEN, "reviewer waitlist test", Resolution.INTCOMPLETED,
				"note waitlist test");
		appList.executeCommand(id, reopenWaitlist);
		assertEquals("RefCheck", app.getStateName());
		assertEquals("reviewer waitlist test", app.getReviewer());
		
		Command acceptRefCheck = new Command(CommandValue.ACCEPT, "reviewer refcheck test", Resolution.REFCHKCOMPLETED,
				"note refcheck test");
		appList.executeCommand(id, acceptRefCheck);
		assertEquals("Offer", app.getStateName());
		assertEquals("reviewer refcheck test", app.getReviewer());
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
