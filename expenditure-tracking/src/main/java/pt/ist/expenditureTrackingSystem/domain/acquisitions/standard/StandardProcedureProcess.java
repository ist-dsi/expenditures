/*
 * @(#)StandardProcedureProcess.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.domain.acquisitions.standard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.finance.util.Money;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionItemClassification;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class StandardProcedureProcess extends StandardProcedureProcess_Base {

    private static Money PROCESS_VALUE_LIMIT = new Money("75000");

    public StandardProcedureProcess() {
        super();
    }

    protected StandardProcedureProcess(final Person requester) {
        super();
        inGenesis();
        new AcquisitionRequest(this, requester);
    }

    protected StandardProcedureProcess(List<Supplier> suppliers, Person person) {
        super();
        inGenesis();
        new AcquisitionRequest(this, suppliers, person);
    }

    @Atomic
    public static StandardProcedureProcess createNewAcquisitionProcess(
            final CreateAcquisitionProcessBean createAcquisitionProcessBean) {
        if (!isCreateNewProcessAvailable()) {
            throw new DomainException(Bundle.EXPENDITURE, "acquisitionProcess.message.exception.invalidStateToRun.create");
        }
        StandardProcedureProcess process =
                new StandardProcedureProcess(createAcquisitionProcessBean.getSuppliers(),
                        createAcquisitionProcessBean.getRequester());
        process.getAcquisitionRequest().setRequestingUnit(createAcquisitionProcessBean.getRequestingUnit());
        if (createAcquisitionProcessBean.isRequestUnitPayingUnit()) {
            final Unit unit = createAcquisitionProcessBean.getRequestingUnit();
            process.getAcquisitionRequest().addFinancers(unit.finance(process.getAcquisitionRequest()));
        }

        return process;
    }

    @Override
    public Money getAcquisitionRequestValueLimit() {
        return PROCESS_VALUE_LIMIT;
    }

    @Override
    public List<AcquisitionProcessStateType> getAvailableStates() {
        List<AcquisitionProcessStateType> availableStates = new ArrayList<AcquisitionProcessStateType>();
        availableStates.add(AcquisitionProcessStateType.IN_GENESIS);
        availableStates.add(AcquisitionProcessStateType.SUBMITTED_FOR_APPROVAL);
        availableStates.add(AcquisitionProcessStateType.SUBMITTED_FOR_FUNDS_ALLOCATION);
        availableStates.add(AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER);
        availableStates.add(AcquisitionProcessStateType.FUNDS_ALLOCATED);
        availableStates.add(AcquisitionProcessStateType.AUTHORIZED);
        availableStates.add(AcquisitionProcessStateType.INVITES_SENT);
        availableStates.add(AcquisitionProcessStateType.IN_NEGOTIATION);
        availableStates.add(AcquisitionProcessStateType.NEGOTIATION_ENDED);
        availableStates.add(AcquisitionProcessStateType.SELECTED_SERVICE_PROVIDER);
        availableStates.add(AcquisitionProcessStateType.DOCUMENTATION_INSERTED);
        availableStates.add(AcquisitionProcessStateType.ACQUISITION_PROCESSED);
        availableStates.add(AcquisitionProcessStateType.INVOICE_RECEIVED);
        availableStates.add(AcquisitionProcessStateType.SUBMITTED_FOR_CONFIRM_INVOICE);
        availableStates.add(AcquisitionProcessStateType.INVOICE_CONFIRMED);
        availableStates.add(AcquisitionProcessStateType.FUNDS_ALLOCATED_PERMANENTLY);
        availableStates.add(AcquisitionProcessStateType.ACQUISITION_PAYED);
        availableStates.add(AcquisitionProcessStateType.REJECTED);
        availableStates.add(AcquisitionProcessStateType.CANCELED);
        return availableStates;
    }

    @Override
    public boolean isAppiableForYear(final int year) {
        throw new Error("not.implemented");
    }

    /*
     * TODO: Implement this methods correctly
     */
    @Override
    public <T extends WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation>> List<T> getActivities() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString("resources/AcquisitionResources", "label.StandardProcedureProcess");
    }

    @Override
    public AcquisitionItemClassification getGoodsOrServiceClassification() {
        return null;
    }

}
