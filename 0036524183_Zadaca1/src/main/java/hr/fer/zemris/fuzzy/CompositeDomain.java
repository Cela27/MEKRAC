package hr.fer.zemris.fuzzy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CompositeDomain extends Domain {
	
	private SimpleDomain[] domains; 
	
	public CompositeDomain(SimpleDomain[] domains) {
		this.domains=domains;
	}
	
	public SimpleDomain[] getDomains() {
		return domains;
	}

	public void setDomains(SimpleDomain[] domains) {
		this.domains = domains;
	}
	//slucaj akd je 1 mozda neije dobar al nezz
	@Override
	public int getCardinality() {
		int br=1;
		for(int i=0; i<domains.length;i++) {
			br*=domains[i].getCardinality();
		}
		return br;
	}

	@Override
	public IDomain getComponent(int numOfComponent) {
		return domains[numOfComponent];
	}
	
	@Override
	public int getNumberOfComponents() {
		return this.domains.length;
	}

	@Override
	public Iterator<DomainElement> iterator() {
		Set<DomainElement> domena=new TreeSet<>();
		
		SimpleDomain sd= domains[0];
		
		Iterator<DomainElement> iter= sd.iterator();
		
		//tu ima nepotrebnof koda jer oetkja zapravo ide samo jedan krug kad je simplew domain
		while(iter.hasNext()) {
			int[] values= iter.next().getValues();
			for(int j=0; j<values.length;j++) {
				int[] tmp = {values[j]};
				domena.add(new DomainElement(tmp));
			}
		}
		
		for(int i=1; i<domains.length;i++) {
			sd= domains[i];
			
			Set<DomainElement> tmpDomena=new TreeSet<>();
			iter= sd.iterator();
			
			while(iter.hasNext()) {
				int next= iter.next().getValues()[0];
				for(DomainElement de: domena) {
					
					int values[]=de.getValues();
					List<Integer> vrijednosti= new ArrayList<Integer>();
					
					for(int k=0; k<values.length;k++) {
						vrijednosti.add(values[k]);
					}
					
					vrijednosti.add(next);
					
					values = vrijednosti.stream().mapToInt(br->br).toArray();
					
					tmpDomena.add(new DomainElement(values));
				}
			}
			domena=tmpDomena;
		}
		return domena.iterator();
	}
}
