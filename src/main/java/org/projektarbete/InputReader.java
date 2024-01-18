package org.projektarbete;

import java.util.Scanner;

public class InputReader {

    Scanner input = new Scanner(System.in);

    /**
     * Läser in en sträng från tangentbordet
     *
     * @param prompt är det som användaren ska svara på
     * @return vad användaren skrev in
     */
    public String readString(String prompt) {
        System.out.print(prompt);
        return input.nextLine();

    }

    public int readInt(String prompt) {
        System.out.print(prompt);
        int number = input.nextInt();
        input.nextLine();
        return number;

    }
}

