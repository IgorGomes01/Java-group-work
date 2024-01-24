package org.projektarbete;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Test class for Validation class
class ValidationTest {

    // Test case for validating a valid name
    @Test
    void validateName_ValidName_NoError() {
        // Arrange
        List<String> errorMessage = new ArrayList<>();

        // Act
        Validation.validateName("John Doe", errorMessage);

        // Assert
        assertTrue(errorMessage.isEmpty(), "No error should be present for a valid name");
    }

    // Test case for validating a null name
    @Test
    void validateName_NullName_Error() {
        // Arrange
        List<String> errorMessage = new ArrayList<>();

        // Act
        Validation.validateName(null, errorMessage);

        // Assert
        assertFalse(errorMessage.isEmpty(), "Error should be present for a null name");
    }

    // Test case for validating a valid ID number
    @Test
    void validateIdNumber_ValidIdNumber_NoError() {
        // Arrange
        List<String> errorMessage = new ArrayList<>();

        // Act
        Validation.validateIdNumber("1234567890", errorMessage);

        // Assert
        assertTrue(errorMessage.isEmpty(), "No error should be present for a valid ID number");
    }

    // Test case for validating an invalid ID number
    @Test
    void validateIdNumber_InvalidIdNumber_Error() {
        // Arrange
        List<String> errorMessage = new ArrayList<>();

        // Act
        Validation.validateIdNumber("123", errorMessage);

        // Assert
        assertFalse(errorMessage.isEmpty(), "Error should be present for an invalid ID number");
    }

    // ... (similar comments for the remaining test cases)

    // Test case for validating all fields with valid input
    @Test
    void validateAllFields_AllValidFields_NoError() {
        // Arrange
        List<String> errorMessage = new ArrayList<>();

        // Act
        Validation.validateAllFields("John Doe", "1234567890", "john.doe@example.com", "2024-01-31", "15:30", "Project meeting", errorMessage);

        // Assert
        assertTrue(errorMessage.isEmpty(), "No error should be present for all valid fields");
    }

    // Test case for validating all fields with some invalid input
    @Test
    void validateAllFields_InvalidFields_Error() {
        // Arrange
        List<String> errorMessage = new ArrayList<>();

        // Act
        Validation.validateAllFields(null, "123", "invalid.email", "2024/01/31", "25:30", null, errorMessage);

        // Assert
        assertFalse(errorMessage.isEmpty(), "Errors should be present for some invalid fields");
    }
}
