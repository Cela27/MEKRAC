package hr.fer.zemiris;

public class Vector {
	
	private double X;
	private double Y;
	
	
	public Vector(double x, double y) {
		super();
		this.X = x;
		this.Y = y;
	}


	public double getX() {
		return X;
	}


	public void setX(double x) {
		X = x;
	}


	public double getY() {
		return Y;
	}


	public void setY(double y) {
		Y = y;
	}


	@Override
	public String toString() {
		return "Vector [X=" + X + ", Y=" + Y + "]";
	}
	
	
	
	
}
