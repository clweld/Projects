package app_manager.model.manager;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import app_manager.model.application.Application;
import app_manager.model.application.Application.AppType;
import app_manager.model.command.Command;
import app_manager.model.io.AppReader;
import app_manager.model.io.AppWriter;

/**
 * Controls the creation and modification of AppLists. Implements the Singleton
 * pattern, works with files that contain saved Applications by calling
 * AppReader and AppWriter classes. Provides information to GUI through methods.
 * 
 * @author Christine Weld
 */
public class AppManager {

	/** Single instance of AppManager */
	private static AppManager instance;
	/** list of applications */
	private AppList appList;

	/**
	 * Constructor for AppManager
	 */
	public AppManager() {
		setAppList(new AppList());
	}

	/**
	 * Returns current instance of AppManager
	 * 
	 * @return instance of AppManager
	 */
	public static synchronized AppManager getInstance() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * Reads in applications from the given file by calling AppReader.
	 * 
	 * @param fileName name of file containing list of applications
	 * @throws FileNotFoundException if AppReader unable to load file
	 */
	public void loadAppsFromFile(String fileName) {
		try {
			ArrayList<Application> fileApps = AppReader.readAppsFromFile(fileName);
			appList.addApps(fileApps);
		} catch (Exception e) {
			throw new IllegalArgumentException("Unable to load file.");
		}
	}

	/**
	 * Write applications to the given file by calling AppWriter.
	 * 
	 * @param fileName name of file to write application list to.
	 */
	public void saveAppsToFile(String fileName) {
		AppWriter.writeAppsToFile(fileName, appList.getApps());
	}

	/**
	 * Updates the global appList reference to point to a new AppList object.
	 */
	public void createNewAppList() {
		setAppList(new AppList());
	}

	/**
	 * Returns a 2D Object array of a list of applications that is used to populate
	 * in the GUI. The array stores rows & columns, with 1 row for every Application
	 * and 4 columns (id, state, type, summary).
	 * 
	 * @return 2D object array of a list of applications
	 */
	public Object[][] getAppListAsArray() {
		int numRows = appList.getApps().size();
		Object[][] appListAsArray = new Object[numRows][4];
		for (int i = 0; i < appList.getApps().size(); i++) {

			Application app = appList.getApps().get(i);
			Object[] appFieldArray = new Object[4];
			Integer appId = app.getAppId();
			appFieldArray[0] = appId;
			appFieldArray[1] = app.getStateName();
			appFieldArray[2] = app.getAppType();
			appFieldArray[3] = app.getSummary();

			appListAsArray[i] = appFieldArray;
		}
		return appListAsArray;
	}

	/**
	 * Returns a 2D Object array of a list of applications filtered by type that is
	 * used to populate in the GUI. The array stores rows & columns, with 1 row for
	 * every Application and 4 columns (id, state, type, summary).
	 * 
	 * @param appType application type to be filtered
	 * @return 2D object array of a list of applications filtered by type
	 * @throws IllegalArgumentException if given appType is null
	 */
	public Object[][] getAppListAsArrayByAppType(String appType) {
		ArrayList<Application> appListByType = appList.getAppsByType(appType);
		int numRows = appListByType.size();
		Object[][] appListAsArrayByType = new Object[numRows][4];
		for (int i = 0; i < appListByType.size(); i++) {
			
			

			Application app = appListByType.get(i);
			Object[] appFieldArray = new Object[4];
			
			Integer appId = app.getAppId();
			appFieldArray[0] = appId;
			appFieldArray[1] = app.getStateName();
			appFieldArray[2] = appType;
			appFieldArray[3] = app.getSummary();

			appListAsArrayByType[i] = appFieldArray;
		}
		return appListAsArrayByType;
	}

	/**
	 * Returns an application from list that matches given id
	 * 
	 * @param id application id to search in list
	 * @return Application that matched id
	 */
	public Application getAppById(int id) {
		return appList.getAppById(id);
	}

	/**
	 * Removes an application from list that matches given id
	 * 
	 * @param id application id to search in list
	 */
	public void deleteAppById(int id) {
		appList.deleteAppById(id);
	}

	/**
	 * Executes given command on an application from list that matches given id
	 * 
	 * @param id application id to search in list
	 * @param c  Command to be executed
	 */
	public void executeCommand(int id, Command c) {
		appList.executeCommand(id, c);
	}

	/**
	 * Constructs an application from given fields and adds it to the list.
	 * 
	 * @param appType application type
	 * @param summary application summary
	 * @param note    application note
	 */
	public void addAppToList(AppType appType, String summary, String note) {
		appList.addApp(appType, summary, note);
	}

	/**
	 * Returns appList
	 * 
	 * @return the appList
	 */
	public AppList getAppList() {
		return appList;
	}

	/**
	 * Sets the appList
	 * 
	 * @param appList the appList to set
	 */
	public void setAppList(AppList appList) {
		this.appList = appList;
	}

}
