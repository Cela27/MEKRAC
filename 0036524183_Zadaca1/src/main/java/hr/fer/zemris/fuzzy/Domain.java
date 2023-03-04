package hr.fer.zemris.fuzzy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public abstract class Domain implements IDomain {


	public Domain() {
		super();
	}
	
	public static IDomain intRange(int i, int j) {
		return new SimpleDomain(i, j);
	}

	public static IDomain combine(IDomain d1, IDomain d2) {
		
		if(d1 instanceof SimpleDomain) {
			
			SimpleDomain sd1= (SimpleDomain) d1;
			
			if(d2 instanceof SimpleDomain) {
				SimpleDomain sd2= (SimpleDomain) d2;
				SimpleDomain[] domains = {sd1, sd2};
				return new CompositeDomain(domains);
			}
			else {
				CompositeDomain cd2=(CompositeDomain) d2;
				SimpleDomain[] domains = {sd1};
				for(int i=1; i<=cd2.getDomains().length;i++) {
					domains[i]=cd2.getDomains()[i-1];
				}
				return new CompositeDomain(domains);
			}
		}else {
			CompositeDomain cd1=(CompositeDomain) d1;
			SimpleDomain[] domains= cd1.getDomains();
			if(d2 instanceof SimpleDomain) {
				SimpleDomain sd2= (SimpleDomain) d2;
				domains[domains.length]=sd2;
				return new CompositeDomain(domains);
			}
			else {
				CompositeDomain cd2=(CompositeDomain) d2;
				int j=domains.length;
				//tu moza <=
				for(int i=j; i<cd2.getDomains().length+j;i++) {
					domains[i]=cd2.getDomains()[i-j];
				}
				return new CompositeDomain(domains);
			}
		}
		
	}


	@Override
	public int indexOfElement(DomainElement domainElement) {
		Iterator<DomainElement> iter=this.iterator();
		int br=0;
		while(iter.hasNext()) {
			
			if(iter.next().equals(domainElement)) {
				return br;
			}
			br++;
		}
		return -1;
	}

	@Override
	public DomainElement elementForIndex(int index) {
		Iterator<DomainElement> iter=this.iterator();
		int br=0;
		while(iter.hasNext()) {
			if(br==index) {
				return iter.next();
			}
			br++;
			iter.next();
		}
		return null;
	}

}
