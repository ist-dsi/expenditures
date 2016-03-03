/**
 * Copyright © 2015 Instituto Superior Técnico
 *
 * This file is part of Applications and Admissions Module.
 *
 * Applications and Admissions Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applications and Admissions Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Spaces.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ist.internalBilling.ui;

import java.util.Set;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixframework.DomainObject;
import pt.ist.internalBilling.domain.Billable;

@SpringApplication(group = "logged", path = "internalBilling", title = "title.internalBilling",
        hint = "internal-billing")
@SpringFunctionality(app = InternalBillingController.class, title = "title.internalBilling")
@RequestMapping("/internalBilling")
public class InternalBillingController {

    @RequestMapping(method = RequestMethod.GET)
    public String home(final Model model) {
        final Set<Billable> pendingAuthorization = Billable.pendingAuthorization();
        model.addAttribute("pendingAuthorization", pendingAuthorization);
        return "internalBilling/home";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(final Model model, @RequestParam DomainObject unitOrUser) {
        final String pathPart = unitOrUser instanceof User ? "user" : "unit";
        return "redirect:/internalBilling/" + pathPart + "/" + unitOrUser.getExternalId();
    }

    @RequestMapping(value = "/user/{user}", method = RequestMethod.GET)
    public String user(final Model model, @PathVariable final User user) {
        model.addAttribute("user", user);
        return "internalBilling/user";
    }

    @RequestMapping(value = "/unit/{unit}", method = RequestMethod.GET)
    public String unit(final Model model, @PathVariable final Unit unit) {
        model.addAttribute("unit", unit);
        return "internalBilling/unit";
    }

    @RequestMapping(value = "/billable/{billable}/authorize", method = RequestMethod.POST)
    public String authorize(final Model model, @PathVariable Billable billable) {
        if (billable.getUnit().isCurrentUserResponsibleForUnit()) {
            billable.authorize();
        }
        return "redirect:/internalBilling/unit/" + billable.getUnit().getExternalId();
    }

    @RequestMapping(value = "/billable/{billable}/revoke", method = RequestMethod.POST)
    public String revoke(final Model model, @PathVariable Billable billable) {
        if (billable.getUnit().isCurrentUserResponsibleForUnit()) {
            billable.revoke();
        }
        return "redirect:/internalBilling/unit/" + billable.getUnit().getExternalId();
    }

    @RequestMapping(value = "/unit/{unit}/authorizeAll", method = RequestMethod.POST)
    public String authorize(final Model model, @PathVariable final Unit unit, @RequestParam Billable[] billable) {
        for (final Billable b : billable) {
            if (b.getUnit().isCurrentUserResponsibleForUnit()) {
                b.authorize();
            }
        }
        return "redirect:/internalBilling/unit/" + unit.getExternalId();
    }

}
