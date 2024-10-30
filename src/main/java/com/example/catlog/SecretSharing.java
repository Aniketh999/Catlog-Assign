package com.example.catlog;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SecretSharing {

    public static void runSecretSharing() {
        try {
            // Step 1: Read JSON input from a file located in resources
            String content = new String(Files.readAllBytes(Paths.get(SecretSharing.class.getClassLoader().getResource("input.json").toURI())));
            JSONObject jsonObject = new JSONObject(content); // Use JSONObject directly

            int n = jsonObject.getJSONObject("keys").getInt("n");
            int k = jsonObject.getJSONObject("keys").getInt("k");

            List<Integer> xValues = new ArrayList<>();
            List<Integer> yValues = new ArrayList<>();

            // Step 2: Decode Y values
            for (String key : jsonObject.keySet()) {
                if (!key.equals("keys")) {
                    JSONObject root = jsonObject.getJSONObject(key);
                    int base = root.getInt("base");
                    String value = root.getString("value");
                    int decodedValue = decodeValue(base, value);

                    xValues.add(Integer.parseInt(key));
                    yValues.add(decodedValue);
                }
            }

            // Step 3: Calculate constant term c using Lagrange interpolation
            int c = calculateConstantTerm(xValues, yValues, k);

            // Step 4: Output the result
            System.out.println("The constant term c is: " + c);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to decode value from a given base
    private static int decodeValue(int base, String value) {
        int decodedValue = 0;
        int power = 0;

        for (int i = value.length() - 1; i >= 0; i--) {
            decodedValue += (value.charAt(i) - '0') * Math.pow(base, power);
            power++;
        }

        return decodedValue;
    }

    // Method to calculate the constant term c using Lagrange interpolation
    private static int calculateConstantTerm(List<Integer> xValues, List<Integer> yValues, int k) {
        double c = 0; // Use double for more accurate calculations

        for (int i = 0; i < k; i++) {
            int xi = xValues.get(i);
            int yi = yValues.get(i);

            double li = 1.0; // Lagrange basis polynomial

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    li *= (0.0 - xValues.get(j)) / (xi - xValues.get(j));
                }
            }

            c += yi * li;
        }

        return (int) Math.round(c); // Round to nearest integer
    }
}