package org.projektarbete;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class Appointment {
    //private final int id;
    private final String name;
    private final String idNumber;
    private final String email;
    private final String date;
    private final String time;
    private final String description;

    public Appointment(String name, String idNumber, String email, String date, String time, String description) {
        validateAppointmentInput(name, idNumber, email, date, time, description);
        //this.id = id;
        this.name = name;
        this.idNumber = idNumber;
        this.email = email;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    // Validate all fields
    private static void validateAppointmentInput(String name, String idNumber, String email, String date, String time, String description) {
        validateName(name);
        validateIdNumber(idNumber);
        validateEmail(email);
        validateDate(date);
        validateTime(time);
        validateDescription(description);
    }


    // Validation methods with improved error messages and corrected return types
    static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Namn kan inte vara eller tomt");
        }
    }
    //we might not need this
    static void validateIdNumber(String idNumber) {
        if (idNumber == null || !idNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("Ogiltigt personnummer. Ange 10 siffror");
        }
    }

//*Added that an email must end in a .com
    //Checks the format of the email.
    // it has to have a 'name', '@' sign and end with '.com'


    static void validateEmail(String email) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.com$")) {
            throw new IllegalArgumentException("Ogiltig e-postadress.format måste blir namn@namn.com");
        }
    }
    // Checks so that the date is in the future and not in the past
    static void validateDate(String date) {
        if (date == null || !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Ogiltigt datumformat. Använd ÅÅÅÅ-MM-DD");
        }
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate providedDate = LocalDate.parse(date,formatter);

        if (providedDate.isBefore(currentDate)){
            throw  new IllegalArgumentException("Datumet måste blir i framtiden");
        }
    }
    //Checks if the right format for time has been typed
    static void validateTime(String time) {
        if (time == null || !time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
            throw new IllegalArgumentException("Ogiltigt tidsformat. Använd HH:MM");
        }
    }
    //Checks if something has been typed or not.
    static void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Beskrivning kan inte vara tom");
        }
    }


    // Getters
    //public int getId() {
      //  return id;
    //}

    public String getName() {
        return name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }
}
