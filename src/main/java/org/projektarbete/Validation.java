package org.projektarbete;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validator-klassen tillhandahåller metoder för att validera olika inmatningsfält som vanligtvis används vid schemaläggning av möten.
 */
public class Validation {

    /**
     * Validerar namnfältet.
     *
     * @param name Namnet som ska valideras.
     * @throws IllegalArgumentException om namnet är null eller tomt.
     * @implNote Ytterligare specifik valideringslogik för namn kan läggas till vid behov.
     */
    public static void validateName(String name, List<String> errorMessage) {
        if (name == null || name.trim().isEmpty()) {
            errorMessage.add("Namnet får inte vara null eller tomt");
        }
    }

    /**
     * Validerar ID-nummerfältet.
     *
     * @param idNumber ID-numret som ska valideras.
     * @throws IllegalArgumentException om ID-numret är null eller inte är 10 siffror långt.
     * @implNote Ytterligare specifik valideringslogik för ID-nummer kan läggas till vid behov.
     */
    public static void validateIdNumber(String idNumber, List<String> errorMessage) {
        if (idNumber == null || !idNumber.matches("\\d{10}")) {
            errorMessage.add("Ogiltigt ID-nummer. Måste vara 10 siffror");
        }
    }

    /**
     * Validerar e-postadressfältet.
     *
     * @param email E-postadressen som ska valideras.
     * @throws IllegalArgumentException om e-postadressen är ogiltig.
     */
    public static void validateEmail(String email, List<String> errorMessage) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!Pattern.matches(emailRegex, email)) {
            errorMessage.add("Ogiltig e-postadress");
        }
    }

    /**
     * Validerar datumfältet.
     *
     * @param date Datumet som ska valideras (format: ÅÅÅÅ-MM-DD).
     * @throws IllegalArgumentException om datumet är null, har ogiltigt format eller är i det förflutna.
     * @implNote Ytterligare specifik valideringslogik för datum kan läggas till vid behov.
     */
    public static void validateDate(String date, List<String> errorMessage) {
        if (date == null || !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            errorMessage.add("Ogiltigt datumformat. Använd ÅÅÅÅ-MM-DD");
        }

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate providedDate = LocalDate.parse(date, formatter);

        if (providedDate.isBefore(currentDate)) {
            errorMessage.add("Datumet måste vara i framtiden");
        }
    }

    /**
     * Validerar tidsfältet.
     *
     * @param time Tiden som ska valideras (format: HH:MM).
     * @throws IllegalArgumentException om tiden är null, har ogiltigt format eller är i det förflutna.
     * @implNote Ytterligare specifik valideringslogik för tid kan läggas till vid behov.
     */
    public static void validateTime(String time, List<String> errorMessage) {
        if (time == null || !time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
            errorMessage.add("Ogiltigt tidsformat. Använd HH:MM");
        }

        LocalTime currentTime = LocalTime.now();
        LocalTime providedTime = LocalTime.parse(time);

        if (providedTime.isBefore(currentTime)) {
            errorMessage.add("Tiden måste vara i framtiden");
        }
    }

    /**
     * Validerar beskrivningsfältet.
     *
     * @param description Beskrivningen som ska valideras.
     * @throws IllegalArgumentException om beskrivningen är null eller tom.
     * @implNote Ytterligare specifik valideringslogik för beskrivning kan läggas till vid behov.
     */
    public static void validateDescription(String description, List<String> errorMessage) {
        if (description == null || description.trim().isEmpty()) {
            errorMessage.add("Beskrivningen får inte vara null eller tom");
        }
    }

    /**
     * Validerar alla inmatningsfält som vanligtvis används vid schemaläggning av möten.
     *
     * @param name        Namnet som ska valideras.
     * @param idNumber    ID-numret som ska valideras.
     * @param email       E-postadressen som ska valideras.
     * @param date        Datumet som ska valideras (format: ÅÅÅÅ-MM-DD).
     * @param time        Tiden som ska valideras (format: HH:MM).
     * @param description Beskrivningen som ska valideras.
     * @throws IllegalArgumentException om något av fälten inte klarar valideringen.
     */
    public static void validateAllFields(String name, String idNumber, String email, String date, String time, String description, List<String> errorMessage) {
        validateName(name, errorMessage);
        validateIdNumber(idNumber, errorMessage);
        validateEmail(email, errorMessage);
        validateDate(date, errorMessage);
        validateTime(time, errorMessage);
        validateDescription(description, errorMessage);
    }
}
