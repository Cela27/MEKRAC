package hr.fer.zemris.fuzzy;

public class Debug {
	public static void print(IDomain domain, String headingText) {
		if (headingText != null) {
			System.out.println(headingText);
		}
		for (DomainElement e : domain) {
			System.out.println("Element domene: " + e);
		}
		System.out.println("Kardinalitet domene je: " + domain.getCardinality());
		System.out.println();
	}
	
	public static void print1(IFuzzySet set, String headingText) {
		if (headingText != null) {
			System.out.println(headingText);
		}
		for (DomainElement e : set.getDomain()) {
			
			System.out.println("d("+e.getValues()[0]+")="+set.getValuesAt(e));
		}
		System.out.println();
	}

}
