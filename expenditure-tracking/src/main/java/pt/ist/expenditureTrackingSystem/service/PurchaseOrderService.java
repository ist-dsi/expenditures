package pt.ist.expenditureTrackingSystem.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.AddSignedPurchaseOrder;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.AddSignedPurchaseOrderInformation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RefusePurchaseOrderSignature;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RefusePurchaseOrderSignatureInformation;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Ricardo Almeida
 *
 */
public class PurchaseOrderService {

    @Atomic
    public void addSignedDocument(MultipartFile file, AcquisitionProcess process, String requestId) throws IOException {
        final byte[] fileBytes = file.getBytes();
        final RegularAcquisitionProcess regularProcess = (RegularAcquisitionProcess) process;
        final AddSignedPurchaseOrder activity = new AddSignedPurchaseOrder();
        final AddSignedPurchaseOrderInformation information = new AddSignedPurchaseOrderInformation(regularProcess, activity);
        information.setFile(fileBytes);
        information.setRequestId(requestId);
        activity.execute(information);

    }

    @Atomic
    public void refuseDocumentSignature(AcquisitionProcess process, String refuseReason, HttpServletRequest request) {
        if (process instanceof RegularAcquisitionProcess) {
            final RegularAcquisitionProcess regularProcess = (RegularAcquisitionProcess) process;
            final RefusePurchaseOrderSignature refuseActivity = new RefusePurchaseOrderSignature();
            final RefusePurchaseOrderSignatureInformation refuseInfo =
                    new RefusePurchaseOrderSignatureInformation(regularProcess, refuseActivity);
            refuseInfo.setReason(refuseReason);
            refuseActivity.execute(refuseInfo);
        }
    }
}
