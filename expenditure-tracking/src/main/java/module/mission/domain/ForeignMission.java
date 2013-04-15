package module.mission.domain;

import java.util.ResourceBundle;

import module.geography.domain.Country;
import module.mission.domain.activity.UpdateForeignMissionDetailsActivityInformation;
import module.mission.domain.activity.UpdateMissionDetailsActivityInformation;

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.util.ClassNameBundle;
import pt.utl.ist.fenix.tools.util.i18n.Language;

@ClassNameBundle(bundle = "resources/MissionResources")
public class ForeignMission extends ForeignMission_Base {

    public ForeignMission(final ForeignMissionProcess foreignMissionProcess, final Country country, final String location,
            final DateTime daparture, final DateTime arrival, final String objective,
            final Boolean useRequestingUnitAsPayingUnit, final Boolean grantOwnerEquivalence) {
        setMissionProcess(foreignMissionProcess);
        setCountry(country);
        setMissionInformation(location, daparture, arrival, objective, useRequestingUnitAsPayingUnit, grantOwnerEquivalence);
    }

    @Override
    public void fill(final UpdateMissionDetailsActivityInformation updateMissionDetailsActivityInformation) {
        super.fill(updateMissionDetailsActivityInformation);
        final UpdateForeignMissionDetailsActivityInformation updateForeignMissionDetailsActivityInformation =
                (UpdateForeignMissionDetailsActivityInformation) updateMissionDetailsActivityInformation;
        updateForeignMissionDetailsActivityInformation.setCountry(getCountry());
    }

    @Override
    public void updateDetails(final UpdateMissionDetailsActivityInformation updateMissionDetailsActivityInformation) {
        super.updateDetails(updateMissionDetailsActivityInformation);
        final UpdateForeignMissionDetailsActivityInformation updateForeignMissionDetailsActivityInformation =
                (UpdateForeignMissionDetailsActivityInformation) updateMissionDetailsActivityInformation;
        final Country country = updateForeignMissionDetailsActivityInformation.getCountry();
        setCountry(country);
    }

    @Override
    public void setCountry(final Country country) {
        if (country == null) {
            throw new DomainException("error.mission.country.none", ResourceBundle.getBundle("resources/MissionResources",
                    Language.getLocale()));
        }
        if (country == MissionSystem.getInstance().getCountry()) {
            throw new DomainException("mission.process.exception.foreign.mission.not.allowed.inside.national.country",
                    ResourceBundle.getBundle("resources/MissionResources", Language.getLocale()));
        }
        super.setCountry(country);
    }

    @Override
    public String getDestinationDescription() {
        final Country country = getCountry();
        return country == null ? getLocation() : getLocation() + ", " + country.getName().getContent();
    }

    @Deprecated
    public boolean hasCountry() {
        return getCountry() != null;
    }

}
