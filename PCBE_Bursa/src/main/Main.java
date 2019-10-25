package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main
{

	public static void main(String[] args)
	{
		// Server
		Server server = new Server();
		Thread serverTh = new Thread(server);
		serverTh.start();
		
		// Delete everything from file
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter("Log.txt");
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		writer.println("##START##");
		
		try
		{
			Thread.sleep(100);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}	
		
		// Clients
		Client clientObserver = new Client("ClientObserver", 0, true);
		Thread clientObserverThread = new Thread(clientObserver);
		clientObserverThread.start();
		
		Client client1 = new Client("Client1", 1, false);
		Thread clientTh1 = new Thread(client1);
		clientTh1.start();
		Client client2 = new Client("Client2", 1, false);
		Thread clientTh2 = new Thread(client2);
		clientTh2.start();
		
		Client client3 = new Client("Client3", 2, false);
		Thread clientTh3 = new Thread(client3);
		clientTh3.start();
		Client client4 = new Client("Client4", 2, false);
		Thread clientTh4 = new Thread(client4);
		clientTh4.start();
	}
}
