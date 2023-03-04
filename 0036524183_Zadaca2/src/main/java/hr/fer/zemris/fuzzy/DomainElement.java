package hr.fer.zemris.fuzzy;

import java.util.Arrays;

public class DomainElement implements Comparable<DomainElement>{
	private int[] values;
	
	public DomainElement(int[] values) {
		this.setValues(values);
	}
	public int getNumberOfComponents() {
		return values.length;
	}
	
	public int getComponentValue(int index) {
		return values[index];
	}
	
	public int[] getValues() {
		return values;
	}

	public void setValues(int[] values) {
		this.values = values;
	}
	@Override
	public int hashCode() {
		return values.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof DomainElement)) {
			return false;
		}
		DomainElement pom= (DomainElement) obj;
		
		if(pom.getValues().length!=this.values.length)
			return false;
		
		for(int i=0; i<values.length; i++) {
			if(values[i]!=pom.getValues()[i])
				return false;
		}
		
		return true;
		
	}
	@Override
	public String toString() {
		return Arrays.toString(values);
	}
	
	public static DomainElement of(int... values) {
		return new DomainElement(values);
	}
	
	@Override
	public int compareTo(DomainElement o) {
		if(this.values.length<o.values.length)
			return -1;
		else if(this.values.length>o.values.length)
			return 1;
		else {
			for(int i=0; i<this.values.length;i++) {
				Integer v1= this.values[i];
				Integer v2= o.values[i];
				if(v1==v2) continue;
				return v1.compareTo(v2);
			}
		}
		return 0;
	}
	
	
}
