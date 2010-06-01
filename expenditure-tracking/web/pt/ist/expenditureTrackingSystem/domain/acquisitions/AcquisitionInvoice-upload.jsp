<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="process" name="process" toScope="request"/>
<bean:define id="processOID" name="process" property="externalId" toScope="request" type="java.lang.String"/>

<bean:define id="selectedInstance" name="bean" property="selectedInstance.simpleName"/>

<bean:define id="schema" value="<%= "addFile-" + selectedInstance%>" toScope="request"/>

<bean:define id="urlView">/workflowProcessManagement.do?method=viewProcess&amp;processId=<bean:write name="process" property="externalId"/></bean:define>
<bean:define id="urlPostBack">/workflowProcessManagement.do?method=uploadPostBack&amp;processId=<bean:write name="process" property="externalId"/></bean:define>
<bean:define id="urlInvalid">/workflowProcessManagement.do?method=invalidFileUpload&amp;processId=<bean:write name="process" property="externalId"/></bean:define>
	
<fr:edit name="bean" id="uploadFile" action='<%= "workflowProcessManagement.do?method=upload&processId=" + processOID %>' schema="<%= schema %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlView %>" />
	<fr:destination name="postBack"  path="<%= urlPostBack %>"/>
	<fr:destination name="invalid" path="<%= urlInvalid %>"/>
</fr:edit>

<script type="text/javascript">
var checkBox = $("input[id$='hasMoreInvoices']");
var itemList = $(checkBox).parents("tr").next("tr:last");

if (checkBox.attr('checked') == false) {
	itemList.hide();
}

checkBox.click(function(){	
	itemList.toggle();
	if (checkBox.attr('checked') == false) {
		$("input[id*=items_]").each(function(){
			$(this).attr('checked','true');
		});
	}
});
</script>