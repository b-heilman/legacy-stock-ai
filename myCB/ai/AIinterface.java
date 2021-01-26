package myCB.ai;

public interface AIinterface {
	public void setLogic(AIlogicalSerial d, AIoptions o);
	public void setWork(int in);
	public void setGoal(float precent);
	public void setRange(int in);
	public void evalTo(boolean in);
	public double[] getValue(int preview, double percent);
}
