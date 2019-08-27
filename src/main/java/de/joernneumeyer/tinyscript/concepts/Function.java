package de.joernneumeyer.tinyscript.concepts;

import java.util.Collection;
import java.util.Map;

import de.joernneumeyer.tinyscript.exceptions.RuntimeError;
import de.joernneumeyer.tinyscript.runtime.ExecutionContext;
import de.joernneumeyer.tinyscript.runtime.ast.ASTNode;

public class Function implements ASTNode {
  private ASTNode[] instructions;
  private String name;
  private Collection<String> parameterNames;
  
  public Function(String name, Collection<ASTNode> instructions, Collection<String> parameterNames) {
    this.name = name;
    this.instructions = new ASTNode[instructions.size()];
    instructions.toArray(this.instructions);
    this.parameterNames = parameterNames;
  }
  
  public String getName() {
    return this.name;
  }
  
  public Collection<String> getParameterNames() {
    return this.parameterNames;
  }
  
  public Object apply(Map<String, Variable> parameters) throws RuntimeError {
    ExecutionContext ec = new ExecutionContext();
    for (String p : this.parameterNames) {
      ec.putVariable(parameters.get(p));
    }
    for (int i = 0; i < this.instructions.length - 1; ++i) {
      this.instructions[i].evaluate(ec);
    }
    return this.instructions[this.instructions.length - 1].evaluate(ec);
  }

  @Override
  public Object evaluate(ExecutionContext ec) throws RuntimeError {
    ec.putFunction(this);
    return null;
  }
}
