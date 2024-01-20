package org.projektarbete;
import java.util.InputMismatchException;

/**
 * En klass som representerar användargränssnittet för möteshanteringsapplikationen.
 */
public class UserInterface {

    /**
     * Alternativ för att lägga till ett nytt möte.
     */
    public static final int ADD_APPOINTMENT_OPTION = 1;

    /**
     * Alternativ för att söka efter ett möte.
     */
    public static final int SEARCH_APPOINTMENT_OPTION = 2;

    /**
     * Alternativ för att uppdatera ett möte.
     */
    public static final int UPDATE_APPOINTMENT_OPTION = 3;

    /**
     * Alternativ för att ta bort ett möte.
     */
    public static final int DELETE_APPOINTMENT_OPTION = 4;

    /**
     * Alternativ för att visa alla schemalagda möten.
     */
    public static final int SHOW_ALL_APPOINTMENTS_OPTION = 5;

    /**
     * Alternativ för att avsluta programmet.
     */
    public static final int EXIT_OPTION = 6;

    /**
     * En instans av InputReader för att hantera användarinmatning.
     */
    private static final InputReader inputReader = new InputReader();

    /**
     * Visar huvudmenyn för användaren och hanterar deras val.
     */
    public static void mainMenuOptions() {
        int option;

        do {
            displayMainMenu();

            while (true) {
                try {
                    option = inputReader.nextInt();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Var god ange ett giltigt heltal.");
                    inputReader.nextLine();
                }
            }

            processMainMenuOption(option);

        } while (option != EXIT_OPTION);
    }

    /**
     * Visar huvudmenyn för mötesapplikationen.
     */
    static void displayMainMenu() {
        System.out.println("------------------------------------------------------------");
        System.out.println("                       MÖTESCHEMA");
        System.out.println("------------------------------------------------------------\n");

        System.out.println(" ANGE ETT ALTERNATIV\n");
        displayOption(ADD_APPOINTMENT_OPTION, "LÄGG TILL NYTT MÖTE");
        displayOption(SEARCH_APPOINTMENT_OPTION, "SÖK MÖTE");
        displayOption(UPDATE_APPOINTMENT_OPTION, "UPPDATERA MÖTE");
        displayOption(DELETE_APPOINTMENT_OPTION, "TA BORT MÖTE");
        displayOption(SHOW_ALL_APPOINTMENTS_OPTION, "VISA ALLA MÖTEN");
        displayOption(EXIT_OPTION, "AVSLUTA PROGRAMMET");

        System.out.println("------------------------------------------------------------\n");
    }

    /**
     * Visar ett menyalternativ med en beskrivning.
     *
     * @param option      Det numeriska värdet för menyalternativet.
     * @param description Beskrivningen av menyalternativet.
     */
    static void displayOption(int option, String description) {
        System.out.printf(" %d - %-30s%n", option, description);
    }

    /**
     * Hanterar användarens val från huvudmenyn.
     *
     * @param option Användarens val.
     */
    static void processMainMenuOption(int option) {
        System.out.println("Bearbetar alternativ: " + option);
        switch (option) {
            case ADD_APPOINTMENT_OPTION:
                displaySubMenu("Lägg till nytt möte");
                AppointmentAgenda.addAppointment();
                break;
            case SEARCH_APPOINTMENT_OPTION:
                displaySubMenu("Söker...");
                AppointmentAgenda.searchMenu();
                displaySubMenu("Slutet på sökningen");
                break;
            case UPDATE_APPOINTMENT_OPTION:
                displaySubMenu("Uppdatera möte");
                AppointmentAgenda.updateMenu();
                System.out.println("\nDitt möte har uppdaterats framgångsrikt!\n");
                break;
            case DELETE_APPOINTMENT_OPTION:
                displaySubMenu("Ta bort möte");
                AppointmentAgenda.deleteMenu();
                break;
            case SHOW_ALL_APPOINTMENTS_OPTION:
                displaySubMenu("Schemalagda möten");
                AppointmentAgenda.showAppointments();
                displaySubMenu("Slut på samråd");
                break;
            case EXIT_OPTION:
                System.out.println("Avslutar programmet. Adjö!");
                break;
            default:
                System.out.println("Ogiltigt alternativ");
                break;
        }
    }

    /**
     * Visar en undermeny med en given titel.
     *
     * @param title Titeln på undermenyn.
     */
    static void displaySubMenu(String title) {
        System.out.println("------------------------------------------------------------");
        System.out.printf("                     %s%n", title);
        System.out.println("------------------------------------------------------------\n");
    }
}