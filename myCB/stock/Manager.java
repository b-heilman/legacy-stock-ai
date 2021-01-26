package myCB.stock;

import java.awt.BorderLayout;
import java.io.*;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

import myCB.gui.*;

public class Manager 
	{
	Portfolio p;
	JFrame frame;
	public Manager()
		{
		frame = new JFrame();
		portfolioGUI g;
		if (!loadPortfolio("temp"))
			p = new Portfolio();
		
		g = p.getGUI();
		g.draw();
		
		frame = new JFrame();
		JLabel spacer = new JLabel("One");
		JLabel spacer2 = new JLabel("Two");
		frame.getContentPane().add(BorderLayout.NORTH, spacer);
		frame.getContentPane().add(BorderLayout.NORTH, spacer2);
		
		frame.setSize(600,700);
		frame.setVisible(true);
		
		System.out.println("Closing Manager");
		savePortfolio("temp");
		}
	
 	public boolean loadPortfolio(String tag)
		{
 		try
 			{
 			System.out.println("(stockDataGUI) loading serialized --> "+tag);
		
 			ObjectInputStream os = new ObjectInputStream(new FileInputStream(tag+".sbro"));
			
 			p = (Portfolio)(os.readObject());
		
 			os.close();
 			}
 		catch (Exception ex)
 			{
 			System.out.println("can't find file: "+tag);
 			return false;
 			}
 		return true;
		}
	public void savePortfolio(String tag)
		{
		try 
			{
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(tag+".sbro"));
			
			os.writeObject(p);
			os.close();
			}
		catch (FileNotFoundException ex)
			{
			System.out.println("File couldn't be opened");
			}
		catch (IOException ex)
			{
			System.out.println("IO error");
			}
		}
	}
