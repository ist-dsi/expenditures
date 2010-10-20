<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message bundle="MISSION_RESOURCES" key="text.help.title.index"/></h2>

<!-- BLOCK_HAS_CONTEXT -->

<ol>
	<li><!-- NO_CHECKSUM --><a href="#introduction"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.introduction"/></a></li>
	<li><!-- NO_CHECKSUM --><a href="#processSummary"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.processSummary"/></a></li>
	<li>
		<!-- NO_CHECKSUM --><a href="#createMissionProcess"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.createMissionProcess"/></a>
		<ol>
			<li><!-- NO_CHECKSUM --><a href="#createMissionProcessCancelProcess"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.createMissionProcess.cancelProcess"/></a></li>
		</ol>
	</li>
	<li>
		<!-- NO_CHECKSUM --><a href="#manageMissionProcess"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageMissionProcess"/></a>
		<ol>
			<li>
				<!-- NO_CHECKSUM --><a href="#manageMissionProcessPayingUnits"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageMissionProcess.payingUnits"/></a>
				<ol>
					<li><!-- NO_CHECKSUM --><a href="#manageMissionProcessPayingUnitsInsert"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageMissionProcess.payingUnits.insert"/></a></li>
					<li><!-- NO_CHECKSUM --><a href="#manageMissionProcessPayingUnitsRemove"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageMissionProcess.payingUnits.remove"/></a></li>
				</ol>
			</li>
			<li>
				<!-- NO_CHECKSUM --><a href="#manageMissionProcessManageParticipantes"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageMissionProcess.manageParticipantes"/></a>
				<ol>
					<li><!-- NO_CHECKSUM --><a href="#manageMissionProcessManageParticipantesAdd"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageMissionProcess.manageParticipantes.add"/></a></li>
					<li><!-- NO_CHECKSUM --><a href="#manageMissionProcessManageParticipantesRemove"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageMissionProcess.manageParticipantes.remove"/></a></li>
				</ol>
			</li>
		</ol>
	</li>
	<li>
		<!-- NO_CHECKSUM --><a href="#manageItems"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageItems"/></a>
		<ol>
			<li><!-- NO_CHECKSUM --><a href="#manageItemsAdd"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageItems.add"/></a></li>
			<li><!-- NO_CHECKSUM --><a href="#manageItemsEdit"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageItems.edit"/></a></li>
			<li><!-- NO_CHECKSUM --><a href="#manageItemsDistributeCosts"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageItems.distributeCosts"/></a></li>
			<li><!-- NO_CHECKSUM --><a href="#manageItemsRemove"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageItems.remove"/></a></li>
		</ol>
	</li>
	<li><!-- NO_CHECKSUM --><a href="#submitForApproval"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.submitForApproval"/></a></li>
	<li>
		<!-- NO_CHECKSUM --><a href="#processApproval"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.processApproval"/></a>
		<ol>
			<li><!-- NO_CHECKSUM --><a href="#processApprovalWithoutCosts"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.processApproval.withoutCosts"/></a></li>
			<li><!-- NO_CHECKSUM --><a href="#processApprovalWithCosts"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.processApproval.withCosts"/></a></li>
		</ol>
	</li>
	<li>
		<!-- NO_CHECKSUM --><a href="#fundAllocationAndDislocationAuthorization"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.fundAllocationAndDislocationAuthorization"/></a>
		<ol>
			<li>
				<!-- NO_CHECKSUM --><a href="#fundAllocation"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.fundAllocation"/></a>
				<ol>
					<li><!-- NO_CHECKSUM --><a href="#fundAllocationProjects"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.fundAllocation.projects"/></a></li>
					<li><!-- NO_CHECKSUM --><a href="#fundAllocationCostCenters"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.fundAllocation.costCenters"/></a></li>
				</ol>
			</li>
			<li>
				<!-- NO_CHECKSUM --><a href="#dislocationAuthorization"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.dislocationAuthorization"/></a>
				<ol>
					<li><!-- NO_CHECKSUM --><a href="#dislocationAuthorizationUnits"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.dislocationAuthorization.units"/></a></li>
					<li><!-- NO_CHECKSUM --><a href="#dislocationAuthorizationScientificCouncil"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.dislocationAuthorization.scientificCouncil"/></a></li>
					<li><!-- NO_CHECKSUM --><a href="#dislocationAuthorizationManagementCouncil"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.dislocationAuthorization.managementCouncil"/></a></li>
					<li><!-- NO_CHECKSUM --><a href="#dislocationAuthorizationPresident"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.dislocationAuthorization.president"/></a></li>
				</ol>
			</li>
		</ol>
	</li>
	<li><!-- NO_CHECKSUM --><a href="#expenseAuthorization"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.expenseAuthorization"/></a></li>
	<li><!-- NO_CHECKSUM --><a href="#processPersonnelInformation"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.processPersonnelInformation"/></a></li>
	<li>
		<!-- NO_CHECKSUM --><a href="#archieveProcess"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.archieveProcess"/></a>
		<ol>
			<li><!-- NO_CHECKSUM --><a href="#archieveProcessWithoutChanges"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.archieveProcess.withoutChanges"/></a></li>
			<li><!-- NO_CHECKSUM --><a href="#archieveProcessWithChanges"><bean:message bundle="MISSION_RESOURCES" key="text.help.title.archieveProcess.withChanges"/></a></li>
		</ol>
	</li>
</ol>

<h2><a name="introduction"></a>1. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.introduction"/></h2>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.introduction.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.introduction.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.introduction.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.introduction.paragraph4"/></p>

<h2><a name="processSummary"></a>2. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.processSummary"/></h2>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.paragraph1"/></p>
<blockquote>
	<h3><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.stage1.title"/></h3>
	<h3><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.stage2.title"/></h3>
	<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.stage2.content"/></p>
	<h3><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.stage3.title"/></h3>
	<p>
		<bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.stage3.paragraph1"/>
		<blockquote>
			<ul>
				<li><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.stage3.paragraph1.subList1"/></li>
				<li><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.stage3.paragraph1.subList2"/></li>
			</ul>
		</blockquote>
	</p>
	<h3><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.stage4.title"/></h3>
	<p>
		<bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.stage4.paragraph1"/>
		<ol type="a">
			<li><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.stage4.paragraph1.subList1"/></li>
			<li><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.stage4.paragraph1.subList2"/></li>
		</ol>
	</p>
	<h3><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.stage5.title"/></h3>
	<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.stage5.paragraph1"/></p>
	<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.stage5.paragraph2"/></p>
	<h3><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.stage6.title"/></h3>
	<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.stage6.paragraph1"/></p>
	<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.stage6.paragraph2"/></p>
</blockquote>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processSummary.paragraph2"/></p>

<h2><a name="createMissionProcess"></a>3. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.createMissionProcess"/></h2>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.title.createMissionProcess.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.title.createMissionProcess.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.title.createMissionProcess.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.title.createMissionProcess.paragraph4"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.title.createMissionProcess.paragraph5"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.title.createMissionProcess.paragraph6"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.title.createMissionProcess.paragraph7"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.title.createMissionProcess.paragraph8"/></p>
<p align="center"><img src="<%= request.getContextPath() + "/images/mission/help/Screen01_1.png" %>" alt="process_creation" /></p>

<h3><a name="createMissionProcessCancelProcess"></a>3.1. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.createMissionProcess.cancelProcess"/></h3>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.createMissionProcessCancelProcess.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.createMissionProcessCancelProcess.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.createMissionProcessCancelProcess.paragraph3"/></p>


<h2><a name="manageMissionProcess"></a>4. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageMissionProcess"/></h2>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageMissionProcess.paragraph1"/></p>

<h3><a name="manageMissionProcessPayingUnits"></a>4.1. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageMissionProcess.payingUnits"/></h3>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageMissionProcess.payingUnits"/></p>

<h4><a name="manageMissionProcessPayingUnitsInsert"></a>4.1.1. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageMissionProcess.payingUnits.insert"/></h4>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageMissionProcess.payingUnits.insert.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageMissionProcess.payingUnits.insert.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageMissionProcess.payingUnits.insert.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageMissionProcess.payingUnits.insert.paragraph4"/></p>

<h4><a name="manageMissionProcessPayingUnitsRemove"></a>4.1.2. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageMissionProcess.payingUnits.remove"/></h4>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageMissionProcess.payingUnits.remove.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageMissionProcess.payingUnits.remove.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageMissionProcess.payingUnits.remove.paragraph3"/></p>

<h3><a name="manageMissionProcessManageParticipantes"></a>4.2. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageMissionProcess.manageParticipantes"/></h3>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageMissionProcess.manageParticipantes"/></p>

<h4><a name="manageMissionProcessManageParticipantesAdd"></a>4.2.1. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageMissionProcess.manageParticipantes.add"/></h4>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageMissionProcess.manageParticipantes.add.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageMissionProcess.manageParticipantes.add.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageMissionProcess.manageParticipantes.add.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageMissionProcess.manageParticipantes.add.paragraph4"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageMissionProcess.manageParticipantes.add.paragraph5"/></p>

<h4><a name="manageMissionProcessManageParticipantesRemove"></a>4.2.2. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageMissionProcess.manageParticipantes.remove"/></h4>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageMissionProcess.manageParticipantes.remove.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageMissionProcess.manageParticipantes.remove.paragraph2"/></p>

<h2><a name="manageItems"></a>5. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageItems"/></h2>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageItems.paragraph1"/></p>

<h3><a name="manageItemsAdd"></a>5.1. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageItems.add"/></h3>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageItems.add.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageItems.add.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageItems.add.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageItems.add.paragraph4"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageItems.add.paragraph5"/></p>

<h3><a name="manageItemsEdit"></a>5.2. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageItems.edit"/></h3>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageItems.edit.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageItems.edit.paragraph2"/></p>

<h3><a name="manageItemsDistributeCosts"></a>5.3. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageItems.distributeCosts"/></h3>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageItems.distributeCosts.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageItems.distributeCosts.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageItems.distributeCosts.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageItems.distributeCosts.paragraph4"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageItems.distributeCosts.paragraph5"/></p>

<h3><a name="manageItemsRemove"></a>5.4. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.manageItems.remove"/></h3>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.manageItems.remove"/></p>

<h2><a name="submitForApproval"></a>6. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.submitForApproval"/></h2>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.submitForApproval.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.submitForApproval.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.submitForApproval.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.submitForApproval.paragraph4"/></p>
<p align="center"><img src="<%= request.getContextPath() + "/images/mission/help/Screen02_1.png" %>" alt="submit_for_approval" /></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.submitForApproval.paragraph5"/></p>

<h2><a name="processApproval"></a>7. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.processApproval"/></h2>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval"/></p>

<h3><a name="processApprovalWithoutCosts"></a>7.1. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.processApproval.withoutCosts"/></h3>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withoutCosts.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withoutCosts.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withoutCosts.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withoutCosts.paragraph4"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withoutCosts.paragraph5"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withoutCosts.paragraph6"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withoutCosts.paragraph7"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withoutCosts.paragraph8"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withoutCosts.paragraph9"/></p>

<h3><a name="processApprovalWithCosts"></a>7.2. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.processApproval.withCosts"/></h3>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withCosts.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withCosts.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withCosts.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withCosts.paragraph4"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withCosts.paragraph5"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withCosts.paragraph6"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withCosts.paragraph7"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withCosts.paragraph8"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withCosts.paragraph9"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processApproval.withCosts.paragraph10"/></p>
<p align="center"><img src="<%= request.getContextPath() + "/images/mission/help/Screen03_1.png" %>" alt="mission_approval" /></p>

<h2><a name="fundAllocationAndDislocationAuthorization"></a>8. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.fundAllocationAndDislocationAuthorization"/></h2>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocationAndDislocationAuthorization.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocationAndDislocationAuthorization.paragraph2"/></p>

<h3><a name="fundAllocation"></a>8.1. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.fundAllocation"/></h3>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.paragraph4"/></p>

<h4><a name="fundAllocationProjects"></a>8.1.1. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.fundAllocation.projects"/></h4>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.projects.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.projects.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.projects.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.projects.paragraph4"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.projects.paragraph5"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.projects.paragraph6"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.projects.paragraph7"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.projects.paragraph8"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.projects.paragraph9"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.projects.paragraph10"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.projects.paragraph11"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.projects.paragraph12"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.projects.paragraph13"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.projects.paragraph14"/></p>

<h4><a name="fundAllocationCostCenters"></a>8.1.2. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.fundAllocation.costCenters"/></h4>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.costCenters.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.costCenters.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.costCenters.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.costCenters.paragraph4"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.costCenters.paragraph5"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.costCenters.paragraph6"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.fundAllocation.costCenters.paragraph7"/></p>
<p align="center"><img src="<%= request.getContextPath() + "/images/mission/help/Screen04_1.png" %>" alt="fund_allocation" /></p>

<h3><a name="dislocationAuthorization"></a>8.2. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.dislocationAuthorization"/></h3>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.paragraph4"/></p>

<h4><a name="dislocationAuthorizationUnits"></a>8.2.1. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.dislocationAuthorization.units"/></h4>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.units.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.units.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.units.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.units.paragraph4"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.units.paragraph5"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.units.paragraph6"/></p>

<h4><a name="dislocationAuthorizationScientificCouncil"></a>8.2.2. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.dislocationAuthorization.scientificCouncil"/></h4>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.scientificCouncil.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.scientificCouncil.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.scientificCouncil.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.scientificCouncil.paragraph4"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.scientificCouncil.paragraph5"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.scientificCouncil.paragraph6"/></p>

<h4><a name="dislocationAuthorizationManagementCouncil"></a>8.2.3. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.dislocationAuthorization.managementCouncil"/></h4>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.managementCouncil.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.managementCouncil.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.managementCouncil.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.managementCouncil.paragraph4"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.managementCouncil.paragraph5"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.managementCouncil.paragraph6"/></p>

<h4><a name="dislocationAuthorizationPresident"></a>8.2.4. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.dislocationAuthorization.president"/></h4>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.president.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.president.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.president.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.president.paragraph4"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.president.paragraph5"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.president.paragraph6"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.dislocationAuthorization.president.paragraph7"/></p>
<p align="center"><img src="<%= request.getContextPath() + "/images/mission/help/Screen05_1.png" %>" alt="dislocation_Authorization" /></p>

<h2><a name="expenseAuthorization"></a>9. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.expenseAuthorization"/></h2>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.expenseAuthorization.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.expenseAuthorization.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.expenseAuthorization.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.expenseAuthorization.paragraph4"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.expenseAuthorization.paragraph5"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.expenseAuthorization.paragraph6"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.expenseAuthorization.paragraph7"/></p>
<p align="center"><img src="<%= request.getContextPath() + "/images/mission/help/Screen06_1.png" %>" alt="expense_authorization" /></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.expenseAuthorization.paragraph8"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.expenseAuthorization.paragraph9"/></p>

<h2><a name="processPersonnelInformation"></a>10. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.processPersonnelInformation"/></h2>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processPersonnelInformation.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processPersonnelInformation.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processPersonnelInformation.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processPersonnelInformation.paragraph4"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processPersonnelInformation.paragraph5"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.processPersonnelInformation.paragraph6"/></p>
<p align="center"><img src="<%= request.getContextPath() + "/images/mission/help/Screen07_1.png" %>" alt="personnel_information_processing" /></p>

<h2><a name="archieveProcess"></a>11. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.archieveProcess"/></h2>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.paragraph2"/></p>

<h3><a name="archieveProcessWithoutChanges"></a>11.1. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.archieveProcess.withoutChanges"/></h3>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withoutChanges.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withoutChanges.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withoutChanges.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withoutChanges.paragraph4"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withoutChanges.paragraph5"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withoutChanges.paragraph6"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withoutChanges.paragraph7"/></p>

<h3><a name="archieveProcessWithChanges"></a>11.2. <bean:message bundle="MISSION_RESOURCES" key="text.help.title.archieveProcess.withChanges"/></h3>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph1"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph2"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph3"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph4"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph5"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph6"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph7"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph8"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph9"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph10"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph11"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph12"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph13"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph14"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph15"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph16"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph17"/></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph18"/></p>
<p align="center"><img src="<%= request.getContextPath() + "/images/mission/help/Screen08_01.png" %>" alt="archieve_process" /></p>
<p><bean:message bundle="MISSION_RESOURCES" key="text.help.content.archieveProcess.withChanges.paragraph19"/></p>

<!-- END_BLOCK_HAS_CONTEXT -->
