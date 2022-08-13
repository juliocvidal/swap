package com.wingriders.exercise.swap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        int A;
        BigDecimal poolX;
        BigDecimal poolY;
        BigDecimal exchangeX;

        MathContext mathContext = new MathContext(2);

        Scanner scanner = null;
        try {
            scanner = getScanner();
            try (PrintWriter writer = new PrintWriter("answers.out", "UTF-8")) {
                String header = scanner.nextLine();

                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    String[] values = line.split(",");
                    A = Integer.parseInt(values[0]);
                    poolX = new BigDecimal(values[1]);
                    poolY = new BigDecimal(values[2]);
                    exchangeX = new BigDecimal(values[3]);
                    writer.println("A: " + A + "; Pool X: " + poolX + "; Pool Y: " + poolY + "; Exchange X: " + exchangeX);

                    // BigDecimal deductionFee = new BigDecimal("0.9965");
                    BigDecimal valueToBeExchanged = poolX.multiply(exchangeX, mathContext);//.multiply(deductionFee, mathContext);
                    writer.println("valueToBeExchanged: " + valueToBeExchanged);

                    BigDecimal exchangeY = valueToBeExchanged.divide(poolY, mathContext);
                    writer.println("exchangeY: " + exchangeY);
                }
            }

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private static Scanner getScanner() throws FileNotFoundException {
        File input = new File("StableSwap-test-2.csv");
        Scanner reader = new Scanner(input);
        return reader;
    }
}
