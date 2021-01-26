package myCB.math.lines;

import java.util.*;

public class Line2D {
	private LinkedList<Point2D> line;
	private int Mode;
	private double Slope;
	private double Intercept;
	private double Accuracy;
	
	public Line2D(){
		line = new LinkedList<Point2D>();
		Mode = 0;
		Slope = 0;
		Intercept = 0;
		Accuracy = 0;
		}
	
	public void add(double x, double y){
		line.add(new Point2D(x,y));
		}
	
	public void add(Point2D in){
		line.add(in);
		}
	
	@SuppressWarnings("unchecked")
	public void calcLinearReg(){
		int n = line.size();
		double xy = 0, x = 0, y = 0, x2 = 0, y2 = 0;
		Point2D p;
		
		Collections.sort(line);
		
		ListIterator i = line.listIterator();
		
		while(i.hasNext()){
			p = (Point2D) i.next();
			
			xy += p.XY();
			x += p.X();
			y += p.Y();
			x2 += p.sqX();
			y2 += p.sqY();
			}
		double temp = (n*xy - x*y);
		double temp2 = (n*x2 - Math.pow(x,2.0));
		double temp3 = (n*y2 - Math.pow(y,2.0));
		
		Slope = temp/temp2;
		Intercept = (y - Slope*x)/n;
		Accuracy = Math.abs(temp/(Math.pow(temp2,0.5) * Math.pow(temp3,0.5)));
		Mode = 1;
		}
	
	public void calcExponentialReg(){
		Line2D hold = new Line2D();
		ListIterator i = line.listIterator();
		Point2D p;
		
		while(i.hasNext()){
			p = (Point2D) i.next();
			hold.add(Math.abs(p.X()),Math.log10(p.Y()));
			}
		
		hold.calcLinearReg();
		Slope = hold.getSlope();
		Intercept = hold.getIntercept();
		Accuracy = hold.getAccuracy();
		Mode = 2;
		}
	
	public void calcBestReq(){
		calcLinearReg();
		double s = Slope, i = Intercept, a = Accuracy;

		calcExponentialReg();
		
		if (a > Accuracy) {
			Accuracy = a;
			Intercept = i;
			Slope = s;
			Mode = 1;
			}
		}
	
	public double getSlope(){
		return Slope;
		}
	
	public double getIntercept(){
		return Intercept;
		}
	
	public double getAccuracy(){
		return Accuracy;
		}
	
	public double calcValue(double place){
		switch (Mode)
			{
			case 1: return Slope*place+Intercept;
			case 2: return Math.pow(10,Intercept) * Math.pow(Math.pow(10,Slope),place);
			}
		return 0;
		}
	}
