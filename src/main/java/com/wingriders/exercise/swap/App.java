package com.wingriders.exercise.swap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        int A; // Amplification coefficient
        BigDecimal D; // total deposit size
        int n = 2; // number of currencies

        BigDecimal poolX;
        BigDecimal poolY;
        BigDecimal exchangeX;

        Scanner scanner;
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

                    D = calculatesDValue(A, n, poolX, poolY);

                    BigDecimal deductionFee = new BigDecimal("0.9965");
                    BigDecimal valueToBeExchanged = poolX.multiply(exchangeX).multiply(deductionFee).multiply(D);

                    BigDecimal exchangeY = valueToBeExchanged.divide(poolY, 2, RoundingMode.HALF_UP);
                    writer.println(exchangeY);
                }
            }

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private static BigDecimal calculatesDValue(int A, int n, BigDecimal poolX, BigDecimal poolY) {

//        https://github.com/curvefi/curve-contract/blob/master/tests/simulation.py
//       D invariant calculation in non-overflowing integer operations
//       iteratively
//       A * sum(x_i) * n**n + D = A * D * n**n + D**(n+1) / (n**n * prod(x_i))
//       Converging solution:
//       D[j+1] = (A * n**n * sum(x_i) - D[j]**(n+1) / (n**n prod(x_i))) / (A * n**n - 1)
//
        int ann = A * n;
        BigDecimal numberOfCoinsRaisedToOwnPower = BigDecimal.valueOf(Math.pow(n, n));
        return poolX.multiply(BigDecimal.valueOf(A)).multiply(numberOfCoinsRaisedToOwnPower)  // (A * n**n * sum(x_i) - D[j]**(n+1)
                .divide(poolX.multiply(numberOfCoinsRaisedToOwnPower) , 2, RoundingMode.HALF_UP) // (n**n prod(x_i)))
                .divide (numberOfCoinsRaisedToOwnPower.multiply(BigDecimal.valueOf(A)).subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP); // (A * n**n - 1)

    }

    private static Scanner getScanner() throws FileNotFoundException {
        File input = new File("StableSwap-test-2.csv");
        Scanner reader = new Scanner(input);
        return reader;
    }
}
