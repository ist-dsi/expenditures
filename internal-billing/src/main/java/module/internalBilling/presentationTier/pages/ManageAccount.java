package module.internalBilling.presentationTier.pages;

import java.util.Map;
import java.util.ResourceBundle;

import module.internalBilling.domain.Account;
import module.internalBilling.domain.InternalBillingSystem;
import module.internalBilling.domain.Invoice;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.vaadinframework.EmbeddedApplication;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.data.reflect.DomainContainer;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;
import pt.ist.vaadinframework.ui.GridSystemLayout;
import pt.ist.vaadinframework.ui.TransactionalTable;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

@SuppressWarnings("serial")
@EmbeddedComponent(path = { "ManageAccount" }, args = { "account" })
public class ManageAccount extends CustomComponent implements EmbeddedComponentContainer {

    private Panel accountInformationPanel;

    private Panel warningsPanel;

    private Panel invoicesPanel;

    private final ResourceBundle bundle;

    //   private Account account;

    public ManageAccount() {
        bundle = InternalBillingSystem.getBundle();
        GridSystemLayout gsl = new GridSystemLayout();

        setCompositionRoot(gsl);

        HorizontalLayout selectAccountLayout = getSelectAccountLayout();

        accountInformationPanel = new Panel();
        accountInformationPanel.setVisible(false);
        warningsPanel = new Panel();
        warningsPanel.setVisible(false);

        invoicesPanel = new Panel();
        ((VerticalLayout) invoicesPanel.getContent()).setSpacing(true);
        invoicesPanel.setVisible(false);

        gsl.setCell("selectAccountLayout", 16, selectAccountLayout);
        gsl.setCell("accountInformationPanel", 8, accountInformationPanel);
        gsl.setCell("warningsPanel", 8, warningsPanel);
        gsl.setCell("invoicesPanel", 16, invoicesPanel);
    }

    @Override
    public boolean isAllowedToOpen(final Map<String, String> arguments) {
        return true;
    }

    @Override
    public void setArguments(final Map<String, String> arguments) {
        final String accountOID = arguments.get("account");
        if (!StringUtils.isBlank(accountOID)) {
            setPanels((Account) FenixFramework.getDomainObject(accountOID));
        }
    }

    private void setPanels(final Account account) {
        if (account != null) {
            accountInformationPanel.setCaption(bundle.getString("module.internalBilling.domain.Account.presentationName") + ": "
                    + account.getNumber());
            accountInformationPanel.removeAllComponents();
            accountInformationPanel.addComponent(new Label(bundle.getString("module.internalBilling.domain.Operator") + ": "
                    + account.getOperator().getPresentationName()));
            accountInformationPanel.addComponent(new Label(bundle.getString("module.internalBilling.domain.Account.description")
                    + ": " + account.getDescription()));

            warningsPanel.setCaption(bundle.getString("label.warnings"));
            warningsPanel.removeAllComponents();
            warningsPanel.addComponent(new Label("Contestações pendentes: 0"));
            warningsPanel.addComponent(new Label("Pendente de verificação: 0"));
            warningsPanel.addComponent(new Label("Tem uma factura por pagar nos próximos 15 dias"));

            invoicesPanel.setCaption(bundle.getString("label.invoices"));
            invoicesPanel.removeAllComponents();

            Button addInvoice = new Button(bundle.getString("label.invoice.add"), new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    EmbeddedApplication.open(getApplication(), AddInvoice.class, account.getExternalId());
                }
            });
            addInvoice.setStyleName(BaseTheme.BUTTON_LINK);

            invoicesPanel.addComponent(addInvoice);

            HorizontalLayout selectInvoicesDatesLayout = new HorizontalLayout();

            DateTime today = new LocalDate().toDateTimeAtStartOfDay();
            PopupDateField beginDate = new PopupDateField("", today.minusMonths(1).toDate());
            beginDate.setDateFormat("yyyy-MM-dd");
            beginDate.setResolution(DateField.RESOLUTION_DAY);
            PopupDateField endDate = new PopupDateField("", today.toDate());
            endDate.setDateFormat("yyyy-MM-dd");
            endDate.setResolution(DateField.RESOLUTION_DAY);
            selectInvoicesDatesLayout.addComponent(beginDate);
            selectInvoicesDatesLayout.addComponent(endDate);
            Button chooseDates = new Button("Pesquisar", new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    EmbeddedApplication.open(getApplication(), CreateAccount.class);
                }
            });
            selectInvoicesDatesLayout.addComponent(chooseDates);

            invoicesPanel.addComponent(selectInvoicesDatesLayout);

            TransactionalTable table = new TransactionalTable(InternalBillingSystem.getBundleName());
            table.setSizeFull();
            table.setImmediate(true);
            DomainContainer<Invoice> container = new DomainContainer<Invoice>(account.getInvoiceSet(), Invoice.class);
            container.setContainerProperties("number", "invoiceDate", "paymentLimit");

            table.setContainerDataSource(container);
            invoicesPanel.addComponent(table);

            accountInformationPanel.setVisible(true);
            warningsPanel.setVisible(true);
            invoicesPanel.setVisible(true);
        } else {
            accountInformationPanel.setVisible(false);
            warningsPanel.setVisible(false);
            invoicesPanel.setVisible(false);
        }
    }

    private HorizontalLayout getSelectAccountLayout() {
        HorizontalLayout selectAccountLayout = new HorizontalLayout();
        selectAccountLayout.addComponent(new Label("Conta: "));
        final Select accountSelector = new Select();
        accountSelector.setImmediate(Boolean.TRUE);
        DomainContainer<Account> container =
                new DomainContainer<Account>(InternalBillingSystem.getInstance().getAccountSet(), Account.class);
        accountSelector.setContainerDataSource(container);
        accountSelector.setItemCaptionPropertyId("presentationName");
        final ValueChangeListener accountListener = new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                Account account = (Account) event.getProperty().getValue();
                setPanels(account);
            }
        };
        accountSelector.addListener(accountListener);

        selectAccountLayout.addComponent(accountSelector);

        Button createAccount = new Button("Criar Conta", new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                EmbeddedApplication.open(getApplication(), CreateAccount.class);
            }
        });
        createAccount.setStyleName(BaseTheme.BUTTON_LINK);
        selectAccountLayout.addComponent(createAccount);
        return selectAccountLayout;
    }
}
