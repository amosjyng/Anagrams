package com.amosjyng;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for finding matching anagrams.
 */
public class AnagramFinderTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName
   *          name of the test case
   */
  public AnagramFinderTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(AnagramFinderTest.class);
  }

  /**
   * Test adding and querying anagrams of words
   */
  public void testAddingAndQueryingSingleWords() {
    AnagramFinder af = new AnagramFinder();
    af.addToAnagramsMap("tea");
    af.addToAnagramsMap("ate");
    assertEquals(Arrays.asList("tea", "ate"), af.getAnagrams("eat"));
  }
}
