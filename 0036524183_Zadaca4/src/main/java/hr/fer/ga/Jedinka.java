package hr.fer.ga;

public class Jedinka {
	
	private double b0;
	private double b1;
	private double b2;
	private double b3;
	private double b4;
	
	private double dobrota;
	private double kazna;
	
	
	
	public double getKazna() {
		return kazna;
	}

	public void setKazna(double kazna) {
		this.kazna = kazna;
	}

	public Jedinka(double b0, double b1, double b2, double b3, double b4) {
		super();
		this.b0 = b0;
		this.b1 = b1;
		this.b2 = b2;
		this.b3 = b3;
		this.b4 = b4;
		
		this.kazna=0;
	}
	
	public Jedinka(double b0, double b1, double b2, double b3, double b4, double kazna) {
		super();
		this.b0 = b0;
		this.b1 = b1;
		this.b2 = b2;
		this.b3 = b3;
		this.b4 = b4;
		
		this.kazna=kazna;
	}

	public double getB0() {
		return b0;
	}


	public double getB1() {
		return b1;
	}



	public double getB2() {
		return b2;
	}



	public double getB3() {
		return b3;
	}



	public double getB4() {
		return b4;
	}



	public double getDobrota() {
		return dobrota;
	}

	public void setDobrota(double dobrota) {
		this.dobrota = dobrota;
	}
	
	@Override
	public String toString() {
		return "Jedinka sa kaznom "+ kazna+ "		i parametrima [b0=" + b0 + ", b1=" + b1 + ", b2=" + b2 + ", b3=" + b3 + ", b4=" + b4 + "]";
	}
	
	
	
}
