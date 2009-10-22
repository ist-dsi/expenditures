package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

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
