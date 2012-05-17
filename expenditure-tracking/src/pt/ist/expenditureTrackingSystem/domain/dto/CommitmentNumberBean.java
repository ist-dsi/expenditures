/*
 * @(#)CommitmentNumberBean.java
 *
 * Copyright 2012 Instituto Superior Tecnico
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
 * @author Luis Cruz
 * 
 */
public class CommitmentNumberBean implements Serializable {

    private Financer financer;
    private String commitmentNumber;

    public CommitmentNumberBean(final Financer financer) {
	setFinancer(financer);
	if (financer != null) {
	    commitmentNumber = financer.getCommitmentNumber();
	}
    }

    public void setFinancer(final Financer financer) {
	this.financer = financer;
    }

    public Financer getFinancer() {
	return this.financer;
    }

    public String getCommitmentNumber() {
        return commitmentNumber;
    }

    public void setCommitmentNumber(String commitmentNumber) {
        this.commitmentNumber = commitmentNumber;
    }

}
