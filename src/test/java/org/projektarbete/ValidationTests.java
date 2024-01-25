package org.projektarbete;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Enhetstester för Validation-klassen.
 */
class ValidationTest {

    /**
     * Testar metoden validateAllFields med giltig inmatning för att säkerställa att inga felmeddelanden returneras.
     * Förväntad utgång: Inga felmeddelanden förväntas.
     */
    @Test
    void validateAllFields_ValidInput_NoErrors() {
        // Giltig inmatning för att säkerställa att inga felmeddelanden returneras
        List<String> errors = Validation.validateAllFields("Anna Svensson", "1987654321", "anna.svensson@gmail.com",
                "2024-01-26", "16:30", "Möte om personalutveckling");

        assertTrue(errors.isEmpty());
    }

    /**
     * Testar metoden validateName med ett null-namn för att säkerställa att ett felmeddelande genereras.
     * Förväntad utgång: Ett felmeddelande förväntas om namnet är null eller tomt.
     */
    @Test
    void validateName_NullName_Error() {
        // Null-namn för att säkerställa att ett felmeddelande genereras
        List<String> errors = Validation.validateAllFields(null, "9876543210", "james.suarez@hotmail.com",
                "2024-01-25", "12:00", "Möte om ekonomi");

        // Skriv ut felmeddelanden för felsökning
        System.out.println("Antal fel: " + errors.size());
        System.out.println("Felmeddelanden: " + errors);

        // Kontrollera antalet fel
        assertEquals(2, errors.size(), "Oväntat antal fel. Förväntat 2 fel.");

        // Skriv ut varje felmeddelande för att underlätta felsökning
        for (String error : errors) {
            System.out.println("Fel: " + error);
        }

        // Kontrollera om det förväntade felmeddelandet finns i listan
        assertTrue(errors.contains("Namnet får inte vara null eller tomt."),
                "Förväntat felmeddelande 'Namnet får inte vara null eller tomt.' hittades inte i felmeddelandelistan.");
        assertTrue(errors.contains("Tiden måste vara i framtiden."),
                "Förväntat felmeddelande 'Tiden måste vara i framtiden.' hittades inte i felmeddelandelistan.");
    }


    /**
     * Testar metoden validateIdNumber med ett ogiltigt personnummer för att säkerställa att ett felmeddelande genereras.
     * Förväntad utgång: Ett felmeddelande förväntas om personnumret är ogiltigt.
     */
    @Test
    void validateIdNumber_InvalidIdNumber_Error() {
        // Ogiltigt personnummer för att säkerställa att ett felmeddelande genereras
        List<String> errors = Validation.validateAllFields("Erik Andersson", "1234", "erik.andersson@gmail.com",
                "2024-01-25", "12:00", "Möte om hållbarhet");

        // Filter out errors other than "Ogiltigt personnummer"
        List<String> filteredErrors = errors.stream()
                .filter(error -> error.equals("Ogiltigt personnummer. Personnumret måste vara exakt 10 siffror utan andra tecken."))
                .collect(Collectors.toList());

        assertEquals(1, filteredErrors.size());
        assertEquals("Ogiltigt personnummer. Personnumret måste vara exakt 10 siffror utan andra tecken.", filteredErrors.get(0));
    }


    /**
     * Testar metoden validateEmail med en ogiltig e-postadress för att säkerställa att ett felmeddelande genereras.
     * Förväntad utgång: Två felmeddelande förväntas om e-postadressen är ogiltig och iden måste vara i framtiden.
     */
    @Test
    void validateEmail_InvalidEmail_Error() {
        // Ogiltig e-postadress för att säkerställa att ett felmeddelande genereras
        List<String> errors = Validation.validateAllFields("Maria Karlsson", "8765432109", "maria.karlssonhotmail.com",
                "2024-01-25", "12:00", "Möte om projekt");

        // Skriv ut felmeddelanden för felsökning
        System.out.println("Antal fel: " + errors.size());
        System.out.println("Felmeddelanden: " + errors);

        // Kontrollera antalet fel
        assertEquals(2, errors.size(), "Oväntat antal fel. Förväntat 2 fel.");

        // Skriv ut varje felmeddelande för att underlätta felsökning
        for (String error : errors) {
            System.out.println("Fel: " + error);
        }

        // Kontrollera om det förväntade felmeddelandet finns i listan
        assertTrue(errors.contains("Ogiltig e-postadressformat. Använd t.ex. användarnamn@domän.com."),
                "Förväntat felmeddelande 'Ogiltig e-postadressformat. Använd t.ex. användarnamn@domän.com.' hittades inte i felmeddelandelistan.");
        assertTrue(errors.contains("Tiden måste vara i framtiden."),
                "Förväntat felmeddelande 'Tiden måste vara i framtiden.' hittades inte i felmeddelandelistan.");
    }

    /**
     * Testar metoden validateDate med ett förflutet datum för att säkerställa att ett felmeddelande genereras.
     * Förväntad utgång: Ett felmeddelande förväntas om datumet är förflutet.
     */
    @Test
    void validateDate_PastDate_Error() {
        // Förflutet datum för att säkerställa att ett felmeddelande genereras
        List<String> errors = Validation.validateAllFields("Gustav Olsson", "1122334455", "gustav.olsson@gmail.com",
                "2020-01-25", "12:00", "Möte om strategi och mål");

        assertEquals(1, errors.size());
        assertEquals("Datumet måste vara i framtiden.", errors.get(0));
    }

    /**
     * Testar metoden validateTime med en förfluten tid för att säkerställa att ett felmeddelande genereras.
     * Förväntad utgång: Ett felmeddelande förväntas om tiden är förfluten.
     */
    @Test
    void validateTime_PastTime_Error() {
        // Förfluten tid för att säkerställa att ett felmeddelande genereras
        LocalDate aktuelltDatum = LocalDate.now();
        LocalTime aktuellTid = LocalTime.now();

        // Sätt datumet till dagens datum
        String aktuelltDatumStr = aktuelltDatum.toString();

        // Använd DateTimeFormatter för att formatera tiden som "HH:MM"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String pastTimeStr = aktuellTid.minusHours(1).format(formatter);

        // Skapa en lista för att lagra felmeddelanden
        List<String> felmeddelanden = new ArrayList<>();

        // Anropa metoden validateTime med aktuellt datum och tid från det förflutna
        Validation.validateTime(pastTimeStr, aktuelltDatumStr, felmeddelanden);

        // Skriv ut felsökningsinformation
        System.out.println("Hittade fel: " + felmeddelanden);

        // Se till att felmeddelandelistan inte är tom
        assertFalse(felmeddelanden.isEmpty(), "Förväntade fel, men hittade inga");

        // Kontrollera om det specifika felmeddelandet 'Tiden måste vara i framtiden.' finns i listan
        assertTrue(felmeddelanden.stream().anyMatch(msg -> msg.contains("Tiden måste vara i framtiden.")),
                "Förväntat felmeddelande 'Tiden måste vara i framtiden.' hittades inte");
    }

    /**
     * Testar metoden validateDescription med en null-beskrivning för att säkerställa att ett felmeddelande genereras.
     * Förväntad utgång: Ett felmeddelande förväntas om beskrivningen är null eller tom.
     */
    @Test
    void validateDescription_NullDescription_Error() {
        // Null-beskrivning för att säkerställa att ett felmeddelande genereras
        List<String> errors = new ArrayList<>();
        Validation.validateDescription(null, errors);

        // Skriv ut felmeddelanden för felsökning
        System.out.println("Antal fel: " + errors.size());
        System.out.println("Felmeddelanden: " + errors);

        // Kontrollera antalet fel
        assertEquals(1, errors.size(), "Oväntat antal fel. Förväntat 1 fel.");

        // Skriv ut varje felmeddelande för att underlätta felsökning
        for (int i = 0; i < errors.size(); i++) {
            System.out.println("Fel " + (i + 1) + ": " + errors.get(i));
        }
    }
}