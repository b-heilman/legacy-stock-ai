package myCB.CGS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Seeder implements Serializable{
	private static final long serialVersionUID = 1;
	private static String dirN = "seedlings";
	private Chain Kernel_true;
	private Chain Kernel_false;
	private Genecode Seed_true;
	private Genecode Seed_false;
	
	public Seeder(){
		Kernel_true = null;
		Kernel_false = null;
		Seed_true = null;
		Seed_false = null;
		}
	
	public boolean hasKernel(boolean how){
		return ( (how)?Kernel_true:Kernel_false ) != null;
		}
	
	public boolean hasSeed(boolean how){
		return ( (how)?Seed_true:Seed_false ) != null;
		}
	
	public void setSeed(Genecode g, boolean how){
		if (how)
			Seed_true = g;
		else
			Seed_false = g;
		}
	
	public void setKernel(Chain c, boolean how){
		if (how)
			Kernel_true = c;
		else
			Kernel_false = c;
		}
	
	public Chain getKernel(boolean how){
		return (how)?Kernel_true:Kernel_false;
		}
	
	public Genecode getSeed(boolean how){
		return (how)?Seed_true:Seed_false;
		}
	
	public static Seeder loadSeed(String n){
		Seeder s;
		try{
			String fileName = dirN+File.separator+n+".sed";
			ObjectInputStream os = new ObjectInputStream(new FileInputStream(fileName));
			s = (Seeder)(os.readObject());
			os.close();
			}
		catch (Exception ex){
			System.out.println("No seeder found...");
			s = new Seeder();
			}
		
		return s;
		}
	
	public static boolean saveSeed(Seeder in, String n){
		String fileName = dirN+File.separator+n+".sed";
		try 
			{
			File dir = new File(dirN);
			if (!dir.exists())
				dir.mkdir();
			
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName));
			
			os.writeObject(in);
			os.close();
			}
		catch (Exception ex)
			{
			System.out.println("can't save seed: "+ex.getMessage());
			ex.printStackTrace();
			return false;
			}
		return true;
		}
	}
