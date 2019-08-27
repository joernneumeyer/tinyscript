package libreplatforms.tinyscript.runtime;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import libreplatforms.tinyscript.concepts.Function;
import libreplatforms.tinyscript.concepts.Variable;

public class ExecutionContext {
  private Map<String, Variable> variables = new HashMap<String, Variable>();
  private Map<String, Function> functions = new HashMap<String, Function>();
  
  public Variable getVariableByName(String name) {
    return this.variables.get(name);
  }
  
  public void putVariable(Variable v) {
    this.variables.put(v.getName(), v);
  }
  
  public Function getFunction(String name) {
    return this.functions.get(name);
  }
  
  public void putFunction(Function f) {
    this.functions.put(f.getName(), f);
  }
  
  @Override
  public String toString() {
    return "ExecutionContext(variables="
        + Arrays.toString(this.variables.entrySet().stream().map(x -> x.getValue().toString()).toArray())
        + ";functions="
        + Arrays.toString(this.functions.entrySet().stream().map(x -> x.getValue().toString()).toArray())
      + ")";
  }
}
