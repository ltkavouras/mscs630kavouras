/*
 * File: Driver_lab1.java
 * Author: Louis Thomas Kavouras
 * Course: CMPT 630, Security Algorithms and Protocols
 * Professor: Dr. Pablo Rivas
 * Assignment: Lab 1 Introduction To Cryptography
 * Due Date: February 2nd, 2020
 * Version: 0.5
 *
 * Brief Description:
 * This algorithm accepts input such as a word or sentence and converts the input's characters into integers based on
 * each letter's respective position of the English alphabet.  The algorithm then returns the provided input with the
 * converted text underneath.
 *
 * Example:
 * If the user entered "hello james" the output will be the following:
 *
 * 7 4 11 11 14 9 0 12 4 18
 * hello <--> 7 4 11 11 14
 * james <--> 9 0 12 4 18
 *
 * Each integer below its respective letter corresponds to the position of the letter within the English alphabet
 */

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Driver_lab1 {

  public static void main(String[] args) {

    // Initialize Scanner
    Scanner input = new Scanner(System.in);

    // While loop declaration for receiving input and displaying output
    while (input.hasNext()) {

      int[] l = str2int(input.nextLine());

      // For-each loop used to output the characters seperated by a space.  
      for (int c : l) {

        System.out.print(c + " ");

      }

      // Seperate output
      System.out.println();

    }

  }

  // Method that converts String input to an integer
  static int[] str2int(String Sentence) {

    // Initialize the integer output array
    int[] output = new int[Sentence.length()];

    // Initialize the Treemap with the key pair of characters with respective integers
    Map<String, Integer> tm = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    tm.put("a", 0);
    tm.put("b", 1);
    tm.put("c", 2);
    tm.put("d", 3);
    tm.put("e", 4);
    tm.put("f", 5);
    tm.put("g", 6);
    tm.put("h", 7);
    tm.put("i", 8);
    tm.put("j", 9);
    tm.put("k", 10);
    tm.put("l", 11);
    tm.put("m", 12);
    tm.put("n", 13);
    tm.put("o", 14);
    tm.put("p", 15);
    tm.put("q", 16);
    tm.put("r", 17);
    tm.put("s", 18);
    tm.put("t", 19);
    tm.put("u", 20);
    tm.put("v", 21);
    tm.put("w", 22);
    tm.put("x", 23);
    tm.put("y", 24);
    tm.put("z", 25);
    tm.put(" ", 26);

    // This for loop extracts each indiviudla character from the provided string
    for (int i = 0; i < Sentence.length(); i++) {

      char c = Sentence.charAt(i);

      output[i] = tm.get(String.valueOf(c));

    }

    return output;

  }

}

