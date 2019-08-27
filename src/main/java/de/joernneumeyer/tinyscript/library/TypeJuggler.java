package de.joernneumeyer.tinyscript.library;

import de.joernneumeyer.tinyscript.runtime.ExecutionContext;

@Library
public class TypeJuggler {
  
  @Parameters(names = {"value"})
  public static Object juggle_number(ExecutionContext ec) {
    return Double.valueOf(ec.getVariableByName("value").getValue().toString());
  }
}
