package module.mission.domain.util;

import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;
import myorg.domain.scheduler.WriteCustomTask;

public class MigrateMissionProcessQueues extends WriteCustomTask {

    @Override
    protected void doService() {
	int missionProcessesWithQueue = 0;
	int missionProcessesWithQueues = 0;
	int missionProcessesWithMoreThanOneQueue = 0;
	for (final Mission mission : MissionSystem.getInstance().getMissionsSet()) {
	    final MissionProcess missionProcess = mission.getMissionProcess();
	    if (missionProcess.hasCurrentQueue()) {
		missionProcessesWithQueue++;
		missionProcess.addCurrentQueues(missionProcess.getCurrentQueue());
		missionProcess.setCurrentQueueToNullWithoutAddingToHistory();
	    }
	    if (missionProcess.getCurrentQueuesCount() == 1) {
		missionProcessesWithQueues++;
	    }
	    if (missionProcess.getCurrentQueuesCount() > 1) {
		missionProcessesWithMoreThanOneQueue++;
	    }
	}
	out.println("Found: " + missionProcessesWithQueue + " mission processes with an old queue.");
	out.println("Found: " + missionProcessesWithQueues + " mission processes with one of the new queues.");
	out.println("Found: " + missionProcessesWithMoreThanOneQueue + " mission processes with more than one of the new queues.");
    }
}
