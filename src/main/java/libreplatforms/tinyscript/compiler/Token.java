package libreplatforms.tinyscript.compiler;

public class Token {
  private TokenType type;
  private String value;
  
  public Token(TokenType type, String value) {
    this.type = type;
    this.value = value;
  }
  
  public Token(TokenType type) {
    this(type, "");
  }
  
  public TokenType getType() {
    return this.type;
  }
  
  public String getValue() {
    return this.value;
  }
  
  @Override
  public String toString() {
    return "Token(type=" + this.type + ";value=" + this.value + ")";
  }
}
