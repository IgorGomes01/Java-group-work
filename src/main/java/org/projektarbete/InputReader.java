package org.projektarbete;
import java.util.Scanner;

/**
 * En klass som hanterar inläsning av användarinmatning från terminalen.
 */
public class InputReader implements AutoCloseable {

    private final Scanner input = new Scanner(System.in);

    /**
     * Läser in en sträng från terminalen.
     *
     * @param prompt en prompt som visas för användaren
     * @return den inmatade strängen från användaren
     */
    public String readString(String prompt) {
        System.out.print(prompt + " ");
        return input.nextLine();
    }

    /**
     * Läser in en hel rad från terminalen.
     *
     * @return den inmatade raden från användaren
     */
    public String nextLine() {
        return input.nextLine();
    }

    /**
     * Läser in ett enskilt ord eller token från terminalen.
     *
     * @return det inmatade ordet eller tokenet från användaren
     */
    public String next() {
        return input.next();
    }

    /**
     * Läser in ett heltal från terminalen.
     *
     * @return det inmatade heltalet från användaren
     */
    public int nextInt() {
        return input.nextInt();
    }

    /**
     * Stänger Scanner-objektet för att slutföra inmatningsoperationer.
     */
    public void closeScanner() {
        input.close();
    }

    /**
     * Stänger InputReader och därmed även Scanner-objektet.
     */
    @Override
    public void close() {
        closeScanner();
    }
}