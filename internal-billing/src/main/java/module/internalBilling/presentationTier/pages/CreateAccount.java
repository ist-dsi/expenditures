package module.internalBilling.presentationTier.pages;

import java.util.Map;
import java.util.ResourceBundle;

import module.internalBilling.domain.Account;
import module.internalBilling.domain.InternalBillingSystem;
import module.internalBilling.domain.Operator;
import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.fenixframework.Atomic;
import pt.ist.vaadinframework.EmbeddedApplication;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.data.reflect.DomainContainer;
import pt.ist.vaadinframework.data.reflect.DomainItem;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;
import pt.ist.vaadinframework.ui.TransactionalForm;
import pt.ist.vaadinframework.ui.TransactionalTable;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;

@SuppressWarnings("serial")
@EmbeddedComponent(path = { "CreateAccount" }, args = {})
public class CreateAccount extends CustomComponent implements EmbeddedComponentContainer {

    @Override
    public boolean isAllowedToOpen(final Map<String, String> arguments) {
        return true;
    }

    @Override
    public void setArguments(final Map<String, String> arguments) {
    }

    @Atomic
    private void createAccount(Form form) {
        String operatorName = (String) form.getItemProperty("operator.name").getValue();
        String operatorNumber = (String) form.getItemProperty("operator.fiscalIdentificationNumber").getValue();
        Operator operator = Operator.getOrCreateOperator(operatorName, operatorNumber);
        String accountNumber = (String) form.getItemProperty("number").getValue();
        String accountDescription = (String) form.getItemProperty("description").getValue();
        Account account = new Account(operator, accountNumber, accountDescription);
    }

    @Override
    public void attach() {
        final ResourceBundle bundle = InternalBillingSystem.getBundle();
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        final TransactionalForm form = new TransactionalForm(InternalBillingSystem.getBundleName());
        form.setCaption(bundle.getString("label.account.create"));
        form.setItemDataSource(new DomainItem<Account>(Account.class));
        form.setVisibleItemProperties(new String[] { "operator.fiscalIdentificationNumber", "operator.name", "number",
                "description" });

        final ValueChangeListener operatorFinListener = new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                Operator operator =
                        Operator.getOperator((String) form.getItemProperty("operator.fiscalIdentificationNumber").getValue());
                if (operator != null) {
                    form.getItemProperty("operator.name").setValue(operator.getName());
                }
            }
        };

        form.getField("operator.fiscalIdentificationNumber").addListener(operatorFinListener);
        form.setImmediate(Boolean.TRUE);
        layout.addComponent(form);
        Button createAccountButton = new Button(bundle.getString("label.create"));
        createAccountButton.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    createAccount(form);
                    Notification notification =
                            new Notification(bundle.getString("label.sucess"), "Conta foi criada com sucesso!",
                                    Notification.TYPE_TRAY_NOTIFICATION);
                    notification.setDelayMsec(500);
                    getWindow().showNotification(notification);
                    EmbeddedApplication.open(getApplication(), ManageAccount.class);
                } catch (DomainException e) {
                    Notification notification =
                            new Notification(bundle.getString("label.error"), bundle.getString(e.getMessage()),
                                    Notification.TYPE_ERROR_MESSAGE);
                    notification.setDelayMsec(500);
                    getWindow().showNotification(notification);
                }
            }

        });

        Button manageAccountsButton = new Button(bundle.getString("label.back"));
        manageAccountsButton.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                EmbeddedApplication.open(getApplication(), ManageAccount.class);
            }
        });

        HorizontalLayout buttonslayout = new HorizontalLayout();
        buttonslayout.setSpacing(true);
        buttonslayout.addComponent(createAccountButton);
        buttonslayout.addComponent(manageAccountsButton);

        layout.addComponent(buttonslayout);

        TransactionalTable table = new TransactionalTable(InternalBillingSystem.getBundleName());
        table.setSizeFull();
        table.setImmediate(true);
        DomainContainer<Account> container =
                new DomainContainer<Account>(InternalBillingSystem.getInstance().getAccountSet(), Account.class);
        container.setContainerProperties("operator.name", "operator.fiscalIdentificationNumber", "number", "description");

        table.setContainerDataSource(container);

        layout.addComponent(table);
        setCompositionRoot(layout);
        super.attach();
    }
}
