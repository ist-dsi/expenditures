/*
 * @(#)PersistHelp.java
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
package module.mission;

import module.contents.domain.Container;
import module.contents.domain.Page;
import module.contents.domain.Section;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.domain.contents.Node;
import pt.ist.bennu.core.domain.groups.UserGroup;
import pt.ist.bennu.core.domain.scheduler.WriteCustomTask;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.bennu.vaadin.domain.contents.VaadinNode;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class PersistHelp extends WriteCustomTask {

    @Override
    protected void doService() {
        final VirtualHost virtualHost = AbstractDomainObject.fromExternalId("395136991834");
        //final Node node = AbstractDomainObject.fromExternalId("335007455097");
        final Node node = AbstractDomainObject.fromExternalId("335007455097");

        final Page page = Page.createNewPage();

//	VaadinNode.createVaadinNode(virtualHost, node, "resources.MissionResources", "link.sideBar.help", "SectionedPage-" + page.getExternalId(), UserGroup.getInstance());
        VaadinNode.createVaadinNode(virtualHost, node, "resources.MissionResources", "link.sideBar.help", "SectionedPageViewer-"
                + page.getExternalId(), UserGroup.getInstance());

        writePage(page);

        out.println("Done.");
    }

    private void writePage(final Page page) {
        final MultiLanguageString title = read("text.help.title.index");
        page.setTitle(title);

        final Section introduction = add(page, "text.help.title.introduction");
        set(introduction, "text.help.content.introduction.paragraph1", "text.help.content.introduction.paragraph2",
                "text.help.content.introduction.paragraph3", "text.help.content.introduction.paragraph4");

        final Section processSummary = add(page, "text.help.title.processSummary");
        final StringBuilder processSummaryBody = new StringBuilder();
        addP(processSummaryBody, "text.help.content.processSummary.paragraph1");
        processSummaryBody.append("<blockquote>");
        {
            addH3(processSummaryBody, "text.help.content.processSummary.stage1.title");
            addH3(processSummaryBody, "text.help.content.processSummary.stage2.title");
            addP(processSummaryBody, "text.help.content.processSummary.stage2.content");
            addH3(processSummaryBody, "text.help.content.processSummary.stage3.title");
            processSummaryBody.append("<p>");
            {
                add(processSummaryBody, "text.help.content.processSummary.stage3.paragraph1");
                processSummaryBody.append("<blockquote>");
                {
                    processSummaryBody.append("<ul>");
                    {
                        addLI(processSummaryBody, "text.help.content.processSummary.stage3.paragraph1.subList1");
                        addLI(processSummaryBody, "text.help.content.processSummary.stage3.paragraph1.subList2");
                    }
                    processSummaryBody.append("</ul>");
                }
                processSummaryBody.append("</blockquote>");
            }
            processSummaryBody.append("</p>");
            addH3(processSummaryBody, "text.help.content.processSummary.stage4.title");
            processSummaryBody.append("<p>");
            {
                add(processSummaryBody, "text.help.content.processSummary.stage4.paragraph1");
                processSummaryBody.append("<blockquote>");
                {
                    processSummaryBody.append("<ul>");
                    {
                        addLI(processSummaryBody, "text.help.content.processSummary.stage4.paragraph1.subList1");
                        addLI(processSummaryBody, "text.help.content.processSummary.stage4.paragraph1.subList2");
                    }
                    processSummaryBody.append("</ul>");
                }
                processSummaryBody.append("</blockquote>");
            }
            processSummaryBody.append("</p>");
            addH3(processSummaryBody, "text.help.content.processSummary.stage5.title");
            addP(processSummaryBody, "text.help.content.processSummary.stage5.paragraph1");
            addP(processSummaryBody, "text.help.content.processSummary.stage5.paragraph2");
            addH3(processSummaryBody, "text.help.content.processSummary.stage6.title");
            addP(processSummaryBody, "text.help.content.processSummary.stage6.paragraph1");
            addP(processSummaryBody, "text.help.content.processSummary.stage6.paragraph2");
        }
        processSummaryBody.append("</blockquote>");
        addP(processSummaryBody, "text.help.content.processSummary.paragraph2");
        setCustome(processSummary, processSummaryBody.toString());

        final Section create = add(page, "text.help.title.createMissionProcess");
        set(create, "text.help.title.createMissionProcess.paragraph1", "text.help.title.createMissionProcess.paragraph2",
                "text.help.title.createMissionProcess.paragraph3", "text.help.title.createMissionProcess.paragraph4",
                "text.help.title.createMissionProcess.paragraph5", "text.help.title.createMissionProcess.paragraph6",
                "text.help.title.createMissionProcess.paragraph7", "text.help.title.createMissionProcess.paragraph8");
        setImage(create, "/images/mission/help/Screen01_1.png");
        final Section create1 = add(create, "text.help.title.createMissionProcess.cancelProcess");
        set(create1, "text.help.content.createMissionProcessCancelProcess.paragraph1",
                "text.help.content.createMissionProcessCancelProcess.paragraph2",
                "text.help.content.createMissionProcessCancelProcess.paragraph3");

        final Section manageProcess = add(page, "text.help.title.manageMissionProcess");
        set(manageProcess, "text.help.content.manageMissionProcess.paragraph1");
        final Section manageProcess1 = add(manageProcess, "text.help.title.manageMissionProcess.payingUnits");
        set(manageProcess1, "text.help.content.manageMissionProcess.payingUnits");
        final Section manageProcess11 = add(manageProcess1, "text.help.title.manageMissionProcess.payingUnits.insert");
        set(manageProcess11, "text.help.content.manageMissionProcess.payingUnits.insert.paragraph1",
                "text.help.content.manageMissionProcess.payingUnits.insert.paragraph2",
                "text.help.content.manageMissionProcess.payingUnits.insert.paragraph3",
                "text.help.content.manageMissionProcess.payingUnits.insert.paragraph4");
        final Section manageProcess12 = add(manageProcess1, "text.help.title.manageMissionProcess.payingUnits.remove");
        set(manageProcess12, "text.help.content.manageMissionProcess.payingUnits.remove.paragraph1",
                "text.help.content.manageMissionProcess.payingUnits.remove.paragraph2",
                "text.help.content.manageMissionProcess.payingUnits.remove.paragraph3");
        final Section manageProcess2 = add(manageProcess, "text.help.title.manageMissionProcess.manageParticipantes");
        set(manageProcess2, "text.help.content.manageMissionProcess.manageParticipantes");
        final Section manageProcess21 = add(manageProcess2, "text.help.title.manageMissionProcess.manageParticipantes.add");
        set(manageProcess21, "text.help.content.manageMissionProcess.manageParticipantes.add.paragraph1",
                "text.help.content.manageMissionProcess.manageParticipantes.add.paragraph2",
                "text.help.content.manageMissionProcess.manageParticipantes.add.paragraph3",
                "text.help.content.manageMissionProcess.manageParticipantes.add.paragraph4",
                "text.help.content.manageMissionProcess.manageParticipantes.add.paragraph5");
        final Section manageProcess22 = add(manageProcess2, "text.help.title.manageMissionProcess.manageParticipantes.remove");
        set(manageProcess22, "text.help.content.manageMissionProcess.manageParticipantes.remove.paragraph1",
                "text.help.content.manageMissionProcess.manageParticipantes.remove.paragraph2");

        final Section manageItems = add(page, "text.help.title.manageItems");
        set(manageItems, "text.help.content.manageItems.paragraph1");
        final Section manageItems1 = add(manageItems, "text.help.title.manageItems.add");
        set(manageItems1, "text.help.content.manageItems.add.paragraph1", "text.help.content.manageItems.add.paragraph2",
                "text.help.content.manageItems.add.paragraph3", "text.help.content.manageItems.add.paragraph4",
                "text.help.content.manageItems.add.paragraph5");
        final Section manageItems2 = add(manageItems, "text.help.title.manageItems.edit");
        set(manageItems2, "text.help.content.manageItems.edit.paragraph1", "text.help.content.manageItems.edit.paragraph2");
        final Section manageItems3 = add(manageItems, "text.help.title.manageItems.distributeCosts");
        set(manageItems3, "text.help.content.manageItems.distributeCosts.paragraph1",
                "text.help.content.manageItems.distributeCosts.paragraph2",
                "text.help.content.manageItems.distributeCosts.paragraph3",
                "text.help.content.manageItems.distributeCosts.paragraph4",
                "text.help.content.manageItems.distributeCosts.paragraph5");
        final Section manageItems4 = add(manageItems, "text.help.title.manageItems.remove");
        set(manageItems4, "text.help.content.manageItems.remove");

        final Section submitForApproval = add(page, "text.help.title.submitForApproval");
        set(submitForApproval, "text.help.content.submitForApproval.paragraph1",
                "text.help.content.submitForApproval.paragraph2", "text.help.content.submitForApproval.paragraph3",
                "text.help.content.submitForApproval.paragraph4");
        setImage(submitForApproval, "/images/mission/help/Screen02_1.png");
        set(submitForApproval, "text.help.content.submitForApproval.paragraph5");

        final Section processApproval = add(page, "text.help.title.processApproval");
        set(processApproval, "text.help.content.processApproval.withoutCosts.paragraph1",
                "text.help.content.processApproval.withoutCosts.paragraph2",
                "text.help.content.processApproval.withoutCosts.paragraph3",
                "text.help.content.processApproval.withoutCosts.paragraph4",
                "text.help.content.processApproval.withoutCosts.paragraph5",
                "text.help.content.processApproval.withoutCosts.paragraph6",
                "text.help.content.processApproval.withoutCosts.paragraph7",
                "text.help.content.processApproval.withoutCosts.paragraph8",
                "text.help.content.processApproval.withoutCosts.paragraph9");
        setImage(submitForApproval, "/images/mission/help/Screen03_1.png");

        final Section fundAllocationAndParticipantAuthorization =
                add(page, "text.help.title.fundAllocationAndDislocationAuthorization");
        set(fundAllocationAndParticipantAuthorization, "text.help.content.fundAllocationAndDislocationAuthorization.paragraph1",
                "text.help.content.fundAllocationAndDislocationAuthorization.paragraph2");
        final Section fundAllocation = add(fundAllocationAndParticipantAuthorization, "text.help.title.fundAllocation");
        set(fundAllocation, "text.help.content.fundAllocation.paragraph1", "text.help.content.fundAllocation.paragraph2",
                "text.help.content.fundAllocation.paragraph3", "text.help.content.fundAllocation.paragraph4");
        final Section fundAllocation1 = add(fundAllocation, "text.help.title.fundAllocation.projects");
        set(fundAllocation1, "text.help.content.fundAllocation.projects.paragraph1",
                "text.help.content.fundAllocation.projects.paragraph2", "text.help.content.fundAllocation.projects.paragraph3",
                "text.help.content.fundAllocation.projects.paragraph4", "text.help.content.fundAllocation.projects.paragraph5",
                "text.help.content.fundAllocation.projects.paragraph6", "text.help.content.fundAllocation.projects.paragraph7",
                "text.help.content.fundAllocation.projects.paragraph8", "text.help.content.fundAllocation.projects.paragraph9",
                "text.help.content.fundAllocation.projects.paragraph10", "text.help.content.fundAllocation.projects.paragraph11",
                "text.help.content.fundAllocation.projects.paragraph12", "text.help.content.fundAllocation.projects.paragraph13",
                "text.help.content.fundAllocation.projects.paragraph14");
        final Section fundAllocation2 = add(fundAllocation, "text.help.title.fundAllocation.costCenters");
        set(fundAllocation2, "text.help.content.fundAllocation.costCenters.paragraph1",
                "text.help.content.fundAllocation.costCenters.paragraph2",
                "text.help.content.fundAllocation.costCenters.paragraph3",
                "text.help.content.fundAllocation.costCenters.paragraph4",
                "text.help.content.fundAllocation.costCenters.paragraph5",
                "text.help.content.fundAllocation.costCenters.paragraph6",
                "text.help.content.fundAllocation.costCenters.paragraph7");
        setImage(fundAllocation2, "/images/mission/help/Screen04_1.png");
        final Section participantAuthorization =
                add(fundAllocationAndParticipantAuthorization, "text.help.title.dislocationAuthorization");
        set(participantAuthorization, "text.help.content.dislocationAuthorization.paragraph1",
                "text.help.content.dislocationAuthorization.paragraph2", "text.help.content.dislocationAuthorization.paragraph3",
                "text.help.content.dislocationAuthorization.paragraph4");
        final Section participantAuthorization1 = add(participantAuthorization, "text.help.title.dislocationAuthorization.units");
        set(participantAuthorization1, "text.help.content.dislocationAuthorization.units.paragraph1",
                "text.help.content.dislocationAuthorization.units.paragraph2",
                "text.help.content.dislocationAuthorization.units.paragraph3",
                "text.help.content.dislocationAuthorization.units.paragraph4",
                "text.help.content.dislocationAuthorization.units.paragraph5",
                "text.help.content.dislocationAuthorization.units.paragraph6");
        final Section participantAuthorization2 =
                add(participantAuthorization, "text.help.title.dislocationAuthorization.scientificCouncil");
        set(participantAuthorization2, "text.help.content.dislocationAuthorization.scientificCouncil.paragraph1",
                "text.help.content.dislocationAuthorization.scientificCouncil.paragraph2",
                "text.help.content.dislocationAuthorization.scientificCouncil.paragraph3",
                "text.help.content.dislocationAuthorization.scientificCouncil.paragraph4",
                "text.help.content.dislocationAuthorization.scientificCouncil.paragraph5",
                "text.help.content.dislocationAuthorization.scientificCouncil.paragraph6");
        final Section participantAuthorization3 =
                add(participantAuthorization, "text.help.title.dislocationAuthorization.managementCouncil");
        set(participantAuthorization3, "text.help.content.dislocationAuthorization.managementCouncil.paragraph1",
                "text.help.content.dislocationAuthorization.managementCouncil.paragraph2",
                "text.help.content.dislocationAuthorization.managementCouncil.paragraph3",
                "text.help.content.dislocationAuthorization.managementCouncil.paragraph4",
                "text.help.content.dislocationAuthorization.managementCouncil.paragraph5",
                "text.help.content.dislocationAuthorization.managementCouncil.paragraph6");
        final Section participantAuthorization4 =
                add(participantAuthorization, "text.help.title.dislocationAuthorization.president");
        set(participantAuthorization4, "text.help.content.dislocationAuthorization.president.paragraph1",
                "text.help.content.dislocationAuthorization.president.paragraph2",
                "text.help.content.dislocationAuthorization.president.paragraph3",
                "text.help.content.dislocationAuthorization.president.paragraph4",
                "text.help.content.dislocationAuthorization.president.paragraph5",
                "text.help.content.dislocationAuthorization.president.paragraph6",
                "text.help.content.dislocationAuthorization.president.paragraph7");
        setImage(participantAuthorization4, "/images/mission/help/Screen05_1.png");

        final Section expenseAuthorization = add(page, "text.help.title.expenseAuthorization");
        set(expenseAuthorization, "text.help.content.expenseAuthorization.paragraph1",
                "text.help.content.expenseAuthorization.paragraph2", "text.help.content.expenseAuthorization.paragraph3",
                "text.help.content.expenseAuthorization.paragraph4", "text.help.content.expenseAuthorization.paragraph5",
                "text.help.content.expenseAuthorization.paragraph6", "text.help.content.expenseAuthorization.paragraph7");
        setImage(expenseAuthorization, "/images/mission/help/Screen06_1.png");
        set(expenseAuthorization, "text.help.content.expenseAuthorization.paragraph8",
                "text.help.content.expenseAuthorization.paragraph9");

        final Section personelInformation = add(page, "text.help.title.processPersonnelInformation");
        set(personelInformation, "text.help.content.processPersonnelInformation.paragraph1",
                "text.help.content.processPersonnelInformation.paragraph2",
                "text.help.content.processPersonnelInformation.paragraph3",
                "text.help.content.processPersonnelInformation.paragraph4",
                "text.help.content.processPersonnelInformation.paragraph5",
                "text.help.content.processPersonnelInformation.paragraph6");
        setImage(personelInformation, "/images/mission/help/Screen07_1.png");

        final Section archievedProcess = add(page, "text.help.title.archieveProcess");
        set(archievedProcess, "text.help.content.archieveProcess.paragraph1", "text.help.content.archieveProcess.paragraph2");
        final Section archievedProcess1 = add(archievedProcess, "text.help.title.archieveProcess.withoutChanges");
        set(archievedProcess1, "text.help.content.archieveProcess.withoutChanges.paragraph1",
                "text.help.content.archieveProcess.withoutChanges.paragraph2",
                "text.help.content.archieveProcess.withoutChanges.paragraph3",
                "text.help.content.archieveProcess.withoutChanges.paragraph4",
                "text.help.content.archieveProcess.withoutChanges.paragraph5",
                "text.help.content.archieveProcess.withoutChanges.paragraph6",
                "text.help.content.archieveProcess.withoutChanges.paragraph7");
        final Section archievedProcess2 = add(archievedProcess, "text.help.title.archieveProcess.withChanges");
        set(archievedProcess2, "text.help.content.archieveProcess.withChanges.paragraph1",
                "text.help.content.archieveProcess.withChanges.paragraph2",
                "text.help.content.archieveProcess.withChanges.paragraph3",
                "text.help.content.archieveProcess.withChanges.paragraph4",
                "text.help.content.archieveProcess.withChanges.paragraph5",
                "text.help.content.archieveProcess.withChanges.paragraph6",
                "text.help.content.archieveProcess.withChanges.paragraph7",
                "text.help.content.archieveProcess.withChanges.paragraph8",
                "text.help.content.archieveProcess.withChanges.paragraph9",
                "text.help.content.archieveProcess.withChanges.paragraph10",
                "text.help.content.archieveProcess.withChanges.paragraph11",
                "text.help.content.archieveProcess.withChanges.paragraph12",
                "text.help.content.archieveProcess.withChanges.paragraph13",
                "text.help.content.archieveProcess.withChanges.paragraph14",
                "text.help.content.archieveProcess.withChanges.paragraph15",
                "text.help.content.archieveProcess.withChanges.paragraph16",
                "text.help.content.archieveProcess.withChanges.paragraph17",
                "text.help.content.archieveProcess.withChanges.paragraph18");
        setImage(archievedProcess2, "/images/mission/help/Screen08_01.png");
        set(archievedProcess2, "text.help.content.archieveProcess.withChanges.paragraph19");
    }

    private void setImage(final Section section, final String imagePath) {
        final StringBuilder stringBuilder = new StringBuilder(section.getContents().getContent());
        stringBuilder.append("<p align=\"center\"");
        stringBuilder.append("<img src=\"https://dot.ist.utl.pt");
        stringBuilder.append(imagePath);
        stringBuilder.append("\">");
        stringBuilder.append("</p>\n");
        setCustome(section, stringBuilder.toString());
    }

    private void add(final StringBuilder stringBuilder, final String key) {
        stringBuilder.append(BundleUtil.getStringFromResourceBundle("resources.MissionResources", key));
    }

    private void add(final StringBuilder stringBuilder, final String tag, final String key) {
        stringBuilder.append("<");
        stringBuilder.append(tag);
        stringBuilder.append(">");
        add(stringBuilder, key);
        stringBuilder.append("</");
        stringBuilder.append(tag);
        stringBuilder.append(">\n");
    }

    private void addH3(StringBuilder stringBuilder, String key) {
        add(stringBuilder, "h3", key);
    }

    private void addP(final StringBuilder stringBuilder, final String key) {
        add(stringBuilder, "p", key);
    }

    private void addLI(final StringBuilder stringBuilder, final String key) {
        add(stringBuilder, "li", key);
    }

    private void setCustome(final Section section, final String html) {
        final MultiLanguageString content = new MultiLanguageString(html);
        section.setContents(content);
    }

    private void set(final Section section, String... keys) {
        final MultiLanguageString contents = section.getContents();
        final StringBuilder stringBuilder =
                contents != null && contents.hasContent() ? new StringBuilder(contents.getContent()) : new StringBuilder();
        for (final String key : keys) {
            addP(stringBuilder, key);
        }
        setCustome(section, stringBuilder.toString());
    }

    private Section add(final Container container, final String key) {
        final Section section = container.addSection();
        section.setTitle(read(key));
        return section;
    }

    private MultiLanguageString read(final String key) {
        return BundleUtil.getMultilanguageString("resources.MissionResources", key);
    }

}
