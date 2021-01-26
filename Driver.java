import myCB.stock.*;

public class Driver 
	{
	public static void main(String[] args) 
		{
		StockCGS control = new StockCGS("AAPL", 600, 600, .80, .80);
		System.out.println(control.check());
		}
	}

