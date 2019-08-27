package de.joernneumeyer.tinyscript.runtime.ast;

import de.joernneumeyer.tinyscript.concepts.DataType;
import de.joernneumeyer.tinyscript.concepts.DataTypes;
import de.joernneumeyer.tinyscript.concepts.Variable;
import de.joernneumeyer.tinyscript.exceptions.RuntimeError;
import de.joernneumeyer.tinyscript.runtime.ExecutionContext;

public class Assignment implements ASTNode {
  private String variableName;
  private ASTNode value;
  
  public Assignment(String variableName, ASTNode value) {
    this.variableName = variableName;
    this.value = value;
  }
  
  @Override
  public Object evaluate(ExecutionContext ec) throws RuntimeError {
    Object assignmentValue = this.value.evaluate(ec);
    DataType assignmentType = DataTypes.getType(assignmentValue);
    Variable v = ec.getVariableByName(this.variableName);
    
    if (v == null) {
      v = new Variable(this.variableName, assignmentType, ec);
      v.setValue(assignmentValue);
      ec.putVariable(v);
    } else {
      v.setType(assignmentType);
      v.setValue(assignmentValue);
    }
    return v.getValue();
  }
  
  @Override
  public String toString() {
    return "Assignment to variable with name '" + this.variableName + "'";
  }
}
