package hr.fer.ga;

public class IsprobajGA {

	public static void main(String[] args) {
		GA ga= new GA("zad4-dataset1.txt");
		System.out.println("**********************Kanonska, bez šuma**********************");
		ga.kanonskiGA(500, 0.2, true, 1000);
		
		System.out.println("\n**********************3-K, bez šuma**********************");
		
		ga.troturnirskiGA(1000, 0.2, 100);
		
		GA ga2= new GA("zad4-dataset2.txt");
		System.out.println("\n**********************Kanonska, sa šumom**********************");
		ga2.kanonskiGA(500, 0.2, true, 1000);
		
		System.out.println("\n**********************3-K, sa šumom**********************");
		
		ga2.troturnirskiGA(1000, 0.2, 100);
		
	}
}
