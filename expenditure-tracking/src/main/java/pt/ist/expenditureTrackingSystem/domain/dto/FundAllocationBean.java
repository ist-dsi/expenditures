/*
 * @(#)FundAllocationBean.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;

/**
 * 
 * @author Pedro Santos
 * @author Luis Cruz
 * 
 */
public class FundAllocationBean implements Serializable {

    private Financer financer;
    private String fundAllocationId;
    private String effectiveFundAllocationId;
    private boolean allowedToAddNewFund;
    private String diaryNumber;
    private String transactionNumber;

    public FundAllocationBean(final Financer financer) {
        setFinancer(financer);
        setAllowedToAddNewFund(true);
    }

    public void setFinancer(Financer financer) {
        this.financer = financer;
    }

    public Financer getFinancer() {
        return this.financer;
    }

    public String getFundAllocationId() {
        return fundAllocationId;
    }

    public void setFundAllocationId(final String fundAllocationId) {
        this.fundAllocationId = fundAllocationId;
    }

    public void setEffectiveFundAllocationId(String effectiveFundAllocationId) {
        this.effectiveFundAllocationId = effectiveFundAllocationId;
    }

    public String getEffectiveFundAllocationId() {
        return effectiveFundAllocationId;
    }

    public boolean isAllowedToAddNewFund() {
        return allowedToAddNewFund;
    }

    public void setAllowedToAddNewFund(boolean allowedToAddNewFund) {
        this.allowedToAddNewFund = allowedToAddNewFund;
    }

    public String getDiaryNumber() {
        return diaryNumber;
    }

    public void setDiaryNumber(String diaryNumber) {
        this.diaryNumber = diaryNumber;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

}
