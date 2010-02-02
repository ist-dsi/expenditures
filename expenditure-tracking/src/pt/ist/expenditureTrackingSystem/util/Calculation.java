package pt.ist.expenditureTrackingSystem.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Calculation<C extends Comparable<C>> {

    public static enum Operation {
	SUM, AVERAGE, MEDIAN;
    }

    private final Operation[] operations = new Operation[Operation.values().length];
    
    private final C[] catagories;

    private List<BigDecimal>[] values = null;
    private BigDecimal[] sum;
    private long[] count;

    public Calculation(final C[] catagories, final Operation... operations) {
	if (catagories == null) {
	    throw new NullPointerException("categories.not.specified");
	}
	if (operations == null || operations.length == 0) {
	    throw new NullPointerException("operations.not.specified");
	}
	this.catagories = Arrays.copyOf(catagories, catagories.length);
	for (final Operation operation : operations) {
	    this.operations[operation.ordinal()] = operation;
	}
	if (this.operations[Operation.SUM.ordinal()] != null || this.operations[Operation.AVERAGE.ordinal()] != null) {
	    sum = new BigDecimal[catagories.length];
	    final BigDecimal zero = new BigDecimal(0);
	    for (int i = 0; i < catagories.length; i++) {
		sum[i] = zero;
	    }
	}
	if (this.operations[Operation.AVERAGE.ordinal()] != null) {
	    count = new long[catagories.length];
	}
	if (this.operations[Operation.MEDIAN.ordinal()] != null) {
	    values = new List[catagories.length];
	    for (int i = 0; i < catagories.length; i++) {
		values[i] = new ArrayList<BigDecimal>();
	    }
	}
    }

    public void registerValue(final C catagory, final BigDecimal value) {
	final int index = getIndex(catagory);
	if (operations[Operation.AVERAGE.ordinal()] != null) {
	    count[index]++;
	    sum[index] = sum[index].add(value);
	} else if (operations[Operation.SUM.ordinal()] != null) {
	    sum[index] = sum[index].add(value);
	}
	if (operations[Operation.MEDIAN.ordinal()] != null) {
	    values[index].add(value);
	}
    }

    private int getIndex(final C catagory) {
	for (int i = 0; i < catagories.length; i++) {
	    if (catagory.compareTo(catagories[i]) == 0) {
		return i;
	    }
	}
	return -1;
    }

    public SortedMap<C, BigDecimal> getResult(final Operation operation) {
	final SortedMap<C, BigDecimal> result = new TreeMap<C, BigDecimal>();
	for (int i = 0; i < catagories.length; i++) {
	    final C catagory = catagories[i];
	    final BigDecimal value;
	    if (operation == Operation.SUM) {
		value = this.sum[i];
		result.put(catagory, value);
	    } else if (operation == Operation.AVERAGE) {
		final BigDecimal sum = this.sum[i];
		final long count = this.count[i];
		if (count > 0) {
		    value = sum.divide(new BigDecimal(count), RoundingMode.HALF_EVEN);
		    result.put(catagory, value);
		}
	    } else if (operation == Operation.MEDIAN) {
		if (values[i].size() > 0) {
    	    	    Collections.sort(values[i]);
    	    	    final double midPoint = 0.5 * values[i].size();
    	    	    if (midPoint == (int) midPoint) {
    	    		final double nextPoint = midPoint - 1;
    	    		final BigDecimal value1 = values[i].get((int) midPoint);
    	    		final BigDecimal value2 = values[i].get((int) nextPoint);
    	    		final BigDecimal sum = value1.add(value2);
    	    		value = sum.divide(new BigDecimal(2));
    	    	    } else {
    	    		final double index = midPoint - 0.5;
    	    		value = values[i].get((int) index);
    	    	    }
    	    	    result.put(catagory, value);
		}
	    } else {
		throw new NullPointerException("no.requested.operation");
	    }
	}
	return result;
    }

    public SortedMap<C, BigDecimal> getResultSum() {
	return getResult(Operation.SUM);
    }

    public SortedMap<C, BigDecimal> getResultAverage() {
	return getResult(Operation.AVERAGE);
    }

    public SortedMap<C, BigDecimal> getResultMedian() {
	return getResult(Operation.MEDIAN);
    }

    public SortedMap<C, BigDecimal> getMins() {
	final SortedMap<C, BigDecimal> result = new TreeMap<C, BigDecimal>();
	for (int i = 0; i < catagories.length; i++) {
	    final C catagory = catagories[i];

	    if (values != null && values[i] != null && values[i].size() > 0) {
		final BigDecimal min = Collections.min(values[i]);
		result.put(catagory, min);
	    } else {
		result.put(catagory, null);
	    }

	}
	return result;
    }

    public SortedMap<C, BigDecimal> getMaxs() {
	final SortedMap<C, BigDecimal> result = new TreeMap<C, BigDecimal>();
	for (int i = 0; i < catagories.length; i++) {
	    final C catagory = catagories[i];

	    if (values != null && values[i] != null && values[i].size() > 0) {
		final BigDecimal min = Collections.max(values[i]);
		result.put(catagory, min);
	    } else {
		result.put(catagory, null);
	    }

	}
	return result;
    }

}
