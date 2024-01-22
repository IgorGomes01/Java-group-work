package org.projektarbete;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import static org.junit.Assert.assertTrue;

public class UserInterfaceTest {

    /**
     * Testar metoden displayMainMenu i UserInterface.
     * Den redirectar System.out för att fånga det utskrivna resultatet,
     * kallar på displayMainMenu och jämför det förväntade resultatet med det fångade resultatet.
     */
    @Test
    public void testDisplayMainMenu() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        UserInterface.displayMainMenu();

        System.setOut(System.out);

        String menuOutput = outputStream.toString();

        assertTrue(menuOutput.contains("LÄGG TILL NYTT MÖTE"));
        assertTrue(menuOutput.contains("SÖK MÖTE"));
    }

    /**
     * Testar metoden processMainMenuOption med alternativet för att lägga till ett nytt möte.
     * Mockar användarinput för att simulera valet av "LÄGG TILL NYTT MÖTE",
     * redirectar System.out för att fånga det utskrivna resultatet,
     * kallar på processMainMenuOption och jämför det förväntade resultatet med det fångade resultatet.
     */
    @Test
    public void testProcessMainMenuOption_AddAppointmentOption() {
        InputStream inputStream = new ByteArrayInputStream("1\n".getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        UserInterface.processMainMenuOption(UserInterface.ADD_APPOINTMENT_OPTION);

        System.setIn(System.in);
        System.setOut(System.out);

        String output = outputStream.toString();

        assertTrue(output.contains("Lägg till nytt möte"));
    }

    /**
     * Testar hantering av InputMismatchException.
     * Mockar användarinput för att simulera att användaren anger en icke-integer,
     * redirectar System.out för att fånga det utskrivna resultatet,
     * kallar på mainMenuOptions och jämför det förväntade resultatet med det fångade resultatet.
     */
    @Test
    public void testInputMismatchExceptionHandling() {
        InputStream inputStream = new ByteArrayInputStream("abc\n".getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        UserInterface.mainMenuOptions();

        System.setIn(System.in);
        System.setOut(System.out);

        String output = outputStream.toString();

        assertTrue(output.contains("Var god ange ett giltigt heltal."));
    }

    /**
     * Testar hantering av NoSuchElementException.
     * Mockar användarinput för att simulera att ingen input är tillgänglig,
     * redirectar System.out för att fånga det utskrivna resultatet,
     * kallar på mainMenuOptions och jämför det förväntade resultatet med det fångade resultatet.
     */
    @Test
    public void testNoSuchElementExceptionHandling() {
        InputStream inputStream = new ByteArrayInputStream("".getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        UserInterface.mainMenuOptions();

        System.setIn(System.in);
        System.setOut(System.out);

        String output = outputStream.toString();

        assertTrue(output.contains("Var god ange ett giltigt heltal."));
    }
}