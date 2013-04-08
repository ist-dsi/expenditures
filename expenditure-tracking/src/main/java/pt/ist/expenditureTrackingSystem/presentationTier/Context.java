package pt.ist.expenditureTrackingSystem.presentationTier;

public class Context {

    private static final InheritableThreadLocal<Context> contextVar = new InheritableThreadLocal<Context>();

    public static Context getContext() {
        return contextVar.get();
    }

    public static void setContext(final Context context) {
        contextVar.set(context);
    }

    public static boolean isPresent(final String module) {
        final Context context = getContext();
        return context != null && context.contains(module);
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

    public boolean contains(final String module) {
        return this.module == module || (this.module != null && this.module.equals(module));
    }

}
