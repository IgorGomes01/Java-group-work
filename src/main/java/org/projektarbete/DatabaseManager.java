package org.projektarbete;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Hanterar anslutningen till databasen och skapar databas och tabell om de inte redan existerar.
 */
public class DatabaseManager {

    private static final String SERVER_NAME = "localhost";
    private static final String DATABASE_NAME = "AppointmentScheduler";
    static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    private static final String JDBC_URL = "jdbc:sqlserver://" + SERVER_NAME + ":1433;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
    private static Connection connection; // Deklarera anslutningen på klassnivå

    /**
     * Lägger till en stängningskrok för att rensa upp vid avslut, t.ex. stänga databasanslutningen.
     */
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Utför städuppgifter vid avslut, såsom att stänga databasanslutningen
            closeConnection();
        }));
    }
    /**
     * Skapar databas och tabell om de inte redan existerar.
     */
    static void createDatabaseAndTableIfNotExists() {
        try {
            Class.forName(DRIVER); // Ladda JDBC-drivrutinen

            try (Connection localConnection = DriverManager.getConnection(JDBC_URL);
                 Statement statement = localConnection.createStatement()) {

                String checkDatabaseQuery = "SELECT 1 FROM sys.databases WHERE name = '" + DATABASE_NAME + "'";
                ResultSet resultSet = statement.executeQuery(checkDatabaseQuery);

                if (!resultSet.next()) {
                    String createDatabaseQuery = "CREATE DATABASE " + DATABASE_NAME;
                    statement.executeUpdate(createDatabaseQuery);
                    System.out.println("Din databas har skapats framgångsrikt!");

                    try (Connection dbConnection = DriverManager.getConnection(JDBC_URL + ";databaseName=" + DATABASE_NAME);
                         Statement dbStatement = dbConnection.createStatement()) {

                        String createTableQuery = "CREATE TABLE Appointments ( " +
                                "Id INT PRIMARY KEY IDENTITY(1,1), " + // Auto-ökande ID
                                "Name NVARCHAR(255), " +
                                "IdNumber NVARCHAR(20), " +
                                "Email NVARCHAR(255), " +
                                "Date NVARCHAR(20), " +
                                "Time NVARCHAR(20), " +
                                "Description NVARCHAR(MAX) " +
                                ")";

                        dbStatement.executeUpdate(createTableQuery);
                        System.out.println("Din tabell har skapats framgångsrikt!");
                    }
                } else {
                    System.out.println("Din databas har redan skapats tidigare.");
                }

                // Tilldela localConnection till anslutningen på klassnivå
                connection = localConnection;

            } catch (SQLException e) {
                System.err.println("Fel vid skapande eller granskning av databas/tabell: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("JDBC driver not found");
        }
    }


    /**
     * Etablerar en anslutning till databasen.
     *
     * @return Connection-objekt om det är framgångsrikt, annars null.
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(JDBC_URL + ";databaseName=" + DATABASE_NAME + ";integratedSecurity=true;encrypt=true;trustServerCertificate=true;");
        } catch (SQLException e) {
            System.err.println("Fel vid etablering av databasanslutning: " + e.getMessage());
            e.printStackTrace();
            return null;
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
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Databasanslutning stängd.");
            }
        } catch (SQLException e) {
            System.err.println("Fel vid stängning av databasanslutning: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
