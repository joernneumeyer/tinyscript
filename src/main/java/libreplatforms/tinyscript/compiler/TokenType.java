package libreplatforms.tinyscript.compiler;

public enum TokenType {
  TOKEN,
  OPERATION,
  LINE_BREAK,
  VARIABLE_START,
  ASSIGNMENT,
  STRING,
  NUMBER,
  SUB_TREE_OPEN,
  SUB_TREE_CLOSE,
  FUNCTION_START,
  FUNCTION_CALL_OPEN,
  FUNCTION_CALL_CLOSE,
  FUNCTION_ARGUMENT_DIVIDER
}
