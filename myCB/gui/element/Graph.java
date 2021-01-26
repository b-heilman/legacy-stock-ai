package myCB.gui.element;

import java.awt.*;
import myCB.data.*;

@SuppressWarnings("serial")
class Graph
	{
	int xMax, xMin, yMax, yMin;
	MyList p;
	Color c;
	
	public Graph(int[] list, Color x)
		{
		c = x;
		xMax = 0;
		xMin = 999999999;
		yMax = 0;
		yMin = 999999999;
		
		p = new MyList();
		addData(list);
		}
	
	private void addData(int[] list)
		{
		for(int i = list.length-1;i >= 0 ;i--)
			{
			setPoint(i,list[i]);
			}
		}
	
	public void setPoint(int x, int y)
		{
		boolean flag = true;
		for(p.start();!p.done() && flag;p.next())
			{
			flag = (((Point)p.get()).getX() < x);
			}
		
		if (x > xMax)
			xMax = x;
		if (x < xMin)
			xMin = x;
		if (y > yMax)
			yMax = y;
		if (y < yMin)
			yMin = y;
		
		p.prev();
		if(!p.done())
			p.push_back(new Point(x,y));
		else
			p.insert(new Point(x,y));
		}
	public MyList getLine()
		{
		return p;
		}
	public Color getColor()
		{
		return c;
		}
	public int xMax()
		{
		return xMax;
		}
	public int xMin()
		{
		return xMin;
		}
	public int yMax()
		{
		return yMax;
		}
	public int yMin()
		{
		return yMin;
		}
	}
