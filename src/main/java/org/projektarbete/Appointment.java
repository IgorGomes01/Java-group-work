package org.projektarbete;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Appointment {
    private final int id;
    private final String name;
    private final String idNumber;
    private final String email;
    private final String date;
    private final String time;
    private final String description;

    public Appointment(int id, String name, String idNumber, String email, String date, String time, String description) {
        List<String> errorMessage = new ArrayList<>();
        validateAppointmentInput(name, idNumber, email, date, time, description,errorMessage);
        if (!errorMessage.isEmpty()){
            throw new IllegalArgumentException(String.join("\n", errorMessage));
        }
        this.id = id;
        this.name = name;
        this.idNumber = idNumber;
        this.email = email;
        this.date = date;
        this.time = time;
        this.description = description;
    }


    /*
    *Centralises all logic for validations
    * Allows for checking of mutiple aspects of the appoinment input in one place
     */
    private static void validateAppointmentInput(String name, String idNumber, String email, String date, String time, String description,List<String> errorMessage) {
        validateName(name,errorMessage);
        validateIdNumber(idNumber,errorMessage);
        validateEmail(email,errorMessage);
        validateDate(date,errorMessage);
        validateTime(time,errorMessage);
        validateDescription(description,errorMessage);
    }
    /*
    *Testing validations in appointment
    * calling validateallfields from appointment
    * hard coded some specific parameter such my name, id, example email
    * if everything is correct format and nothing is empty we get a message confirming that it was succful
    * otherwise we get it failed.
     */
    public  static void main (String[]args){
        try {
            Appointment.validateAllFields("Zion Awino", "1234567890", "awno.zion@gmail.com", "2024-01-17", "12:30", "Something");
            System.out.println("Validation successful");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation failed: " + e.getMessage());
        }
    }
/*
*Initilize an empty arraylist to store error message
* call the methods used for validation
* it accumulates the error messages if an input fails/is in the wrong format
* is the array is not empty joins all error messages
 */
    static void validateAllFields(String name,String idNumber,String email, String date,String time, String description){
        List<String> errorMessage = new ArrayList<>();

        validateName(name, errorMessage);
        validateEmail(email,errorMessage);
        validateIdNumber(idNumber,errorMessage);
        validateDate(date, errorMessage);
        validateTime(time,errorMessage);
        validateDescription(description,errorMessage);

        if (!errorMessage.isEmpty()){
            throw new IllegalArgumentException(String.join("\n",errorMessage));
        }
    }
    // Validation methods with improved error messages and corrected return types
    static void validateName(String name, List<String> errorMessage) {
        if (name == null || name.trim().isEmpty()) {
            errorMessage.add("Namn kan inte vara eller tomt");
        }
    }
    //we might not need this
    static void validateIdNumber(String idNumber,List<String> errorMessage) {
        if (idNumber == null || !idNumber.matches("\\d{10}")) {
            errorMessage.add("Ogiltigt personnummer. Ange 10 siffror");
        }
    }

//*Added that an email must end in a .com
    //Checks the format of the email.
    // it has to have a 'name', '@' sign and end with '.com'


    static void validateEmail(String email,List <String> errorMessage) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            errorMessage.add("Ogiltig e-postadress.format måste blir namn@namn.com/.org/.net");
        }
    }
    // Checks so that the date is in the future and not in the past
    //At the same time checks if it is the right format.
    static void validateDate(String date, List <String> errorMessage) {
        if (date == null || !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            errorMessage.add("Ogiltigt datumformat. Använd ÅÅÅÅ-MM-DD");
        } else {
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate providedDate = LocalDate.parse(date, formatter);

            if (providedDate.isBefore(currentDate)) {
                errorMessage.add("Datumet måste blir i framtiden");
            }
        }
    }
    //Checks if the right format for time has been typed
    static void validateTime(String time,List <String> errorMessage) {
        if (time == null || !time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
            errorMessage.add("Ogiltigt tidsformat. Använd HH:MM");
        }
    }
    //Checks if something has been typed or not.
    static void validateDescription(String description,List <String> errorMessage) {
        if (description == null || description.trim().isEmpty()) {
            errorMessage.add("Beskrivning kan inte vara tom");
        }
    }


    // Getters
    public int getId() {
        return id;
    }

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
