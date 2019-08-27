package de.joernneumeyer.tinyscript.concepts;

public enum DataTypes implements DataType {
  T_STRING("string"), T_NUMBER("number"), T_UNKNOWN("unknown");
  
  private String name;
  
  private DataTypes(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return this.name;
  }
  
  @Override
  public Boolean isType(Object o) {
    return DataTypes.getType(o) == this;
  }
  
  public static DataType getType(Object o) {
    if (o instanceof String) {
      return T_STRING;
    } else if (o instanceof Double) {
      return T_NUMBER;
    } else {
      return T_UNKNOWN;
    }
  }
}
