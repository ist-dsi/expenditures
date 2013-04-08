<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

	
<h2>
	<bean:message key="acquisitionProcess.title.viewAcquisitionRequest" bundle="ACQUISITION_RESOURCES"/>
	<span class="processNumber">(<fr:view name="process" property="acquisitionRequest.acquisitionProcessId"/>)</span>	
</h2> 
