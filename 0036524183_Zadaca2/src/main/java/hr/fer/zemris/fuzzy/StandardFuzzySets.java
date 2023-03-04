package hr.fer.zemris.fuzzy;

public class StandardFuzzySets {

	public StandardFuzzySets() {
		super();
	}

	public static IIntUnaryFunction lFunction(int alfa, int beta) {

		return new IIntUnaryFunction() {

			@Override
			public double valueAt(int num) {
				if (num < alfa)
					return 1;

				else if (num >= beta)
					return 0;

				return (float) (beta - num) / (beta - alfa);
			}
		};
	}

	public static IIntUnaryFunction gammaFunction(int alfa, int beta) {

		return new IIntUnaryFunction() {

			@Override
			public double valueAt(int num) {
				if (num < alfa)
					return 0;

				else if (num >= beta)
					return 1;

				return (float) (num - alfa) / (num - beta);
			}
		};

	}

	public static IIntUnaryFunction lambdaFunction(int alfa, int beta, int gama) {
		return new IIntUnaryFunction() {

			@Override
			public double valueAt(int num) {

				if (num < alfa)
					return 0;

				else if (num >= gama)
					return 0;

				else if (num >= alfa && num < beta)
					return (float) (num - alfa) / (beta - alfa);

				else
					return (float) (gama - num) / (gama - beta);

			}
		};
	}

}
