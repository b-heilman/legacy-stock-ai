package myCB.ai;

public interface AIlogical{
	// The logic table for the element... values to be processed
	public Double[][] getLogicTable();
	// The value of the logic table at that spot
	public Double getValue(int row, int element);
	// The weight of the table
	public Double getWeight();
	// The statistics for the logic table
	public AIstats stats();
	// The array for the operators used by the logic table
	public AIoptions[] getOps();
	
	}
