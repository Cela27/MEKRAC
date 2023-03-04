package hr.fer.zemris.fuzzy;

public class MutableFuzzySet implements IFuzzySet{
	
	private double[] memberships;
	private IDomain domain;
	
	//defaultno ih na 0 stavi
	public MutableFuzzySet(IDomain domain) {
		this.domain=domain;
		memberships= new double[domain.getCardinality()];
		for(int i=0;i<memberships.length;i++) {
			memberships[i]=0;
		}
	}
	
	
	public double[] getMemberships() {
		return memberships;
	}

	public MutableFuzzySet(IDomain domain, double[] memberships) {
		this.domain=domain;
		this.memberships=memberships;
			
	}
	
	public MutableFuzzySet set(DomainElement domainElement, double mu) {
		int i=domain.indexOfElement(domainElement);
		memberships[i]=mu;
		return new MutableFuzzySet(domain, memberships);
	}
	
	@Override
	public Domain getDomain() {
		return (Domain) domain;
	}

	@Override
	public double getValuesAt(DomainElement domainElement) {
		int i=domain.indexOfElement(domainElement);
		return memberships[i];
	}

}
