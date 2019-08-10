package libreplatforms.tinyscript.runtime;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Queue;

import libreplatforms.tinyscript.concepts.Function;
import libreplatforms.tinyscript.exceptions.RuntimeError;
import libreplatforms.tinyscript.library.Parameters;
import libreplatforms.tinyscript.runtime.ast.ASTNode;

public class Runtime {
  ExecutionContext ec = new ExecutionContext();

  public Runtime(ExecutionContext ec) {
    this.ec = ec;
  }

  public Runtime() {
    this(new ExecutionContext());
  }

  public void loadLibrary(Class library) {
    for (Method m : library.getMethods()) {
      String[] parameters;
      Parameters[] p = m.getAnnotationsByType(Parameters.class);
      if (p.length == 1) {
        parameters = p[0].names();
      } else {
        parameters = new String[0];
      }
      
      ASTNode astMethod = (ec) -> {
        try {
          return m.invoke(null, ec);
        } catch (Exception e) {
          return null;
        }
      };
      
      Function f = new Function(m.getName(), Arrays.asList(astMethod), Arrays.asList(parameters));
      this.ec.putFunction(f);
    }
  }

  public void run(Queue<ASTNode> instructions) throws RuntimeError {
    for (ASTNode a : instructions) {
      a.evaluate(this.ec);
    }
  }

  public void dump() {
    System.out.println(this.ec);
  }
}
