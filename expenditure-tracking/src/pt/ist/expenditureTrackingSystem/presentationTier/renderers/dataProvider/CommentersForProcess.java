package pt.ist.expenditureTrackingSystem.presentationTier.renderers.dataProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.CommentBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.expenditureTrackingSystem.domain.processes.ProcessComment;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CommentersForProcess implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	CommentBean bean = (CommentBean) source;
	Set<Person> availablePeopleToNotify = new HashSet<Person>();
	GenericProcess process = bean.getProcess();
	if (PaymentProcess.class.isAssignableFrom(process.getClass())) {
	    availablePeopleToNotify.add(((PaymentProcess) process).getRequestor());
	}

	for (ProcessComment comment : process.getCommentsSet()) {
	    availablePeopleToNotify.add(comment.getCommenter());
	}

	return new ArrayList<Person>(availablePeopleToNotify);
    }
}
