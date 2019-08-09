package libreplatforms.tinyscript.exceptions;

public class RuntimeError extends Exception {
  public RuntimeError(String message) {
    super(message);
  }
  
  public RuntimeError(String message, Exception inner) {
    super(message, inner);
  }
}
