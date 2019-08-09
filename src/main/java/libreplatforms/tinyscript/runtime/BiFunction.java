package libreplatforms.tinyscript.runtime;

@FunctionalInterface
public interface BiFunction<V, W, R> {
  R apply(V arg0, W arg1, ExecutionContext ec);
}
