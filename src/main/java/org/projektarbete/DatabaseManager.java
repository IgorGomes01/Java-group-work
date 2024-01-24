package org.projektarbete;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hanterar anslutningen till databasen och skapar databas och tabell om de inte redan existerar.
 */
public class DatabaseManager {

    private static final String SERVER_NAME = "localhost";
    private static final String DATABASE_NAME = "AppointmentScheduler";
    static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static final String JDBC_URL = "jdbc:sqlserver://" + SERVER_NAME + ":1433;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
    private static Connection connection; // Deklarera anslutningen på klassnivå
    static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());

    /**
     * Lägger till en stängningskrok för att rensa upp vid avslut, t.ex. stänga databasanslutningen.
     */
    static {
        // Utför städuppgifter vid avslut, såsom att stänga databasanslutningen
        Runtime.getRuntime().addShutdownHook(new Thread(DatabaseManager::closeConnection));
    }

    /**
     * Skapar databas och tabell om de inte redan existerar.
     *
     * @throws SQLException Kastas om det uppstår fel vid skapande eller granskning av databas/tabell.
     */
    static void createDatabaseAndTableIfNotExists() throws SQLException {
        try {
            // Ladda JDBC-drivrutinen
            Class.forName(DRIVER);

            try (Connection localConnection = DriverManager.getConnection(JDBC_URL);
                 Statement statement = localConnection.createStatement()) {

                String checkDatabaseQuery = "SELECT 1 FROM sys.databases WHERE name = '" + DATABASE_NAME + "'";
                ResultSet resultSet = statement.executeQuery(checkDatabaseQuery);

                if (!resultSet.next()) {
                    // Skapa databasen om den inte redan finns
                    String createDatabaseQuery = "CREATE DATABASE " + DATABASE_NAME;
                    statement.executeUpdate(createDatabaseQuery);
                    System.out.println("Databasen har skapats framgångsrikt!");

                    try (Connection dbConnection = DriverManager.getConnection(JDBC_URL + ";databaseName=" + DATABASE_NAME);
                         Statement dbStatement = dbConnection.createStatement()) {

                        // Skapa tabellen Appointments
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
                        System.out.println("Tabellen har skapats framgångsrikt!");
                    }
                } else {
                    System.out.println("Databasen har redan skapats tidigare.");
                }

                // Tilldela localConnection till anslutningen på klassnivå
                connection = localConnection;

            } catch (SQLException e) {
                // Logga fel med logger
                logError("Fel vid skapande eller granskning av databas/tabell: " + e.getMessage());
                logger.log(Level.SEVERE, "Fel vid skapande eller granskning av databas/tabell", e);
                throw e; // Kasta om exception för ytterligare hantering
            }
        } catch (ClassNotFoundException e) {
            // Logga fel med logger
            logError("JDBC-drivrutinen hittades inte");
            logger.log(Level.SEVERE, "JDBC-drivrutinen hittades inte", e);
        }
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
    private static void logError(String errorMessage) {
        logger.log(Level.SEVERE, errorMessage);
    }
}



