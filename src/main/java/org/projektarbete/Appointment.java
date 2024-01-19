package org.projektarbete;

/**
 * Appointment-klassen representerar ett möte och innehåller information om mötet,
 * inklusive namn, ID-nummer, e-postadress, datum, tid och beskrivning.
 */
public class Appointment {
    private String name; // Namnet på personen som mötet gäller
    private String idNumber; // Personnummer
    private String email; // E-postadress för personen
    private String date; // Datumet för mötet (format: ÅÅÅÅ-MM-DD)
    private String time; // Tiden för mötet (format: HH:MM)
    private String description; // Beskrivning av mötet

    /**
     * Skapar en ny instans av Appointment med angiven information.
     *
     * @param name        Namnet för mötet.
     * @param idNumber    ID-numret för personen.
     * @param email       E-postadressen för personen.
     * @param date        Datumet för mötet (format: ÅÅÅÅ-MM-DD).
     * @param time        Tiden för mötet (format: HH:MM).
     * @param description Beskrivningen av mötet.
     */
    public Appointment(String name, String idNumber, String email, String date, String time, String description) {
        this.name = name;
        this.idNumber = idNumber;
        this.email = email;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    /**
     * Returnerar namnet för mötet.
     *
     * @return Namnet för mötet.
     */
    public String getName() {
        return name;
    }

    /**
     * Returnerar ID-numret för personen.
     *
     * @return ID-numret för personen.
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * Returnerar e-postadressen för personen.
     *
     * @return E-postadressen för personen.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returnerar datumet för mötet.
     *
     * @return Datumet för mötet (format: ÅÅÅÅ-MM-DD).
     */
    public String getDate() {
        return date;
    }

    /**
     * Returnerar tiden för mötet.
     *
     * @return Tiden för mötet (format: HH:MM).
     */
    public String getTime() {
        return time;
    }

    /**
     * Returnerar beskrivningen av mötet.
     *
     * @return Beskrivningen av mötet.
     */
    public String getDescription() {
        return description;
    }
}
