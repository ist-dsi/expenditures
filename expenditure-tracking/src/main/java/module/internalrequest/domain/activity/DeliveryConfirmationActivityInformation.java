package module.internalrequest.domain.activity;

import java.io.Serializable;

import com.google.common.base.Strings;

import module.internalrequest.domain.InternalRequestProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public class DeliveryConfirmationActivityInformation extends ActivityInformation<InternalRequestProcess> implements Serializable {

    private String deliveryCode;


    public DeliveryConfirmationActivityInformation(final InternalRequestProcess internalRequestProcess,
            final WorkflowActivity<InternalRequestProcess, ? extends ActivityInformation<InternalRequestProcess>> activity) {
        super(internalRequestProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
        return !Strings.isNullOrEmpty(this.deliveryCode);
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }


}
