package hr.fer.zemris.fuzzy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//n=1
public class SimpleDomain extends Domain {
	private int first;
	private int last;
	
	
	public SimpleDomain(int first, int last) {
		super();
		this.first = first;
		this.last = last;
	}
	
	public SimpleDomain(int i) {
		this.first=this.last=i;
	}
	@Override
	public int getCardinality() {
		if(this.last <=0 && this.first<=0) {
			return (this.last-this.first)*-1;
		}
//		else if (this.last >=0 && this.first<=0){
//			return (this.last-this.first)+1;
//		}
		else {
			return this.last-this.first;
		}
	}
	
	@Override
	public IDomain getComponent(int numOfComponent) {
		return new SimpleDomain(this.first+numOfComponent);
	}
	
	@Override
	public int getNumberOfComponents() {
		return getCardinality();
	}
	
	@Override
	public Iterator<DomainElement> iterator() {
		List<DomainElement> lista= new ArrayList<>();
		for(int i=this.first;i<this.last;i++) {
			int values[]= {i};
			lista.add(new DomainElement(values));
		}
		return lista.iterator();
	}
	@Override
	public String toString() {
		return "SimpleDomain [first=" + first + ", last=" + last + "]";
	}

	public int getFirst() {
		return first;
	}
	public void setFirst(int first) {
		this.first = first;
	}
	public int getLast() {
		return last;
	}
	public void setLast(int last) {
		this.last = last;
	}
	
	
}
