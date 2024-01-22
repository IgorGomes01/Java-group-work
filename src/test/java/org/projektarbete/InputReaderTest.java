package org.projektarbete;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeEach;

public class InputReaderTest extends TestCase {

    private InputReader inputReader;

    @BeforeEach
    void testSetUp() {
        // Skapa en instans av InputReader innan varje test
        inputReader = new InputReader();
    }

    @AfterEach
    void tearDown() {
        // Stäng InputReader efter varje test
        inputReader.close();
    }

    @Test
    void testReadString() {
        // Mocka System.in för att simulera inmatning från användaren
        String simulatedInput = "Test input";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);

        // Anropa metoden och verifiera att den returnerar förväntat värde
        String result = inputReader.readString("Prompt");
        assertEquals("Test input", result);

        // Återställ System.in efter testet
        System.setIn(System.in);
    }

    void testNextLine() {
        // Mocka System.in för att simulera inmatning från användaren
        String simulatedInput = "Test input";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);

        // Anropa metoden och verifiera att den returnerar förväntat värde
        String result = inputReader.nextLine();
        assertEquals("Test input", result);

        // Återställ System.in efter testet
        System.setIn(System.in);
    }

    @Test
    void testNext() {
        // Mocka System.in för att simulera inmatning från användaren
        String simulatedInput = "Test input";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);

        // Anropa metoden och verifiera att den returnerar förväntat värde
        String result = inputReader.next();
        assertEquals("Test", result);

        // Återställ System.in efter testet
        System.setIn(System.in);
    }

    @Test
    void testNextInt() {
        // Mocka System.in för att simulera inmatning från användaren
        String simulatedInput = "42";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);

        // Anropa metoden och verifiera att den returnerar förväntat värde
        int result = inputReader.nextInt();
        assertEquals(42, result);

        // Återställ System.in efter testet
        System.setIn(System.in);
    }
}

