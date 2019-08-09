package libreplatforms.tinyscript.concepts;

public class Variable {
  private DataType type;
  private Object value;
  private String name;
  
  public Variable(String name, DataType type, Object value) {
    this.type = type;
    this.value = value;
    this.name = name;
  }
  
  public DataType getType() {
    return this.type;
  }
  
  public void setType(DataType type) {
    this.type = type;
  }
  
  public Object getValue() {
    return this.value;
  }
  
  public void setValue(Object value) {
    this.value = value;
  }
  
  public String getName() {
    return this.name;
  }
  
  @Override
  public String toString() {
    return "Variable(name=" + this.name + ";type=" + this.type + ";value=" + this.value + ")";
  }
}
