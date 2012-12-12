<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/collection-pager" prefix="cp"%>

<h2><bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.management.configure.acquisition.limit.title"/></h2>

<fr:edit id="workingCapitalSystem" name="workingCapitalSystem" action="/workingCapital.do?method=configuration">
	<fr:schema type="module.workingCapital.domain.WorkingCapitalSystem" bundle="WORKING_CAPITAL_RESOURCES">
		<fr:slot name="acquisitionValueLimit" key="label.module.workingCapital.configuration.acquisition.limit.short" bundle="WORKING_CAPITAL_RESOURCES">
			<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.NumberRangeValidator">
				<fr:property name="lowerBound" value="0"/>
			</fr:validator>
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form listInsideClear" />
		<fr:property name="columnClasses" value="width100px,,tderror" />
	</fr:layout>
</fr:edit>

<p>
<html:link action="/workingCapital.do?method=resetAcquisitionLimit">
	<bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.acquisition.limit.reset"/>
</html:link>
