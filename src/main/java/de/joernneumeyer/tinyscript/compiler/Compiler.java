package libreplatforms.tinyscript.compiler;

import java.util.Queue;

import libreplatforms.tinyscript.exceptions.LexingError;
import libreplatforms.tinyscript.exceptions.ParsingError;
import libreplatforms.tinyscript.runtime.Runtime;
import libreplatforms.tinyscript.runtime.ast.ASTNode;

public class Compiler {
  public Queue<ASTNode> compile(String code) throws LexingError, ParsingError {
    Lexer l = new Lexer();
    Parser p = new Parser();
    
    var tokens = l.lex(code);
    var ast = p.parse(tokens);
    return ast;
  }
}
