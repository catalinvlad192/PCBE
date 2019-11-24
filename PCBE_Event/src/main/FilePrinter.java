package main;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FilePrinter
{
	private static String fileName = "Log.txt";
	private static PrintWriter writer = null;
	
	public static synchronized void printLine(String s)
	{
		try
		{
			FileWriter fileWriter = new FileWriter(fileName, true);
			writer = new PrintWriter(fileWriter);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException x)
		{
			x.printStackTrace();
		}
		
		writer.println(s);
		writer.close();
		writer = null;
	}
}
