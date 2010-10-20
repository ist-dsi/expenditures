package module.mission.domain.activity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import module.mission.domain.Mission;
import module.mission.domain.MissionItem;
import module.mission.domain.MissionProcess;
import module.mission.domain.PersonelExpenseItem;
import module.mission.domain.Salary;
import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.IllegalWriteException;
import dml.DomainClass;
import dml.DomainModel;

public class ItemActivityInformation extends ActivityInformation<MissionProcess> implements Serializable {

    private Class topLevelMissionItemType;
    private Class concreteMissionItemType;
    private MissionItem missionItem;
    private Collection<Person> people = new ArrayList<Person>();

    public ItemActivityInformation(final MissionProcess missionProcess,
	    final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
	super(missionProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
	return false;
    }

    public MissionItem getMissionItem() {
	return missionItem;
    }

    public void setMissionItem(final MissionItem missionItem) {
	this.missionItem = missionItem;
	if (missionItem != null) {
	    setPeople(missionItem.getPeopleSet());
	}
    }

    public Class getTopLevelMissionItemType() {
	return topLevelMissionItemType;
    }

    public void setTopLevelMissionItemType(Class topLevelMissionItemType) {
	this.topLevelMissionItemType = topLevelMissionItemType;
    }

    public Class getConcreteMissionItemType() {
	return concreteMissionItemType;
    }

    public void setConcreteMissionItemType(Class concreteMissionItemType) {
	final MissionItem missionItem = getMissionItem();
	if (missionItem == null || missionItem.getClass() != concreteMissionItemType) {
	    setMissionItem(null);
	}
	this.concreteMissionItemType = concreteMissionItemType;
    }

    @Service
    private MissionItem createNewMissionItem() throws IllegalWriteException {
	final String concreteMissionItemTypeName = concreteMissionItemType.getName();
	final DomainModel domainModel = FenixWebFramework.getDomainModel();
	for (final DomainClass domainClass : domainModel.getDomainClasses()) {
	    if (domainClass.getFullName().equals(concreteMissionItemTypeName)) {
		try {
		    final Class clazz = Class.forName(domainClass.getFullName());
		    final MissionItem missionItem = (MissionItem) clazz.getConstructor().newInstance();
		    final Mission mission = getProcess().getMission();
		    missionItem.getPeopleSet().addAll(mission.getParticipantesSet());
		    if (missionItem.isPersonelExpenseItem()) {
			final PersonelExpenseItem personelExpenseItem = (PersonelExpenseItem) missionItem;
			personelExpenseItem.setStart(mission.getDaparture());
			personelExpenseItem.setEnd(mission.getArrival());
			personelExpenseItem.setMissionForCreation(mission);
			personelExpenseItem.setDailyPersonelExpenseCategory(Salary.getDefaultDailyPersonelExpenseCategory(
				mission.getDailyPersonelExpenseTable(), missionItem.getPeopleSet()));
		    }
		    return missionItem;
		} catch (final ClassNotFoundException e) {
		    throw new Error(e);
		} catch (IllegalArgumentException e) {
		    throw new Error(e);
		} catch (SecurityException e) {
		    throw new Error(e);
		} catch (InstantiationException e) {
		    throw new Error(e);
		} catch (IllegalAccessException e) {
		    throw new Error(e);
		} catch (InvocationTargetException e) {
		    final Throwable t = e.getCause();
		    if (t instanceof IllegalWriteException) {
			final IllegalWriteException iwe = (IllegalWriteException) t;
			throw iwe;
		    }
		    throw new Error(e);
		} catch (NoSuchMethodException e) {
		    throw new Error(e);
		}
	    }
	}
	return null;
    }

    public void setMissionItem() throws IllegalWriteException {
	final MissionItem missionItem = getMissionItem();
	if (missionItem == null && concreteMissionItemType != null) {
	    setMissionItem(createNewMissionItem());
	}
    }

    public Collection<Person> getPeople() {
	return people;
    }

    public void setPeople(final Collection<Person> people) {
	this.people.clear();
	this.people.addAll(people);
    }

}
