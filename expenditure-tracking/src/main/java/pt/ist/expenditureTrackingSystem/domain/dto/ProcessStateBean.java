package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

public class ProcessStateBean implements Serializable {

	private String justification;

	public ProcessStateBean() {
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}
}
