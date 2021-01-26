package myCB.ai;

public class OpsValues implements AIoptions {

	public OpsValues()
		{
		}
	
	public boolean eval(double left, int op, double right) 
		{
		return (left > (1+(op/400)) * right);
		}

	public int numOfOps() 
		{
		return 40;
		}
}
