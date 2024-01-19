package org.projektarbete;
import java.sql.*;
import java.util.InputMismatchException;

/**
 * AppointmentAgenda-klassen hanterar mötesagendan och interaktionen med användaren.
 * Den innehåller också huvudmetoden för att köra programmet.
 */
public class AppointmentAgenda {
    static final InputReader inputReader = new InputReader(); // Instans av InputReader för att hantera användarinput
    private static final AppointmentRepository appointmentRepository = new AppointmentRepository(); // Instans av AppointmentRepository för att hantera mötesdata
    private static final String FIELD_ID_NUMBER = "idNumber"; // Konstant för fältet ID-nummer i mötesdata
    private static final String FIELD_NAME = "name"; // Konstant för fältet namn i mötesdata
    private static final String FIELD_DATE = "date"; // Konstant för fältet datum i mötesdata

    /**
     * Huvudmetod för att köra programmet. Initialiserar AppointmentRepository och etablerar en databasanslutning.
     * Anropar sedan huvudmenyoptionerna och stänger av skannern när den inte längre behövs.
     */
    public static void main(String[] args) {
        appointmentRepository.initializeDatabase();

        UserInterface.mainMenuOptions();

        inputReader.closeScanner();

        appointmentRepository.closeInputReader();
    }

    /**
     * Lägger till ett nytt möte genom att begära användarinput för mötesinformation.
     * Visar sedan totalt antal möten i systemet efter att ha lagt till det nya mötet.
     */
    static void addAppointment() {
        try {
            System.out.println("Ange fullständigt namn:");
            String name = inputReader.readString("");

            System.out.println("\nAnge ditt 10-siffriga personnummer:");
            String idNumber = inputReader.readString("");

            System.out.println("\nAnge e-postadress:");
            String email = inputReader.readString("");

            System.out.println("\nAnge datum för mötet:");
            String date = inputReader.readString("");

            System.out.println("\nAnge tid för mötet:");
            String time = inputReader.readString("");

            System.out.println("\nAnge en beskrivning av mötet:");
            String description = inputReader.readString("");

            Appointment newAppointment = new Appointment(name, idNumber, email, date, time, description);
            appointmentRepository.addAppointment(newAppointment);

            System.out.println("\nDitt möte har sparats korrekt.\n");

            System.out.println("Totalt antal möten i systemet: " + appointmentRepository.getRecordCount() + "\n");

        } catch (InputMismatchException e) {
            System.out.println("Fel: Ange giltiga data.");
            inputReader.readString(""); // Konsumera ogiltig inmatning
        }
    }

    /**
     * Huvudmeny för sökalternativ. Användaren kan välja att söka efter namn, personnummer, datum eller återgå till huvudmenyn.
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

                if (searchOption != 4 && askForContinue("sökning")) {
                    // Fortsätt söka
                } else {
                    // Återgå till huvudmenyn
                    return;
                }
            } catch (InputMismatchException e) {
                System.out.println("Var god ange ett giltigt heltal.");
                inputReader.nextLine(); // Konsumera ogiltig inmatning
            }
        } while (searchOption != 4);
    }

    private static void searchByName() {
        System.out.println("Ange namn att söka efter:");
        String name = inputReader.nextLine();
        searchByField("name", name);
    }

    private static void searchByIdNumber() {
        System.out.println("Ange personnummer att söka efter:");
        String idNumber = inputReader.nextLine();
        searchByField("idNumber", idNumber);
    }

    private static void searchByDate() {
        System.out.println("Ange datum att söka efter:");
        String date = inputReader.nextLine();
        searchByField("date", date);
    }

    private static void searchByField(String field, String value) {
        appointmentRepository.searchByField(field, value);
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
            System.out.println("Ange det befintliga värdet för " + appointmentRepository.getSwedishFieldName(field) + " att uppdatera efter:");
            String oldValue = inputReader.next();

            // Konsumera resten av raden
            inputReader.nextLine();

            Appointment newAppointment = createAppointmentFromUserInput();

            // Anropa updateRecord-metoden i appointmentRepository
            appointmentRepository.updateRecord(field, oldValue, newAppointment);

        } catch (InputMismatchException e) {
            System.out.println("Fel: Ange giltiga data.");
            inputReader.nextLine();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Appointment createAppointmentFromUserInput() {
        System.out.println("Ange nytt namn:");
        String newName = inputReader.nextLine();

        System.out.println("Ange nytt 10-siffrigt personnummer:");
        String newIdNumber = inputReader.nextLine();

        System.out.println("Ange ny e-postadress:");
        String newEmail = inputReader.nextLine();

        System.out.println("Ange nytt datum för mötet:");
        String newDate = inputReader.nextLine();

        System.out.println("Ange ny tid för mötet:");
        String newTime = inputReader.nextLine();

        System.out.println("Ange ny beskrivning av mötet:");
        String newDescription = inputReader.nextLine();

        // Skapa och returnera en ny Appointment-objekt med den insamlade informationen
        return new Appointment(newName, newIdNumber, newEmail, newDate, newTime, newDescription);
    }

    private static boolean askForContinue(String context) {
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
                        System.out.println("Ange namnet på mötet du vill ta bort:");
                        String nameToDelete = inputReader.next();
                        appointmentRepository.deleteAppointmentByName(nameToDelete);
                        break;
                    case 2:
                        System.out.println("Ange personnummer för mötet du vill ta bort:");
                        String idNumberToDelete = inputReader.next();
                        appointmentRepository.deleteAppointmentByIdNumber(idNumberToDelete);
                        break;
                    case 3:
                        System.out.println("Ange datumet för mötet du vill ta bort:");
                        String dateToDelete = inputReader.next();
                        appointmentRepository.deleteAppointmentByDate(dateToDelete);
                        break;
                    case 4:
                        if (appointmentRepository.getRecordCount() == 0) {
                            System.out.println("Inga möten att ta bort.");
                            continue;
                        }
                        appointmentRepository.deleteAllRecords();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Ogiltigt alternativ.");
                        break;
                }

                if (deleteOption != 5 && appointmentRepository.getRecordCount() > 0) {
                    if (!askForContinue("borttagning")) {
                        return; // Om användaren inte vill fortsätta, återgå till huvudmenyn
                    }
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Fel: Ange ett giltigt alternativ");
            inputReader.nextLine();
        }
    }

    /**
     * Visar alla möten i systemet.
     */
    static void showAppointments() {
        try {
            appointmentRepository.showRecords();
        } catch (SQLException e) {
            System.err.println("Fel vid visning av möten: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
