package org.projektarbete;
import java.sql.*;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.projektarbete.AppointmentRepository.getRecordCount;

/**
 * Klassen AppointmentAgenda hanterar mötesagendan och interaktionen med användaren.
 * Den innehåller också huvudmetoden för att köra programmet.
 */
public class AppointmentAgenda {
    static final InputReader inputReader = new InputReader(); // Instans av InputReader för att hantera användarinput
    private static final AppointmentRepository appointmentRepository = new AppointmentRepository(); // Instans av AppointmentRepository för att hantera mötesdata
    private static final String FIELD_ID_NUMBER = "idNumber"; // Konstant för fältet ID-nummer i mötesdata
    private static final String FIELD_NAME = "name"; // Konstant för fältet namn i mötesdata
    private static final String FIELD_DATE = "date"; // Konstant för fältet datum i mötesdata

    private static final Logger logger = Logger.getLogger(AppointmentAgenda.class.getName());


    /**
     * Huvudmetod för att köra programmet. Initialiserar AppointmentRepository och etablerar en databasanslutning.
     * Anropar sedan huvudmenyoptionerna och stänger av skannern när den inte längre behövs.
     */
    public static void main(String[] args) {
        try {
            // Initialisera databasen med hjälp av DatabaseManager
            DatabaseManager.createDatabaseAndTableIfNotExists();

            // Fortsätt med resten av din kod
            appointmentRepository.initializeDatabase();

            // Visa huvudmenyn
            UserInterface.mainMenuOptions();
        } catch (SQLException e) {
            // Logga SQL-fel med logger
            logger.log(Level.SEVERE, "Fel vid initialisering av databasen: " + e.getMessage(), e);
        } catch (Exception e) {
            // Logga andra oväntade fel med logger
            logger.log(Level.SEVERE, "Oväntat fel under initiering: " + e.getMessage(), e);
        } finally {
            // Stäng resurser
            inputReader.closeScanner();
            appointmentRepository.closeInputReader();
            DatabaseManager.closeConnection();
        }
    }

    /**
     * Lägger till ett nytt möte genom att begära användarinput för mötesinformation.
     * Visar sedan totalt antal möten i systemet efter att ha lagt till det nya mötet.
     */
    static void addAppointment() {
        String name = inputReader.readString("\nAnge fullständigt namn:\n");
        String idNumber = inputReader.readString("\nAnge ditt 10-siffriga personnummer:\n");
        String email = inputReader.readString("\nAnge e-postadress:\n");
        String date = inputReader.readString("\nAnge datum för mötet (ÅÅÅÅ-MM-DD):\n");
        String time = inputReader.readString("\nAnge tid för mötet (HH:MM):\n");
        String description = inputReader.readString("\nAnge beskrivningen av mötet:\n");

        List<String> validationErrors = Validation.validateAllFields(name, idNumber, email, date, time, description);

        if (!validationErrors.isEmpty()) {
            // Display validation errors
            System.out.println("Fel vid inmatning:");
            for (String error : validationErrors) {
                System.out.println("- " + error);
            }
            return; // Stop further processing if there are errors
        }

        // If validation succeeds, create and save the appointment
        Appointment newAppointment = new Appointment(name, idNumber, email, date, time, description);
        appointmentRepository.addAppointment(newAppointment);

        System.out.println("\nDitt möte har sparats korrekt.\n");
        System.out.println("Totalt antal möten i systemet: " + getRecordCount() + "\n");
    }

    /**
     * Visar menyn för sökalternativ och hanterar användarens val.
     */
    static void searchMenu() {
        int searchOption = 0;

        do {
            System.out.println("Välj sökkriterium:");
            System.out.println("1 - Sök efter namn");
            System.out.println("2 - Sök efter personnummer");
            System.out.println("3 - Sök efter datum");
            System.out.println("4 - Återgå till huvudmenyn");

            try {
                searchOption = inputReader.nextInt();

                // Konsumera resten av raden
                inputReader.nextLine();

                switch (searchOption) {
                    case 1 -> searchByName();
                    case 2 -> searchByIdNumber();
                    case 3 -> searchByDate();
                    case 4 -> System.out.println("Återgår till huvudmenyn...");
                    default -> System.out.println("Ogiltigt alternativ.");
                }

                if (searchOption != 4 && getRecordCount() > 0 && askForContinue("sökning")) {
                    // Fortsätt söka
                } else {
                    // Återgå till huvudmenyn
                    return;
                }
            } catch (InputMismatchException | InterruptedException e) {
                System.out.println("Var god ange ett giltigt heltal.");
                inputReader.nextLine(); // Konsumera ogiltig inmatning
            }
        } while (searchOption != 4);
    }

    /**
     * Söker efter möten baserat på användarens angivna namn.
     *
     * @throws InterruptedException Om det uppstår ett avbrott under sökningen.
     */
    private static void searchByName() throws InterruptedException {
        try {
            System.out.println("Ange namn att söka efter:");
            String name = inputReader.nextLine();
            AppointmentRepository.searchByField("name", name);
        } catch (InterruptedException e) {
            System.out.println("Något gick fel vid sökningen. Vill du fortsätta? (ja/nej)");
            if (!askForContinue("sökning")) {
                return; // Ingen ytterligare åtgärd behövs
            }
        }
    }

    /**
     * Söker efter möten baserat på användarens angivna personnummer.
     *
     * @throws InterruptedException Om det uppstår ett avbrott under sökningen.
     */
    private static void searchByIdNumber() throws InterruptedException {
        try {
            System.out.println("Ange personnummer att söka efter:");
            String idNumber = inputReader.nextLine();
            AppointmentRepository.searchByField("idNumber", idNumber);
        } catch (InterruptedException e) {
            System.out.println("Något gick fel vid sökningen. Vill du fortsätta? (ja/nej)");
            if (!askForContinue("sökning")) {
                return; // Ingen ytterligare åtgärd behövs
            }
        }
    }

    /**
     * Söker efter möten baserat på användarens angivna datum.
     *
     * @throws InterruptedException Om det uppstår ett avbrott under sökningen.
     */
    private static void searchByDate() throws InterruptedException {
        try {
            System.out.println("Ange datum att söka efter:");
            String date = inputReader.nextLine();
            AppointmentRepository.searchByField("date", date);
        } catch (InterruptedException e) {
            System.out.println("Något gick fel vid sökningen. Vill du fortsätta? (ja/nej)");
            if (!askForContinue("sökning")) {
                return; // Ingen ytterligare åtgärd behövs
            }
        }
    }

    /**
     * Huvudmeny för uppdateringsalternativ. Användaren kan välja att uppdatera efter namn, personnummer, datum eller återgå till huvudmenyn.
     */
    static void updateMenu() {

        boolean exitUpdateMenu = false;

        while (!exitUpdateMenu) {
            try {
                System.out.println("Välj alternativ för uppdatering:");
                System.out.println("1. Uppdatera efter namn");
                System.out.println("2. Uppdatera efter personnummer");
                System.out.println("3. Uppdatera efter datum");
                System.out.println("4. Gå tillbaka till huvudmenyn");
                int option = inputReader.nextInt();

                switch (option) {
                    case 1, 2, 3 -> updateRecordByField(getUpdateFieldByOption(option));
                    case 4 -> {
                        System.out.println("Återgår till huvudmenyn...");
                        exitUpdateMenu = true;
                    }
                    default -> System.out.println("Felaktigt alternativ. Var vänlig försök igen.");
                }

                if (option != 4) {
                    // Om användaren inte valde att återgå till huvudmenyn, fråga om fortsättning i uppdateringskontexten
                    exitUpdateMenu = !askForContinue("uppdatering");
                }

            } catch (InputMismatchException e) {
                System.out.println("Fel: Ange ett giltigt alternativ.");
                inputReader.nextLine();
            }
        }
    }

    private static String getUpdateFieldByOption(int option) {
        switch (option) {
            case 1 -> {
                return FIELD_NAME;
            }
            case 2 -> {
                return FIELD_ID_NUMBER;
            }
            case 3 -> {
                return FIELD_DATE;
            }
            default -> throw new IllegalArgumentException("Ogiltigt alternativ för uppdatering.");
        }
    }

    private static void updateRecordByField(String field) {
        try {
            System.out.println("Ange det befintliga värdet för " + AppointmentRepository.getSwedishFieldName(field) + " att uppdatera efter:");
            String oldValue = inputReader.next();

            // Consume the rest of the line
            inputReader.nextLine();

            Appointment oldAppointment = appointmentRepository.getAppointmentByField(field, oldValue);

            if (oldAppointment == null) {
                System.out.println("Ingen post hittades med det angivna värdet. Ingen uppdatering utförd.");
                return;
            }

            Appointment newAppointment = createAppointmentFromUserInput(oldAppointment);

            if (newAppointment == null) {
                System.out.println("Uppdateringen avbröts på grund av felaktig inmatning.");
                return;
            }

            // Validate the new appointment
            List<String> validationErrors = Validation.validateAllFields(
                    newAppointment.getName(),
                    newAppointment.getIdNumber(),
                    newAppointment.getEmail(),
                    newAppointment.getDate(),
                    newAppointment.getTime(),
                    newAppointment.getDescription()
            );

            if (!validationErrors.isEmpty()) {
                // Display validation errors
                System.out.println("Fel vid inmatning:");
                for (String error : validationErrors) {
                    System.out.println("- " + error);
                }
                return; // Stop further processing if there are errors
            }

            // Update the appointment if validation succeeds
            appointmentRepository.updateRecord(field, oldValue, newAppointment);
            System.out.println("Posten har uppdaterats framgångsrikt!");

        } catch (InputMismatchException e) {
            System.out.println("Fel: Ange giltiga data.");
            inputReader.nextLine();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Appointment createAppointmentFromUserInput(Appointment oldAppointment) {
        System.out.println("Ange nytt namn (gamla värde: " + oldAppointment.getName() + "):");
        String name = inputReader.nextLine();

        System.out.println("Ange nytt 10-siffrigt personnummer (gamla värde: " + oldAppointment.getIdNumber() + "):");
        String idNumber = inputReader.nextLine();

        System.out.println("Ange ny e-postadress (gamla värde: " + oldAppointment.getEmail() + "):");
        String email = inputReader.nextLine();

        System.out.println("Ange nytt datum för mötet (gamla värde: " + oldAppointment.getDate() + "):");
        String date = inputReader.nextLine();

        System.out.println("Ange ny tid för mötet (gamla värde: " + oldAppointment.getTime() + "):");
        String time = inputReader.nextLine();

        System.out.println("Ange ny beskrivning av mötet (gamla värde: " + oldAppointment.getDescription() + "):");
        String description = inputReader.nextLine();

        try {
            return new Appointment(name, idNumber, email, date, time, description);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    static boolean askForContinue(String context) {
        System.out.println("Vill du fortsätta " + context + "? (ja/nej)");
        String response = inputReader.next().toLowerCase();

        if (response.equals("nej")) {
            System.out.println("Återgår till huvudmenyn...");
            return false;
        } else if (!response.equals("ja")) {
            System.out.println("Ogiltigt svar. Återgår till huvudmenyn...");
            return false;
        }

        // Om svaret är "ja", fortsätt i menyn
        return true;
    }

    /**
     * Huvudmeny för att ta bort möten. Användaren kan välja att ta bort efter namn, personnummer, datum, alla möten eller återgå till huvudmenyn.
     */
    static void deleteMenu() {
        try {
            while (true) {
                System.out.println("Välj alternativ för att ta bort mötet:");
                System.out.println("1 - Ta bort efter namn");
                System.out.println("2 - Ta bort efter personnummer");
                System.out.println("3 - Ta bort efter datum");
                System.out.println("4 - Ta bort alla möten");
                System.out.println("5 - Gå tillbaka till huvudmenyn");

                int deleteOption = inputReader.nextInt();

                switch (deleteOption) {
                    case 1:
                        if (getRecordCount() == 0) {
                            System.out.println("Inga möten att ta bort.");
                            printReturningToMainMenu();
                            Thread.sleep(500);
                            return;
                        }
                        System.out.println("Ange namnet på mötet du vill ta bort:");
                        String nameToDelete = inputReader.next();
                        appointmentRepository.deleteAppointmentByName(nameToDelete);
                        break;
                    case 2:
                        if (getRecordCount() == 0) {
                            System.out.println("Inga möten att ta bort.");
                            printReturningToMainMenu();
                            Thread.sleep(500);

                            return;
                        }
                        System.out.println("Ange personnummer för mötet du vill ta bort:");
                        String idNumberToDelete = inputReader.next();
                        appointmentRepository.deleteAppointmentByIdNumber(idNumberToDelete);
                        break;
                    case 3:
                        if (getRecordCount() == 0) {
                            System.out.println("Inga möten att ta bort.");
                            printReturningToMainMenu();
                            Thread.sleep(500);
                            return;
                        }
                        System.out.println("Ange datumet för mötet du vill ta bort:");
                        String dateToDelete = inputReader.next();
                        appointmentRepository.deleteAppointmentByDate(dateToDelete);
                        break;
                    case 4:
                        if (getRecordCount() == 0) {
                            System.out.println("Inga möten att ta bort.");
                            printReturningToMainMenu();
                            Thread.sleep(500);
                            return;
                        }
                        appointmentRepository.deleteAllRecords();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Ogiltigt alternativ.");
                        break;
                }

                if (getRecordCount() > 0) {
                    if (!askForContinue("borttagning")) {
                        return; // Om användaren inte vill fortsätta, återgå till huvudmenyn
                    }
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Fel: Ange ett giltigt alternativ");
            inputReader.nextLine();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static void printReturningToMainMenu() {
        System.out.println("Återgår till huvudmenyn...");
    }


    /**
     * Visar alla möten i systemet.
     */
    static void showAppointments() {
        try {
            appointmentRepository.showRecords();
        } catch (SQLException e) {
            // Log the error using the Logger
            logger.log(Level.SEVERE, "Fel vid visning av möten: " + e.getMessage(), e);
        }
    }
}
