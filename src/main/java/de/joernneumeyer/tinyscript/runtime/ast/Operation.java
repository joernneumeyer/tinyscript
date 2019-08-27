package de.joernneumeyer.tinyscript.runtime.ast;

import de.joernneumeyer.tinyscript.runtime.BiFunction;
import de.joernneumeyer.tinyscript.runtime.ExecutionContext;

public class Operation implements ASTNode {
  private ASTNode left, right;
  private BiFunction<Object, Object, Object> op;
  
  public Operation(ASTNode left, ASTNode right, BiFunction<Object, Object, Object> op) {
    this.left = left;
    this.right = right;
    this.op = op;
  }
  
  @Override
  public Object evaluate(ExecutionContext ec) {
    return this.op.apply(this.left, this.right, ec);
  }
  
}
