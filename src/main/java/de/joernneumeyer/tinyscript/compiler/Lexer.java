package de.joernneumeyer.tinyscript.compiler;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import de.joernneumeyer.tinyscript.exceptions.LexingError;



public class Lexer {
  public static final Predicate<String> WORD_CHARACTER_REGEX = Pattern.compile("[a-zA-Z_]").asMatchPredicate();
  public static final Pattern NUMBER_CHARACTER_REGEX = Pattern.compile("[0-9]+(\\.[0-9]+)?");
  
  public Queue<Token> lex(String code) throws LexingError {
    Queue<Token> tokens = new LinkedList<Token>();
    
    for (int i = 0; i < code.length(); ++i) {
      char c = code.charAt(i);
      switch (c) {
        case '(':
          tokens.add(new Token(TokenType.SUB_TREE_OPEN));
          break;
        
        case ')':
          tokens.add(new Token(TokenType.SUB_TREE_CLOSE));
          break;
          
        case '[':
          tokens.add(new Token(TokenType.FUNCTION_CALL_OPEN));
          break;
        
        case ']':
          tokens.add(new Token(TokenType.FUNCTION_CALL_CLOSE));
          break;
          
        case ':':
          if (code.charAt(i + 1) == '=') {
            tokens.add(new Token(TokenType.ASSIGNMENT));
            ++i;
          } else {
            tokens.add(new Token(TokenType.FUNCTION_ARGUMENT_DIVIDER));
          }
          break;
          
        case '@':
          tokens.add(new Token(TokenType.RUNTIME_DIRECTIVE_INDICATOR));
          break;
          
        case '*':
          if (code.charAt(i + 1) == '*') {
            ++i;
            tokens.add(new Token(TokenType.OPERATION, "**"));
            break;
          }
        case '+':
        case '-':
        case '/':
          tokens.add(new Token(TokenType.OPERATION, String.valueOf(c)));
          break;
          
        case '$':
          tokens.add(new Token(TokenType.VARIABLE_START));
          break;
        case '\n':
          tokens.add(new Token(TokenType.LINE_BREAK));
          break;
        case '"':
        {
          int j = 1;
          for (; code.charAt(i + j) != '"' && code.charAt(i + j) != '\n'; j++);
          if (code.charAt(i + j) != '"') {
            // TODO throw error - invalid string ending or line break in string
          }
          tokens.add(new Token(TokenType.STRING, code.substring(i + 1, i + j)));
          i += j;
        }
          break;
          
        case '#':
          tokens.add(new Token(TokenType.FUNCTION_START));
          break;
          
        // case list for characters to ignore
        case ' ':
          
          break;
          
        default:
          if (WORD_CHARACTER_REGEX.test(String.valueOf(c))) {
            String t = readToken(code, i);
            i += t.length() - 1;
            tokens.add(new Token(TokenType.SYMBOL, t));
          } else if (NUMBER_CHARACTER_REGEX.matcher(String.valueOf(c)).find()) {
            var m = NUMBER_CHARACTER_REGEX.matcher(String.valueOf(code.substring(i)));
            m.find();
            String match = m.toMatchResult().group();
            tokens.add(new Token(TokenType.NUMBER, match));
            i += match.length() - 1;
          } else {
            throw new LexingError("Could not lex character '" + c + "'!");
          }
      }
    }
    
    
    return tokens;
  }
  
  private String readToken(String code, int fromIndex) {
    int i = 0;
    for (; WORD_CHARACTER_REGEX.test(String.valueOf(code.charAt(fromIndex + i))); ++i);
    String tokenText = code.substring(fromIndex, fromIndex + i);
    return tokenText;
  }
}
