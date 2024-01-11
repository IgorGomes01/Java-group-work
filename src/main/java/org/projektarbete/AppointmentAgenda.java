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
}
