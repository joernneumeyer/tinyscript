package libreplatforms.tinyscript.runtime.ast;

import libreplatforms.tinyscript.exceptions.RuntimeError;
import libreplatforms.tinyscript.runtime.ExecutionContext;

@FunctionalInterface
public interface ASTNode {
  Object evaluate(ExecutionContext ec) throws RuntimeError;
}
