package pt.ist.expenditureTrackingSystem.util;

import java.util.Collection;
import java.util.concurrent.Callable;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import pt.ist.esw.advice.pt.ist.fenixframework.AtomicInstance;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class TransactionalDataSource implements JRDataSource {

    private final Atomic atomic = new AtomicInstance(TxMode.READ, true);
    private JRBeanCollectionDataSource wrap;


    public TransactionalDataSource(Collection beanCollection) {
        wrap = new JRBeanCollectionDataSource(beanCollection);
    }


    @Override
    public boolean next() throws JRException {
        return wrap.next();
    }

    private class FieldCallable implements Callable<Void> {

        final JRField field;
        Object result;

        private FieldCallable(final JRField field) {
            this.field = field;
        }

        @Override
        public Void call() throws Exception {
            result = wrap.getFieldValue(field);
            return null;
        }
        
    }

    @Override
    public Object getFieldValue(final JRField field) throws JRException {
        try {
            final FieldCallable callable = new FieldCallable(field);
            FenixFramework.getTransactionManager().withTransaction(callable, atomic);
            return callable.result;
        } catch (final Exception e) {
            throw new Error(e);
        }
    }

}
