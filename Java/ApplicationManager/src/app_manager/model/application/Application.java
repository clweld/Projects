package app_manager.model.application;

import java.util.ArrayList;

import app_manager.model.command.Command;
import app_manager.model.command.Command.CommandValue;
import app_manager.model.command.Command.Resolution;

/**
 * Represents an Application object in the ApplicationManager program. Each
 * application has its own state which is updated from Commands passed to it
 * from the UI. Other fields may be updated when a Command is passed. This class
 * is also the context class for the AppState interface and inner State classes.
 * 
 * @author Christine Weld
 */
public class Application {

	/** String constant for a new application */
	public static final String A_NEW = "New";
	/** String constant for an old application */
	public static final String A_OLD = "Old";
	/** String constant for a hired application */
	public static final String A_HIRED = "Hired";

	/** Unique application id for an application. */
	private int appId;
	/** Current state for the application */
	private AppState state;
	/** Type of application */
	private AppType appType;
	/** Application’s summary information from when the application is created */
	private String summary;
	/** User id of the application reviewer or null if none assigned */
	private String reviewer;
	/** True if hired application paperwork has been processed */
	private boolean processPaperwork;
	/** Status of application updated after state transition */
	private Resolution resolution;
	/** ArrayList of notes appended after each state transition */
	private ArrayList<String> notes;

	/** String constant for Review State name */
	public static final String REVIEW_NAME = "Review";
	/** String constant for Interview State name */
	public static final String INTERVIEW_NAME = "Interview";
	/** String constant for RefCheck State name */
	public static final String REFCHK_NAME = "RefCheck";
	/** String constant for Offer State name */
	public static final String OFFER_NAME = "Offer";
	/** String constant for Waitlist State name */
	public static final String WAITLIST_NAME = "Waitlist";
	/** String constant for Closed State name */
	public static final String CLOSED_NAME = "Closed";

	/** Final instance of ReviewState */
	private ReviewState reviewState = new ReviewState();
	/** Final instance of InterviewState */
	private InterviewState interviewState = new InterviewState();
	/** Final instance of RefChkState */
	private RefChkState refChkState = new RefChkState();
	/** Final instance of OfferState */
	private OfferState offerState = new OfferState();
	/** Final instance of WaitlistState */
	private WaitlistState waitlistState = new WaitlistState();
	/** Final instance of ClosedState */
	private ClosedState closedState = new ClosedState();

	/**
	 * Represents the status of an application after state transitions.
	 */
	public enum AppType {
		/** type New */
		NEW,
		/** type Old */
		OLD,
		/** type Hired */
		HIRED
	}

	/**
	 * Constructs an Application object managed by the program with given fields
	 * appId, appType, summary, and note, with other fields state, summary,
	 * reviewer, processPaperwork, resolution, and notes initialized to null or
	 * false values. Adds an initial note to the notes array.
	 * 
	 * @param appId   int id of application
	 * @param appType AppType type of application
	 * @param summary String summary of application
	 * @param note    String note for application notes array
	 * @throws IllegalArgumentException "Application cannot be created." if any
	 *                                  parameters are null or empty strings, or id
	 *                                  is < 1.
	 */
	public Application(int appId, AppType appType, String summary, String note) {
		if (appType == null || note == null || "".equals(note)) {
			throw new IllegalArgumentException("Application cannot be created.");
		}

		setAppId(appId);
		this.state = reviewState;
		this.appType = appType;
		setSummary(summary);
		this.reviewer = null;
		this.processPaperwork = false;
		this.resolution = null;
		this.notes = new ArrayList<String>();
		addNote(note);
	}

	/**
	 * Constructs an Application object with given values for all fields.
	 * 
	 * @param appId            int id of application
	 * @param state            State state of application
	 * @param appType          AppType type of application
	 * @param summary          String summary of application
	 * @param reviewer         String reviewer for application
	 * @param processPaperwork boolean true if paperwork processed for application
	 * @param resolution       Resolution of application
	 * @param notes            ArrayList of notes for application
	 * @throws IllegalArgumentException "Application cannot be created." if any
	 *                                  parameters are null or empty strings, or id
	 *                                  is < 1.
	 */
	public Application(int appId, String state, String appType, String summary, String reviewer,
			boolean processPaperwork, String resolution, ArrayList<String> notes) {
		setAppId(appId);
		setState(state);
		setAppType(appType);
		setSummary(summary);
		setReviewer(reviewer);
		setProcessPaperwork(processPaperwork);
		setResolution(resolution);
		setNotes(notes);
	}

	/**
	 * Sets the application's appId field.
	 * 
	 * @param appId the appId to set
	 * @throws IllegalArgumentException "Application cannot be created." if appId
	 *                                  less than 1
	 */
	private void setAppId(int appId) {
		if (appId < 1) {
			throw new IllegalArgumentException("Application cannot be created.");
		}
		this.appId = appId;
	}

	/**
	 * Sets the application's state field.
	 * 
	 * @param state the state to set
	 * @throws IllegalArgumentException "Application cannot be created." if invalid
	 *                                  AppState
	 */
	private void setState(String state) {
		if (REVIEW_NAME.equals(state)) {
			this.state = reviewState;
		} else if (INTERVIEW_NAME.equals(state)) {
			this.state = interviewState;
		} else if (REFCHK_NAME.equals(state)) {
			this.state = refChkState;
		} else if (OFFER_NAME.equals(state)) {
			this.state = offerState;
		} else if (WAITLIST_NAME.equals(state)) {
			this.state = waitlistState;
		} else if (CLOSED_NAME.equals(state)) {
			this.state = closedState;
		} else {
			throw new IllegalArgumentException("Application cannot be created.");
		}
	}

	/**
	 * Sets the application's appType field.
	 * 
	 * @param appType the appType to set
	 * @throws IllegalArgumentException "Application cannot be created." if invalid
	 *                                  AppType
	 */
	private void setAppType(String appType) {
		if (A_NEW.equals(appType) && (state == reviewState || state == waitlistState || state == closedState)) {
			this.appType = AppType.NEW;
		} else if (A_OLD.equals(appType)) {
			this.appType = AppType.OLD;
		} else if (A_HIRED.equals(appType)) {
			this.appType = AppType.HIRED;
		} else {
			throw new IllegalArgumentException("Application cannot be created.");
		}
	}

	/**
	 * Sets the application's summary field.
	 * 
	 * @param summary the summary to set
	 * @throws IllegalArgumentException "Application cannot be created." if null or
	 *                                  empty string
	 */
	private void setSummary(String summary) {
		if (summary == null || "".equals(summary)) {
			throw new IllegalArgumentException("Application cannot be created.");
		}
		this.summary = summary;
	}

	/**
	 * Sets the application's reviewer field.
	 * 
	 * @param reviewer the reviewer to set
	 * @throws IllegalArgumentException "Application cannot be created." if null or
	 *                                  empty string
	 * 
	 */
	private void setReviewer(String reviewer) {
		if (state == interviewState || state == refChkState || state == offerState
				|| state == waitlistState && appType == AppType.OLD) {
			if (reviewer == null || "".equals(reviewer)) {
				throw new IllegalArgumentException("Application cannot be created.");
			}
			this.reviewer = reviewer;
		} else if ("".equals(reviewer)) {
			this.reviewer = null;
		} else {
			this.reviewer = reviewer;
		}
	}

	/**
	 * Sets the application's processPaperwork field.
	 * 
	 * @param processPaperwork the processPaperwork to set
	 */
	private void setProcessPaperwork(boolean processPaperwork) {
		// The Application cannot be in the Interview, Review state, or
		// (waitlisted and new) or (closed and new) if processed is true.
		if (processPaperwork && (state == reviewState || state == interviewState
				|| state == waitlistState && appType == AppType.NEW
				|| state == closedState && appType == AppType.NEW)) {
			throw new IllegalArgumentException("Application cannot be created.");
		} else if (!processPaperwork && (state == refChkState || state == offerState)) {
			throw new IllegalArgumentException("Application cannot be created.");
		} else {
			this.processPaperwork = processPaperwork;
		}
	}

	/**
	 * Sets the application's resolution field.
	 * 
	 * @param resolution the resolution to set
	 */
	private void setResolution(String resolution) {
		if ("".equals(resolution) && state != closedState && state != waitlistState) {
			this.resolution = null;
		} else if (Command.R_REVCOMPLETED.equals(resolution) && (state == waitlistState || state == closedState)) {
			this.resolution = Resolution.REVCOMPLETED;
		} else if (Command.R_INTCOMPLETED.equals(resolution) && (state == waitlistState || state == closedState)
				&& appType != AppType.NEW) {
			this.resolution = Resolution.INTCOMPLETED;
		} else if (Command.R_REFCHKCOMPLETED.equals(resolution) && state == closedState) {
			this.resolution = Resolution.REFCHKCOMPLETED;
		} else if (Command.R_OFFERCOMPLETED.equals(resolution) && state == closedState) {
			this.resolution = Resolution.OFFERCOMPLETED;
		} else {
			throw new IllegalArgumentException("Application cannot be created.");
		}
	}

	/**
	 * Sets the application's notes field.
	 * 
	 * @param notes the notes to set
	 */
	private void setNotes(ArrayList<String> notes) {
		if (notes == null || notes.size() == 0) {
			throw new IllegalArgumentException("Application cannot be created.");
		}
		this.notes = notes;
	}

	/**
	 * Returns the application's appId field.
	 * 
	 * @return the appId
	 */
	public int getAppId() {
		return appId;
	}

	/**
	 * Returns a String of the application's state field.
	 * 
	 * @return the state name
	 */
	public String getStateName() {
		if (state == reviewState) {
			return REVIEW_NAME;
		} else if (state == interviewState) {
			return INTERVIEW_NAME;
		} else if (state == refChkState) {
			return REFCHK_NAME;
		} else if (state == offerState) {
			return OFFER_NAME;
		} else if (state == waitlistState) {
			return WAITLIST_NAME;
		} else if (state == closedState) {
			return CLOSED_NAME;
		} else {
			return "";
		}
	}

	/**
	 * Returns the application's appType field.
	 * 
	 * @return the appType
	 */
	public String getAppType() {
		if (appType == AppType.NEW) {
			return A_NEW;
		} else if (appType == AppType.OLD) {
			return A_OLD;
		} else if (appType == AppType.HIRED) {
			return A_HIRED;
		} else {
			return "";
		}
	}

	/**
	 * Returns the application's summary field.
	 * 
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * Returns the application's reviewer field.
	 * 
	 * @return the reviewer
	 */
	public String getReviewer() {
		return reviewer;
	}

	/**
	 * Returns the application's processPaperwork field.
	 * 
	 * @return the processPaperwork
	 */
	public boolean isProcessed() {
		return processPaperwork;
	}

	/**
	 * Returns the application's resolution field.
	 * 
	 * @return the resolution
	 */
	public String getResolution() {
		if (resolution == Resolution.REVCOMPLETED) {
			return Command.R_REVCOMPLETED;
		} else if (resolution == Resolution.INTCOMPLETED) {
			return Command.R_INTCOMPLETED;
		} else if (resolution == Resolution.REFCHKCOMPLETED) {
			return Command.R_REFCHKCOMPLETED;
		} else if (resolution == Resolution.OFFERCOMPLETED) {
			return Command.R_OFFERCOMPLETED;
		} else {
			return null;
		}
	}

	/**
	 * Returns the application's notes field.
	 * 
	 * @return the notes
	 */
	public ArrayList<String> getNotes() {
		return notes;
	}

	/**
	 * Returns String representation of the notes Array
	 * 
	 * @return String representation of notes array
	 */
	public String getNotesString() {
		String notesString = "";
		for (int i = 0; i < notes.size(); i++) {
			notesString += "-" + notes.get(i) + "\n";
		}
		return notesString;
	}

	/**
	 * Returns String representation of the Application that is printed during file
	 * save operations.
	 * 
	 * @return String representation of an Application
	 */
	@Override
	public String toString() {
		String appString = "*" + appId + "," + getStateName() + "," + getAppType() + "," + summary + "," + reviewer
				+ "," + processPaperwork + ",";

		if (resolution == null) {
			appString += "";
		} else {
			appString += getResolution();
		}

		appString += "\n" + getNotesString();
		return appString;
	}

	/**
	 * Adds the given note to the Notes array with the state name in brackets
	 * prepended to the note.
	 * 
	 * @param note the note to add to notes array
	 */
	private void addNote(String note) {
		if (note == null || "".equals(note)) {
			throw new IllegalArgumentException("Invalid information.");
		}
		notes.add("[" + getStateName() + "] " + note);
	}

	/**
	 * Delegates to the current state's updateState(Command) method. Update the
	 * Application based on the given Command. An UnsupportedOperationException is
	 * thrown if the Command is not a valid action for the given state.
	 * 
	 * @param command Command describing the action that will update the
	 *                Application's state.
	 * @throws UnsupportedOperationException if the Command is not a valid action
	 *                                       for the given state.
	 */
	public void update(Command command) {
		try {
			state.updateState(command);
		} catch (UnsupportedOperationException e) {
			throw new UnsupportedOperationException("Invalid information.");
		}
	}

	/**
	 * Interface for states in the Application State Pattern. All concrete
	 * application states must implement the AppState interface. The AppState
	 * interface should be a private interface of the Application class.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private interface AppState {

		/**
		 * Update the Application based on the given Command. An
		 * UnsupportedOperationException is thrown if the Command is not a valid action
		 * for the given state.
		 * 
		 * @param command Command describing the action that will update the
		 *                Application's state.
		 * @throws UnsupportedOperationException if the Command is not a valid action
		 *                                       for the given state.
		 */
		void updateState(Command command);

		/**
		 * Returns the name of the current state as a String.
		 * 
		 * @return the name of the current state as a String.
		 */
		String getStateName();

	}

	/**
	 * Concrete class for the AppState interface representing the Review phase of an
	 * application cycle.
	 */
	private class ReviewState implements AppState {

		/**
		 * Update the Application based on the given Command. An
		 * UnsupportedOperationException is thrown if the Command is not a valid action
		 * for the given state.
		 * 
		 * @param c Command describing the action that will update the Application's
		 *          state.
		 * @throws UnsupportedOperationException "Invalid information." if the Command
		 *                                       is not a valid action for the given
		 *                                       state.
		 */
		public void updateState(Command c) {
			if (c.getCommand() == CommandValue.ACCEPT) {
				reviewer = c.getReviewerId();
				appType = AppType.OLD;
				state = interviewState;
			} else if (c.getCommand() == CommandValue.STANDBY && appType == AppType.NEW) {
				resolution = Resolution.REVCOMPLETED;
				reviewer = c.getReviewerId();
				state = waitlistState;
			} else if (c.getCommand() == CommandValue.REJECT) {
				resolution = Resolution.REVCOMPLETED;
				reviewer = c.getReviewerId();
				state = closedState;
			} else {
				throw new UnsupportedOperationException("Invalid information.");
			}
			addNote(c.getNote());
		}

		/**
		 * Returns String of state name
		 * 
		 * @return state name
		 */
		@Override
		public String getStateName() {
			return REVIEW_NAME;
		}
	}

	/**
	 * Concrete class for the AppState interface representing the Interview phase of
	 * an application cycle.
	 */
	private class InterviewState implements AppState {

		/**
		 * Update the Application based on the given Command. An
		 * UnsupportedOperationException is thrown if the Command is not a valid action
		 * for the given state.
		 * 
		 * @param c Command describing the action that will update the Application's
		 *          state.
		 * @throws UnsupportedOperationException "Invalid information." if the Command
		 *                                       is not a valid action for the given
		 *                                       state.
		 */
		public void updateState(Command c) {
			if (c.getCommand() == CommandValue.REOPEN) {
				throw new UnsupportedOperationException("Invalid information.");
			} else if (c.getCommand() == CommandValue.ACCEPT) {
				reviewer = c.getReviewerId();
				processPaperwork = true;
				state = refChkState;
			} else if (c.getCommand() == CommandValue.STANDBY) {
				reviewer = c.getReviewerId();
				resolution = Resolution.INTCOMPLETED;
				state = waitlistState;
			} else if (c.getCommand() == CommandValue.REJECT) {
				resolution = Resolution.INTCOMPLETED;
				reviewer = c.getReviewerId();
				state = closedState;
			}
			addNote(c.getNote());
		}

		/**
		 * Returns String of state name
		 * 
		 * @return state name
		 */
		@Override
		public String getStateName() {
			return INTERVIEW_NAME;
		}
	}

	/**
	 * Concrete class for the AppState interface representing the Reference Check
	 * phase of an application cycle.
	 */
	private class RefChkState implements AppState {

		/**
		 * Update the Application based on the given Command. An
		 * UnsupportedOperationException is thrown if the Command is not a valid action
		 * for the given state.
		 * 
		 * @param c Command describing the action that will update the Application's
		 *          state.
		 * @throws UnsupportedOperationException "Invalid information." if the Command
		 *                                       is not a valid action for the given
		 *                                       state.
		 */
		public void updateState(Command c) {
			if (c.getCommand() == CommandValue.REOPEN || c.getCommand() == CommandValue.STANDBY) {
				throw new UnsupportedOperationException("Invalid information.");
			} else if (c.getCommand() == CommandValue.ACCEPT) {
				reviewer = c.getReviewerId();
				processPaperwork = true;
				state = offerState;
			} else if (c.getCommand() == CommandValue.REJECT) {
				resolution = Resolution.REFCHKCOMPLETED;
				reviewer = c.getReviewerId();
				state = closedState;
			}
			addNote(c.getNote());
		}

		/**
		 * Returns String of state name
		 * 
		 * @return state name
		 */
		@Override
		public String getStateName() {
			return REFCHK_NAME;
		}
	}

	/**
	 * Concrete class for the AppState interface representing the Offer phase of an
	 * application cycle.
	 */
	private class OfferState implements AppState {

		/**
		 * Update the Application based on the given Command. An
		 * UnsupportedOperationException is thrown if the Command is not a valid action
		 * for the given state.
		 * 
		 * @param c Command describing the action that will update the Application's
		 *          state.
		 * @throws UnsupportedOperationException "Invalid information." if the Command
		 *                                       is not a valid action for the given
		 *                                       state.
		 */
		public void updateState(Command c) {
			if (c.getCommand() == CommandValue.REOPEN || c.getCommand() == CommandValue.STANDBY) {
				throw new UnsupportedOperationException("Invalid information.");
			} else if (c.getCommand() == CommandValue.ACCEPT) {
				reviewer = c.getReviewerId();
				processPaperwork = true;
				resolution = Resolution.OFFERCOMPLETED;
				appType = AppType.HIRED;
				state = closedState;
			} else if (c.getCommand() == CommandValue.REJECT) {
				resolution = Resolution.OFFERCOMPLETED;
				reviewer = c.getReviewerId();
				state = closedState;
			}
			addNote(c.getNote());
		}

		/**
		 * Returns String of state name
		 * 
		 * @return state name
		 */
		@Override
		public String getStateName() {
			return OFFER_NAME;
		}
	}

	/**
	 * Concrete class for the AppState interface representing the Waitlist phase of
	 * an application cycle.
	 */
	private class WaitlistState implements AppState {

		/**
		 * Update the Application based on the given Command. An
		 * UnsupportedOperationException is thrown if the Command is not a valid action
		 * for the given state.
		 * 
		 * @param c Command describing the action that will update the Application's
		 *          state.
		 * @throws UnsupportedOperationException "Invalid information." if the Command
		 *                                       is not a valid action for the given
		 *                                       state.
		 */
		public void updateState(Command c) {
			if (resolution == Resolution.INTCOMPLETED && c.getCommand() == CommandValue.REOPEN) {
				reviewer = c.getReviewerId();
				processPaperwork = true;
				resolution = null;
				state = refChkState;
			} else if (resolution == Resolution.REVCOMPLETED && appType == AppType.NEW
					&& c.getCommand() == CommandValue.REOPEN) {
				appType = AppType.OLD;
				resolution = null;
				state = reviewState;
			} else {
				throw new UnsupportedOperationException("Invalid information.");
			}
			addNote(c.getNote());
		}

		/**
		 * Returns String of state name
		 * 
		 * @return state name
		 */
		@Override
		public String getStateName() {
			return WAITLIST_NAME;
		}
	}

	/**
	 * Concrete class for the AppState interface representing the Closed phase of an
	 * application cycle.
	 */
	private class ClosedState implements AppState {

		/**
		 * Update the Application based on the given Command. An
		 * UnsupportedOperationException is thrown if the Command is not a valid action
		 * for the given state.
		 * 
		 * @param c Command describing the action that will update the Application's
		 *          state.
		 * @throws UnsupportedOperationException "Invalid information." if the Command
		 *                                       is not a valid action for the given
		 *                                       state.
		 */
		public void updateState(Command c) {
			if (c.getCommand() == CommandValue.ACCEPT || c.getCommand() == CommandValue.STANDBY
					|| c.getCommand() == CommandValue.REJECT) {
				throw new UnsupportedOperationException("Invalid information.");
			} else if (resolution == Resolution.REVCOMPLETED && appType == AppType.NEW
					&& c.getCommand() == CommandValue.REOPEN) {
				appType = AppType.OLD;
				resolution = null;
				state = reviewState;
			}
			addNote(c.getNote());
		}

		/**
		 * Returns String of state name
		 * 
		 * @return state name
		 */
		@Override
		public String getStateName() {
			return CLOSED_NAME;
		}
	}

};