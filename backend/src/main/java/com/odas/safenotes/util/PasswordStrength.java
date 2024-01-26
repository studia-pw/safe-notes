package com.odas.safenotes.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PasswordStrength {

    public boolean passwordIsStrongEnough(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=.*[a-zA-Z0-9@#$%^&+=!]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

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
