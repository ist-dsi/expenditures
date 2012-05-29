<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>

<h2><bean:message bundle="WORKING_CAPITAL_RESOURCES" key="label.module.workingCapital.configuration.acquisition.classification.add"/></h2>

<fr:edit id="acquisitionClassificationBean" name="acquisitionClassificationBean" action="/workingCapital.do?method=addAcquisitionClassification">
	<fr:schema type="module.workingCapital.domain.util.AcquisitionClassificationBean" bundle="WORKING_CAPITAL_RESOURCES">
		<fr:slot name="description" key="label.module.workingCapital.configuration.acquisition.classifications.description" required="true"/>
		<fr:slot name="economicClassification" key="label.module.workingCapital.configuration.acquisition.classifications.economicClassification" required="true"/>
		<fr:slot name="pocCode" key="label.module.workingCapital.configuration.acquisition.classifications.pocCode" required="true"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form listInsideClear" />
		<fr:property name="columnClasses" value="width100px,,tderror" />
	</fr:layout>
	<fr:destination name="cancel" path="/workingCapital.do?method=configuration" />
</fr:edit>