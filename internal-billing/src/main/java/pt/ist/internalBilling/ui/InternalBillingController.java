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
import java.util.stream.Collectors;

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
import pt.ist.internalBilling.domain.BillableTransaction;

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
        return unitOrUser == null ? home(model) : "redirect:/internalBilling/" + pathPart + "/" + unitOrUser.getExternalId();
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

    @RequestMapping(value = "/unit/{unit}/transactions/{yearMonth}", method = RequestMethod.GET)
    public String unit(final Model model, @PathVariable final Unit unit, @PathVariable final String yearMonth) {
        model.addAttribute("unit", unit);
        final int year = Integer.parseInt(yearMonth.substring(0,  4));
        final int month = Integer.parseInt(yearMonth.substring(5,  7));
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("transactions", unit.getBillableSet().stream()
                .flatMap(b -> b.getBillableTransactionSet().stream())
                .filter(tx -> tx.getTxDate().getYear() == year && tx.getTxDate().getMonthOfYear() == month)
                .sorted(BillableTransaction.COMPARATOR_BY_DATE.reversed())
                .collect(Collectors.toList()));
        return "internalBilling/unitTransactions";
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
