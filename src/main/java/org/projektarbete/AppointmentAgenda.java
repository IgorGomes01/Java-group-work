package org.projektarbete;
import java.sql.SQLOutput;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.projektarbete.Appointment.*;

public class AppointmentAgenda {

    private static final InputReader input = new InputReader();
    private static final List<Appointment> appointments = new ArrayList<>();
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    private static final int ADD_APPOINTMENT_OPTION = 1;
    private static final int SEARCH_APPOINTMENT_OPTION = 2;
    private static final int UPDATE_APPOINTMENT_OPTION = 3;
    private static final int DELETE_APPOINTMENT_OPTION = 4;
    private static final int SHOW_ALL_APPOINTMENTS_OPTION = 5;
    private static final int EXIT_OPTION = 6;

    public static void main(String[] args) {
        load();
        mainMenuOptions();
    }

    private static void mainMenuOptions() {
        int option;

        do {
            displayMainMenu();

            while (true) {
                try {
                    option = input.readInt("Ange val");
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Var god ange ett giltigt heltal.");
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
                addAppointment();
                break;
            case SEARCH_APPOINTMENT_OPTION:
                System.out.println("\n------------------------------------------------------------");
                System.out.println("                         Söker...");
                System.out.println("------------------------------------------------------------\n");
                searchAppointment();
                System.out.println("\n------------------------------------------------------------");
                System.out.println("                    Slutet på sökningen");
                System.out.println("------------------------------------------------------------\n\n");
                break;
            case UPDATE_APPOINTMENT_OPTION:
                System.out.println("------------------------------------------------------------");
                System.out.println("                     Uppdatera möte");
                System.out.println("------------------------------------------------------------\n");
                updateAppointment();
                break;
            case DELETE_APPOINTMENT_OPTION:
                System.out.println("------------------------------------------------------------");
                System.out.println("                    Ta bort möte");
                System.out.println("------------------------------------------------------------\n");
                deleteAppointment();
                break;
            case SHOW_ALL_APPOINTMENTS_OPTION:
                System.out.println("------------------------------------------------------------");
                System.out.println("                    Schemalagda möten");
                System.out.println("------------------------------------------------------------\n\n");
                showAllAppointments();
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
    //Har bara gjort själva menyn
    private static void printUpdateAppointmentmenu(){
        System.out.println("1: Uppdatera namn ");
        System.out.println("2: Uppdatera personnummer ");
        System.out.println("3: Uppdatera E-post");
        System.out.println("4: Uppdatera datum");
        System.out.println("5: Uppdatera tid");
        System.out.println("6: Uppdatera beskrivning");
        System.out.println("7: Återgå till huvudmenyn");
    }
    //Visar menyn
    private static void updateAppointment() {
        printUpdateAppointmentmenu();

    }

    //Visar menyn för att söka efter möten
    private static void printFindAppointmentsMenu() {
        //MENY SOM SKA PRINTAS
        System.out.println("1: Sök efter namn ");
        System.out.println("2: Sök efter personnummer ");
        System.out.println("3: Sök efter datum");
        System.out.println("4: Återgå till huvudmenyn");
    }

    //Själva valen för sökning av respektive namn, personnummer, datum.
    private static void appointmentChoice() {

        int option = input.readInt("Ange val>");

        switch (option) {
            case 1:
                String name = input.readString("Sök efter namn: ");
                findName(name);
                break;

            case 2:
                System.out.println("Sök efter 10-siffrigt personnummer: ");
                String idNumber;
                do {
                    idNumber = input.readString("");
                    if (idNumber.length()!= 10){
                        System.out.println("FEL: OBS personnummret måste vara 10 siffror långt");
                    }
                }while (idNumber.length()!= 10);
                findSSNumber(idNumber);
                break;

            case 3:
                String date = input.readString("Sök efter datum: ");
                findDate(date);
                break;
        }
    }

    private static void searchAppointment(){
        printFindAppointmentsMenu();
        appointmentChoice();
    }

    private static void findName(String s) {
        for (Appointment app : appointments){
            if (app.getName().equals(s)){
                System.out.println(app.toString());
                return;
            }
        }
        System.out.println("FEL: Ingen bokning i detta namnet hittades.");
    }
    private static void findSSNumber(String id) {
        for (Appointment app : appointments){
            if (app.getIdNumber().equals(id)){
                System.out.println(app.toString());
                return;
            }
        }
        System.out.printf("FEL: Ingen bokning med detta personnummer hittades.");
    }
    private static void findDate(String date) {
        for (Appointment app : appointments){
            if (app.getDate().equals(date)){
                System.out.println(app.toString());
                return;
            }
        }
        System.out.printf("FEL: Ingen bokning hittades på det sökta datumet %s.%n", date);
    }

    private static void addAppointment() {
        try {
            String name = input.readString("Ange fullständigt namn: ");
            String idNumber = input.readString("\nAnge ditt 10-siffriga personnummer: ");
            String email = input.readString("\nAnge e-postadress: ");
            String date = input.readString("\nAnge datum för mötet med format (ÅÅÅÅ-MM-DD): ");
            String time = input.readString("\nAnge tid för mötet med format (HH:MM): ");
            String description = input.readString("\nAnge en beskrivning av mötet: ");
/*
*Allows accumulation of errors while inputing
 */
            validateName(name);
            validateIdNumber(idNumber,new ArrayList<>());
            validateEmail(email,new ArrayList<>());
            validateDate(date,new ArrayList<>());
            validateTime(time,new ArrayList<>());
            validateDescription(description,new ArrayList<>());

            Appointment newAppointment = new Appointment(
                    generateUniqueId(),
                    name,
                    idNumber,
                    email,
                    date,
                    time,
                    description
            );

            appointments.add(newAppointment);

            System.out.println("\nDitt möte har lagts till framgångsrikt!\n");
        } catch (IllegalArgumentException e) {
            System.out.println("Fel: " + e.getMessage());
        }
    }

    private static void validateName(String name) {
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
                option = input.readInt("");

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
            input.readInt("");
        }
    }
    private static void deleteByName() {
        String appointmentName = input.readString("Ange namnet för mötet du vill ta bort: ");
        appointments.removeIf(appointment -> appointment.getName().equalsIgnoreCase(appointmentName));
        System.out.println("Mötet med namnet '" + appointmentName + "' har tagits bort.");
    }
    private static void deleteByIdNumber() {
        try {
            String appointmentIdNumber = input.readString("Ange personnumret som är kopplat till mötet du vill ta bort:");

            boolean removed = appointments.removeIf(appointment -> appointment.getIdNumber().equals(appointmentIdNumber));

            if (removed) {
                System.out.println("Mötet med personnumret" + appointmentIdNumber + " har tagits bort.");
            } else {
                System.out.println("Inget möte hittades med personnumret " + appointmentIdNumber);
            }
        } catch (InputMismatchException e) {
            System.out.println("Fel: Ange ett giltigt personnummer.");
            input.readInt("");
        }
    }

    private static void deleteByDate() {
        String appointmentDate = input.readString("Ange datumet för mötet du vill ta bort: ");

        appointments.removeIf(appointment -> appointment.getDate().equalsIgnoreCase(appointmentDate));

        System.out.println("Möten med datumet '" + appointmentDate + "' har tagits bort.");
    }

    private static void deleteAllAppointments() {
        String confirmation = input.readString("Varning: Detta kommer att radera alla möten. Är du säker? (ja/nej):");

        if (confirmation.equalsIgnoreCase("ja")) {
            appointments.clear();
            System.out.println("Alla möten har raderats.");
        } else {
            System.out.println("Radering av alla möten avbruten.");
        }
    }

    private static void printAppointmentDetails(Appointment appointment) {
        System.out.println("Mötet med följande detaljer hittades:");
        System.out.println("ID: " + appointment.getId());
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
    private static int generateUniqueId() {
        return idCounter.getAndIncrement();


    }


    //Denna är bara tilllagd som test för att altid ha peroner i listan.
    //Ska tas bort till redovisningen.
    private static void load(){
        Appointment app1 = new Appointment(1234, "app1", "1212121212", "mattias@outlook.com", "2025-12-12", "15:30", "bokning1");
        appointments.add(app1);
        Appointment app2 = new Appointment(4242, "app2", "5252525252", "mattias@hotmail.com", "2024-12-12", "15:35", "bokning2");
        appointments.add(app2);
    }
}
