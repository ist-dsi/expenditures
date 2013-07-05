package module.internalBilling.presentationTier.pages;

import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import module.fileManagement.presentationTier.component.GenericFileField;
import module.internalBilling.domain.Account;
import module.internalBilling.domain.InternalBillingSystem;
import module.internalBilling.domain.Invoice;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.vaadin.easyuploads.UploadField;

import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.plugins.fileSupport.domain.GenericFile;
import pt.ist.vaadinframework.EmbeddedApplication;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.data.reflect.DomainItem;
import pt.ist.vaadinframework.ui.DefaultFieldFactory;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;
import pt.ist.vaadinframework.ui.TransactionalForm;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;

@SuppressWarnings("serial")
@EmbeddedComponent(path = { "AddInvoice" }, args = { "account" })
public class AddInvoice extends CustomComponent implements EmbeddedComponentContainer {

    private Account account;

    @Override
    public boolean isAllowedToOpen(final Map<String, String> arguments) {
        return true;
    }

    @Override
    public void setArguments(final Map<String, String> arguments) {
        final String accountOID = arguments.get("account");
        if (StringUtils.isBlank(accountOID)) {
            EmbeddedApplication.open(getApplication(), ManageAccount.class);
        }
        account = FenixFramework.getDomainObject(accountOID);
    }

    public static class IntervalField extends CustomField {

        PopupDateField start = new PopupDateField();
        PopupDateField end = new PopupDateField();

        @Override
        public Class<?> getType() {
            return Interval.class;
        }

        public IntervalField() {
            HorizontalLayout hl = new HorizontalLayout();
            hl.setSpacing(true);
            hl.setSizeFull();

            start.setDateFormat("yyyy-MM-dd");
            start.setResolution(DateField.RESOLUTION_DAY);
            end.setResolution(DateField.RESOLUTION_DAY);

            start.setWriteThrough(true);
            end.setWriteThrough(true);

            start.setImmediate(true);
            end.setImmediate(true);

            setImmediate(true);
            setWriteThrough(true);

            hl.addComponent(start);
            hl.addComponent(end);

            setCompositionRoot(hl);
        }

        @Override
        public Object getValue() {
            final Date startDate = (Date) start.getValue();
            final Date endDate = (Date) end.getValue();
            if (startDate == null || endDate == null) {
                return null;
            }
            return new Interval(new DateTime(startDate.getTime()), new DateTime(endDate.getTime()));
        }

        @Override
        public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
            Interval interval = (Interval) newValue;
            if (interval != null) {
                start.setValue(interval.getStart().toDate());
                end.setValue(interval.getEnd().toDate());
            }
        }

        @Override
        protected void setInternalValue(Object newValue) {
            super.setInternalValue(newValue);
            getPropertyDataSource().setValue(newValue);
        }

    }

    private class InvoiceFieldFactory extends DefaultFieldFactory {

        public InvoiceFieldFactory(String bundlename) {
            super(bundlename);
        }

        @Override
        protected Field makeField(Item item, Object propertyId, Component uiContext) {
            final Class<?> type = item.getItemProperty(propertyId).getType();
            if (Interval.class.isAssignableFrom(type)) {
                return new IntervalField();
            }
            if (GenericFile.class.isAssignableFrom(type)) {
                GenericFileField genericFileField = new GenericFileField();
                genericFileField.setImmediate(false);
                return genericFileField;
            }
            return super.makeField(item, propertyId, uiContext);
        }
    }

    @Override
    public void attach() {
        final ResourceBundle bundle = InternalBillingSystem.getBundle();
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        Panel accountInformationPanel = getAccountInformationPanel(bundle);
        layout.addComponent(accountInformationPanel);

        String bundleName = InternalBillingSystem.getBundleName();
        final TransactionalForm form = new TransactionalForm(bundleName);

        form.setFormFieldFactory(new InvoiceFieldFactory(bundleName));
        UploadField field = new UploadField();
        form.setCaption(bundle.getString("label.invoice.add"));
        form.setItemDataSource(new DomainItem<Invoice>(Invoice.class));
        form.setVisibleItemProperties(new String[] { "number", "interval", "invoiceDate", "paymentLimit" });
        form.addField("file", field);
        form.setImmediate(Boolean.TRUE);
        layout.addComponent(form);

        Button addInvoiceButton = new Button(bundle.getString("label.invoice.add"));
        addInvoiceButton.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    createInvoice(form);
                    Notification notification =
                            new Notification(bundle.getString("label.sucess"), "Factura Adicionada com sucesso!",
                                    Notification.TYPE_TRAY_NOTIFICATION);
                    notification.setDelayMsec(500);
                    getWindow().showNotification(notification);
                    EmbeddedApplication.open(getApplication(), ManageAccount.class, account.getExternalId());
                } catch (DomainException e) {
                    Notification notification =
                            new Notification(bundle.getString("label.error"), bundle.getString(e.getMessage()),
                                    Notification.TYPE_ERROR_MESSAGE);
                    notification.setDelayMsec(500);
                    getWindow().showNotification(notification);
                }
            }

            private void createInvoice(TransactionalForm form) {
                Interval interval = (Interval) form.getField("interval").getValue();
            }

        });

        Button manageAccountsButton = new Button(bundle.getString("label.back"));
        manageAccountsButton.addListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                EmbeddedApplication.open(getApplication(), ManageAccount.class, account.getExternalId());
            }
        });

        HorizontalLayout buttonslayout = new HorizontalLayout();
        buttonslayout.setSpacing(true);
        buttonslayout.addComponent(addInvoiceButton);
        buttonslayout.addComponent(manageAccountsButton);

        layout.addComponent(buttonslayout);

        setCompositionRoot(layout);
        super.attach();
    }

    private Panel getAccountInformationPanel(ResourceBundle bundle) {
        Panel accountInformationPanel = new Panel();
        accountInformationPanel.setCaption(bundle.getString("module.internalBilling.domain.Account.presentationName") + ": "
                + account.getNumber());
        accountInformationPanel.removeAllComponents();
        accountInformationPanel.addComponent(new Label(bundle.getString("module.internalBilling.domain.Operator") + ": "
                + account.getOperator().getPresentationName()));
        accountInformationPanel.addComponent(new Label(bundle.getString("module.internalBilling.domain.Account.description")
                + ": " + account.getDescription()));
        return accountInformationPanel;
    }
}
