package app_manager.model.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import app_manager.model.application.Application.AppType;
import app_manager.model.command.Command;
import app_manager.model.command.Command.CommandValue;
import app_manager.model.command.Command.Resolution;

/**
 * Tests the Application class, including inner State classes. Individual tests
 * for getters and setters are omitted and are tested within other test methods.
 * 
 * @author Christine Weld
 */
public class ApplicationTest {

	/** App id */
	private static final int APP_ID = 1;
	/** App state string */
	private static final String STATE_STR = "Review";
	/** App type string */
	private static final String APP_TYPE_STR = "New";
	/** App type AppType */
	private static final AppType APP_TYPE = AppType.NEW;
	/** App summary */
	private static final String SUMMARY = "summaryTest";
	/** App reviewer */
	private static final String REVIEWER = "reviewerTest";
	/** App processed */
	private static final boolean PROCESSED = false;
	/** App resolution string */
	private static final String RESOLUTION_STR = "";
	/** App notes */
	private static final ArrayList<String> NOTES = new ArrayList<String>();
	/** App note */
	private static final String NOTE = "noteTest";

	/**
	 * Tests Application constructor with 4 parameters (appId, appType, summary,
	 * note).
	 */
	@Test
	public void testApplicationFourParam() {
		// valid params
		Application app = new Application(APP_ID, APP_TYPE, SUMMARY, NOTE);
		assertEquals(1, app.getAppId());
		assertEquals("New", app.getAppType());
		assertEquals("summaryTest", app.getSummary());

		// invalid id
		Exception e1 = assertThrows(IllegalArgumentException.class, () -> new Application(0, APP_TYPE, SUMMARY, NOTE));
		assertEquals("Application cannot be created.", e1.getMessage());

		// invalid appType
		Exception e2 = assertThrows(IllegalArgumentException.class, () -> new Application(APP_ID, null, SUMMARY, NOTE));
		assertEquals("Application cannot be created.", e2.getMessage());

		// invalid summary
		Exception e3 = assertThrows(IllegalArgumentException.class,
				() -> new Application(APP_ID, APP_TYPE, null, NOTE));
		assertEquals("Application cannot be created.", e3.getMessage());

		// invalid note
		Exception e4 = assertThrows(IllegalArgumentException.class,
				() -> new Application(APP_ID, APP_TYPE, SUMMARY, null));
		assertEquals("Application cannot be created.", e4.getMessage());
	}

	/**
	 * Tests Application constructor with 8 parameters (appId, state, appType,
	 * summary, reviewer, processPaperwork, resolution, notes).
	 */
	@Test
	public void testApplicationEightParam() {
		// valid params
		NOTES.add(NOTE);
		Application app = new Application(APP_ID, STATE_STR, APP_TYPE_STR, SUMMARY, REVIEWER, PROCESSED, RESOLUTION_STR,
				NOTES);
		assertEquals(1, app.getAppId());
		assertEquals("Review", app.getStateName());
		assertEquals("New", app.getAppType());
		assertEquals("summaryTest", app.getSummary());
		assertEquals("reviewerTest", app.getReviewer());
		assertEquals(false, app.isProcessed());
		assertEquals("-noteTest\n", app.getNotesString());

		// invalid state
		Exception e1 = assertThrows(IllegalArgumentException.class,
				() -> new Application(APP_ID, null, APP_TYPE_STR, SUMMARY, REVIEWER, PROCESSED, RESOLUTION_STR, NOTES));
		assertEquals("Application cannot be created.", e1.getMessage());

		// invalid app type str
		Exception e2 = assertThrows(IllegalArgumentException.class,
				() -> new Application(APP_ID, STATE_STR, null, SUMMARY, REVIEWER, PROCESSED, RESOLUTION_STR, NOTES));
		assertEquals("Application cannot be created.", e2.getMessage());

		// invalid reviewer
		Exception e3 = assertThrows(IllegalArgumentException.class,
				() -> new Application(APP_ID, "Offer", APP_TYPE_STR, SUMMARY, null, PROCESSED, RESOLUTION_STR, NOTES));
		assertEquals("Application cannot be created.", e3.getMessage());

		// invalid resolution
		Exception e4 = assertThrows(IllegalArgumentException.class,
				() -> new Application(APP_ID, STATE_STR, APP_TYPE_STR, SUMMARY, REVIEWER, PROCESSED, null, NOTES));
		assertEquals("Application cannot be created.", e4.getMessage());

		// invalid notes
		Exception e5 = assertThrows(IllegalArgumentException.class, () -> new Application(APP_ID, STATE_STR,
				APP_TYPE_STR, SUMMARY, REVIEWER, PROCESSED, RESOLUTION_STR, null));
		assertEquals("Application cannot be created.", e5.getMessage());

	}

	/**
	 * Tests Application.toString()
	 */
	@Test
	public void testToString() {
		Application app = new Application(APP_ID, STATE_STR, APP_TYPE_STR, SUMMARY, REVIEWER, PROCESSED, RESOLUTION_STR,
				NOTES);
		assertEquals("*1,Review,New,summaryTest,reviewerTest,false,\n-noteTest\n", app.toString());
	}

	/**
	 * Tests Application.update() transitions through ReviewA, InterviewA,
	 * RefCheckA, OfferA on FSM diagram
	 */
	@Test
	public void testUpdateFSMOfferA() {
		Application app = new Application(APP_ID, APP_TYPE, SUMMARY, NOTE);
		assertEquals("Review", app.getStateName());

		// ReviewA
		app_manager.model.command.Command acceptReview = new Command(CommandValue.ACCEPT, "reviewer review test", Resolution.REVCOMPLETED,
				"note review test");
		app.update(acceptReview);
		assertEquals(acceptReview.getReviewerId(), app.getReviewer());
		assertEquals("-[Review] noteTest\n-[Interview] note review test\n", app.getNotesString());

		// InterviewA
		Command acceptInterview = new Command(CommandValue.ACCEPT, "reviewer interview test", Resolution.INTCOMPLETED,
				"note interview test");
		app.update(acceptInterview);
		assertEquals(acceptInterview.getReviewerId(), app.getReviewer());
		assertEquals("-[Review] noteTest\n-[Interview] note review test\n-[RefCheck] note interview test\n",
				app.getNotesString());

		// RefCheckA
		Command acceptRefCheck = new Command(CommandValue.ACCEPT, "reviewer refcheck test", Resolution.REFCHKCOMPLETED,
				"note refcheck test");
		app.update(acceptRefCheck);
		assertEquals(acceptRefCheck.getReviewerId(), app.getReviewer());
		assertEquals(true, app.isProcessed());
		assertEquals(
				"-[Review] noteTest\n-[Interview] note review test\n-[RefCheck] note interview test\n-[Offer] note refcheck test\n",
				app.getNotesString());

		// OfferA
		Command acceptOffer = new Command(CommandValue.ACCEPT, "reviewer offer test", Resolution.OFFERCOMPLETED,
				"note offer test");
		app.update(acceptOffer);
		assertEquals("Closed", app.getStateName());
		assertEquals("OfferCompleted", app.getResolution());
		assertEquals(
				"-[Review] noteTest\n-[Interview] note review test\n-[RefCheck] note interview test\n-[Offer] note refcheck test\n-[Closed] note offer test\n",
				app.getNotesString());
	}

	/**
	 * Tests Application.update() transitions through ReviewA, InterviewB,
	 * WaitlistB, RefCheckB on FSM diagram
	 */
	@Test
	public void testUpdateFSMRefCheckB() {
		Application app = new Application(APP_ID, APP_TYPE, SUMMARY, NOTE);
		assertEquals("Review", app.getStateName());

		// ReviewA
		Command acceptReview = new Command(CommandValue.ACCEPT, "reviewer review test", Resolution.REVCOMPLETED,
				"note review test");
		app.update(acceptReview);
		assertEquals(acceptReview.getReviewerId(), app.getReviewer());
		assertEquals("-[Review] noteTest\n-[Interview] note review test\n", app.getNotesString());

		// InterviewB
		Command standbyInterview = new Command(CommandValue.STANDBY, "reviewer interview test", Resolution.INTCOMPLETED,
				"note interview test");
		app.update(standbyInterview);
		assertEquals(standbyInterview.getReviewerId(), app.getReviewer());
		assertEquals("-[Review] noteTest\n-[Interview] note review test\n-[Waitlist] note interview test\n",
				app.getNotesString());

		// WaitlistB
		Command reopenWaitlist = new Command(CommandValue.REOPEN, "reviewer waitlist test", Resolution.INTCOMPLETED,
				"note waitlist test");
		app.update(reopenWaitlist);
		assertEquals(reopenWaitlist.getReviewerId(), app.getReviewer());
		assertEquals(
				"-[Review] noteTest\n-[Interview] note review test\n-[Waitlist] note interview test\n-[RefCheck] note waitlist test\n",
				app.getNotesString());

		// RefCheckB
		Command rejectRefCheck = new Command(CommandValue.REJECT, "reviewer refcheck test", Resolution.OFFERCOMPLETED,
				"note refcheck test");
		app.update(rejectRefCheck);
		assertEquals("Closed", app.getStateName());
		assertEquals("ReferenceCheckCompleted", app.getResolution());
		assertEquals(
				"-[Review] noteTest\n-[Interview] note review test\n-[Waitlist] note interview test\n-[RefCheck] note waitlist test\n-[Closed] note refcheck test\n",
				app.getNotesString());
	}

	/**
	 * Tests Application.update() transitions through ReviewC, ClosedA, ReviewA,
	 * InterviewC on FSM diagram
	 */
	@Test
	public void testUpdateFSMInterviewC() {
		Application app = new Application(APP_ID, APP_TYPE, SUMMARY, NOTE);
		assertEquals("Review", app.getStateName());

		// ReviewC
		Command rejectReview = new Command(CommandValue.REJECT, "reviewer review test", Resolution.REVCOMPLETED,
				"note review test");
		app.update(rejectReview);
		assertEquals("-[Review] noteTest\n-[Closed] note review test\n", app.getNotesString());

		// ClosedA
		Command reopenClosed = new Command(CommandValue.REOPEN, "reviewer closed test", Resolution.REVCOMPLETED,
				"note closed test");
		app.update(reopenClosed);
		assertEquals("-[Review] noteTest\n-[Closed] note review test\n-[Review] note closed test\n",
				app.getNotesString());

		// ReviewA
		Command acceptReview = new Command(CommandValue.ACCEPT, "reviewer review test", Resolution.REVCOMPLETED,
				"note review test");
		app.update(acceptReview);
		assertEquals(acceptReview.getReviewerId(), app.getReviewer());
		assertEquals(
				"-[Review] noteTest\n-[Closed] note review test\n-[Review] note closed test\n-[Interview] note review test\n",
				app.getNotesString());

		// InterviewC
		Command rejectInterview = new Command(CommandValue.REJECT, "reviewer interview test", Resolution.INTCOMPLETED,
				"note interview test");
		app.update(rejectInterview);
		assertEquals("Closed", app.getStateName());
		assertEquals("InterviewCompleted", app.getResolution());
		assertEquals(
				"-[Review] noteTest\n-[Closed] note review test\n-[Review] note closed test\n-[Interview] note review test\n-[Closed] note interview test\n",
				app.getNotesString());
	}

	/**
	 * Tests Application.update() transitions through ReviewB, WaitlistA, ReviewC on
	 * FSM diagram
	 */
	@Test
	public void testUpdateFSMWaitlistA() {
		Application app = new Application(APP_ID, APP_TYPE, SUMMARY, NOTE);
		assertEquals("Review", app.getStateName());

		// ReviewB
		Command standbyReview = new Command(CommandValue.STANDBY, "reviewer review test", Resolution.REVCOMPLETED,
				"note review test");
		app.update(standbyReview);
		assertEquals("-[Review] noteTest\n-[Waitlist] note review test\n", app.getNotesString());

		// WaitlistA
		Command reopenWaitlist = new Command(CommandValue.REOPEN, "reviewer waitlist test", Resolution.REVCOMPLETED,
				"note waitlist test");
		app.update(reopenWaitlist);
		assertEquals("-[Review] noteTest\n-[Waitlist] note review test\n-[Review] note waitlist test\n",
				app.getNotesString());

		// ReviewC
		Command rejectReview = new Command(CommandValue.REJECT, "reviewer review test", Resolution.REVCOMPLETED,
				"note review test");
		app.update(rejectReview);
		assertEquals(
				"-[Review] noteTest\n-[Waitlist] note review test\n-[Review] note waitlist test\n-[Closed] note review test\n",
				app.getNotesString());
		assertEquals("Closed", app.getStateName());
		assertEquals("ReviewCompleted", app.getResolution());
	}

}
