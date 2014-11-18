package module.mission.domain;

import org.fenixedu.bennu.scheduler.CronTask;

public class ClearTemporaryMissionItems extends CronTask {

    @Override
    public String getLocalizedName() {
        return getClass().getName();
    }

    @Override
    public void runTask() throws Exception {
        for (final TemporaryMissionItemEntry temporaryMissionItemEntry : MissionSystem.getInstance()
                .getTemporaryMissionItemEntriesSet()) {
            temporaryMissionItemEntry.gc();
        }
    }

}
