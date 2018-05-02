<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="page-header">
	<h2>Criar novo Processo de Aquisição/Reembolso</h2>
</div>

<style>
.btn-xlarge {
    padding: 18px 28px;
    font-size: 20px;
    line-height: normal;
    -webkit-border-radius: 8px;
    -moz-border-radius: 8px;
    border-radius: 8px;
    border-color: #07a;
    border-width: 2px;
    border-style: dotted;
}

.btable {
    margin-top: 25px;
    margin-bottom: 25px;
    width: 100%;
    text-align: center;
}

.btable td {
    width: 25%;
}
</style>

<c:if test="${!suggestSimplified}">
	<div class="alert alert-danger">
		Não é possível criar um novo processo de aquisição por <strong>Ajusto Direto</strong> porque o fornecedor escolhido (${supplier.presentationName}) tem compras acumuladas no valor de ${supplier.softTotalAllocated.toFormatString()} tendo já ultrapassado o limite de ${supplier.supplierLimit.toFormatString()}.
	</div>
	<div class="alert alert-danger">
		Não é possível criar um novo processo de <strong>Reembolso</strong> porque o fornecedor escolhido (${supplier.presentationName}) tem compras acumuladas no valor de ${supplier.softTotalAllocated.toFormatString()} tendo já ultrapassado o limite de ${supplier.supplierLimit.toFormatString()}.
	</div>
</c:if>
<c:if test="${!suggestConsultation}">
	<div class="alert alert-danger">
		Não é possível criar um novo processo de aquisição por <strong>Consulta Prévia</strong> porque o fornecedor escolhido (${supplier.presentationName}) tem comprar acumuladas no valor de ${supplier.totalAllocatedForMultipleSupplierConsultation.toFormatString()}) tendo já ultrapassado o limite de ${supplier.multipleSupplierLimit.toFormatString()}.
	</div>
</c:if>
<p class="mvert05">
	Selecione o tipo de processo de acquisição:
</p>

<spring:url var="backUrl" value="/expenditure/acquisitons/create" />
<spring:url var="acquisitionUrl" value="/expenditure/acquisitons/create/acquisition?supplier=${supplier.fiscalIdentificationCode}" />
<spring:url var="consultationUrl" value="/expenditure/acquisitons/create/consultation?supplier=${supplier.fiscalIdentificationCode}" />
<spring:url var="refundUrl" value="/expenditure/acquisitons/create/refund?supplier=${supplier.fiscalIdentificationCode}" />

<table class="btable">
    <tr>
        <td>
            <c:choose>
                <c:when test="${suggestSimplified}">
                    <a href='${acquisitionUrl}' class="btn btn-default btn-xlarge">
                        Ajusto Direto
                        <br/>Regime Simplificado
                    </a>
                </c:when>
                <c:otherwise>
                    <button class="btn btn-default btn-xlarge" disabled="disabled">
                        Ajusto Direto
                        <br/>Regime Simplificado
                    </button>
                </c:otherwise>
            </c:choose>
        </td>
        <td>
            <button class="btn btn-default btn-xlarge" disabled="disabled">
                Ajusto Direto
                <br/>Regime Geral
            </button>
        </td>
        <td>
            <c:choose>
                <c:when test="${suggestConsultation}">
                    <a href="${consultationUrl}" class="btn btn-default btn-xlarge">
                        Consulta Prévia
                        <br/>&nbsp;
                    </a>
                </c:when>
                <c:otherwise>
                    <button class="btn btn-default btn-xlarge" disabled="disabled">
                        Consulta Prévia
                        <br/>&nbsp;
                    </button>
                </c:otherwise>
            </c:choose>
        </td>
        <td>
            <c:choose>
                <c:when test="${suggestRefund}">
                    <a href="${refundUrl}" class="btn btn-default btn-xlarge">
                        Reembolso
                        <br/>&nbsp;
                    </a>
                </c:when>
                <c:otherwise>
                    <button class="btn btn-default btn-xlarge" disabled="disabled">
                        Reembolso
                        <br/>&nbsp;
                    </button>
                </c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>
<div class="form-group">
    <div class="col-sm-12">
	     <a class="btn btn-default" href="${backUrl}">
	     	<spring:message code="link.back" text="Back" />
	     </a>
    </div>
</div>