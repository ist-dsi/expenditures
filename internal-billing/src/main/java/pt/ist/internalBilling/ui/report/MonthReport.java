package pt.ist.internalBilling.ui.report;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import module.finance.util.Money;
import pt.ist.internalBilling.domain.BillableTransaction;

public class MonthReport {

    public static class Entry {

        private int count = 0;
        private Money value = Money.ZERO;

        public int getCount() {
            return count;
        }
        public Money getValue() {
            return value;
        }

    }

    public static <T> void process(final Map<T, MonthReport> userReportMap, final Function<BillableTransaction, T> keyMapper, BillableTransaction tx) {
        final T key = keyMapper.apply(tx);
        if (key == null) {
        } else {
            final MonthReport userReport = userReportMap.computeIfAbsent(key, (k) -> new MonthReport());
            userReport.process(tx);
        }
    }

    public static <T> Map<T, MonthReport> consolidate(final Map<T, MonthReport> map) {
        map.values().stream()
            .flatMap(r -> r.serviceMap.keySet().stream())
            .distinct()
            .forEach(service -> map.forEach((k, v) -> v.serviceMap.computeIfAbsent(service, (s) -> new Entry())));
        return map;
    }

    private Map<String, Entry> serviceMap = new TreeMap<>();

    public BillableTransaction process(final BillableTransaction tx) {
        final String serviceCode = tx.getServiceCode();
        final int count = tx.getCount();
        final Money value = tx.getTxValue();

        process(serviceCode, count, value);
        process("total", count, value);

        return tx;
    }

    private void process(final String key, final int count, final Money value) {
        final Entry entry = serviceMap.computeIfAbsent(key, (k) -> new Entry());
        entry.count += count;
        entry.value = entry.value.add(value);        
    }

    public Map<String, Entry> getServiceMap() {
        return serviceMap;
    }

}
