package pt.ist.expenditureTrackingSystem.presentationTier.renderers;

public interface PresentableAcquisitionProcessState {

    public boolean showFor(PresentableAcquisitionProcessState state);

    public String getLocalizedName();

    public String getDescription();

}
