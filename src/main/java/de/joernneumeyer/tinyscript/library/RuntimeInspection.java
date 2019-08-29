package de.joernneumeyer.tinyscript.library;

import de.joernneumeyer.tinyscript.runtime.ExecutionContext;

@Library
public class RuntimeInspection {
  public Object dump_ec(ExecutionContext ec) {
    System.out.println(ec);
    return null;
  }
}
