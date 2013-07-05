package module.internalBilling.domain;

import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class InternalBillingSystem extends InternalBillingSystem_Base {

    public static final String BUNDLE = "resources.InternalBillingResources";

    private InternalBillingSystem(final VirtualHost virtualHost) {
        super();
        setVirtualHost(virtualHost);
    }

    @Atomic
    public static InternalBillingSystem getInstance() {
        final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
        if (virtualHost != null) {
            return virtualHost.getInternalBillingSystem() == null ? new InternalBillingSystem(virtualHost) : virtualHost
                    .getInternalBillingSystem();
        }
        return null;
    }

    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle(BUNDLE, Language.getLocale());
    }

    public static String getBundleName() {
        return BUNDLE;
    }

    public Set<Account> getAccountSet() {
        Set<Account> accountSet = new HashSet<Account>();
        for (Operator operator : getOperatorSet()) {
            accountSet.addAll(operator.getAccountSet());
        }
        return accountSet;
    }
}
