package de.joernneumeyer.tinyscript;

import java.util.LinkedList;

public class MemorizedQueue<T> extends LinkedList<T> {
  private T lastElement = null;
  
  public T getLastPolledElement() {
    return this.lastElement;
  }
  
  @Override
  public T poll() {
    return this.lastElement = super.poll();
  }
}
