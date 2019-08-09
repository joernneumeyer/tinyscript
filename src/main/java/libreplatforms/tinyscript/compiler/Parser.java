package libreplatforms.tinyscript.compiler;

import java.util.LinkedList;
import java.util.Queue;

import libreplatforms.tinyscript.MemorizedQueue;
import libreplatforms.tinyscript.concepts.DataTypes;
import libreplatforms.tinyscript.concepts.Variable;
import libreplatforms.tinyscript.exceptions.ParsingError;
import libreplatforms.tinyscript.exceptions.RuntimeError;
import libreplatforms.tinyscript.runtime.BiFunction;
import libreplatforms.tinyscript.runtime.ExecutionContext;
import libreplatforms.tinyscript.runtime.ast.ASTNode;
import libreplatforms.tinyscript.runtime.ast.Assignment;

public class Parser {
  ASTNode lastAst = null;

  public Queue<ASTNode> parse(Queue<Token> tokens) throws ParsingError {
    Queue<ASTNode> result = new LinkedList<ASTNode>();
    var a = new LinkedList<Token>();
    for (Token t : tokens) {
      a.add(t);
    }

    while (!a.isEmpty()) {
      ASTNode nextAst = this.parseSnippet(a, this.lastAst);
      if (nextAst != null) {
        this.lastAst = nextAst;
        if (!a.isEmpty()) {
          if (a.peek().getType() == TokenType.LINE_BREAK) {
            result.add(nextAst);
          }
        } else {
          result.add(nextAst);
        }
      }
    }

    this.lastAst = null;

    return result;
  }

  private ASTNode parseSnippet(Queue<Token> tokens, ASTNode previousAst) throws ParsingError {
    if (tokens.isEmpty()) {
      return null;
    }

    if (tokens.peek().getType() == TokenType.LINE_BREAK) {
      tokens.poll();
      return null;
    }

    while (!tokens.isEmpty()) {
      Token t = tokens.poll();
      if (t.getType() == TokenType.LINE_BREAK) {
        return null;
      }

      switch (t.getType()) {
        case SUB_TREE_OPEN:
        {
          int counter = 1;
          Queue<Token> subTokens = new LinkedList<Token>();
          while (true) {
            if (tokens.isEmpty()) {
              break;
            }
            Token sub = tokens.poll();
            if (sub.getType() == TokenType.SUB_TREE_OPEN) {
              ++counter;
            } else if (sub.getType() == TokenType.SUB_TREE_CLOSE) {
              --counter;
            }
            
            if (counter == 0) {
              break;
            }
            subTokens.add(sub);
          }
          return this.parse(subTokens).poll();
        }
        
        case VARIABLE_START: {
          Token variableName = tokens.poll();
          if (variableName.getType() != TokenType.TOKEN) {
            continue;
          }

          if (tokens.peek() == null) {
            return (ec) -> ec.getVariableByName(variableName.getValue()).getValue();
          }

          if (tokens.peek().getType() == TokenType.ASSIGNMENT) {
            tokens.poll();
            Queue<Token> foo = new LinkedList<Token>();
            while (!tokens.isEmpty()) {
              if (tokens.peek().getType() != TokenType.LINE_BREAK) {
                foo.add(tokens.poll());
              } else {
                break;
              }
            }
            ASTNode assignment = new Assignment(variableName.getValue(), this.parse(foo).poll());
            return assignment;
          } else {
            return (ec) -> ec.getVariableByName(variableName.getValue()).getValue();
          }
        }

        case NUMBER: {
          ASTNode literalValue = (ec) -> {
            return Double.valueOf(t.getValue());
          };
          return literalValue;
          /*if (tokens.peek().getType() == TokenType.LINE_BREAK) {
            
          } else {
            return this.parseSnippet(tokens, literalValue);
          }*/
        }

        case STRING: {
          ASTNode literalValue = (ec) -> String.valueOf(t.getValue());
          if (tokens.peek().getType() == TokenType.LINE_BREAK) {
            return literalValue;
          } else {
            return this.parseSnippet(tokens, literalValue);
          }
        }
          // break;

        case OPERATION:
          ASTNode leftAst = previousAst;
          ASTNode rightAst = this.parseSnippet(tokens, null);
          switch (t.getValue()) {
            case "+":
              return (ec) -> {
                Object leftValue = leftAst.evaluate(ec);
                Object rightValue = rightAst.evaluate(ec);

                if (DataTypes.T_NUMBER.isType(leftValue) && DataTypes.T_NUMBER.isType(rightValue)) {
                  return (Double) leftValue + (Double) rightValue;
                } else {
                  return String.valueOf(leftValue) + String.valueOf(rightValue);
                }
              };

            case "-":
              return (ec) -> {
                Object leftValue = leftAst.evaluate(ec);
                Object rightValue = rightAst.evaluate(ec);

                if (DataTypes.T_NUMBER.isType(leftValue) && DataTypes.T_NUMBER.isType(rightValue)) {
                  return (Double) leftValue - (Double) rightValue;
                } else {
                  throw new RuntimeError("Cannot perform subtraction on values of types " + DataTypes.getType(leftValue)
                      + " and " + DataTypes.getType(rightValue) + "!");
                }
              };

            case "*":
              return (ec) -> {
                Object leftValue = leftAst.evaluate(ec);
                Object rightValue = rightAst.evaluate(ec);

                if (DataTypes.T_NUMBER.isType(leftValue) && DataTypes.T_NUMBER.isType(rightValue)) {
                  System.out.println("mul -> left: " + leftValue + ";right: " + rightValue);
                  return (Double) leftValue * (Double) rightValue;
                } else {
                  throw new RuntimeError("Cannot perform multiplication on values of types "
                      + DataTypes.getType(leftValue) + " and " + DataTypes.getType(rightValue) + "!");
                }
              };

            case "**":
              return (ec) -> {
                Object leftValue = leftAst.evaluate(ec);
                Object rightValue = rightAst.evaluate(ec);

                if (DataTypes.T_NUMBER.isType(leftValue) && DataTypes.T_NUMBER.isType(rightValue)) {
                  System.out.println("pow -> left: " + leftValue + ";right: " + rightValue);
                  return Math.pow((Double) leftValue, (Double) rightValue);
                } else {
                  throw new RuntimeError("Cannot perform power on values of types " + DataTypes.getType(leftValue)
                      + " and " + DataTypes.getType(rightValue) + "!");
                }
              };

            case "/":
              return (ec) -> {
                Object leftValue = leftAst.evaluate(ec);
                Object rightValue = rightAst.evaluate(ec);

                if (DataTypes.T_NUMBER.isType(leftValue) && DataTypes.T_NUMBER.isType(rightValue)) {
                  return (Double) leftValue / (Double) rightValue;
                } else {
                  throw new RuntimeError("Cannot perform division on values of types " + DataTypes.getType(leftValue)
                      + " and " + DataTypes.getType(rightValue) + "!");
                }
              };

          }
          break;
          
          default:
            throw new ParsingError("Unepected token " + t + "!");
      }
    }

    return null;
  }
}
