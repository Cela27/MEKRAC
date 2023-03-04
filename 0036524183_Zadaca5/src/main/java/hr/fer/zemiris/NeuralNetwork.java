package hr.fer.zemiris;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Cutom nural network.
 * 
 * @author Antonio
 *
 */
public class NeuralNetwork {

	private String data;
	private int M;
	private int[] slojeviMreze;
	private int algUcenja;
	private int BATCH_SIZE = 5;
	private int NUM_EPOCHS;
	private double LEARNING_RATE;
	private double ERROR_THRESHOLD = 0.0002;
	private int brUzorakaSvakeKlase;

	private List<Uzorak> uzorci;

	private Random rng = new Random();

	List<Matrix> weights = new ArrayList<>();

	/**
	 * Creates and trains new neural network.
	 * 
	 * @param data
	 * @param brUzorakaSvakeKlase
	 * @param algUcenja
	 * @param num_epochs
	 * @param learning_rate
	 * @param M
	 * @param slojeviMreze
	 * @throws Exception
	 */
	public NeuralNetwork(String data, int brUzorakaSvakeKlase, int algUcenja, int num_epochs, double learning_rate,
			int M, int... slojeviMreze) throws Exception {
		super();
		this.data = data;
		this.brUzorakaSvakeKlase = brUzorakaSvakeKlase;
		this.M = M;
		this.slojeviMreze = slojeviMreze;
		this.algUcenja = algUcenja;
		this.NUM_EPOCHS = num_epochs;
		this.LEARNING_RATE = learning_rate;

		if (slojeviMreze[0] != 2 * M || slojeviMreze[slojeviMreze.length - 1] != 5) {
			throw new Exception("Ulazni sloj mora bit 2*M a izlazni 5");
		}

		uzorci = loadData(data);
		inicijalizirajTezine();

		if (algUcenja == 1)
			trainEpochsNB();
		else if (algUcenja == 2)
			trainEpochsSB();
		else if (algUcenja == 3)
			trainEpochsBB();
		else
			throw new Exception("Krivi alg ucenja, odaberi 1, 2 ili 3");

	}

	/**
	 * Train {@link NeuralNetwork} with Normal backpropagation.
	 * 
	 * @throws Exception
	 */
	private void trainEpochsNB() throws Exception {

		for (int i = 1; i <= NUM_EPOCHS; i++) {
			double error = trainNormalBackpropagation(uzorci);

			if (error < ERROR_THRESHOLD) {
				System.out.println("Konvergira u epohi br: " + i + " s errorom " + error);
				break;
			}

			if (i % 1000 == 0) {
				System.out.println("U epohi " + i + " pogrska je " + error);
			}

		}

	}

	/**
	 * Train {@link NeuralNetwork} with Stohastic backpropagation.
	 * @throws Exception
	 */
	private void trainEpochsSB() throws Exception {
		for (int i = 1; i <= NUM_EPOCHS; i++) {
			double error = 0;
			for (Uzorak uzorak : uzorci) {
				error += trainStohasticBackpropagation(uzorak.getX(), uzorak.getY());
			}
			error = error / uzorci.size();

			if (error < ERROR_THRESHOLD) {
				System.out.println("Konvergira u epohi br: " + i + " s errorom " + error);
				break;
			}

			if (i % 1000 == 0) {
				System.out.println("U epohi " + i + " pogrska je " + error);
			}

		}

	}
	
	/**
	 * Train {@link NeuralNetwork} with Mini-batch(size 2) backpropagation.
	 * @throws Exception
	 */
	private void trainEpochsBB() throws Exception {

		List<List<Uzorak>> batches = new ArrayList<>();

		for (int i = 0; i < brUzorakaSvakeKlase; i = i + 2) {
			List<Uzorak> batch = new ArrayList<>();
			batch.add(uzorci.get(i));
			batch.add(uzorci.get(i + 1));

			batch.add(uzorci.get(i + brUzorakaSvakeKlase));
			batch.add(uzorci.get(i + brUzorakaSvakeKlase + 1));

			batch.add(uzorci.get(i + brUzorakaSvakeKlase * 2));
			batch.add(uzorci.get(i + brUzorakaSvakeKlase * 2 + 1));

			batch.add(uzorci.get(i + brUzorakaSvakeKlase * 3));
			batch.add(uzorci.get(i + brUzorakaSvakeKlase * 3 + 1));

			batch.add(uzorci.get(i + brUzorakaSvakeKlase * 4));
			batch.add(uzorci.get(i + brUzorakaSvakeKlase * 4 + 1));
			batches.add(batch);
		}

		for (int i = 1; i <= NUM_EPOCHS; i++) {
			double error = trainBatchBackpropagation(batches);

			if (error < ERROR_THRESHOLD) {
				System.out.println("Konvergira u epohi br: " + i + " s errorom " + error);
				break;
			}

			if (i % 1000 == 0) {
				System.out.println("U epohi " + i + " pogrska je " + error);
			}

		}

	}

	private double trainBatchBackpropagation(List<List<Uzorak>> batches) throws Exception {
		Double errorMain = 0.;
		for (List<Uzorak> batch : batches) {
			List<List<Matrix>> listOfHiddenOutputs = new ArrayList<>();
			List<Matrix> listOfErrorMatrixes = new ArrayList<>();
			Double error = 0.;

			// Matrix errorMatrix = null;
			Matrix output = null;

			for (Uzorak uzorak : batch) {

				List<Matrix> hiddenOutputs = new ArrayList<>();
				output = Matrix.fromArray(uzorak.getX());
				hiddenOutputs.add(output);

				for (int i = 0; i < weights.size(); i++) {
					output = Matrix.multiply(Matrix.transpose(weights.get(i)), output);
					output.sigmoid();
					hiddenOutputs.add(output);
				}

				// hiddenOutputs.remove(hiddenOutputs.size() - 1);

				listOfHiddenOutputs.add(hiddenOutputs);

				Matrix target = Matrix.fromArray(uzorak.getY());
				error += Matrix.error(target, output);

				Matrix errorMatrix = Matrix.subtract(target, output);
				listOfErrorMatrixes.add(errorMatrix);

			}

			// errorMatrix.divide((double) uzorci.size());
			error = error / batch.size();
			errorMain += error;
			for (int j = 0; j < listOfHiddenOutputs.size(); j++) {
				List<Matrix> hiddenOutputs = listOfHiddenOutputs.get(j);
				Matrix errorMatrix = listOfErrorMatrixes.get(j);

				// OutputDelta
				Matrix outputDelta = hiddenOutputs.get(hiddenOutputs.size() - 1).derSigmoid();
				hiddenOutputs.remove(hiddenOutputs.size() - 1);
				outputDelta.multiply(errorMatrix);

				Matrix predhodniDelta = outputDelta;

				// azururanje tezina
				outputDelta.multiply(LEARNING_RATE);
				Matrix deltaWeights = Matrix.multiply(hiddenOutputs.get(hiddenOutputs.size() - 1),
						Matrix.transpose(outputDelta));

				Matrix newOutputWeights = weights.get(weights.size() - 1);
				newOutputWeights.add(deltaWeights);
				List<Matrix> newWeights = new ArrayList<>();
				newWeights.add(newOutputWeights);

				for (int i = slojeviMreze.length - 2; i > 0; i--) {
					// DeltaSloja
					Matrix layerDelta = hiddenOutputs.get(i).derSigmoid();
					Matrix predhodneTezine = weights.get(i);
					predhodneTezine = Matrix.mjenjajTezineDeltom(predhodneTezine, predhodniDelta);
					layerDelta = Matrix.mnoziSumom(predhodneTezine, layerDelta);

					// racunaje delte gotocvo
					predhodniDelta = layerDelta;

					// azururanje tezina
					layerDelta.multiply(LEARNING_RATE);
					Matrix layerDeltaWeights = Matrix.multiply(hiddenOutputs.get(i - 1), Matrix.transpose(layerDelta));
					Matrix newLayerWeights = weights.get(i - 1);
					newLayerWeights.add(layerDeltaWeights);

					newWeights.add(0, newLayerWeights);
					predhodniDelta = layerDelta;

				}
				weights = newWeights;
			}
		}
		return errorMain / batches.size();
	}

	private double trainNormalBackpropagation(List<Uzorak> uzorci) throws Exception {
		List<List<Matrix>> listOfHiddenOutputs = new ArrayList<>();

		List<Matrix> listOfErrorMatrixes = new ArrayList<>();

		Double error = 0.;
		// Matrix errorMatrix = null;
		Matrix output = null;

		for (Uzorak uzorak : uzorci) {

			List<Matrix> hiddenOutputs = new ArrayList<>();
			output = Matrix.fromArray(uzorak.getX());
			hiddenOutputs.add(output);

			for (int i = 0; i < weights.size(); i++) {
				output = Matrix.multiply(Matrix.transpose(weights.get(i)), output);
				output.sigmoid();
				hiddenOutputs.add(output);
			}

			// hiddenOutputs.remove(hiddenOutputs.size() - 1);

			listOfHiddenOutputs.add(hiddenOutputs);

			Matrix target = Matrix.fromArray(uzorak.getY());
			error += Matrix.error(target, output);

			Matrix errorMatrix = Matrix.subtract(target, output);
			listOfErrorMatrixes.add(errorMatrix);

		}

		// errorMatrix.divide((double) uzorci.size());
		error = error / uzorci.size();

		for (int j = 0; j < listOfHiddenOutputs.size(); j++) {
			List<Matrix> hiddenOutputs = listOfHiddenOutputs.get(j);
			Matrix errorMatrix = listOfErrorMatrixes.get(j);

			// OutputDelta
			Matrix outputDelta = hiddenOutputs.get(hiddenOutputs.size() - 1).derSigmoid();
			hiddenOutputs.remove(hiddenOutputs.size() - 1);
			outputDelta.multiply(errorMatrix);

			Matrix predhodniDelta = outputDelta;

			// azururanje tezina
			outputDelta.multiply(LEARNING_RATE);
			Matrix deltaWeights = Matrix.multiply(hiddenOutputs.get(hiddenOutputs.size() - 1),
					Matrix.transpose(outputDelta));

			Matrix newOutputWeights = weights.get(weights.size() - 1);
			newOutputWeights.add(deltaWeights);
			List<Matrix> newWeights = new ArrayList<>();
			newWeights.add(newOutputWeights);

			for (int i = slojeviMreze.length - 2; i > 0; i--) {
				// DeltaSloja
				Matrix layerDelta = hiddenOutputs.get(i).derSigmoid();
				Matrix predhodneTezine = weights.get(i);
				predhodneTezine = Matrix.mjenjajTezineDeltom(predhodneTezine, predhodniDelta);
				layerDelta = Matrix.mnoziSumom(predhodneTezine, layerDelta);

				// racunaje delte gotocvo
				predhodniDelta = layerDelta;

				// azururanje tezina
				layerDelta.multiply(LEARNING_RATE);
				Matrix layerDeltaWeights = Matrix.multiply(hiddenOutputs.get(i - 1), Matrix.transpose(layerDelta));
				Matrix newLayerWeights = weights.get(i - 1);
				newLayerWeights.add(layerDeltaWeights);

				newWeights.add(0, newLayerWeights);
				predhodniDelta = layerDelta;

			}
			weights = newWeights;
		}
		return error;
	}

	private double trainStohasticBackpropagation(Double[] X, Double[] Y) throws Exception {
		Matrix output = Matrix.fromArray(X);
		List<Matrix> hiddenOutputs = new ArrayList<>();
		hiddenOutputs.add(output);

		for (int i = 0; i < weights.size(); i++) {
			output = Matrix.multiply(Matrix.transpose(weights.get(i)), output);
			output.sigmoid();
			hiddenOutputs.add(output);
		}

		hiddenOutputs.remove(hiddenOutputs.size() - 1);

		Matrix target = Matrix.fromArray(Y);
		Double error = Matrix.error(target, output);
		Matrix errorMatrix = Matrix.subtract(target, output);

		// OutputDelta
		Matrix outputDelta = output.derSigmoid();
		outputDelta.multiply(errorMatrix);
		Matrix predhodniDelta = outputDelta;

		// azururanje tezina
		outputDelta.multiply(LEARNING_RATE);
		Matrix deltaWeights = Matrix.multiply(hiddenOutputs.get(hiddenOutputs.size() - 1),
				Matrix.transpose(outputDelta));
		Matrix newOutputWeights = weights.get(weights.size() - 1);
		newOutputWeights.add(deltaWeights);

		List<Matrix> newWeights = new ArrayList<>();
		newWeights.add(newOutputWeights);

		// do ovde ok
		// moras ko i ovo gore napravit unurta al ofc suma treba ostat
		for (int i = slojeviMreze.length - 2; i > 0; i--) {
			// DeltaSloja
			Matrix layerDelta = hiddenOutputs.get(i).derSigmoid();
			Matrix predhodneTezine = weights.get(i);
			predhodneTezine = Matrix.mjenjajTezineDeltom(predhodneTezine, predhodniDelta);
			layerDelta = Matrix.mnoziSumom(predhodneTezine, layerDelta);

			// racunaje delte gotocvo
			predhodniDelta = layerDelta;

			// azururanje tezina
			layerDelta.multiply(LEARNING_RATE);
			// ovo bi sve krivo trebalo bit
			Matrix layerDeltaWeights = Matrix.multiply(hiddenOutputs.get(i - 1), Matrix.transpose(layerDelta));
			Matrix newLayerWeights = weights.get(i - 1);
			newLayerWeights.add(layerDeltaWeights);

			newWeights.add(0, newLayerWeights);
			predhodniDelta = layerDelta;

		}
		weights = newWeights;
		return error;
	}

	/**
	 * Predict method for example given to {@link NeuralNetwork}.
	 * @param X
	 * @return
	 * @throws Exception
	 */
	public Double[] predict(Double[] X) throws Exception {
		Matrix x = Matrix.fromArray(X);

		for (int i = 0; i < weights.size(); i++) {
			x = Matrix.multiply(Matrix.transpose(weights.get(i)), x);
			x.sigmoid();
		}

		return x.toArray().toArray(new Double[0]);
	}
	
	private void inicijalizirajTezine() {
		for (int i = 1; i < slojeviMreze.length; i++) {
			Matrix layerWeights = new Matrix(slojeviMreze[i - 1], slojeviMreze[i]);
			weights.add(layerWeights);

		}
	}

	private List<Uzorak> loadData(String file) {
		List<Uzorak> uzorci = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
			String line = reader.readLine();

			while (line != null) {

				String[] splits = line.split("#");

				String[] X_splits = splits[0].split(",");
				List<Double> X = new ArrayList<>();

				for (String x : X_splits) {
					X.add(Double.parseDouble(x));
				}

				String[] Y_splits = splits[1].split(",");
				List<Double> Y = new ArrayList<>();

				for (String y : Y_splits) {
					Y.add(Double.parseDouble(y));
				}
				uzorci.add(new Uzorak(X.toArray(new Double[0]), Y.toArray(new Double[0])));

				line = reader.readLine();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return uzorci;
	}
}