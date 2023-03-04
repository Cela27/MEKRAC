package hr.fer.ga;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmConstraints;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class with genetic algorithm done one canonic and 3-k tourny way.
 * @author Antonio
 *
 */
public class GA {

	private Random random = new Random();

	private List<double[]> mjerenja = new ArrayList<>();

	public GA(String file) {
		loadData(file);
	}
	
	/**
	 * Loading data for calculating penalty function.
	 * @param file
	 */
	private void loadData(String file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
			String line = reader.readLine();
			while (line != null) {
				String[] splits = line.split("	");
				double[] mjerenje = new double[3];

				mjerenje[0] = Double.parseDouble(splits[0]);
				mjerenje[1] = Double.parseDouble(splits[1]);
				mjerenje[2] = Double.parseDouble(splits[2]);

				mjerenja.add(mjerenje);
				line = reader.readLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 3-K tourny implementation of genetic algorithm.
	 * @param brGeneracija
	 * @param mutationChance
	 * @param populationSize
	 * @return
	 */
	public double[] troturnirskiGA(int brGeneracija, double mutationChance, int populationSize) {
		List<Jedinka> populacija = new ArrayList<>();

		for (int i = 0; i < populationSize; i++) {
			double b0 = getRand(-4, 4);
			double b1 = getRand(-4, 4);
			double b2 = getRand(-4, 4);
			double b3 = getRand(-4, 4);
			double b4 = getRand(-4, 4);

			double kazna = getPenalty(b0, b1, b2, b3, b4);

			populacija.add(new Jedinka(b0, b1, b2, b3, b4, kazna));
		}

		System.out.println("Nakon nasumicnog generiranja najbolja je:  " + pronadiNajbolju(populacija));
		Jedinka najbolji = pronadiNajbolju(populacija);

		for (int i = 0; i < brGeneracija; i++) {

			int i1 = (int) Math.round(getRand(0, populationSize - 1));
			int i2 = (int) Math.round(getRand(0, populationSize - 1));
			while (i1 == i2) {
				i2 = (int) Math.round(getRand(0, populationSize - 1));
			}
			int i3 = (int) Math.round(getRand(0, populationSize - 1));
			while (i1 == i3 || i2 == i3) {
				i3 = (int) Math.round(getRand(0, populationSize - 1));
			}

			Jedinka prvi = populacija.get(i1);
			Jedinka drugi = populacija.get(i2);
			Jedinka treci = populacija.get(i3);

			Jedinka parent1;
			Jedinka parent2;

			if (prvi.getKazna() < drugi.getKazna()) {
				parent1 = prvi;
				if (drugi.getKazna() < treci.getKazna()) {
					parent2 = drugi;

					populacija.remove(treci);
				} else {
					parent2 = treci;
					populacija.remove(drugi);

				}
			} else {
				parent1 = drugi;
				if (prvi.getKazna() < treci.getKazna()) {
					parent2 = prvi;
					populacija.remove(treci);
				} else {
					parent2 = treci;
					populacija.remove(prvi);
				}
			}

			boolean mutate = false;

			if (getRand(0, 1) <= mutationChance)
				mutate = true;

			Jedinka dijete = reproduciraj(parent1, parent2, mutate);
			populacija.add(dijete);

			Jedinka sadNajboji = pronadiNajbolju(populacija);

			if (sadNajboji.getKazna() < najbolji.getKazna()) {
				System.out.println(
						"Novi najbolji je naden u " + (i + 1) + "-toj generaciji:  " + pronadiNajbolju(populacija));
				najbolji = sadNajboji;
			}
		}

		Jedinka najbolja = pronadiNajbolju(populacija);

		double[] rjesenje = new double[5];
		rjesenje[0] = najbolja.getB0();
		rjesenje[1] = najbolja.getB1();
		rjesenje[2] = najbolja.getB2();
		rjesenje[3] = najbolja.getB3();
		rjesenje[4] = najbolja.getB4();

		return rjesenje;
	}

	/**
	 * 3-K tourny implementation of genetic algorithm.
	 * @param brGeneracija
	 * @param mutationChance
	 * @param populationSize
	 * @return
	 */
	public double[] kanonskiGA(int brGeneracija, double mutationChance, boolean elitizam, int populationSize) {

		List<Jedinka> populacija = new ArrayList<>();

		for (int i = 0; i < populationSize; i++) {
			double b0 = getRand(-4, 4);
			double b1 = getRand(-4, 4);
			double b2 = getRand(-4, 4);
			double b3 = getRand(-4, 4);
			double b4 = getRand(-4, 4);

			double kazna = getPenalty(b0, b1, b2, b3, b4);

			populacija.add(new Jedinka(b0, b1, b2, b3, b4, kazna));
		}

		double najvecaKazna = pronadiNajgoru(populacija).getKazna();

		for (Jedinka jedinka : populacija) {
			jedinka.setDobrota(najvecaKazna - jedinka.getKazna());
		}

		System.out.println("Nakon nasumicnog generiranja najbolja je:" + pronadiNajbolju(populacija));
		Jedinka najboljaJedinka = pronadiNajbolju(populacija);
		for (int i = 0; i < brGeneracija; i++) {

			List<Jedinka> sljdPopulacija = new ArrayList<>();

			if (elitizam) {
				// dodaj najbolju u novu, najboja je najmanja dobrota
				Jedinka najbolja = pronadiNajbolju(populacija);
				sljdPopulacija.add(najbolja);
			}

			for (int j = 0; j < populationSize - 1; j++) {
				Jedinka parent1 = odaberiRoditeljaProbabilisticki(populacija);
				Jedinka parent2 = odaberiRoditeljaProbabilisticki(populacija);

				boolean mutate = false;

				if (getRand(0, 1) <= mutationChance)
					mutate = true;

				Jedinka dijete = reproduciraj(parent1, parent2, mutate);

				sljdPopulacija.add(dijete);
			}
			populacija = sljdPopulacija;
			najvecaKazna = pronadiNajgoru(populacija).getKazna();

			for (Jedinka jedinka : populacija) {
				jedinka.setDobrota(najvecaKazna - jedinka.getKazna());
			}

			Jedinka sadNajboji = pronadiNajbolju(populacija);

			if (sadNajboji.getKazna() < najboljaJedinka.getKazna()) {
				System.out.println(
						"Novi najbolji je naden u " + (i + 1) + "-toj generaciji:  " + pronadiNajbolju(populacija));
				najboljaJedinka = sadNajboji;
			}
		}

		Jedinka najbolja = pronadiNajbolju(populacija);

		double[] rjesenje = new double[5];
		rjesenje[0] = najbolja.getB0();
		rjesenje[1] = najbolja.getB1();
		rjesenje[2] = najbolja.getB2();
		rjesenje[3] = najbolja.getB3();
		rjesenje[4] = najbolja.getB4();

		return rjesenje;
	}
	
	/**
	 * Finds best individual.
	 * @param populacija
	 * @return
	 */
	private Jedinka pronadiNajbolju(List<Jedinka> populacija) {
		double minKazna = Double.MAX_VALUE;
		Jedinka minJedinka = null;
		for (Jedinka jedinka : populacija) {
			if (jedinka.getKazna() < minKazna) {
				minJedinka = jedinka;
				minKazna = jedinka.getKazna();
			}
		}
		return minJedinka;
	}
	
	/**
	 * Reproduction function
	 * @param parent1
	 * @param parent2
	 * @param mutate
	 * @return
	 */
	private Jedinka reproduciraj(Jedinka parent1, Jedinka parent2, boolean mutate) {
		double b0, b1, b2, b3, b4;
		// b0
		if (getRand(0, 1) > 0.5) {
			b0 = parent2.getB0();
		} else {
			b0 = parent1.getB0();
		}
		// b1
		if (getRand(0, 1) > 0.5) {
			b1 = parent2.getB1();
		} else {
			b1 = parent1.getB1();
		}
		// b2
		if (getRand(0, 1) > 0.5) {
			b2 = parent2.getB2();
		} else {
			b2 = parent1.getB2();
		}
		// b3
		if (getRand(0, 1) > 0.5) {
			b3 = parent2.getB3();
		} else {
			b3 = parent1.getB3();
		}

		// b4
		if (getRand(0, 1) > 0.5) {
			b4 = parent2.getB4();
		} else {
			b4 = parent1.getB4();
		}

		if (mutate) {
			double r = getRand(0, 5);

			if (r >= 0 && r < 1) {
				if (getRand(0, 1) > 0.5)
					b0 = b0 + random.nextGaussian();

				if (b0 > 4)
					b0 = 4;
				if (b0 < -4)
					b0 = -4;
			} else if (r >= 1 && r < 2) {
				if (getRand(0, 1) > 0.5)
					b1 = b1 + random.nextGaussian();

				if (b1 > 4)
					b1 = 4;
				if (b1 < -4)
					b1 = -4;
			} else if (r >= 2 && r < 3) {
				if (getRand(0, 1) > 0.5)
					b2 = b2 + random.nextGaussian();

				if (b2 > 4)
					b2 = 4;
				if (b2 < -4)
					b2 = -4;
			} else if (r >= 3 && r < 4) {
				if (getRand(0, 1) > 0.5)
					b3 = b3 + random.nextGaussian();

				if (b3 > 4)
					b3 = 4;
				if (b3 < -4)
					b3 = -4;
			} else if (r >= 4 && r <= 5) {
				if (getRand(0, 1) > 0.5)
					b4 = b4 + random.nextGaussian();

				if (b4 > 4)
					b4 = 4;
				if (b4 < -4)
					b4 = -4;
			}
		}
		double kazna = getPenalty(b0, b1, b2, b3, b4);

		return new Jedinka(b0, b1, b2, b3, b4, kazna);
	}
	/**
	 * Chooses parents for canonic GA.
	 * @param populacija
	 * @return
	 */
	private Jedinka odaberiRoditeljaProbabilisticki(List<Jedinka> populacija) {
		double sumaDobrota = 0;

		for (Jedinka jedinka : populacija) {
			sumaDobrota += jedinka.getDobrota();
		}

		double limit = getRand(0, sumaDobrota);
		int chosen = 0;
		double upperLimit = populacija.get(chosen).getDobrota();
		
		while (limit > upperLimit && chosen < populacija.size() - 1) {
			chosen++;
			upperLimit += populacija.get(chosen).getDobrota();
		}

		return populacija.get(chosen);
	}
	
	/**
	 * Finds worst induvidual
	 * @param populacija
	 * @return
	 */
	private Jedinka pronadiNajgoru(List<Jedinka> populacija) {
		double maxKazna = Double.MIN_VALUE;
		Jedinka maxJedinka = null;
		for (Jedinka jedinka : populacija) {
			if (jedinka.getKazna() > maxKazna) {
				maxJedinka = jedinka;
				maxKazna = jedinka.getKazna();
			}
		}
		return maxJedinka;
	}
	/**
	 * Calculates penalty for induvidual.
	 * @param b0
	 * @param b1
	 * @param b2
	 * @param b3
	 * @param b4
	 * @return
	 */
	private double getPenalty(double b0, double b1, double b2, double b3, double b4) {

		double ukDobrota = 0;

		for (double[] mjerenje : mjerenja) {
			double x = mjerenje[0];
			double y = mjerenje[1];
			double cilj = mjerenje[2];

			double rez = izracunajFunkciju(x, y, b0, b1, b2, b3, b4);

			ukDobrota += Math.pow(rez - cilj, 2);
		}
		// vrati 1/kazan za dobrotu
		return (ukDobrota / mjerenja.size());

	}
	/**
	 * Calculates funcion used for geting data.
	 * @param x
	 * @param y
	 * @param b0
	 * @param b1
	 * @param b2
	 * @param b3
	 * @param b4
	 * @return
	 */
	private double izracunajFunkciju(double x, double y, double b0, double b1, double b2, double b3, double b4) {

		return Math.sin(b0 + b1 * x)
				+ b2 * Math.cos(x * (b3 + y)) * (1 / (1 + (Math.pow(Math.E, Math.pow(x - b4, 2)))));

	}
	/**
	 * Returns random num in interval.
	 * @param min
	 * @param max
	 * @return
	 */
	private double getRand(double min, double max) {

		double rng = min + random.nextDouble() * (max - min);
		return rng;
	}
}
