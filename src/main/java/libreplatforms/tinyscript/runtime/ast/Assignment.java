package libreplatforms.tinyscript.runtime.ast;

import libreplatforms.tinyscript.concepts.DataType;
import libreplatforms.tinyscript.concepts.DataTypes;
import libreplatforms.tinyscript.concepts.Variable;
import libreplatforms.tinyscript.exceptions.RuntimeError;
import libreplatforms.tinyscript.runtime.ExecutionContext;

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
