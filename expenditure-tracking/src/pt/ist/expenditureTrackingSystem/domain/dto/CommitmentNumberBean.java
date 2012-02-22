package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;

public class CommitmentNumberBean implements Serializable {

    private Financer financer;
    private String commitmentNumber;

    public CommitmentNumberBean(final Financer financer) {
	setFinancer(financer);
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
