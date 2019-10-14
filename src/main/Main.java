package main;

public class Main {

	public static void main(String[] args)
	{
		// Server
		Server server = new Server();
		Thread serverTh = new Thread(server);
		serverTh.start();
				
		// Clients
		for(int i=0; i<10; i++)
		{
			Client client = new Client("Client"+i, 10);
			Thread clientTh = new Thread(client);
			clientTh.start();
		}
	}
}
