package myCB.math.lines;

public class Point2D implements Comparable{
	double daX;
	double daY;
	
	public Point2D(double x, double y){
		daX = x;
		daY = y;
		}
	
	public double slope(Point2D that){
		return deltaX(that)/deltaY(that);
		}
	
	public double deltaX(Point2D that){
		return (this.daX-that.daX);
		}
	
	public double deltaY(Point2D that){
		return (this.daY-that.daY);
		}
	
	public double XY(){
		return (daX * daY);
		}

	public double X(){
		return daX;
		}
	
	public double Y(){
		return daY;
		}
	
	public double sqX(){
		return Math.pow(daX,2.0);
		}
	
	public double sqY(){
		return Math.pow(daY,2.0);
		}
	
	public int compareTo(Object o) throws ClassCastException {
		Point2D that = (Point2D)o;
		return (int)((this.daX - that.daY)*10000);
		}
	}
