package myCB.gui.element;

import java.awt.*;
import javax.swing.*;
import myCB.data.*;

@SuppressWarnings("serial")
public class Grapher extends JPanel 
	{
	int xMax, xMin, yMax, yMin, xSize, ySize;
	Graphics g;
	IndexList Lines;
	Page toDraw;
	
	public Grapher()
		{
		xMax = 0;
		xMin = 999999999;
		yMax = 0;
		yMin = 999999999;
		
		Lines = new IndexList();
		toDraw = new Page();
		}
	
	public void paintComponent(Graphics gr)
		{
		xSize = this.getWidth();
		ySize = this.getHeight();
		g = gr;
		
		draw();
		}
	
	public void add(int[] list, String name, Color c)
		{
		Lines.push_back(name, new Graph(list, c));
		
		if (((Graph)Lines.back()).xMax() > xMax)
			xMax = ((Graph)Lines.back()).xMax();
		
		if (((Graph)Lines.back()).xMin() < xMin)
			xMin = ((Graph)Lines.back()).xMin();
		
		if (((Graph)Lines.back()).yMax() > yMax)
			yMax = ((Graph)Lines.back()).yMax();
		
		if (((Graph)Lines.back()).yMin() < yMin)
			yMin = ((Graph)Lines.back()).yMin();
		}
	
	public void show(String in)
		{
		toDraw.push_back(in);
		}
	
	public void hide(String out)
		{
		toDraw.findDC(out);
		if (!toDraw.done())
			{
			toDraw.remove();
			}
		}
	
	public MyList getNames()
		{
		MyList i = new MyList();
		
		for (Lines.start(); !Lines.done(); Lines.next())
			i.push_back(Lines.getTag().toString());
		
		return i;
		}
	
	public void draw()
		{
		g.clearRect(0, 0, xSize, ySize);
		
		for (toDraw.start(); !toDraw.done(); toDraw.next())
			{
			try
				{
				Lines.firstTag((String)toDraw.get());
				drawOne((Graph)Lines.get());
				}
			catch (Exception ex)
				{
				System.out.println("Cann't draw that line");
				// just ignore it
				}
			}
		}
	
	private void drawOne(Graph it)
		{
		MyList p = it.getLine();
		g.setColor(it.getColor());
		
		Point first, second;
		try
			{
			adjustAll(p);
			p.start();
			second = (Point) p.get();
			for(p.next();!p.done();p.next())
				{
				first = second;
				second =(Point) p.get();
				g.drawLine(first.getX(),first.getY(),second.getX(),second.getY());
				}
			}
		catch (Exception ex)
			{
			System.out.println("oh dear");
			}
		}

	public void adjustAll(MyList p)
		{
		for(p.start();!p.done();p.next())
			((Point)p.get()).adjust(xMax,xMin,xSize,yMax,yMin,ySize);
		}
	}
