package org.projektarbete;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class UserInterfaceTest {

    // Testar om displayMainMenu genererar förväntad menyutmatning
    @Test
    public void testDisplayMainMenu() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        UserInterface.displayMainMenu();

        String menuOutput = outputStream.toString();
        assertTrue(menuOutput.contains("LÄGG TILL NYTT MÖTE"));
        assertTrue(menuOutput.contains("SÖK MÖTE"));
        // Lägg till fler assertions baserat på förväntat menytillstånd

        System.setOut(System.out); // Återställ System.out
    }

    // Testar om displayOption genererar förväntad utmatning för ett alternativ
    @Test
    public void testDisplayOption() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        int option = 1;
        String description = "Test Description";
        UserInterface.displayOption(option, description);

        String output = outputStream.toString();
        assertTrue(output.contains(String.format(" %d - %-30s%n", option, description)));

        System.setOut(System.out); // Återställ System.out
    }

    // Testar om processMainMenuOption hanterar ogiltigt alternativ korrekt
    @Test
    public void testProcessMainMenuOption_InvalidOption() {
        InputStream inputStream = new ByteArrayInputStream("7\n".getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        UserInterface.processMainMenuOption(7);

        String output = outputStream.toString();
        assertTrue(output.contains("Ogiltigt alternativ"));
    }

    // Testar om displaySubMenu genererar förväntad resultat
    @Test
    public void testDisplaySubMenu() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        String title = "Test Title";
        UserInterface.displaySubMenu(title);

        String output = outputStream.toString();
        assertTrue(output.contains("------------------------------------------------------------"));
        assertTrue(output.contains(String.format("                     %s%n", title)));
        assertTrue(output.contains("------------------------------------------------------------"));

        System.setOut(System.out); // Återställ System.out
    }
}