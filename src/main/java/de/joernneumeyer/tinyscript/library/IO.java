package de.joernneumeyer.tinyscript.library;

import de.joernneumeyer.tinyscript.runtime.ExecutionContext;

@Library
public class IO {
  @Parameters(names = {"text"})
  public static Object print(ExecutionContext ec) {
    System.out.println(ec.getVariableByName("text").getValue());
    return null;
  }
}
