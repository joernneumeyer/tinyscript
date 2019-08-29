package de.joernneumeyer.tinyscript.concepts;

import java.util.List;

public enum DataTypes implements DataType {
  T_STRING("string"), T_NUMBER("number"), T_UNKNOWN("unknown"), T_LIST("list"), T_ARRAY("array");
  
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
    if (o == null)
      return T_UNKNOWN;
    if (o instanceof String)
      return T_STRING;
    else if (o instanceof Double)
      return T_NUMBER;
    else if (o.getClass().isArray())
      return T_ARRAY;
    else if (o instanceof List<?>)
      return T_LIST;
    else
      return T_UNKNOWN;
  }
}
