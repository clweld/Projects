package app_manager.model.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import app_manager.model.application.Application;

/**
 * Tests the AppReader class.
 * 
 * @author Christine Weld
 */
public class AppReaderTest {

	/** Valid app file 1 */
	private final String validFileApp1 = "test-files/app1.txt";
	/** Valid app file 2 */
	private final String validFileApp2 = "test-files/app2.txt";

	/**
	 * Tests AppReader.readAppsFromFile() with valid file App1
	 */
	@Test
	public void testReadValidFileApp1() {
		ArrayList<Application> apps = AppReader.readAppsFromFile(validFileApp1);
		assertEquals(6, apps.size());
	}

	/**
	 * Tests AppReader.readAppsFromFile() with valid file App2
	 */
	@Test
	public void testReadValidFileApp2() {
		ArrayList<Application> apps = AppReader.readAppsFromFile(validFileApp2);
		assertEquals(7, apps.size());
	}

	/**
	 * Tests AppReader.readAppsFromFile() with invalid files
	 * 
	 * @param invalidFile file with invalid application fields
	 */
	@ParameterizedTest
	@ValueSource(strings = { "test-files/app3.txt", "test-files/app4.txt", "test-files/app5.txt", "test-files/app6.txt",
			"test-files/app7.txt", "test-files/app8.txt", "test-files/app9.txt", "test-files/app10.txt",
			"test-files/app11.txt", "test-files/app12.txt", "test-files/app13.txt", "test-files/app14.txt",
			"test-files/app15.txt", "test-files/app16.txt", "test-files/app17.txt", "test-files/app18.txt" })
	public void testReadInvalidFiles(String invalidFile) {
		Exception e = assertThrows(IllegalArgumentException.class, () -> AppReader.readAppsFromFile(invalidFile));
		assertEquals("Unable to load file.", e.getMessage());
	}

}
