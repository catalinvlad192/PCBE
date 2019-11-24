package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import client.server.Client;
import client.server.Server;
import validators.BoughtValidator;
import validators.ComplexValidator;
import validators.InterestedInMyOfferValidator;
import validators.InterestingCompaniesValidator;
import validators.PriceBetweenThresholdsValidator;
import validators.SoldValidator;

public class Main
{

	public static void main(String[] args)
	{
		
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

		Server server = new Server();
		ArrayList<Client> list = new ArrayList<Client>();
		
		Client c;
		
		//
		c = new Client(server, "Client0", 0, false, false, true);
		ArrayList<String> strs = new ArrayList<String>();
		strs.add("Client6");
		ComplexValidator comp = new ComplexValidator(new InterestingCompaniesValidator(strs),
				new PriceBetweenThresholdsValidator(1, 3));
		c.setValidator(comp);
		server.subscribe(c);
		list.add(c);
		
		//
		c = new Client(server, "Client1", 2, true, false, false);
		comp = new ComplexValidator(new InterestedInMyOfferValidator("Client1"),
				new ComplexValidator(new SoldValidator(), new BoughtValidator()));
		c.setValidator(comp);
		server.subscribe(c);
		list.add(c);
		
		//
		c = new Client(server, "Client2", 2, false, true, false);
		comp = new ComplexValidator(new SoldValidator(), new BoughtValidator());
		c.setValidator(comp);
		server.subscribe(c);
		list.add(c);
		
		//
		c = new Client(server, "Client3", 4, true, false, false);
		comp = new ComplexValidator(new InterestedInMyOfferValidator("Client1"),
				new ComplexValidator(new SoldValidator(), new BoughtValidator()));
		c.setValidator(comp);
		server.subscribe(c);
		list.add(c);
		
		//
		c = new Client(server, "Client4", 4, false, true, false);
		comp = new ComplexValidator(new SoldValidator(), new BoughtValidator());
		c.setValidator(comp);
		server.subscribe(c);
		list.add(c);
		
		//
		c = new Client(server, "Client5", 6, true, false, false);
		comp = new ComplexValidator(new SoldValidator(), new BoughtValidator());
		c.setValidator(comp);
		server.subscribe(c);
		list.add(c);
		
		//
		c = new Client(server, "Client6", 6, false, true, false);
		comp = new ComplexValidator(new SoldValidator(), new BoughtValidator());
		c.setValidator(comp);
		server.subscribe(c);
		list.add(c);
		
		while(true)
		{
			for(Client cl : list)
			{
				cl.run();
			}
		}
		
	}
}
