package de.joernneumeyer.tinyscript.compiler;

import java.util.Queue;

import de.joernneumeyer.tinyscript.exceptions.LexingError;
import de.joernneumeyer.tinyscript.exceptions.ParsingError;
import de.joernneumeyer.tinyscript.runtime.Runtime;
import de.joernneumeyer.tinyscript.runtime.ast.ASTNode;

public class Compiler {
  public Queue<ASTNode> compile(String code) throws LexingError, ParsingError {
    Lexer l = new Lexer();
    Parser p = new Parser();
    
    var tokens = l.lex(code);
    var ast = p.parse(tokens);
    return ast;
  }
}
