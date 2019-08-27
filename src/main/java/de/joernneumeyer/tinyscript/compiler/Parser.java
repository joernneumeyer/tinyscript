package de.joernneumeyer.tinyscript.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.joernneumeyer.tinyscript.concepts.DataTypes;
import de.joernneumeyer.tinyscript.concepts.Function;
import de.joernneumeyer.tinyscript.concepts.FunctionCall;
import de.joernneumeyer.tinyscript.exceptions.ParsingError;
import de.joernneumeyer.tinyscript.exceptions.RuntimeError;
import de.joernneumeyer.tinyscript.runtime.ast.ASTNode;
import de.joernneumeyer.tinyscript.runtime.ast.Assignment;

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

  private Function parseFunction(Queue<Token> tokens) throws ParsingError {
    Token functionName = tokens.poll();
    if (functionName.getType() != TokenType.TOKEN) {
      throw new ParsingError("Expected token of type " + TokenType.TOKEN + " after " + TokenType.FUNCTION_START
          + " but got " + functionName.getType() + "!");
    }

    List<String> parameterNames = new ArrayList<String>();

    {
      while (tokens.peek().getType() != TokenType.LINE_BREAK) {
        Token varStart = tokens.poll();
        if (varStart.getType() != TokenType.VARIABLE_START) {
          throw new ParsingError("Expected " + TokenType.VARIABLE_START + " but got " + varStart.getType() + "!");
        }
        Token parameterName = tokens.poll();
        if (parameterName.getType() != TokenType.TOKEN) {
          throw new ParsingError("Expected " + TokenType.TOKEN + " but got " + tokens.peek().getType() + "!");
        }
        parameterNames.add(parameterName.getValue());
      }
    }

    Queue<Token> functionTokens = new LinkedList<Token>();

    while (true) {
      Token t = tokens.poll();
      if (t.getType() != TokenType.FUNCTION_START) {
        functionTokens.add(t);
      } else {
        t = tokens.poll();
        if (t.getType() != TokenType.TOKEN) {
          throw new ParsingError(
              "Expected " + TokenType.TOKEN + " for function definition end but got " + t.getType() + "!");
        } else if (!t.equals(functionName)) {
          throw new ParsingError("The function name at the beginning and end habe to match!");
        }
        break;
      }
    }

    return new Function(functionName.getValue(), this.parse(functionTokens), parameterNames);
  }

  private FunctionCall parseFunctionCall(Queue<Token> tokens) throws ParsingError {
    Token functionName = tokens.poll();
    if (functionName.getType() != TokenType.TOKEN) {
      throw new ParsingError(
          "Expected " + TokenType.TOKEN + " for function call end but got " + functionName.getType() + "!");
    }

    Queue<Token> callTokens = new LinkedList<Token>();

    {
      int functionCallBracketCounter = 1;
      while (true) {
        Token t = tokens.poll();
        if (t.getType() == TokenType.FUNCTION_CALL_CLOSE) {
          --functionCallBracketCounter;
        } else if (t.getType() == TokenType.FUNCTION_CALL_OPEN) {
          ++functionCallBracketCounter;
        }
        if (functionCallBracketCounter == 0) {
          break;
        }
        callTokens.add(t);
      }
    }

    if (callTokens.size() == 0) {
      tokens.poll();
      return new FunctionCall(functionName.getValue(), Arrays.asList(new ASTNode[0]));
    }

    List<ASTNode> astParameters = new ArrayList<ASTNode>();

    while (true) {
      Token divider = callTokens.poll();
      if (divider.getType() != TokenType.FUNCTION_ARGUMENT_DIVIDER) {
        System.err.println(divider);
        throw new ParsingError("Expected argutment divider but got " + divider.getType() + " instead!");
      }
      Queue<Token> parameterTokens = new LinkedList<Token>();
      while (true) {
        Token t = callTokens.poll();
        if (t == null) {
          break;
        }
        else if (t.getType() == TokenType.FUNCTION_CALL_OPEN) {
          parameterTokens.add(t);
          int functionCallBracketCounter = 1;
          while (true) {
            Token t2 = callTokens.poll();
            if (t2.getType() == TokenType.FUNCTION_CALL_CLOSE) {
              --functionCallBracketCounter;
            } else if (t2.getType() == TokenType.FUNCTION_CALL_OPEN) {
              ++functionCallBracketCounter;
            }
            parameterTokens.add(t2);
            if (functionCallBracketCounter == 0) {
              break;
            }
          }
        } else {
          parameterTokens.add(t);
        }
        if (callTokens.size() > 0) {
          if (callTokens.peek().getType() == TokenType.FUNCTION_ARGUMENT_DIVIDER) {
            break;
          }
        }
      }
      astParameters.add(this.parse(parameterTokens).poll());
      if (callTokens.size() == 0) {
        break;
      }
    }

    return new FunctionCall(functionName.getValue(), astParameters);
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
        case FUNCTION_START:
          return this.parseFunction(tokens);

        case FUNCTION_CALL_OPEN:
          return this.parseFunctionCall(tokens);

        case SUB_TREE_OPEN: {
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
          /*
           * if (tokens.peek().getType() == TokenType.LINE_BREAK) {
           * 
           * } else { return this.parseSnippet(tokens, literalValue); }
           */
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
