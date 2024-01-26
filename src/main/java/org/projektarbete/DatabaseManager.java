package org.projektarbete;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hanterar kommunikation med databasen för mötesschemahantering.
 */
public class DatabaseManager {

    // Konstanter för databasanslutning
    private static final String SERVER_NAME = "localhost";
    private static final String DATABASE_NAME = "AppointmentScheduler";
    static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    // JDBC URL för anslutning
    private static final String JDBC_URL = "jdbc:sqlserver://" + SERVER_NAME + ":1433;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
    private static Connection connection;
    static Logger logger = Logger.getLogger(DatabaseManager.class.getName());

    // Shutdown hook för att stänga anslutningen vid programslut
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::closeConnection));
    }

    /**
     * Skapar databasen om den inte redan finns.
     *
     * @throws SQLException kastas om det uppstår ett SQL-fel
     */
    static void createDatabaseIfNotExists() throws SQLException {
        try (Connection localConnection = DriverManager.getConnection(JDBC_URL);
             Statement statement = localConnection.createStatement()) {

            // Kontrollera om databasen redan existerar
            String checkDatabaseQuery = "SELECT 1 FROM sys.databases WHERE name = '" + DATABASE_NAME + "'";
            ResultSet resultSet = statement.executeQuery(checkDatabaseQuery);

            if (!resultSet.next()) {
                // Skapa databasen om den inte finns
                String createDatabaseQuery = "CREATE DATABASE " + DATABASE_NAME;
                statement.executeUpdate(createDatabaseQuery);
                System.out.println("Ditt MÖTESSCHEMA har skapats framgångsrikt!");
            } else {
                System.out.println("Välkommen tillbaka!");
            }

            connection = localConnection;

        } catch (SQLException e) {
            logError("Fel vid skapande eller granskning av databas: " + e.getMessage());
            logger.log(Level.SEVERE, "Fel vid skapande eller granskning av databas", e);
            throw e;
        }
    }

    /**
     * Skapar tabellen om den inte redan finns.
     *
     * @throws SQLException kastas om det uppstår ett SQL-fel
     */
    static void createTableIfNotExists() throws SQLException {
        try (Connection dbConnection = DriverManager.getConnection(JDBC_URL + ";databaseName=" + DATABASE_NAME);
             Statement dbStatement = dbConnection.createStatement()) {

            // Kontrollera om tabellen redan existerar
            String checkTableQuery = "SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Appointments'";
            ResultSet resultSet = dbStatement.executeQuery(checkTableQuery);

            if (!resultSet.next()) {
                // Skapa tabellen om den inte finns
                String createTableQuery = "CREATE TABLE Appointments ( " +
                        "Id INT PRIMARY KEY IDENTITY(1,1), " +
                        "Name NVARCHAR(MAX), " +
                        "IdNumber NVARCHAR(20), " +
                        "Email NVARCHAR(50), " +
                        "Date NVARCHAR(20), " +
                        "Time NVARCHAR(20), " +
                        "Description NVARCHAR(MAX) " +
                        ")";
                dbStatement.executeUpdate(createTableQuery);
                System.out.println("Lägg till ditt första möte.");
            } else {
                System.out.println("Data från den senaste anslutningen finns kvar i systemet.");
            }

        } catch (SQLException e) {
            logError("Fel vid skapande eller granskning av tabell: " + e.getMessage());
            logger.log(Level.SEVERE, "Fel vid skapande eller granskning av tabell", e);
            throw e;
        }
    }

    /**
     * Skapar databasen och tabellen om de inte redan finns.
     *
     * @throws SQLException kastas om det uppstår ett SQL-fel
     */
    static void createDatabaseAndTableIfNotExists() throws SQLException {
        createDatabaseIfNotExists();
        createTableIfNotExists();
    }

    /**
     * Etablerar en anslutning till databasen.
     *
     * @return Connection-objekt om det är framgångsrikt, annars null.
     */
    public static Connection getConnection() {
        try {
            // Försök skapa en anslutning
            return DriverManager.getConnection(JDBC_URL + ";databaseName=" + DATABASE_NAME + ";integratedSecurity=true;encrypt=true;trustServerCertificate=true;");
        } catch (SQLException e) {
            // Logga fel med logger
            logger.log(Level.SEVERE, "Fel vid etablering av databasanslutning: " + e.getMessage(), e);
            return null; // Returnera null om anslutningen inte kunde upprättas
        }
    }

    /**
     * Returnerar JDBC URL för databasanslutningen.
     *
     * @return JDBC URL
     */
    public static String getJDBCUrl() {
        return JDBC_URL + ";databaseName=" + DATABASE_NAME;
    }

    /**
     * Returnerar namnet på databasen.
     *
     * @return Databasens namn
     */
    public static String getDatabaseName() {
        return DATABASE_NAME;
    }

    /**
     * Stänger databasanslutningen.
     */
    public static void closeConnection() {
        try {
            // Stäng anslutningen om den inte redan är stängd
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Databasanslutning stängd.");
            }
        } catch (SQLException e) {
            // Logga fel med logger
            logger.log(Level.SEVERE, "Fel vid stängning av databasanslutning: " + e.getMessage(), e);
        }
    }

    /**
     * Loggar ett felmeddelande.
     *
     * @param errorMessage Felmeddelandet att logga.
     */
    static void logError(String errorMessage) {
        logger.log(Level.SEVERE, errorMessage);
    }
}
