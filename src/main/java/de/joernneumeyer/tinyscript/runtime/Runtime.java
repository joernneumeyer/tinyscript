package de.joernneumeyer.tinyscript.runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Queue;

import de.joernneumeyer.tinyscript.concepts.Function;
import de.joernneumeyer.tinyscript.exceptions.RuntimeError;
import de.joernneumeyer.tinyscript.library.Parameters;
import de.joernneumeyer.tinyscript.runtime.ast.ASTNode;

public class Runtime {
  ExecutionContext ec = new ExecutionContext();

  public Runtime(ExecutionContext ec) {
    this.ec = ec;
  }

  public Runtime() {
    this(new ExecutionContext());
  }

  public void loadLibrary(Class library) {
    Object libraryInstance;
    
    try {
      libraryInstance = library.getConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | NoSuchMethodException | SecurityException e1) {
      libraryInstance = new Object();
    }
    
    final Object bindingInstance = libraryInstance;
    
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
          Object result = m.invoke(bindingInstance, ec);
          return result;
        } catch (InvocationTargetException e) {
          throw new RuntimeError("Could not invoke library function '" + m.getName() + "'!", e);
        } catch (IllegalAccessException e) {
          throw new RuntimeError("Could not invoke library function '" + m.getName() + "'!", e);
        }
      };
      
      Function f = new Function(m.getName(), Arrays.asList(astMethod), Arrays.asList(parameters));
      this.ec.putFunction(f);
    }
  }

  public void run(Queue<ASTNode> instructions) throws RuntimeError {
    try {
      for (ASTNode a : instructions) {
        a.evaluate(this.ec);
      }
    } catch (NullPointerException e) {
      throw new RuntimeError("NULL pointer encountered!", e);
    }
  }

  public void dump() {
    System.out.println(this.ec);
  }
}
