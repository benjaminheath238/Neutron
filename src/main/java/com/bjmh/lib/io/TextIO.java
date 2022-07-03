package com.bjmh.lib.io;

import java.io.IOException;

public class TextIO {
  public static String next(int len) {
    byte[] bytes = new byte[len];
    while (true) {
      try {
        if (System.in.read(bytes) > 0) return new String(bytes).replace("\n", "").trim();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  public static String next(int len, String... of) {
    java.util.Arrays.sort(of);
    while (true) {
      String input = next(len);
      if (java.util.Arrays.binarySearch(of, input) >= 0) return input;
    }
  }
  
  public static String next(int len, String regex) {
    while (true) {
      String input = next(len);
      if (input.matches(regex)) return input;
    }
  }
  
  public static void print(String text) {
    System.out.print(text);
  }

  public static void print(String text, Object... args) {
    StringBuilder builder = new StringBuilder(text);
    for (int i = 0; i < args.length; i++) {
      while (true) {
        int index = builder.indexOf("{" + i + "}");
        if (index == -1) break;
        builder.replace(index, index + 3, (args[i] == null) ? "null" : args[i].toString());
      }
    }
    System.out.print(builder.toString());
  }
}
