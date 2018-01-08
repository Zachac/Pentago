package util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleIOFileOut {
	private PrintStream out;
	private PrintStream fout;
	private Scanner in;
	
	public ConsoleIOFileOut(InputStream in, PrintStream out, String filename) throws FileNotFoundException {
		this.out = out;
		this.fout = new PrintStream(filename);
		this.in = new Scanner(in);
	}
	
	public String nextLine() {
		String result = in.nextLine();
		fout.println(result);
		return result;	
	}

	public void print(String value) {
		out.print(value);
		fout.print(value);
	}
	
	public void println(String value) {
		out.println(value);
		fout.println(value);
	}
	
	public void close() {
		in.close();
		out.close();
		fout.close();
	}
	
}
