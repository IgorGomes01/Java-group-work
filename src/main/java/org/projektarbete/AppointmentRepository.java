package org.projektarbete;
import java.sql.*;
import java.util.Map;

/**
 * Hanterar mötesinformation och interaktion med en databas.
 */
public class AppointmentRepository {

    // Fält för databaskolumnernas namn
    private static final String FIELD_NAME = "name";
    private static final String FIELD_ID_NUMBER = "idNumber";
    private static final String FIELD_DATE = "date";

    // Konstanter för JDBC URL och databasnamn
    private static final String JDBC_URL = DatabaseManager.getJDBCUrl();
    private static final String DATABASE_NAME = DatabaseManager.getDatabaseName();

    // InputReader för användarinput
    private static final InputReader inputReader = new InputReader();

    // Kartläggning av engelska fältnamn till svenska visningsnamn
    private static final Map<String, String> SWEDISH_FIELD_NAMES = Map.of(
            FIELD_NAME, "Namn",
            FIELD_ID_NUMBER, "Personnummer",
            FIELD_DATE, "Datum"
    );

    // Totalt antal möten
    private int totalMeetings;

    /**
     * Skapar en AppointmentRepository med en initial totalt möten-räkning på 0.
     */
    public AppointmentRepository() {
        this.totalMeetings = 0;
    }

    /**
     * Hämtar det aktuella antalet mötesposter i systemet.
     *
     * @return Antalet mötesposter i systemet.
     */
    public int getRecordCount() {
        return totalMeetings;
    }

    /**
     * Initialiserar databasen genom att skapa nödvändiga tabeller och laddar det totala antalet möten.
     */
    public void initializeDatabase() {
        DatabaseManager.createDatabaseAndTableIfNotExists();
        loadTotalMeetings();
    }

    /**
     * Laddar det totala antalet möten från databasen.
     */

    /**
     * Hanterar ett SQLException genom att skriva ut felmeddelandet och stacktrace till standardfelströmmen.
     *
     * @param message Beskrivande meddelande om det uppstådda felet.
     * @param e SQLException som innehåller information om det uppstådda felet.
     */
    private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }
    private void loadTotalMeetings() {
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {

            String query = "SELECT COUNT(*) FROM Appointments";
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                totalMeetings = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            handleSQLException("Fel vid inläsning av totalMeetings", e);
        }
    }

    /**
     * Lägger till ett möte i databasen.
     *
     * @param appointment Möte att lägga till.
     * @throws IllegalArgumentException Om ett fel inträffar under insättning i databasen.
     */
    public void addAppointment(Appointment appointment) {
        try (PreparedStatement preparedStatement = DatabaseManager.getConnection().prepareStatement(
                "INSERT INTO Appointments (Name, IdNumber, Email, Date, Time, Description) VALUES (?, ?, ?, ?, ?, ?)")) {

            preparedStatement.setString(1, appointment.getName());
            preparedStatement.setString(2, appointment.getIdNumber());
            preparedStatement.setString(3, appointment.getEmail());
            preparedStatement.setString(4, appointment.getDate());
            preparedStatement.setString(5, appointment.getTime());
            preparedStatement.setString(6, appointment.getDescription());

            preparedStatement.executeUpdate();

            // Ökar totalMeetings-räkningen efter lyckad addition
            totalMeetings++;

        } catch (SQLException e) {
            System.err.println("Fel vid läggning av möte i databasen: " + e.getMessage());
            e.printStackTrace();
            throw new IllegalArgumentException("Fel vid läggning av möte i databasen: " + e.getMessage());
        }
    }

    /**
     * Söker efter möten baserat på ett fält och dess värde.
     *
     * @param field Fältet att söka efter.
     * @param value Värdet att matcha.
     */
    public void searchByField(String field, String value) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL + ";databaseName=" + DATABASE_NAME);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Appointments WHERE " + field + " = ?")) {

            statement.setString(1, value);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    printAppointmentDetails(resultSet);
                }
            }
        } catch (SQLException e) {
            handleSQLException("Fel vid sökning efter poster", e);
        }
    }

    /**
     * Skriver ut detaljer för ett möte från ResultSet till konsolen.
     *
     * @param resultSet ResultSet som innehåller mötesdetaljerna.
     * @throws SQLException Om det uppstår ett SQL-relaterat fel vid åtkomst till ResultSet.
     */
    private void printAppointmentDetails(ResultSet resultSet) throws SQLException {
        System.out.println("Namn: " + resultSet.getString("Name"));
        System.out.println("Personnummer: " + resultSet.getString("IdNumber"));
        System.out.println("E-post: " + resultSet.getString("Email"));
        System.out.println("Datum: " + resultSet.getString("Date"));
        System.out.println("Tid: " + resultSet.getString("Time"));
        System.out.println("Beskrivning: " + resultSet.getString("Description"));
        System.out.println("------------------------------------------------------------");
    }

    /**
     * Kontrollerar om det finns en post i databasen som matchar det angivna fältet och värdet.
     *
     * @param connection En aktiv JDBC Connection.
     * @param field      Fältet att söka efter (t.ex., "name", "idNumber", "date").
     * @param value      Värdet som ska matchas i det angivna fältet.
     * @return true om en matchande post finns, annars false.
     * @throws SQLException om det uppstår ett SQL-relaterat fel.
     */
    private boolean recordExistsByField(Connection connection, String field, String value) throws SQLException {
        String query = "SELECT 1 FROM Appointments WHERE " + field + " = ?";

        try (PreparedStatement checkStatement = connection.prepareStatement(query)) {
            checkStatement.setString(1, value);
            ResultSet resultSet = checkStatement.executeQuery();
            return resultSet.next();
        }
    }


    /**
     * Uppdaterar en mötespost i databasen baserat på det angivna fältet och det gamla värdet.
     *
     * @param field         Fältet att söka efter (t.ex., "name", "idNumber", "date").
     * @param oldValue      Det gamla värdet i det angivna fältet.
     * @param newAppointment En Appointment-objekt med uppdaterade värden.
     * @throws SQLException Om det uppstår ett SQL-relaterat fel under uppdateringen.
     */
    void updateRecord(String field, String oldValue, Appointment newAppointment) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL + ";databaseName=" + DATABASE_NAME)) {

            // Kontrollera om posten finns innan uppdatering
            if (!recordExistsByField(connection, field, oldValue)) {
                System.out.println("Ingen post hittades med det angivna värdet för fältet.");
                return;
            }

            // Posten finns, fortsätt med uppdateringen

            System.out.println("Uppdaterar informationen...");
            Thread.sleep(500);
            System.out.println("Nytt namn: " + newAppointment.getName());
            System.out.println("Nytt personnummer: " + newAppointment.getIdNumber());
            System.out.println("Ny e-post: " + newAppointment.getEmail());
            System.out.println("Nytt datum: " + newAppointment.getDate());
            System.out.println("Ny tid: " + newAppointment.getTime());
            System.out.println("Ny beskrivning: " + newAppointment.getDescription());
            System.out.println("------------------------------------------------------------");

            try (PreparedStatement updateStatement = connection.prepareStatement(
                    "UPDATE Appointments SET name = ?, idNumber = ?, email = ?, date = ?, time = ?, description = ? WHERE " + field + " = ?",
                    Statement.NO_GENERATED_KEYS)) {

                // Sätt parametrar för uppdateringsuttrycket
                updateStatement.setString(1, newAppointment.getName());
                updateStatement.setString(2, newAppointment.getIdNumber());
                updateStatement.setString(3, newAppointment.getEmail());
                updateStatement.setString(4, newAppointment.getDate());
                updateStatement.setString(5, newAppointment.getTime());
                updateStatement.setString(6, newAppointment.getDescription());
                updateStatement.setString(7, oldValue);

                int rowsAffected = updateStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Posten har uppdaterats framgångsrikt!");
                } else {
                    System.out.println("Ingen post hittades med det angivna värdet för fältet.");
                }
            } catch (SQLException e) {
                handleSQLException("Fel vid uppdatering av möte", e);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Hanterar resultatet av en raderingsförfrågan och visar lämpliga meddelanden.
     *
     * @param rowsAffected Antal rader påverkade av raderingsförfrågan.
     * @param value        Värdet som raderades.
     * @param fieldName    Namnet på fältet där raderingen skedde.
     */
    private void handleDeleteResult(int rowsAffected, String value, String fieldName) {
        if (rowsAffected > 0) {
            totalMeetings--;

            if (totalMeetings == 0) {
                // Återställ identitetsfröet för ID-kolumnen när totalMeetings når 0
                resetIdentitySeed();
            }

            System.out.println("\nPost med " + getSwedishFieldName(fieldName) + " '" + value + "' har raderats framgångsrikt!");
            System.out.println("Totalt antal möten i systemet: " + totalMeetings + "\n");
        } else {
            System.out.println("Ingen post hittades med " + getSwedishFieldName(fieldName) + " '" + value + "'.");
        }
    }

    /**
     * Återställer identitetsfröet för ID-kolumnen i databasen.
     */
    private void resetIdentitySeed() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL + ";databaseName=" + DATABASE_NAME);
             Statement statement = connection.createStatement()) {

            // Återställ identitetsfröet för ID-kolumnen
            String resetIdentityQuery = "DBCC CHECKIDENT ('Appointments', RESEED, 0)";
            statement.execute(resetIdentityQuery);

            System.out.println("Identity seed för ID-kolumnen har återställts till 0.");
        } catch (SQLException e) {
            handleSQLException("Fel vid återställning av identity seed", e);
            e.printStackTrace();
        }
    }

    /**
     * Raderar en post från databasen baserat på ett angivet fältnamn och värde.
     * Uppdaterar även totala mötesräkningen och återställer identitetsfröet vid behov.
     *
     * @param fieldName Fältnamn som ska matchas för att hitta posten.
     * @param value     Värdet som används för att matcha och identifiera posten.
     */
    private void deleteRecordByField(String fieldName, String value) {
        try (Connection connection = DatabaseManager.getConnection()) {
            String deleteQuery = "DELETE FROM Appointments WHERE " + fieldName + " = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
                statement.setString(1, value);
                int rowsAffected = statement.executeUpdate();

                handleDeleteResult(rowsAffected, value, fieldName);
            }
        } catch (SQLException e) {
            handleSQLException("Fel vid radering av möte med " + getSwedishFieldName(fieldName), e);
        }
    }


    /**
     * Hämtar det svenska fältnamnet för ett givet engelskt fältnamn.
     *
     * @param englishFieldName Det engelska fältnamnet.
     * @return Det svenska fältnamnet om det finns, annars det engelska fältnamnet.
     */
    String getSwedishFieldName(String englishFieldName) {
        return SWEDISH_FIELD_NAMES.getOrDefault(englishFieldName, englishFieldName);
    }

    /**
     * Raderar möten baserat på namn.
     *
     * @param nameToDelete Namnet på mötet som ska raderas.
     */
    public void deleteAppointmentByName(String nameToDelete) {
        deleteRecordByField(FIELD_NAME, nameToDelete);
    }

    /**
     * Raderar möten baserat på personnummer.
     *
     * @param idNumberToDelete Personnummer för mötet som ska raderas.
     */
    public void deleteAppointmentByIdNumber(String idNumberToDelete) {
        deleteRecordByField(FIELD_ID_NUMBER, idNumberToDelete);
    }

    /**
     * Raderar möten baserat på datum.
     *
     * @param dateToDelete Datumet för mötet som ska raderas.
     */
    public void deleteAppointmentByDate(String dateToDelete) {
        deleteRecordByField(FIELD_DATE, dateToDelete);
    }

    /**
     * Raderar alla möten från databasen.
     */
    public void deleteAllRecords() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL + ";databaseName=" + DATABASE_NAME);
             Statement statement = connection.createStatement()) {

            // Om totalMeetings är 0, återgå till huvudmenyn
            if (totalMeetings == 0) {
                System.out.println("Inga poster att radera.");
                System.out.println("Återgår till huvudmenyn...");
                return;
            }

            // Fråga om bekräftelse för att radera alla möten
            System.out.println("Varning: Detta kommer att radera alla möten. Är du säker? (ja/nej):");
            String confirmation = inputReader.nextLine(); // Använd inputReader istället för scanner

            if (confirmation.equalsIgnoreCase("ja")) {
                String deleteQuery = "DELETE FROM Appointments";
                int rowsAffected = statement.executeUpdate(deleteQuery);

                if (rowsAffected > 0) {
                    totalMeetings = 0;

                    // Återställ identitetsfröet för ID-kolumnen
                    String resetIdentityQuery = "DBCC CHECKIDENT ('Appointments', RESEED, 0)";
                    statement.execute(resetIdentityQuery);

                    System.out.println("Alla poster har raderats framgångsrikt!");
                    System.out.println("Totalt antal möten i systemet: " + totalMeetings + "\n");
                } else {
                    System.out.println("Inga poster hittades att radera.");
                }
            } else {
                System.out.println("Radering av alla möten avbruten.");
            }

        } catch (SQLException e) {
            handleSQLException("Fel vid radering av alla poster", e);
            e.printStackTrace();
        }
    }

    /**
     * Visar alla mötesposter i databasen.
     *
     * @throws SQLException Om det uppstår ett SQL-relaterat fel.
     */
    public void showRecords() throws SQLException {
        String query = "SELECT * FROM Appointments";

        try (Connection connection = DriverManager.getConnection(JDBC_URL + ";databaseName=" + DATABASE_NAME);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            int totalMeetings = 0;

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString(FIELD_NAME);
                String idNumber = resultSet.getString(FIELD_ID_NUMBER);
                String email = resultSet.getString("email");
                String date = resultSet.getString(FIELD_DATE);
                String time = resultSet.getString("time");
                String description = resultSet.getString("description");

                System.out.println("ID: " + id);
                System.out.println(getSwedishFieldName(FIELD_NAME) + ": " + name);
                System.out.println(getSwedishFieldName(FIELD_ID_NUMBER) + ": " + idNumber);
                System.out.println("E-post: " + email);
                System.out.println(getSwedishFieldName(FIELD_DATE) + ": " + date);
                System.out.println("Tid: " + time);
                System.out.println("Beskrivning: " + description);
                System.out.println("------------------------------------------------------------");

                totalMeetings++;
            }

            System.out.println("\nTotalt antal möten i systemet: " + totalMeetings + "\n");

        } catch (SQLException e) {
            System.err.println("Fel vid visning av poster: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Stänger inputläsaren.
     */
    public void closeInputReader() {
        inputReader.closeScanner(); // Se till att skannern stängs när den behövs
    }
}
