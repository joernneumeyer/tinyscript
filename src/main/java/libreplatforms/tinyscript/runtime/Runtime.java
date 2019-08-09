package libreplatforms.tinyscript.runtime;

import java.util.Queue;

import libreplatforms.tinyscript.exceptions.RuntimeError;
import libreplatforms.tinyscript.runtime.ast.ASTNode;

public class Runtime {
  ExecutionContext ec = new ExecutionContext();
  
  public void run(Queue<ASTNode> instructions) throws RuntimeError {
    for (ASTNode a : instructions) {
      a.evaluate(this.ec);
    }
  }
  
  public void dump() {
    System.out.println(this.ec);
  }
}
