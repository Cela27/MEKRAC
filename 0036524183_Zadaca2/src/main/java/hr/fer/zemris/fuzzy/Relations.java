package hr.fer.zemris.fuzzy;

import java.util.Set;
import java.util.TreeSet;

public class Relations {

	public static boolean isSymmetric(IFuzzySet relation) {
		if (!isUtimesURelation(relation))
			return false;

		for (DomainElement de1 : relation.getDomain()) {

			for (DomainElement de2 : relation.getDomain()) {

				if (de1.getValues()[0] == de2.getValues()[1] && de1.getValues()[1] == de2.getValues()[0]) {
					if (!(relation.getValuesAt(de1) == relation.getValuesAt(de2)))
						return false;
				}
			}
		}
		return true;
	}

	public static boolean isReflexive(IFuzzySet relation) {
		if (!isUtimesURelation(relation))
			return false;

		for (DomainElement de : relation.getDomain()) {
			if (de.getValues()[0] == de.getValues()[1]) {
				if (!(relation.getValuesAt(de) == 1))
					return false;
			}
		}
		return true;
	}

	public static boolean isMaxMinTransitive(IFuzzySet relation) {
		if (!isUtimesURelation(relation))
			return false;

		Set<Integer> u = new TreeSet<>();
		for (DomainElement de : relation.getDomain()) {
			u.add(de.getValues()[0]);
		}

		for (Integer x : u) {

			for (Integer z : u) {
				double xz = relation.getValuesAt(DomainElement.of(x, z));

				double max = 0;
				for (Integer y : u) {
					double min = Math.min(relation.getValuesAt(DomainElement.of(x, y)),
							relation.getValuesAt(DomainElement.of(y, z)));

					if (min > max)
						max = min;
				}

				if (!(xz >= max))
					return false;
			}
		}
		return true;

	}

	public static boolean isUtimesURelation(IFuzzySet relation) {
		int len = relation.getDomain().elementForIndex(0).getValues().length;

		if (!(isPerfectSquareByUsingSqrt(relation.getDomain().getCardinality())))
			return false;

		Set<Integer> prviBroj = new TreeSet<>();
		Set<Integer> drugiBroj = new TreeSet<>();

		for (DomainElement de : relation.getDomain()) {
			if (len != de.getValues().length)
				return false;

			prviBroj.add(de.getValues()[0]);
			drugiBroj.add(de.getValues()[1]);
		}

		return prviBroj.equals(drugiBroj);
	}

	public static IFuzzySet compositionOfBinaryRelations(IFuzzySet relation1, IFuzzySet relation2) {

		Set<Integer> u = new TreeSet<>();
		Set<Integer> v1 = new TreeSet<>();
		Set<Integer> v2 = new TreeSet<>();
		Set<Integer> w = new TreeSet<>();

		for (DomainElement de : relation1.getDomain()) {
			u.add(de.getValues()[0]);
			v1.add(de.getValues()[1]);
		}

		for (DomainElement de : relation2.getDomain()) {
			v2.add(de.getValues()[0]);
			w.add(de.getValues()[1]);
		}

		if (!(v1.equals(v2))) {
			return null;
		}	

		IDomain uDomain=new SimpleDomain(u.iterator().next(), u.iterator().next()+u.size());
		IDomain wDomain=new SimpleDomain(w.iterator().next(), w.iterator().next()+w.size());
		IDomain domena= Domain.combine(uDomain, wDomain);

		MutableFuzzySet set= new MutableFuzzySet(domena);
		
		for (Integer x : u) {

			for (Integer z : w) {

				double max = 0;
				for (Integer y : v1) {
					
					double min = Math.min(relation1.getValuesAt(DomainElement.of(x, y)),
							relation2.getValuesAt(DomainElement.of(y, z)));

					if (min > max)
						max = min;
				}

				set.set(DomainElement.of(x, z), max);
			}
		}
		return set;

	}

	public static boolean isFuzzyEquivalence(IFuzzySet relation) {
		if(isSymmetric(relation) && isReflexive(relation) && isMaxMinTransitive(relation))
			return true;
		
		return false;
	}

	private static boolean isPerfectSquareByUsingSqrt(long n) {
		if (n <= 0) {
			return false;
		}
		double squareRoot = Math.sqrt(n);
		long tst = (long) (squareRoot + 0.5);
		return tst * tst == n;
	}

}
