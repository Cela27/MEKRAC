package hr.fer.zemris.fuzzy;

public class Operations {

	public Operations() {
		super();
	}

	public static IFuzzySet unaryOperation(IFuzzySet set, IUnaryFunction function) {

		if (!(set instanceof MutableFuzzySet))
			return null;

		MutableFuzzySet cast = (MutableFuzzySet) set;

		double[] mem = new double[cast.getMemberships().length];

		for (int i = 0; i < mem.length; i++) {
			mem[i] = cast.getMemberships()[i];
		}

		MutableFuzzySet newSet = new MutableFuzzySet(cast.getDomain(), mem);

		for (DomainElement de : newSet.getDomain()) {
			newSet = newSet.set(de, function.valueAt(newSet.getValuesAt(de)));
		}

		return newSet;
	}

	public static IFuzzySet binaryOperation(IFuzzySet set1, IFuzzySet set2, IBinaryFunction function) {

//		System.out.println("*******************************");
//		Debug.print1(set1, "Set1");
//		
//		Debug.print1(set1, "Set2");
//		System.out.println("*******************************");

		if (!(set1 instanceof MutableFuzzySet))
			return null;

		if (!(set2 instanceof MutableFuzzySet))
			return null;

		if (set1.getDomain() != set2.getDomain())
			return null;

		MutableFuzzySet newSet = new MutableFuzzySet(set1.getDomain());

		for (DomainElement de : set1.getDomain()) {
			newSet = newSet.set(de, function.valueAt(set1.getValuesAt(de), set2.getValuesAt(de)));
		}

		return newSet;
	}

	public static IUnaryFunction zadehNot() {
		return new IUnaryFunction() {

			@Override
			public double valueAt(double num) {
				return 1. - num;
			}
		};
	}

	public static IBinaryFunction zadehAnd() {
		return new IBinaryFunction() {

			@Override
			public double valueAt(double num, double num2) {
				return Math.max(num, num2);
			}
		};
	}

	public static IBinaryFunction zadehOr() {
		return new IBinaryFunction() {

			@Override
			public double valueAt(double num, double num2) {
				return Math.min(num, num2);
			}
		};
	}

	public static IBinaryFunction hamacherTNorm(double v) {
		return new IBinaryFunction() {

			@Override
			public double valueAt(double num, double num2) {
				return (double) (num * num2) / (v + (1 - v) * (num + num2 - num * num2));
			}
		};
	}

	public static IBinaryFunction hamacherSNorm(double v) {
		return new IBinaryFunction() {

			@Override
			public double valueAt(double num, double num2) {
				return (double) (num + num2-(2-v)*(num*num2)) / 1-(1 - v) * (num * num2);
			}
		};
	}

}
