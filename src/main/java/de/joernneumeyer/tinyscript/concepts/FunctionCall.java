package libreplatforms.tinyscript.concepts;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import libreplatforms.tinyscript.exceptions.RuntimeError;
import libreplatforms.tinyscript.runtime.ExecutionContext;
import libreplatforms.tinyscript.runtime.ast.ASTNode;

public class FunctionCall implements ASTNode {
  private String functionName;
  private ASTNode[] astParameters;
  
  public FunctionCall(String functionName, Collection<ASTNode> astParameters) {
    this.functionName = functionName;
    this.astParameters = new ASTNode[astParameters.size()];
    astParameters.toArray(this.astParameters);
  }

  @Override
  public Object evaluate(ExecutionContext ec) throws RuntimeError {
    Map<String, Variable> parameters = new HashMap<String, Variable>();
    Function functionToCall = ec.getFunction(this.functionName);
    if (functionToCall == null) {
      throw new RuntimeError("Tried to call undefined function '" + this.functionName + "'!");
    }
    
    if (functionToCall.getParameterNames().size() != this.astParameters.length) {
      throw new RuntimeError("Mismatch in parameter count! Expected " + functionToCall.getParameterNames().size() + " but got " + this.astParameters.length + " instead!");
    }
    
    Object[] parameterNames = functionToCall.getParameterNames().toArray();
    
    for (int i = 0; i < this.astParameters.length; ++i) {
      Object parameterValue = this.astParameters[i].evaluate(ec);
      String parameterName = (String)parameterNames[i];
      parameters.put(parameterName, new Variable(parameterName, DataTypes.getType(parameterValue), parameterValue));
    }
    
    return functionToCall.apply(parameters);
  }

}
