package de.joernneumeyer.tinyscript.library;

import java.util.Scanner;

import de.joernneumeyer.tinyscript.runtime.ExecutionContext;

@Library
public class IO {
  static Scanner consoleScanner = new Scanner(System.in);
  
  @Parameters(names = {"text"})
  public static Object print(ExecutionContext ec) {
    System.out.println(ec.getVariableByName("text").getValue());
    return null;
  }
  
  public static Object scan(ExecutionContext ec) {
    return consoleScanner.nextLine();
  }
  
  @Parameters(names = {"prompt"})
  public static Object prompt(ExecutionContext ec) {
    System.out.print(ec.getVariableByName("prompt").getValue());
    return scan(ec);
  }
}
