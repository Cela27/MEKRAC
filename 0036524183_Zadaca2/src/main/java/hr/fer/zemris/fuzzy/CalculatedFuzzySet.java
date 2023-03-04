package hr.fer.zemris.fuzzy;

public class CalculatedFuzzySet implements IFuzzySet {
	
	private IDomain domain;
	private IIntUnaryFunction function;
	
	
	public CalculatedFuzzySet(IDomain domain, IIntUnaryFunction function) {
		this.domain=domain;
		this.function=function;
	}
	
	@Override
	public Domain getDomain() {
		return (Domain) domain;
	}
	
	
	//ovo rjesi probaj sa izlazima
	@Override
	public double getValuesAt(DomainElement domainElement) {
		return function.valueAt(domainElement.getValues()[0]);
	}
	
	
}
