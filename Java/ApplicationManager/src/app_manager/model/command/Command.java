package app_manager.model.command;

/**
 * Encapsulates user actions that cause a state transition for an application.
 * 
 * @author Christine Weld
 */
public class Command {

	/** String constant for transition out of the Review state */
	public static final String R_REVCOMPLETED = "ReviewCompleted";
	/** String constant for transition out of the Interview state */
	public static final String R_INTCOMPLETED = "InterviewCompleted";
	/** String constant for transition out of the RefCheck state */
	public static final String R_REFCHKCOMPLETED = "ReferenceCheckCompleted";
	/** String constant for transition out of the Offer state */
	public static final String R_OFFERCOMPLETED = "OfferCompleted";
	/** User-input string for reviewer assigned to application */
	private String reviewerId;
	/** User-input string for note added to Notes array */
	private String note;
	/** Current command */
	private CommandValue command;
	/** Current resolution */
	private Resolution resolution;

	/** Represents the four possible commands that a user can make. */
	public enum CommandValue {
		/** command Accept */
		ACCEPT,
		/** command Reject */
		REJECT,
		/** command Standby */
		STANDBY,
		/** command Reopen */
		REOPEN
	}

	/** Represents the four possible ways a user can resolve an application. */
	public enum Resolution {
		/** resolution ReviewCompleted */
		REVCOMPLETED,
		/** resolution InterviewCompleted */
		INTCOMPLETED,
		/** resolution RefCheckCompleted */
		REFCHKCOMPLETED,
		/** resolution OfferCompleted */
		OFFERCOMPLETED
	}

	/**
	 * Constructs a Command object representing user input of action to update an
	 * application.
	 * 
	 * @param c          CommandValue action selection by user
	 * @param reviewerId String input by user to assign reviewer to application
	 * @param r          Resolution outcome for application
	 * @param note       String input by user to add to Application Notes array
	 * @throws IllegalArgumentException "Invalid information." when parameters are
	 *                                  incorrect.
	 */
	public Command(CommandValue c, String reviewerId, Resolution r, String note) {
		if (c == null) {
			throw new IllegalArgumentException("Invalid information.");
		} else if (c == CommandValue.ACCEPT && (reviewerId == null || "".equals(reviewerId))) {
			throw new IllegalArgumentException("Invalid information.");
		} else if ((c == CommandValue.STANDBY || c == CommandValue.REJECT) && r == null) {
			throw new IllegalArgumentException("Invalid information.");
		} else if (note == null || "".equals(note)) {
			throw new IllegalArgumentException("Invalid information.");
		}

		this.command = c;
		this.reviewerId = reviewerId;
		this.resolution = r;
		this.note = note;
	}

	/**
	 * Returns the CommandValue field.
	 * 
	 * @return the CommandValue
	 */
	public CommandValue getCommand() {
		return command;
	}

	/**
	 * Returns the reviewerId field.
	 * 
	 * @return the reviewerId
	 */
	public String getReviewerId() {
		return reviewerId;
	}

	/**
	 * Returns the resolution field.
	 * 
	 * @return the resolution
	 */
	public Resolution getResolution() {
		return resolution;
	}

	/**
	 * Returns the note field.
	 * 
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
}
