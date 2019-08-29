package de.joernneumeyer.tinyscript.library;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.joernneumeyer.tinyscript.exceptions.RuntimeError;
import de.joernneumeyer.tinyscript.runtime.ExecutionContext;

@Library
public class Collections {
  public Object list(ExecutionContext ec) {
    return array_list(ec);
  }
  
  public Object linked_list(ExecutionContext ec) {
    return new LinkedList<Object>();
  }
  
  public Object array_list(ExecutionContext ec) {
    return new ArrayList<Object>();
  }
  
  @Parameters(names = {"list","item"})
  public Object list_push(ExecutionContext ec) throws RuntimeError {
    Object maybeList = ec.getVariableByName("list").getValue();
    if (!(maybeList instanceof List<?>)) {
      throw new RuntimeError("Cannot push item if no valid list is provided!");
    }
    var list = (List)maybeList;
    list.add(ec.getVariableByName("item").getValue());
    return null;
  }
}
