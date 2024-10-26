package app_manager.model.manager;

import java.util.ArrayList;

import app_manager.model.application.Application;
import app_manager.model.application.Application.AppType;
import app_manager.model.command.Command;

/**
 * Maintains a ArrayList of Applications with functionality to add and remove an
 * Application from the list, search for an Application, update an Application,
 * and return sublists.
 * 
 * @author Christine Weld
 */
public class AppList {

	/** id for the next Application added to the list */
	private int counter;
	/** list of applications */
	private ArrayList<Application> apps;

	/**
	 * Constructor for AppList
	 */
	public AppList() {
		apps = new ArrayList<Application>();
		this.counter = 0;
	}

	/**
	 * Constructs an application from given fields, adds it to the list, and returns
	 * the counter for the next application's id number.
	 * 
	 * @param appType application type
	 * @param summary application summary
	 * @param note    application note
	 * @return id of the application added to the list
	 */
	public int addApp(AppType appType, String summary, String note) {
		if (counter == 0) {
			counter = 1;
		}
		Application app = new Application(counter, appType, summary, note);
		addApp(app);

		return counter;
	}

	/**
	 * Adds the given list of applications to this list in order by id.
	 * 
	 * @param list ArrayList of Applications to be added
	 */
	public void addApps(ArrayList<Application> list) {
		apps = new ArrayList<Application>();
		for (int i = 0; i < list.size(); i++) {
			Application listApp = list.get(i);
			addApp(listApp);
		}
	}

	/**
	 * Adds an Application in sorted order to the list and checks for duplicates.
	 * 
	 * @param app application to be added
	 */
	private void addApp(Application app) {
		boolean duplicate = false;
		if (apps.size() > 0) {
			for (int i = 0; i < apps.size(); i++) {
				if (app.getAppId() == apps.get(i).getAppId()) {
					duplicate = true;
				}
			}
			if (!duplicate) {
				int sortIdx = 0;
				for (int i = 0; i < apps.size(); i++) {
					if (app.getAppId() > apps.get(i).getAppId()) {
						sortIdx = i + 1;
					}
				}
				apps.add(sortIdx, app);
			}
		} else {
			apps.add(app);
		}
		counter = apps.get(apps.size() - 1).getAppId() + 1;
	}

	/**
	 * Returns the list of applications
	 * 
	 * @return the list of applications
	 */
	public ArrayList<Application> getApps() {
		return apps;
	}

	/**
	 * Returns a list of applications filtered by type
	 * 
	 * @param type application type to filter list
	 * @return list of applications filtered by type
	 * @throws IllegalArgumentException if type is null
	 */
	public ArrayList<Application> getAppsByType(String type) {
		if (type == null) {
			throw new IllegalArgumentException();
		}
		ArrayList<Application> appsByType = new ArrayList<Application>();
		for (int i = 0; i < apps.size(); i++) {
			if (apps.get(i).getAppType().equals(type)) {
				appsByType.add(apps.get(i));
			}
		}
		return appsByType;
	}

	/**
	 * Returns an application from list that matches given id
	 * 
	 * @param id application id to search in list
	 * @return application that matches given id
	 */
	public Application getAppById(int id) {
		Application foundId = null;
		for (int i = 0; i < apps.size(); i++) {
			if (apps.get(i).getAppId() == id) {
				foundId = apps.get(i);
			}
		}
		return foundId;
	}

	/**
	 * Removes an application from list that matches given id
	 * 
	 * @param id application id to search in list
	 */
	public void deleteAppById(int id) {
		Application app = getAppById(id);
		if (app != null) {
			apps.remove(app);
		}
		counter -= 1;

	}

	/**
	 * Executes command on an application from list that matches given id
	 * 
	 * @param id application id to search in list
	 * @param c  command to execute on application
	 */
	public void executeCommand(int id, Command c) {
		Application app = getAppById(id);
		if (app != null) {
			getAppById(id).update(c);
		}
	}

}
