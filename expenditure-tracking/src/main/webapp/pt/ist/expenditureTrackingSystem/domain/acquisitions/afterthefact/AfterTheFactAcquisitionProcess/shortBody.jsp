<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<div class="infobox">
	<fr:view name="process" property="acquisitionAfterTheFact"
			type="pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact"
			schema="viewAcquisitionAfterTheFact">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
		</fr:layout>
	</fr:view>
</div>
