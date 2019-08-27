package de.joernneumeyer.tinyscript.runtime.ast;

import de.joernneumeyer.tinyscript.exceptions.RuntimeError;
import de.joernneumeyer.tinyscript.runtime.ExecutionContext;

@FunctionalInterface
public interface ASTNode {
  Object evaluate(ExecutionContext ec) throws RuntimeError;
}
