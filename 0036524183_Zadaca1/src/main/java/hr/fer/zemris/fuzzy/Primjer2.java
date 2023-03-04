package hr.fer.zemris.fuzzy;

public class Primjer2 {
	public static void main(String[] args) {
		IDomain d = Domain.intRange(0, 11);
		IFuzzySet set1 = new MutableFuzzySet(d).set(DomainElement.of(0), 1.0).set(DomainElement.of(1), 0.8)
				.set(DomainElement.of(2), 0.6).set(DomainElement.of(3), 0.4).set(DomainElement.of(4), 0.2);
		Debug.print1(set1, "Set1:");
		IFuzzySet notSet1 = Operations.unaryOperation(set1, Operations.zadehNot());
		Debug.print1(notSet1, "notSet1:");
		IFuzzySet union = Operations.binaryOperation(set1, notSet1, Operations.zadehAnd());
		Debug.print1(union, "Set1 union notSet1:");
		IFuzzySet presjek = Operations.binaryOperation(set1, notSet1, Operations.zadehOr());
		Debug.print1(presjek, "Set1 presjek notSet1:");
		IFuzzySet tNorm = Operations.binaryOperation(set1, notSet1, Operations.hamacherTNorm(1.0));
		Debug.print1(tNorm, "Set1 intersection with notSet1 using parameterised Hamacher T norm with parameter 1.0:");
		IFuzzySet sNorm = Operations.binaryOperation(set1, notSet1, Operations.hamacherSNorm(1.0));
		Debug.print1(sNorm, "Set1 intersection with notSet1 using parameterised Hamacher S norm with parameter 1.0:");
	}
}