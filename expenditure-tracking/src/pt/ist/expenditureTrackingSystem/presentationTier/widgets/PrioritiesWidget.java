package pt.ist.expenditureTrackingSystem.presentationTier.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.WidgetController;
import myorg.util.ClassNameBundle;
import myorg.util.Counter;
import myorg.util.MultiCounter;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.util.ProcessMapGenerator;

@ClassNameBundle(bundle = "resources/ExpenditureResources", key = "title.widget.prioritySimplifiedProcedure")
public class PrioritiesWidget extends WidgetController {

    @Override
    public void doView(WidgetRequest request) {
	Map<AcquisitionProcessStateType, MultiCounter<AcquisitionProcessStateType>> simplifiedMap = ProcessMapGenerator
		.generateAcquisitionMap(Person.getLoggedPerson());
	List<Counter<AcquisitionProcessStateType>> priorityCounters = new ArrayList<Counter<AcquisitionProcessStateType>>();

	for (MultiCounter<AcquisitionProcessStateType> multiCounter : simplifiedMap.values()) {
	    Counter<AcquisitionProcessStateType> priorityCounter = ProcessMapGenerator.getPriorityCounter(multiCounter);

	    if (priorityCounter != null) {
		priorityCounters.add(priorityCounter);
	    }
	}

	Collections.sort(priorityCounters, new BeanComparator("countableObject"));
	request.setAttribute("simplifiedCounters-priority", priorityCounters);
    }
}
