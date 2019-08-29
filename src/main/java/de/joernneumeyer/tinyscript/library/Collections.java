package de.joernneumeyer.tinyscript.library;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.joernneumeyer.tinyscript.concepts.DataTypes;
import de.joernneumeyer.tinyscript.concepts.Variable;
import de.joernneumeyer.tinyscript.exceptions.RuntimeError;
import de.joernneumeyer.tinyscript.runtime.ExecutionContext;

@Library
public class Collections {
  public Object list(ExecutionContext ec) {
    return array_list(ec);
  }
  
  @Parameters(names = {"list"})
  public Object list_dump(ExecutionContext ec) {
    System.out.println(ec);
    Variable listVariable = ec.getVariableByName("list");
    System.out.println(listVariable);
    List<Object> list = (List<Object>)listVariable.getValue();
    for (Object o : list) {
      System.out.println(o);
    }
    return null;
  }
  
  @Parameters(names = {".values"})
  public Object list_with_values(ExecutionContext ec) {
    System.out.println("foobar");
    var list = (List<Object>)ec.getVariableByName("values").getValue();
    System.out.println(list.size());
    return list;
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
