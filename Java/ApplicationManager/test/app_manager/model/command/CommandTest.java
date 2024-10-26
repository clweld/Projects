package app_manager.model.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import app_manager.model.command.Command.CommandValue;
import app_manager.model.command.Command.Resolution;

/**
 * Tests the Command class. Individual tests for getters and setters are omitted
 * and are tested within other test methods.
 * 
 * @author Christine Weld
 */
public class CommandTest {

	/**
	 * Tests Command constructor and getters.
	 */
	@Test
	public void testCommandValid() {
		Command command = new Command(CommandValue.ACCEPT, "reviewerTest", Resolution.INTCOMPLETED, "noteTest");
		assertEquals(CommandValue.ACCEPT, command.getCommand());
		assertEquals("reviewerTest", command.getReviewerId());
		assertEquals(Resolution.INTCOMPLETED, command.getResolution());
		assertEquals("noteTest", command.getNote());
	}

	/**
	 * Tests Command constructor with invalid param c = null
	 */
	@Test
	public void testCommandInvalidCNull() {
		Exception e = assertThrows(IllegalArgumentException.class,
				() -> new Command(null, "reviewerTest", Resolution.INTCOMPLETED, "noteTest"));
		assertEquals("Invalid information.", e.getMessage());
	}

	/**
	 * Tests Command constructor with invalid params c = Accept, reviewer = null or
	 * "".
	 */
	@Test
	public void testCommandInvalidCAcceptRevNullEmpty() {
		// reviewerId = null
		Exception e1 = assertThrows(IllegalArgumentException.class,
				() -> new Command(CommandValue.ACCEPT, null, Resolution.INTCOMPLETED, "noteTest"));
		assertEquals("Invalid information.", e1.getMessage());

		// reviewerId = ""
		Exception e2 = assertThrows(IllegalArgumentException.class,
				() -> new Command(CommandValue.ACCEPT, "", Resolution.INTCOMPLETED, "noteTest"));
		assertEquals("Invalid information.", e2.getMessage());
	}

	/**
	 * Tests Command constructor with invalid params c = Standby or Reject, reviewer
	 * = null.
	 */
	@Test
	public void testCommandInvalidCStandbyRejectRevNull() {
		// CommandValue = Standby
		Exception e1 = assertThrows(IllegalArgumentException.class,
				() -> new Command(CommandValue.STANDBY, "reviewerTest", null, "noteTest"));
		assertEquals("Invalid information.", e1.getMessage());

		// CommandValue = Reject
		Exception e2 = assertThrows(IllegalArgumentException.class,
				() -> new Command(CommandValue.REJECT, "reviewerTest", null, "noteTest"));
		assertEquals("Invalid information.", e2.getMessage());
	}
	
	/**
	 * Tests Command constructor with invalid params note = null or "".
	 */
	@Test
	public void testCommandInvalidNoteNullEmpty() {
		// note = null
		Exception e1 = assertThrows(IllegalArgumentException.class,
				() -> new Command(CommandValue.ACCEPT, "reviewerTest", Resolution.INTCOMPLETED, null));
		assertEquals("Invalid information.", e1.getMessage());

		// note = ""
		Exception e2 = assertThrows(IllegalArgumentException.class,
				() -> new Command(CommandValue.ACCEPT, "reviewerTest", Resolution.INTCOMPLETED, ""));
		assertEquals("Invalid information.", e2.getMessage());
	}

}
