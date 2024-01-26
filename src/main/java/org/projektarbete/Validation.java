package org.projektarbete;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Klassen innehåller metoder för att validera olika fält i ett mötesformulär.
 */
public class Validation {

    /**
     * Validerar samtliga fält i mötesformuläret.
     *
     * @param name        Namnet som ska valideras.
     * @param idNumber    Personnumret som ska valideras.
     * @param email       E-postadressen som ska valideras.
     * @param date        Datumet som ska valideras.
     * @param time        Tiden som ska valideras.
     * @param description Beskrivningen som ska valideras.
     * @return Lista med felmeddelanden, om några valideringsfel uppstår.
     */
    public static List<String> validateAllFields(String name, String idNumber, String email, String date, String time, String description) {
        List<String> errorMessages = new ArrayList<>();

        validateName(name, errorMessages);
        validateIdNumber(idNumber, errorMessages);
        validateEmail(email, errorMessages);
        validateDate(date, errorMessages);
        validateTime(time, date, errorMessages); // Skicka med datum för tidsvalidering
        validateDescription(description, errorMessages);

        // Kontrollera om det finns några valideringsfel
        if (!errorMessages.isEmpty()) {
            return errorMessages; // Stoppa vidare bearbetning om det finns fel
        }

        // Fortsätt med ytterligare bearbetning (t.ex. spara mötet)

        // Om det finns ytterligare logik efter validering, placera den här

        return errorMessages;
    }

    /**
     * Validerar namnfältet.
     *
     * @param name          Namnet som ska valideras.
     * @param errorMessage  Lista för att lagra felmeddelanden vid validering.
     * @throws IllegalArgumentException om namnet är null eller tomt.
     * @implNote Ytterligare specifik valideringslogik för namn kan läggas till vid behov.
     */
    public static void validateName(String name, List<String> errorMessage) {
        if (name == null || name.trim().isEmpty()) {
            errorMessage.add("Namnet får inte vara null eller tomt.");
        }
    }

    /**
     * Validerar Personnumret fältet.
     *
     * @param idNumber      Personnumret som ska valideras.
     * @param errorMessage  Lista för att lagra felmeddelanden vid validering.
     * @throws IllegalArgumentException om ID-numret är null eller inte är 10 siffror långt.
     * @implNote Ytterligare specifik valideringslogik för ID-nummer kan läggas till vid behov.
     */
    public static void validateIdNumber(String idNumber, List<String> errorMessage) {
        String cleanIdNumber = idNumber.trim().replaceAll("\\s+", "");
        if (cleanIdNumber.length() != 10 || !cleanIdNumber.matches("\\d{10}")) {
            errorMessage.add("Ogiltigt personnummer. Använd 10 siffror.");
        }
    }

    /**
     * Validerar e-postadressfältet.
     *
     * @param email         E-postadressen som ska valideras.
     * @param errorMessage  Lista för att lagra felmeddelanden vid validering.
     * @throws IllegalArgumentException om e-postadressen är ogiltig.
     */
    public static void validateEmail(String email, List<String> errorMessage) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!Pattern.matches(emailRegex, email)) {
            errorMessage.add("Ogiltig e-postadressformat. T.ex. användarnamn@domän.com.");
        }
    }

    /**
     * Validerar datumfältet.
     *
     * @param date          Datumet som ska valideras.
     * @param errorMessage  Lista för att lagra felmeddelanden vid validering.
     * @throws IllegalArgumentException om datumet är ogiltigt.
     */
    public static void validateDate(String date, List<String> errorMessage) {
        if (date == null || !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            errorMessage.add("Ogiltigt datumformat. Använd ÅÅÅÅ-MM-DD.");
            return;  // Stoppa vidare bearbetning om formatet är ogiltigt
        }

        try {
            LocalDate providedDate = LocalDate.parse(date);

            if (providedDate.isBefore(LocalDate.now())) {
                errorMessage.add("Datumet måste vara i framtiden.");
            }
        } catch (DateTimeParseException e) {
            errorMessage.add("Ogiltigt datum. Försök igen.");
        }
    }

    /**
     * Validerar tidsfältet.
     *
     * @param time          Tiden som ska valideras.
     * @param date          Datumet som används för tidsvalidering.
     * @param errorMessage  Lista för att lagra felmeddelanden vid validering.
     * @throws IllegalArgumentException om tiden är ogiltig.
     */
    public static void validateTime(String time, String date, List<String> errorMessage) {
        if (time == null || !time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
            errorMessage.add("Ogiltigt tidsformat. Använd HH:MM.");
            return;  // Stoppa vidare bearbetning om formatet är ogiltigt
        }

        try {
            LocalTime providedTime = LocalTime.parse(time);
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDate providedDate = LocalDate.parse(date);

            if (providedDate.isEqual(currentDateTime.toLocalDate()) && providedTime.isBefore(currentDateTime.toLocalTime())) {
                errorMessage.add("Tiden måste vara i framtiden.");
            }
        } catch (DateTimeParseException e) {
            errorMessage.add("Ogiltig tid. Försök igen.");
        }
    }

    /**
     * Validerar beskrivningsfältet.
     *
     * @param description   Beskrivningen som ska valideras.
     * @param errorMessage  Lista för att lagra felmeddelanden vid validering.
     * @throws IllegalArgumentException om beskrivningen är null eller tom.
     * @implNote Ytterligare specifik valideringslogik för beskrivning kan läggas till vid behov.
     */
    public static void validateDescription(String description, List<String> errorMessage) {
        if (description == null || description.trim().isEmpty()) {
            errorMessage.add("Beskrivningen får inte vara null eller tom.");
        }
    }
}