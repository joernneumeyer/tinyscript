package libreplatforms.tinyscript.runtime;

@FunctionalInterface
public interface Function<V, R> {
  R apply(V arg0, ExecutionContext ec);
}
