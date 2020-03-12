package module.internalrequest.ui;

import module.internalrequest.domain.InternalRequestProcess;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ist.expenditureTrackingSystem.util.RedirectToStrutsAction;

import java.util.Optional;

@RequestMapping("/internalrequest/deliver")
@SpringFunctionality(app = InternalRequestController.class, title = "title.internalRequests.confirmDelivery")
public class DeliveryConfirmationController {

    @RequestMapping(method = RequestMethod.GET)
    public String prepareConfirmDelivery(final Model model) throws Exception {
        return "internalrequest/confirmDelivery";
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public String confirmDelivery(@RequestParam(required = true, value = "processNumber") String processNumber,
                                        @RequestParam(required = true, value = "deliveryCode") String deliveryCode,
                                        final Model model) throws Exception {

        Optional<InternalRequestProcess> maybeProcess = InternalRequestProcess.getProcessWithNumber(processNumber);
        if ((!maybeProcess.isPresent()) || (!maybeProcess.get().isAccessibleToCurrentUser())) {
            return "redirect:/internalrequest/deliver";
        }

        return RedirectToStrutsAction.redirect("workflowProcessManagement", "actionLink", "activity",
                "DeliveryConfirmationActivity", "processId", maybeProcess.get().getExternalId(), "parameters", "deliveryCode", "deliveryCode", deliveryCode);
    }

}
