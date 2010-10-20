package module.mission.presentationTier.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.workflow.domain.WorkflowQueue;
import module.workflow.domain.WorkflowSystem;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class WorkflowQueueProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	final List<WorkflowQueue> result = new ArrayList<WorkflowQueue>();
	result.addAll(WorkflowSystem.getInstance().getWorkflowQueuesSet());
	Collections.sort(result, WorkflowQueue.COMPARATOR_BY_NAME);
	return result;
    }

}
