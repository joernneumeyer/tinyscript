package de.joernneumeyer.tinyscript;

import java.io.BufferedReader;
import java.io.FileReader;

import org.reflections.Reflections;

import de.joernneumeyer.tinyscript.compiler.Compiler;
import de.joernneumeyer.tinyscript.exceptions.LexingError;
import de.joernneumeyer.tinyscript.exceptions.ParsingError;
import de.joernneumeyer.tinyscript.exceptions.RuntimeError;
import de.joernneumeyer.tinyscript.library.IO;
import de.joernneumeyer.tinyscript.library.Library;
import de.joernneumeyer.tinyscript.runtime.ExecutionContext;
import de.joernneumeyer.tinyscript.runtime.Runtime;

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
	  
	  ExecutionContext ec = new ExecutionContext();
	  Runtime r = new Runtime(ec);
	  
	  (new Reflections(""))
	  .getTypesAnnotatedWith(Library.class)
	  .stream()
	  .sorted((a, b) -> a.getName().compareTo(b.getName()))
	  .forEach(r::loadLibrary);
	  
	  
		Compiler c = new Compiler();
		
		try {
		  var instructions = c.compile(code);
		  r.run(instructions);
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
