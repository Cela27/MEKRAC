package hr.fer.zemris.fuzzy;

public interface IFuzzySet {
	Domain getDomain();
	
	double getValuesAt(DomainElement domainElement);
}
