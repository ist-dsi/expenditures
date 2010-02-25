<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>


<td>
	<fr:view name="workingCapitalTransaction" property="number"/>
</td>
<td>
	<fr:view name="workingCapitalTransaction" property="description"/>
</td>
<td>
	<fr:view name="workingCapitalTransaction" property="value"/>
</td>
<td>
	<fr:view name="workingCapitalTransaction" property="accumulatedValue"/>
</td>
<td>
	<fr:view name="workingCapitalTransaction" property="balance"/>
</td>
<td>
	<fr:view name="workingCapitalTransaction" property="debt"/>
</td>
