package libreplatforms.tinyscript.exceptions;

public class LexingError extends Exception {
  public LexingError(String message) {
    super(message);
  }
  
  public LexingError(String message, Exception inner) {
    super(message, inner);
  }
}
