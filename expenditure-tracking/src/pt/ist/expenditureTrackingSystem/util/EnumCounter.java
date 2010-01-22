package pt.ist.expenditureTrackingSystem.util;

import java.util.SortedMap;
import java.util.TreeMap;

public class EnumCounter<E extends Enum<E>> {

    private int[] counters = null;

    private Class<? extends Enum> clazz = null;

    public EnumCounter() {
    }

    private void init(Class clazz) {
	this.clazz = clazz;
	final Enum[] enums = (Enum[]) clazz.getEnumConstants();
	counters = new int[enums.length];
    }

    public void count(final E e, int count) {
	if (counters == null) {
	    final Class clazz = e.getClass();
	    final Class enclosingClass = clazz.getEnclosingClass();
	    init(enclosingClass == null ? clazz : enclosingClass);
	}
	counters[e.ordinal()] += count;
    }

    public void count(final E e) {
	count(e, 1);
    }

    public SortedMap<E, Integer> getCounts() {
	final SortedMap<E, Integer> result = new TreeMap<E, Integer>();
	if (clazz != null) { 
	    for (final E e : (E[]) clazz.getEnumConstants()) {
		final int count = counters[e.ordinal()];
		result.put(e, Integer.valueOf(count));
	    }
    	}
	return result;
    }

}
