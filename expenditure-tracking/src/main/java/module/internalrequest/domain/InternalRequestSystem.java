package module.internalrequest.domain;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.Singleton;
import org.joda.time.DateTime;

import java.util.function.Supplier;

public class InternalRequestSystem extends InternalRequestSystem_Base {

    private static final Supplier<InternalRequestSystem> SYSTEM_GETTER = () -> Bennu.getInstance().getInternalRequestSystem();
    private static final Supplier<InternalRequestSystem> SYSTEM_CREATOR = () -> new InternalRequestSystem();

    public static InternalRequestSystem getInstance() {
        return Singleton.getInstance(SYSTEM_GETTER, SYSTEM_CREATOR);
    }

    private InternalRequestSystem() {
        super();
        setBennu(Bennu.getInstance());
    }

    protected long getNextProcessNumber(int year) {
        long nextProcessNumber;
        if (year == this.getYear()) {
            nextProcessNumber = this.getNumOfProcessesOfYear() + 1;
        } else {
            this.setYear(year);
            nextProcessNumber = 1;
        }
        this.setNumOfProcessesOfYear(nextProcessNumber);
        return nextProcessNumber;
    }

    public void reset() {
        this.setYear(DateTime.now().getYear());
        this.setNumOfProcessesOfYear(0);
        for(InternalRequestProcess p : this.getInternalRequestProcessesSet()) {
            p.delete();
        }
    }
}
