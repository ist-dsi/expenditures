/*
 * @(#)ListMissionsJune2011.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package module.mission.domain.util;

import java.util.Map;
import java.util.TreeSet;

import module.mission.domain.Mission;
import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;

import org.apache.commons.collections.ComparatorUtils;
import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.scheduler.ReadCustomTask;
import pt.ist.bennu.core.util.BundleUtil;

/**
 * 
 * @author João Neves
 * 
 */
public class ListMissionsJune2011 extends ReadCustomTask {

    @Override
    public void doIt() {
        final DateTime limitDate = new DateTime(2011, 7, 1, 0, 0, 0, 0);
        out.print("N Processo \t Data de Criacao \t"
                + "Aprovacao do Processo \t Cabimentacao \t Autorizacao das Deslocacoes \t Autorizacao da Despesa \t Processamento pela DRH \t Arquivado \t"
                + "Designacao \t Equiparacao Bolseiro? \t Projecto/Unidade Pagadora \t Valor \t Duracao (dias) \n");

        TreeSet<Mission> missions =
                new TreeSet<Mission>(ComparatorUtils.reversedComparator(Mission.COMPARATOR_BY_PROCESS_IDENTIFICATION));
        missions.addAll(MissionSystem.getInstance().getMissions());
        for (final Mission mission : missions) {
            MissionProcess process = mission.getMissionProcess();
            if ((process.isCanceled()) || (process.getCreationDate().isAfter(limitDate))) {
                continue;
            }

            out.print(process.getProcessNumber() + "\t");
            out.print(process.getCreationDate().getYear() + "/" + process.getCreationDate().getMonthOfYear() + "/"
                    + process.getCreationDate().getDayOfMonth() + "\t");

            Map<MissionState, MissionStateProgress> states = process.getMissionStateView().getMissionStateProgress();

            out.print(states.get(MissionState.PROCESS_APPROVAL).getLocalizedName());
            out.print("\t");

            if (states.get(MissionState.FUND_ALLOCATION) != null) {
                out.print(states.get(MissionState.FUND_ALLOCATION).getLocalizedName());
            } else {
                out.print("N/A");
            }
            out.print("\t");

            out.print(states.get(MissionState.PARTICIPATION_AUTHORIZATION).getLocalizedName());
            out.print("\t");

            if (states.get(MissionState.EXPENSE_AUTHORIZATION) != null) {
                out.print(states.get(MissionState.EXPENSE_AUTHORIZATION).getLocalizedName());
            } else {
                out.print("N/A");
            }
            out.print("\t");

            out.print(states.get(MissionState.PERSONEL_INFORMATION_PROCESSING).getLocalizedName());
            out.print("\t");

            out.print(states.get(MissionState.ARCHIVED).getLocalizedName());
            out.print("\t");

            out.print(BundleUtil.getStringFromResourceBundle("resources/MissionResources", "label."
                    + mission.getClass().getName())
                    + "\t");
            out.print((mission.getGrantOwnerEquivalence() ? "Sim" : "Nao") + "\t");

            if (!mission.hasAnyFinancer()) {
                out.print("N/A");
            } else {
                boolean first = true;
                for (MissionFinancer financer : mission.getFinancerSet()) {
                    if (first) {
                        first = false;
                    } else {
                        out.print(" & ");
                    }
                    out.print(financer.getUnit().getPresentationName());
                }
            }
            out.print("\t");

            out.print(mission.getValue().getValue() + " Eur\t");
            out.print(mission.getDurationInDays() + "\n");
        }
    }
}
