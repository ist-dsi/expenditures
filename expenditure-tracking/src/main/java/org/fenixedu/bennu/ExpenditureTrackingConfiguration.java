/**
 * Copyright © 2014 Instituto Superior Técnico
 *
 * This file is part of MGP Viewer.
 *
 * FenixEdu Spaces is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Spaces is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Spaces.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.bennu;

import org.fenixedu.bennu.spring.BennuSpringModule;
import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("pt.ist.expenditureTrackingSystem.ui")
@BennuSpringModule(basePackages = "pt.ist.expenditureTrackingSystem.ui", bundles = "ExpenditureTrackingResources")
public class ExpenditureTrackingConfiguration {

    @ConfigurationManager(description = "Expenditure Tracking Configuration")
    public interface ConfigurationProperties {
    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

}
