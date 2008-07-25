package pt.ist.expenditureTrackingSystem.presentationTier;

public class Context {

    private static final InheritableThreadLocal<Context> contextVar = new InheritableThreadLocal<Context>();

    public static Context getContext() {
	return contextVar.get();
    }

    public static void setContext(final Context context) {
	contextVar.set(context);
    }

    private final String module;

    public Context(final String module) {
	this.module = module;
    }

    public void setAsActive() {
	setContext(this);
    }

    public String getModule() {
        return module;
    }

}
