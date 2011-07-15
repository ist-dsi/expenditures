package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;

public class PaymentReferenceBean implements Serializable {

    private Financer financer;
    private String diaryNumber;

    public PaymentReferenceBean(final Financer financer) {
	setFinancer(financer);
    }

    public void setFinancer(Financer financer) {
	this.financer = financer;
    }

    public Financer getFinancer() {
	return this.financer;
    }

    public String getDiaryNumber() {
        return diaryNumber;
    }

    public void setDiaryNumber(String diaryNumber) {
        this.diaryNumber = diaryNumber;
    }

}
