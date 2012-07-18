package module.mission.domain.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import module.mission.domain.Mission;
import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionItem;
import module.mission.domain.MissionItemFinancer;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.bennu.core.domain.util.Money;

public class DistributeItemCostsActivityInformation extends ActivityInformation<MissionProcess> implements Serializable {

    public static class MissionItemFinancerBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private final MissionFinancer missionFinancer;
	private Money amount;

	public MissionItemFinancerBean(final MissionFinancer missionFinancer, final Money amount) {
	    this.missionFinancer = missionFinancer;
	    setAmount(amount);
	}

	public MissionItemFinancerBean(final MissionFinancer missionFinancer) {
	    this(missionFinancer, Money.ZERO);
	}

	public MissionItemFinancerBean(final MissionItemFinancer missionItemFinancer) {
	    this(missionItemFinancer.getMissionFinancer(), missionItemFinancer.getAmount());
	}

	public MissionFinancer getMissionFinancer() {
	    return missionFinancer;
	}

	public Money getAmount() {
	    return amount;
	}

	public void setAmount(Money amount) {
	    this.amount = amount;
	}
    }

    public static class MissionItemFinancerBeanCollection extends ArrayList<MissionItemFinancerBean> {

	public boolean contains(final MissionFinancer missionFinancer) {
	    for (final MissionItemFinancerBean missionItemFinancerBean : this) {
		if (missionFinancer == missionItemFinancerBean.getMissionFinancer()) {
		    return true;
		}
	    }
	    return false;
	}

	public boolean containsWithValue(final MissionFinancer missionFinancer) {
	    for (final MissionItemFinancerBean missionItemFinancerBean : this) {
		if (missionFinancer == missionItemFinancerBean.getMissionFinancer()
			&& missionItemFinancerBean.getAmount() != null && missionItemFinancerBean.getAmount().isPositive()) {
		    return true;
		}
	    }
	    return false;
	}

    }

    private MissionItem missionItem;
    private final MissionItemFinancerBeanCollection missionItemFinancerBeans = new MissionItemFinancerBeanCollection();

    public DistributeItemCostsActivityInformation(final MissionProcess missionProcess,
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
	missionItemFinancerBeans.clear();
	if (missionItem != null) {
	    for (final MissionItemFinancer missionItemFinancer : missionItem.getMissionItemFinancersSet()) {
		final MissionItemFinancerBean missionItemFinancerBean = new MissionItemFinancerBean(missionItemFinancer);
		missionItemFinancerBeans.add(missionItemFinancerBean);
	    }
	    final MissionProcess missionProcess = getProcess();
	    final Mission mission = missionProcess.getMission();
	    for (final MissionFinancer missionFinancer : mission.getFinancerSet()) {
		if (!missionItemFinancerBeans.contains(missionFinancer)) {
		    final MissionItemFinancerBean missionItemFinancerBean = new MissionItemFinancerBean(missionFinancer);
		    missionItemFinancerBeans.add(missionItemFinancerBean);
		}
	    }
	}
    }

    public MissionItemFinancerBeanCollection getMissionItemFinancerBeans() {
	return missionItemFinancerBeans;
    }

    public void setMissionItemFinancerBeans(Collection<MissionItemFinancerBean> missionItemFinancerBeans) {
	this.missionItemFinancerBeans.clear();
	this.missionItemFinancerBeans.addAll(missionItemFinancerBeans);
    }

    public void distributeMissionItemFinancerValues() {
	final Money money = getMissionItem().getValue();
	int numberFinancers = missionItemFinancerBeans.size();
	final Money shareValues[] = money.allocate(numberFinancers);
	int i = 0;
	for (final MissionItemFinancerBean missionItemFinancerBean : missionItemFinancerBeans) {
	    missionItemFinancerBean.setAmount(shareValues[i++]);
	}
    }

}
