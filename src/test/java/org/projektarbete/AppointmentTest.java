package org.projektarbete;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.format.DateTimeParseException;

/**
 * Enhetstester för Appointment-klassen.
 */
class AppointmentTest {

    /**
     * Testar konstruktorn med giltig inmatning för att säkerställa att ett mötesobjekt skapas utan problem.
     * Förväntad utgång: Inget undantag förväntas kastas.
     */
    @Test
    void constructor_ValidInput_CreatesAppointment() {
        // Testdata
        String name = "John Doe";
        String idNumber = "1234567890";
        String email = "john.doe@example.com";
        String date = "2023-01-01";
        String time = "14:30";
        String description = "Mötesbeskrivning";

        // Testning
        assertDoesNotThrow(() -> new Appointment(name, idNumber, email, date, time, description));
    }

    /**
     * Testar getDate-metoden med giltig inmatning för att säkerställa att datum returneras korrekt.
     * Förväntad utgång: Förväntar att datumet returneras som "2023-01-01".
     */
    @Test
    void getDate_ValidInput_ReturnsFormattedDate() {
        // Testdata
        String name = "John Doe";
        String idNumber = "1234567890";
        String email = "john.doe@example.com";
        String date = "2023-01-01";
        String time = "14:30";
        String description = "Mötesbeskrivning";

        // Skapa möte
        Appointment appointment = new Appointment(name, idNumber, email, date, time, description);

        // Testning
        assertEquals("2023-01-01", appointment.getDate());
    }

    /**
     * Testar getTime-metoden med giltig inmatning för att säkerställa att tiden returneras korrekt.
     * Förväntad utgång: Förväntar att tiden returneras som "14:30".
     */
    @Test
    void getTime_ValidInput_ReturnsFormattedTime() {
        // Testdata
        String name = "John Doe";
        String idNumber = "1234567890";
        String email = "john.doe@example.com";
        String date = "2023-01-01";
        String time = "14:30";
        String description = "Mötesbeskrivning";

        // Skapa möte
        Appointment appointment = new Appointment(name, idNumber, email, date, time, description);

        // Testning
        assertEquals("14:30", appointment.getTime());
    }

    /**
     * Testar konstruktorn med ogiltigt datum som inmatning för att säkerställa att ett undantag kastas.
     * Förväntad utgång: Förväntar att ett DateTimeParseException-undantag kastas.
     */
    @Test
    void constructor_InvalidDateInput_ThrowsException() {
        // Testdata med ogiltigt datum
        String name = "John Doe";
        String idNumber = "1234567890";
        String email = "john.doe@example.com";
        String date = "ogiltigt-datum";
        String time = "14:30";
        String description = "Mötesbeskrivning";

        // Testning
        assertThrows(DateTimeParseException.class, () -> new Appointment(name, idNumber, email, date, time, description));
    }

    /**
     * Testar konstruktorn med ogiltig tid som inmatning för att säkerställa att ett undantag kastas.
     * Förväntad utgång: Förväntar att ett DateTimeParseException-undantag kastas.
     */
    @Test
    void constructor_InvalidTimeInput_ThrowsException() {
        // Testdata med ogiltig tid
        String name = "John Doe";
        String idNumber = "1234567890";
        String email = "john.doe@example.com";
        String date = "2023-01-01";
        String time = "ogiltig-tid";
        String description = "Mötesbeskrivning";

        // Testning
        assertThrows(DateTimeParseException.class, () -> new Appointment(name, idNumber, email, date, time, description));
    }
}
