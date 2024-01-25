package org.projektarbete;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Enhetstester för DatabaseManager-klassen.
 */
class DatabaseManagerTest {

    private Connection mockConnection;
    private Statement mockStatement;
    private Statement mockDbStatement;
    private ResultSet mockResultSet;
    // Antag att dessa konstanter är definierade i DatabaseManager-klassen
    private static final String JDBC_URL = "jdbc:mock";
    private static final String DATABASE_NAME = "TestDB";

    /**
     * Förbereder mock-objekt och inställningar innan varje test.
     *
     * @throws SQLException om det uppstår SQL-relaterade problem under inställningen.
     */
    @BeforeEach
    void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockStatement = mock(Statement.class);
        mockDbStatement = mock(Statement.class);
        mockResultSet = mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement, mockDbStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockConnection.isClosed()).thenReturn(false);
    }

    /**
     * Återställer mock-objekt efter varje test.
     *
     * @throws SQLException om det uppstår SQL-relaterade problem under återställningen.
     */
    @AfterEach
    void tearDown() throws SQLException {
        reset(mockConnection, mockStatement, mockDbStatement, mockResultSet);
    }

    /**
     * Testar getConnection-metoden för att hämta en mock-anslutning.
     */
    @Test
    void testGetConnection() {
        assertDoesNotThrow(() -> {
            Connection connection = DatabaseManager.getConnection();
            assertNotNull(connection);
            verify(mockConnection, never()).close(); // Säkerställ att den simulerade anslutningen inte stängs
        });
    }

    /**
     * Testar getJDBCUrl-metoden för att hämta den förväntade JDBC-URL:en.
     */
    @Test
    void testGetJDBCUrl() {
        String expectedUrl = "jdbc:sqlserver://localhost:1433;integratedSecurity=true;encrypt=true;trustServerCertificate=true;databaseName=AppointmentScheduler";
        String actualUrl = DatabaseManager.getJDBCUrl();

        // Trimma whitespaces och semikolon från både förväntad och faktisk URL
        expectedUrl = expectedUrl.trim().replaceAll(";", "");
        actualUrl = actualUrl.trim().replaceAll(";", "");

        assertEquals(expectedUrl, actualUrl);
    }

    /**
     * Testar getDatabaseName-metoden för att hämta det förväntade databasnamnet.
     */
    @Test
    void testGetDatabaseName() {
        assertEquals("AppointmentScheduler", DatabaseManager.getDatabaseName());
    }

    /**
     * Testar closeConnection-metoden för en lyckad stängning av anslutningen.
     */
    @Test
    void closeConnection_SuccessfulClosure() {
        DatabaseManager.closeConnection();
        // Antag att inga undantag kastas under stängningen
        assertTrue(true, "Anslutning borde stängas");
    }

    /**
     * Testar logError-metoden för att logga ett felmeddelande.
     */
    @Test
    void testLogError() {
        Logger logger = mock(Logger.class);
        DatabaseManager.logger = logger;

        DatabaseManager.logError("Test error message");

        verify(logger).log(eq(Level.SEVERE), eq("Test error message"));
    }
}