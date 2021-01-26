package myCB.gui.element;

public class Point 
	{
	int x, y, printX, printY;
	public Point(int inX, int inY)
		{
		x = inX;
		y = inY;
		}
	public int getX()
		{
		return printX;
		}
	public int getY()
		{
		return printY;
		}
	public void adjust(	int xMax, int xMin, int xSize, 
						int yMax, int yMin, int ySize)
		{
		printX = (int)((x - xMin) * (xSize/(xMax - (float)xMin)));
		printY = (int)((yMax - y) * (ySize/(yMax - (float)yMin)));
		}
	}
