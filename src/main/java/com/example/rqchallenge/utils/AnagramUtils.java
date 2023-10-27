package com.example.rqchallenge.utils;

import java.util.ArrayList;
import java.util.List;

public class AnagramUtils {

    public static List<String> getAllAnagrams(String str) {
        List<String> anagrams = new ArrayList<String>();
        buildAnagrams(anagrams, str, "");
        return anagrams;
    }

    private static void buildAnagrams(List<String> anagrams, String str, String ans) {
        if (str.length() == 0) {
            anagrams.add(ans);
            return;
        }

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            String ros = str.substring(0, i) + str.substring(i + 1);
            buildAnagrams(anagrams, ros, ans + ch);
        }
    }
}
