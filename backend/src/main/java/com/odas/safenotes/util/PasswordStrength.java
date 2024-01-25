package com.odas.safenotes.util;

import java.util.HashMap;
import java.util.Map;

public class PasswordStrength {
    private double countShannonEntropy(String password) {
        final var characterFrequencies = countCharacterFrequencies(password);
        final var passwordLength = password.length();

        double negativeEntropy = 0.0;
        for (final var character : characterFrequencies.keySet()) {
            final var characterFrequency = characterFrequencies.get(character);
            final var characterProbability = (double) characterFrequency / passwordLength;
            negativeEntropy += characterProbability * log2(characterProbability);
        }
        return -negativeEntropy;
    }

    private Map<Character, Integer> countCharacterFrequencies(String password) {
        final var characterFrequencies = new HashMap<Character, Integer>();
        for (final var character : password.toCharArray()) {
            if (characterFrequencies.containsKey(character)) {
                characterFrequencies.put(character, characterFrequencies.get(character) + 1);
            } else {
                characterFrequencies.put(character, 1);
            }
        }

        return characterFrequencies;
    }

    private double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

}
