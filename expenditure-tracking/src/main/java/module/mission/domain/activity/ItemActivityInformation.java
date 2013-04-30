package module.mission.domain.activity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import module.mission.domain.Mission;
import module.mission.domain.MissionItem;
import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;
import module.mission.domain.PersonelExpenseItem;
import module.mission.domain.Salary;
import module.mission.domain.TemporaryMissionItemEntry;
import module.mission.domain.VehiclItem;
import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.fenixframework.dml.DomainClass;
import pt.ist.fenixframework.dml.DomainModel;

public class ItemActivityInformation extends ActivityInformation<MissionProcess> implements Serializable {

    private Class topLevelMissionItemType;
    private Class concreteMissionItemType;
    private MissionItem missionItem;
    private final Collection<Person> people = new ArrayList<Person>();
    private Person driver;

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
            if (missionItem instanceof VehiclItem) {
                setDriver(((VehiclItem) missionItem).getDriver());
            }
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

    @Atomic
    private MissionItem createNewMissionItem() {
        final String concreteMissionItemTypeName = concreteMissionItemType.getName();
        final DomainModel domainModel = FenixFramework.getDomainModel();
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
                    partiallyGC();
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
                    if (t instanceof WriteOnReadError) {
                        final WriteOnReadError iwe = (WriteOnReadError) t;
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

    public void setMissionItem() {
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

    public Person getDriver() {
        return driver;
    }

    public void setDriver(Person driver) {
        this.driver = driver;
    }

    public static int NUMBER_OF_OBJECTS_TO_GC = 10;

    protected void partiallyGC() {
        int cleanedObjects = 0;

        for (final TemporaryMissionItemEntry temporaryMissionItemEntry : MissionSystem.getInstance()
                .getTemporaryMissionItemEntriesSet()) {
            if (temporaryMissionItemEntry != null) {
                cleanedObjects += temporaryMissionItemEntry.gc() ? 1 : 0;
                if (cleanedObjects >= NUMBER_OF_OBJECTS_TO_GC) {
                    break;
                }
            }
        }
    }

}
