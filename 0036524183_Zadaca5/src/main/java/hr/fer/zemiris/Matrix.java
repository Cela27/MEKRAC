package hr.fer.zemiris;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements {@link Matrix} and different matrix functions.
 * @author Antonio
 *
 */
public class Matrix {
	Double[][] data;
	int rows, cols;
	
	/**
	 * Create Matrix with given number of rows and columns. Random values assigned.
	 * @param rows
	 * @param cols
	 */
	public Matrix(int rows, int cols) {
		data = new Double[rows][cols];
		this.rows = rows;
		this.cols = cols;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				data[i][j] = Math.random() * 2 - 1;
			}
		}
	}
	
	/**
	 * Adds given scalar to matrig members.
	 * @param scaler
	 */
	public void add(Double scaler) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				this.data[i][j] += scaler;
			}

		}
	}
	
	/**
	 * Adds matrixes together.
	 * @param m
	 * @throws Exception
	 */
	public void add(Matrix m) throws Exception {
		if (cols != m.cols || rows != m.rows) {
			throw new Exception("Shape mismatch");
		
		}

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				this.data[i][j] += m.data[i][j];
			}
		}
	}
	
	/**
	 * Subtracts matrixes.
	 * @param a
	 * @param b
	 * @return
	 */
	public static Matrix subtract(Matrix a, Matrix b) {
		Matrix temp = new Matrix(a.rows, a.cols);
		for (int i = 0; i < a.rows; i++) {
			for (int j = 0; j < a.cols; j++) {
				temp.data[i][j] = a.data[i][j] - b.data[i][j];
			}
		}
		return temp;
	}
	
	/**
	 * Computes error in between matrixes.
	 * @param a
	 * @param b
	 * @return
	 */
	public static Double error(Matrix a, Matrix b) {
		double sum = 0;
		for (int i = 0; i < a.rows; i++) {
			for (int j = 0; j < a.cols; j++) {
				sum += Math.pow(a.data[i][j] - b.data[i][j], 2);
			}
		}
		return sum / a.rows;
	}

	/**
	 * Transposes matrixes.
	 * @param a
	 * @return
	 */
	public static Matrix transpose(Matrix a) {
		Matrix temp = new Matrix(a.cols, a.rows);
		for (int i = 0; i < a.rows; i++) {
			for (int j = 0; j < a.cols; j++) {
				temp.data[j][i] = a.data[i][j];
			}
		}
		return temp;
	}

	/**
	 * Mutiplys matrixes.
	 * @param a
	 * @param b
	 * @return
	 */
	public static Matrix multiply(Matrix a, Matrix b) {
		Matrix temp = new Matrix(a.rows, b.cols);
		for (int i = 0; i < temp.rows; i++) {
			for (int j = 0; j < temp.cols; j++) {
				double sum = 0;
				for (int k = 0; k < a.cols; k++) {
					sum += a.data[i][k] * b.data[k][j];
				}
				temp.data[i][j] = sum;
			}
		}
		return temp;
	}

	/**
	 * Mutiplies matrixes.
	 * @param a
	 */
	public void multiply(Matrix a) {
		for (int i = 0; i < a.rows; i++) {
			for (int j = 0; j < a.cols; j++) {
				this.data[i][j] *= a.data[i][j];
			}
		}

	}
	/**
	 * Mutiplies matrix members with scalar a.
	 * @param a
	 */
	public void multiply(Double a) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				this.data[i][j] *= a;
			}
		}

	}
	/**
	 * Calculates sigmoid of number.
	 */
	public void sigmoid() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++)
				this.data[i][j] = 1 / (1 + Math.exp(-this.data[i][j]));
		}

	}
	
	/**
	 * Calculate derivate of sigmoid function.
	 * @return
	 */
	public Matrix derSigmoid() {
		Matrix temp = new Matrix(rows, cols);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++)
				temp.data[i][j] = this.data[i][j] * (1 - this.data[i][j]);
		}
		return temp;

	}

	/**
	 * Creates one-dimensional matrix from array.
	 * @param x
	 * @return
	 */
	public static Matrix fromArray(Double[] x) {
		Matrix temp = new Matrix(x.length, 1);
		for (int i = 0; i < x.length; i++)
			temp.data[i][0] = x[i];
		return temp;

	}

	/**
	 * Switches one-dimensional matrix in array.
	 * @param x
	 * @return
	 */
	public List<Double> toArray() {
		List<Double> temp = new ArrayList<Double>();

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				temp.add(data[i][j]);
			}
		}
		return temp;
	}
	
	/**
	 * Writes matrix.
	 */
	public void ispis() {
		System.out.println("*************************");
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				System.out.print(data[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("********************************");
	}
	
	/**
	 * Mnozi tezine i deltom.
	 * @param w
	 * @param delta
	 * @return
	 * @throws Exception
	 */
	public static Matrix mjenjajTezineDeltom(Matrix w, Matrix delta) throws Exception {
		Matrix temp = new Matrix(w.rows, w.cols);
		if(w.cols!=delta.rows)
			throw new Exception("Krive dimenzije");
		
		for (int i = 0; i < w.rows; i++) {
			for (int j = 0; j < w.cols; j++) {
				temp.data[i][j]=w.data[i][j]*delta.data[j][0];
			}
			
		}
		return temp;
	}
	/**
	 * Mnozi tezine ulazima.
	 * @param w
	 * @param ulazi
	 * @return
	 * @throws Exception
	 */
	public static Matrix mjenjajTezineUlazima(Matrix w, Matrix ulazi) throws Exception {
		Matrix temp = new Matrix(w.rows, w.cols);
		if(w.rows!=ulazi.rows)
			throw new Exception("Krive dimenzije");
		
		for (int i = 0; i < w.rows; i++) {
			for (int j = 0; j < w.cols; j++) {
				temp.data[i][j]=w.data[i][j]*ulazi.data[i][0];
			}
			
		}
		return temp;
	}
	/**
	 * Mnozi tezine deltom.
	 * @param w
	 * @param delta
	 * @return
	 * @throws Exception
	 */
	public static Matrix mnoziSumom(Matrix w, Matrix delta) throws Exception {
		Matrix temp = new Matrix(delta.rows, delta.cols);
		if(w.rows!=delta.rows)
			throw new Exception("Krive dimenzije");
		
		for (int i = 0; i < w.rows; i++) {
			double sum=0;
			for (int j = 0; j < w.cols; j++) {
				sum+=w.data[i][j];
			}
			temp.data[i][0]=sum*delta.data[i][0];
			
		}
		return temp;
	}
	
	/**
	 * Djeli clanove matrice danim brojem.
	 * @param a
	 */
	public void divide(Double a) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				this.data[i][j] /= a;
			}
		}

	}
}
