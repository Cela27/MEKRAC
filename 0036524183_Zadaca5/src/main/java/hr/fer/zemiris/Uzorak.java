package hr.fer.zemiris;

import java.util.Arrays;

public class Uzorak {
	
	private Double[] X;
	private Double[] Y;
	
	
	
	public Uzorak(Double[] doubles, Double[] doubles2) {
		super();
		X = doubles;
		Y = doubles2;
	}
	public Double[] getX() {
		return X;
	}
	public void setX(Double[] x) {
		X = x;
	}
	public Double[] getY() {
		return Y;
	}
	public void setY(Double[] y) {
		Y = y;
	}
	@Override
	public String toString() {
		return "Uzorak [X=" + Arrays.toString(X) + ", Y=" + Arrays.toString(Y) + "]";
	}

	
}
