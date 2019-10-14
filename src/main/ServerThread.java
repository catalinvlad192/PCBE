package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread
{
	private Socket client_;
	private Server server_;
	private PrintWriter output_;
	private BufferedReader input_;
	
	public ServerThread(Server server, Socket client)
	{
		server_ = server;
		client_ = client;
	}
	
	public void run()
	{
		System.out.println("Server communicates with client");
		try
		{
			output_ = new PrintWriter(client_.getOutputStream(), true);
			input_ = new BufferedReader(new InputStreamReader(client_.getInputStream()));
			// receive request or offer and send back message.
			
		}catch(IOException e)
		{
			e.printStackTrace();
		}finally
		{
			try
			{
				client_.close();
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}

