package libreplatforms.tinyscript.library;

import libreplatforms.tinyscript.runtime.ExecutionContext;

public class IO {
  @Parameters(names = {"text"})
  public static Object print(ExecutionContext ec) {
    System.out.println(ec.getVariableByName("text").getValue());
    return null;
  }
}
