package myCB.ai;

public interface AIlogicalSerial extends AIlogical {
	public AIlogicalSerial clone();
	// There is an element towards the present marker
	public boolean hasFuture();
	// There is an element towards the past marker
	public boolean hasPast();
	// Go towards the present marker
	public Double[][] toPresent() throws IndexOutOfBoundsException;
	// Go towards the past marker
	public Double[][] toPast() throws IndexOutOfBoundsException;
	// Jump to the present marker
	public void present();
	// Jump to the past marker
	public void past();
	// Jump to the marker n in the past
	public void past(int back);
	// Return the number of elements available
	public int length();
	//The change of the element
	public Double getChange(int in);
	//The change of the element
	public Double getChange();
	// The value (weight) of the element in ahead
	public Double peekAhead(int in);
	// The value (weight) of the element in ago
	public Double lookBack(int in);
	// A smaller version of the logic table, excluding present days
	public AIlogicalSerial subSection(int i);
	// The value for the most current object
	public Double getHeadWeight();
	public Double getWeight(int i);
}
