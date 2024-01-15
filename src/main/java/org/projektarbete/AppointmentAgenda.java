package org.projektarbete;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AppointmentAgenda {

    private static final Scanner scanner = new Scanner(System.in);
    private static final int ADD_APPOINTMENT_OPTION = 1;
    private static final int SEARCH_APPOINTMENT_OPTION = 2;
    private static final int UPDATE_APPOINTMENT_OPTION = 3;
    private static final int DELETE_APPOINTMENT_OPTION = 4;
    private static final int SHOW_ALL_APPOINTMENTS_OPTION = 5;
    private static final int EXIT_OPTION = 6;

    public static void main(String[] args) {
        mainMenuOptions();
    }

    private static void mainMenuOptions() {
        int option;

        do {
            displayMainMenu();

            while (true) {
                try {
                    option = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Var god ange ett giltigt heltal.");
                    scanner.nextLine();
                }
            }

            processMainMenuOption(option);

        } while (option != EXIT_OPTION);
    }

    private static void displayMainMenu() {
        System.out.println("------------------------------------------------------------");
        System.out.println("                       MÖTESCHEMA");
        System.out.println("------------------------------------------------------------\n");

        System.out.println(" ANGE ETT ALTERNATIV\n");
        System.out.println(" " + ADD_APPOINTMENT_OPTION + " - LÄGG TILL NYTT MÖTE");
        System.out.println(" " + SEARCH_APPOINTMENT_OPTION + " - SÖK MÖTE");
        System.out.println(" " + UPDATE_APPOINTMENT_OPTION + " - UPPDATERA MÖTE");
        System.out.println(" " + DELETE_APPOINTMENT_OPTION + " - TA BORT MÖTE");
        System.out.println(" " + SHOW_ALL_APPOINTMENTS_OPTION + " - VISA ALLA MÖTEN");
        System.out.println(" " + EXIT_OPTION + " - AVSLUTA PROGRAMMET");
        System.out.println("------------------------------------------------------------\n");
    }

    private static void processMainMenuOption(int option) {
        switch (option) {
            case ADD_APPOINTMENT_OPTION:
                System.out.println("------------------------------------------------------------");
                System.out.println("                     Lägg till nytt möte");
                System.out.println("------------------------------------------------------------\n");
                break;
            case SEARCH_APPOINTMENT_OPTION:
                System.out.println("\n------------------------------------------------------------");
                System.out.println("                         Söker...");
                System.out.println("------------------------------------------------------------\n");
                System.out.println("\n------------------------------------------------------------");
                System.out.println("                    Slutet på sökningen");
                System.out.println("------------------------------------------------------------\n\n");
                break;
            case UPDATE_APPOINTMENT_OPTION:
                System.out.println("------------------------------------------------------------");
                System.out.println("                     Uppdatera möte");
                System.out.println("------------------------------------------------------------\n");
                break;
            case DELETE_APPOINTMENT_OPTION:
                System.out.println("------------------------------------------------------------");
                System.out.println("                    Ta bort möte");
                System.out.println("------------------------------------------------------------\n");
                break;
            case SHOW_ALL_APPOINTMENTS_OPTION:
                System.out.println("------------------------------------------------------------");
                System.out.println("                    Schemalagda möten");
                System.out.println("------------------------------------------------------------\n\n");
                System.out.println("\n------------------------------------------------------------");
                System.out.println("                      Slut på samråd");
                System.out.println("------------------------------------------------------------\n");
                break;
            case EXIT_OPTION:
                System.out.println("Avslutar programmet. Adjö!");
                break;
            default:
                System.out.println("Ogiltigt alternativ");
                break;
        }
    }
    private static void updateMenu() {
    }

    private static void searchMenu() {
    }

    private static void addAppointment() {
        try {
            System.out.println("Ange fullständigt namn:");
            String name = scanner.nextLine();

            System.out.println("\nAnge personnummer:");
            String idNumber = scanner.nextLine();

            System.out.println("\nAnge e-postadress:");
            String email = scanner.nextLine();

            System.out.println("\nAnge datum för mötet:");
            String date = scanner.nextLine();

            System.out.println("\nAnge tid för mötet:");
            String time = scanner.nextLine();

            System.out.println("\nAnge en beskrivning av mötet:");
            String description = scanner.nextLine();

            // Definiera ett nytt möte och lägg till det i listan
            Appointment newAppointment = new Appointment(appointments.size() + name, idNumber, email, date, time, description);
            appointments.add(newAppointment);

            System.out.println("\nDitt möte har lagts till framgångsrikt!\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Fel: " + e.getMessage());
        }
    }


    private static void deleteAppointment() {
        try {
            int option;
            do {
                System.out.println("Välj alternativ för att ta bort möte:");
                System.out.println("1. Ange namn");
                System.out.println("2. Ange personnummer");
                System.out.println("3. Ange datum");
                System.out.println("4. Radera alla möten");
                System.out.println("5. Gå tillbaka till huvudmenyn");
                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        deleteByName();
                        break;
                    case 2:
                        deleteByIdNumber();
                        break;
                    case 3:
                        deleteByDate();
                        break;
                    case 4:
                        deleteAllAppointments();
                        break;
                    case 5:
                        System.out.println("Återgår till huvudmenyn.");
                        break;
                    default:
                        System.out.println("Ogiltigt alternativ. Försök igen.");
                }
            } while (option != 5);
        } catch (InputMismatchException e) {
            System.out.println("Fel: Ange en giltig siffra.");
            scanner.nextLine();
        }
    }

    private static void deleteByName() {
        System.out.println("Ange namnet för mötet du vill ta bort:");
        String appointmentName = scanner.nextLine();

        appointments.removeIf(appointment -> appointment.getName().equalsIgnoreCase(appointmentName));

        System.out.println("Mötet med namnet '" + appointmentName + "' har tagits bort.");
    }

    private static void deleteByIdNumber() {
        try {
            System.out.println("Ange personnumret som är kopplat till mötet du vill ta bort:");
            String appointmentIdNumber = scanner.next();

            boolean removed = appointments.removeIf(appointment -> appointment.getIdNumber().equals(appointmentIdNumber));

            if (removed) {
                System.out.println("Mötet med personnumret" + appointmentIdNumber + " har tagits bort.");
            } else {
                System.out.println("Inget möte hittades med personnumret " + appointmentIdNumber);
            }
        } catch (InputMismatchException e) {
            System.out.println("Fel: Ange ett giltigt personnummer.");
            scanner.nextLine();
        }
    }

    private static void deleteByDate() {
        System.out.println("Ange datumet för mötet du vill ta bort:");
        String appointmentDate = scanner.nextLine();

        appointments.removeIf(appointment -> appointment.getDate().equalsIgnoreCase(appointmentDate));

        System.out.println("Möten med datumet '" + appointmentDate + "' har tagits bort.");
    }

    private static void deleteAllAppointments() {
        System.out.println("Varning: Detta kommer att radera alla möten. Är du säker? (ja/nej):");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("ja")) {
            appointments.clear();
            System.out.println("Alla möten har raderats.");
        } else {
            System.out.println("Radering av alla möten avbruten.");
        }
    }
    private static void printAppointmentDetails(Appointment appointment) {
        System.out.println("Mötet med följande detaljer hittades:");
        System.out.println("Namn: " + appointment.getName());
        System.out.println("Personnummer: " + appointment.getIdNumber());
        System.out.println("E-postadress: " + appointment.getEmail());
        System.out.println("Datum: " + appointment.getDate());
        System.out.println("Tid: " + appointment.getTime());
        System.out.println("Beskrivning: " + appointment.getDescription());
        System.out.println("------------------------------------------------------------");
    }

    private static void showAllAppointments() {
        if (appointments.isEmpty()) {
            System.out.println("Inga möten är schemalagda för närvarande.");
        } else {
            for (Appointment appointment : appointments) {
                printAppointmentDetails(appointment);
            }
        }
    }
}

