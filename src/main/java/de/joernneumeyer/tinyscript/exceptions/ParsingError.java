package de.joernneumeyer.tinyscript.exceptions;

public class ParsingError extends Exception {
  public ParsingError(String message) {
    super(message);
  }
  
  public ParsingError(String message, Exception inner) {
    super(message, inner);
  }
}
