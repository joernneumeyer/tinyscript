package de.joernneumeyer.tinyscript.concepts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.joernneumeyer.tinyscript.exceptions.RuntimeError;
import de.joernneumeyer.tinyscript.runtime.ExecutionContext;
import de.joernneumeyer.tinyscript.runtime.ast.ASTNode;

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
    if (functionToCall == null)
      throw new RuntimeError("Tried to call undefined function '" + this.functionName + "'!");
    
    Object[] parameterNames = functionToCall.getParameterNames().toArray();
    boolean hasVargs = false;
    
    if (parameterNames.length > 0)
      if (parameterNames[parameterNames.length - 1].toString().startsWith(".")) {
        if (this.astParameters.length < parameterNames.length - 1)
          throw new RuntimeError("Mismatch in parameter count! Expected at least " + (parameterNames.length - 1) + " but got " + this.astParameters.length + " instead!");
        hasVargs = true;
      }
    
    if (!hasVargs)
      if (functionToCall.getParameterNames().size() != this.astParameters.length)
        throw new RuntimeError("Mismatch in parameter count! Expected " + functionToCall.getParameterNames().size() + " but got " + this.astParameters.length + " instead!");
    
    for (int i = 0; i < parameterNames.length; ++i) {
      if (hasVargs && i >= parameterNames.length - 2)
        break;
      Object parameterValue = this.astParameters[i].evaluate(ec);
      String parameterName = (String)parameterNames[i];
      parameters.put(parameterName, new Variable(parameterName, DataTypes.getType(parameterValue), parameterValue));
    }
    
    if (hasVargs) {
      String vargName = parameterNames[parameterNames.length - 1].toString().substring(1);
      int vargsLength = this.astParameters.length - parameterNames.length + 1;
      List<Object> vargs = new ArrayList<Object>();
      for (int i = 0; i < vargsLength; ++i) {
        vargs.add(this.astParameters[parameterNames.length - 1 + i]);
      }
      parameters.put(
          vargName,
          new Variable(vargName, DataTypes.getType(vargs), vargs)
      );
    }
    
    return functionToCall.apply(parameters);
  }

}
