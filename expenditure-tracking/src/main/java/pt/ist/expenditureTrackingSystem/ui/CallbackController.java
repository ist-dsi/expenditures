package pt.ist.expenditureTrackingSystem.ui;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.fenixedu.bennu.SmartsiignerSdkConfiguration;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.Jwts;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.service.PurchaseOrderService;

/**
 * 
 * @author Ricardo Almeida
 *
 */
@RestController("MissionsCallbackController")
@RequestMapping("/mission")
public class CallbackController {

    @Autowired
    private HttpServletRequest request;

    final private PurchaseOrderService purchaseOrderService;

    public CallbackController() {
        this.purchaseOrderService = new PurchaseOrderService();
    }

    @SkipCSRF
    @RequestMapping(value = "/{process}/sign", method = RequestMethod.POST)
    public Response addSignedPurchaseOrderDocument(@PathVariable("process") AcquisitionProcess process,
            @QueryParam("nounce") String nounce, @RequestParam(required = false) MultipartFile file,
            @RequestParam("username") String signerUsername, @RequestParam(value = "", required = false) String refuseReason) {

        final String uuid = Jwts.parser().setSigningKey(SmartsiignerSdkConfiguration.getConfiguration().jwtSecret().getBytes())
                .parseClaimsJws(nounce).getBody().getSubject();
        final String requestDocumentId = process.getAcquisitionRequestDocumentID();
        try {
            // If uuid does not match, it is referring to an old document that was replaced
            if (process.getPurchaseOrderDocument() == null || !process.getPurchaseOrderDocument().getUuid().equals(uuid)) {
                return Response.ok().build();
            }

            Authenticate.mock(User.findByUsername(signerUsername), "System Automation (SmartSigner)");

            // If no file is received it means the signing request was refused
            if (file == null || file.isEmpty()) {
                purchaseOrderService.refuseDocumentSignature(process, refuseReason, request);
            } else {
                purchaseOrderService.addSignedDocument(file, process, requestDocumentId);
            }

        } catch (final IOException e) {
            return Response.serverError().build();
        } finally {
            Authenticate.unmock();
        }
        return Response.ok().build();
    }
}