package libreplatforms.tinyscript;

import java.io.BufferedReader;
import java.io.FileReader;
import libreplatforms.tinyscript.compiler.Compiler;
import libreplatforms.tinyscript.exceptions.LexingError;
import libreplatforms.tinyscript.exceptions.ParsingError;
import libreplatforms.tinyscript.exceptions.RuntimeError;
import libreplatforms.tinyscript.runtime.Runtime;

public class Main {
	public static void main(String[] args) {
	  String code;
	  {
	    StringBuilder sb = new StringBuilder();
	    try (BufferedReader reader = new BufferedReader(new FileReader("/home/bitcrusher/eclipse-workspace/java/TinyScript/src/main/resources/sample.tisc"))) {
	      for (Object line : reader.lines().toArray()) {
	        sb.append((String)line);
	        sb.append('\n');
	      }
	    } catch (Exception e) {
	      System.err.println("could not read source! error: " + e.getMessage());
	      System.exit(1);
	    }
      code = sb.toString();
	  }
		System.out.println("read source.");
		Compiler c = new Compiler();
		Runtime r = new Runtime();
		try {
		  var instructions = c.compile(code);
		  r.run(instructions);
		  r.dump();
		} catch (LexingError e) {
      System.err.println("A lexing error occurred: " + e.getMessage());
      e.printStackTrace();
    } catch (ParsingError e) {
      System.err.println("A parsing error occurred: " + e.getMessage());
      e.printStackTrace();
    } catch (RuntimeError e) {
      System.err.println("A runtime error occurred: " + e.getMessage());
    }
	}
}
