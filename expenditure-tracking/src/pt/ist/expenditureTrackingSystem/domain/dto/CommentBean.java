/*
 * @(#)CommentBean.java
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
package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class CommentBean implements Serializable {

    private GenericProcess process;
    private String comment;
    private List<Person> peopleToNotify;

    public CommentBean(GenericProcess process) {
	this.process = process;
	this.peopleToNotify = Collections.emptyList();
    }

    public GenericProcess getProcess() {
	return process;
    }

    public void setProcess(GenericProcess process) {
	this.process = process;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public List<Person> getPeopleToNotify() {
	return peopleToNotify;
    }

    public void setPeopleToNotify(List<Person> peopleToNotify) {
	this.peopleToNotify = peopleToNotify;
    }

}
