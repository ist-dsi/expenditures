package module.internalrequest.task;

import module.internalrequest.domain.InternalRequestSystem;
import module.organization.domain.AccountabilityType;
import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.commons.i18n.LocalizedString;

import java.util.Locale;

public class SetupInternalRequestsModule extends CustomTask {

    private static final String DELIVERY_ACCOUNTABILITY_TYPE = "OrganizationalInternalRequestDelivery";
    private static final String DELIVERY_ACCOUNTABILITY_NAME_PT = "Organizacional de Entregas de Requisições Internas";
    private static final String DELIVERY_ACCOUNTABILITY_NAME_EN = "Organizational for Internal Request Delivery";

    @Override
    public void runTask() throws Exception {
        if(AccountabilityType.readBy(DELIVERY_ACCOUNTABILITY_TYPE) == null) {
            final LocalizedString name =
                    new LocalizedString.Builder().with(Locale.forLanguageTag("pt-PT"), DELIVERY_ACCOUNTABILITY_NAME_PT).with(Locale.forLanguageTag("en-GB"), DELIVERY_ACCOUNTABILITY_NAME_EN).build();
            final AccountabilityType.AccountabilityTypeBean deliveryTypeBean = new AccountabilityType.AccountabilityTypeBean();
            deliveryTypeBean.setName(name);
            deliveryTypeBean.setType(DELIVERY_ACCOUNTABILITY_TYPE);

            final AccountabilityType deliveryType = AccountabilityType.create(deliveryTypeBean);

            InternalRequestSystem.getInstance().setOrganizationalInternalRequestDeliveryAccountabilityType(deliveryType);
        }

        InternalRequestSystem.getInstance().reset();
    }

}
