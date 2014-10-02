package com.amosjyng;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Find all words in a dictionary that are anagrams of some given words
 */
public class AnagramFinder {
  /**
   * Map between sorted strings and all anagrams of that string in a dictionary
   */
  private Map<String, List<String>> anagramsMap;

  /** Where to cache all the sets of anagrams */
  private static String CACHE = "dict.ser";

  public AnagramFinder() {
    anagramsMap = new HashMap<String, List<String>>();
  }

  /**
   * Read in every word from a dictionary and puts it into the anagram mapping
   * 
   * Assumes that each word is on its own line.
   */
  public void createMapping(String dictionaryFilename) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(dictionaryFilename));
    String line;
    while ((line = br.readLine()) != null) {
      addToAnagramsMap(line);
    }
    br.close();
    // save cached mapping so we can just read it in next time
    try {
      ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CACHE));
      oos.writeObject(anagramsMap);
      oos.close();
    } catch (IOException e) {
      System.err.println("Unable to cache mapping, will recreate on next run.");
    }
  }

  /**
   * Reads the mapping from cache. If it fails to do so, reads it from the provided dictionary file
   * instead.
   */
  public void readMapping(String dictionaryFilename) throws IOException {
    try {
      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CACHE));
      anagramsMap = (Map<String, List<String>>) ois.readObject();
      ois.close();
    } catch (IOException e) {
      createMapping(dictionaryFilename);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Given a string, returns the string with its characters in sorted order
   */
  private String getSortedString(String lowercaseWord) {
    char[] chars = lowercaseWord.toCharArray();
    Arrays.sort(chars);
    return new String(chars);
  }

  /**
   * Add a word to its set of anagrams
   */
  public void addToAnagramsMap(String newWord) {
    String lowercaseWord = newWord.toLowerCase(), sortedWord = getSortedString(lowercaseWord);
    if (!anagramsMap.containsKey(sortedWord)) {
      anagramsMap.put(sortedWord, new ArrayList<String>());
    }
    anagramsMap.get(sortedWord).add(lowercaseWord);
  }

  /**
   * Query for all known anagrams of a particular word
   */
  public List<String> getAnagrams(String word) {
    List<String> anagrams = anagramsMap.get(getSortedString(word.toLowerCase()));
    return anagrams == null ? new ArrayList<String>() : anagrams;
  }

  /**
   * Loads a dictionary file into memory, and outputs anagrams for each of the rest of the arguments
   * to this program
   */
  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Usage: java AnagramFinder <dictionary file> [<queries>]");
      System.exit(0);
    }

    AnagramFinder af = new AnagramFinder();
    try {
      af.readMapping(args[0]);
    } catch (IOException e) {
      System.err.println("There was an error reading the dictionary file. Stopping.");
      e.printStackTrace();
      System.exit(1);
    }

    for (int i = 1; i < args.length; i++) {
      String lowercaseWord = args[i].toLowerCase();
      System.out.println("Case-insensitive anagrams of \"" + args[i] + "\":");
      boolean found = false;
      for (String anagram : af.getAnagrams(args[i])) {
        if (!anagram.equals(lowercaseWord)) {
          System.out.println("\t" + anagram);
          found = true;
        }
      }
      if (!found) {
        System.out.println("\tNone found!");
      }
      System.out.println();
    }
  }
}
