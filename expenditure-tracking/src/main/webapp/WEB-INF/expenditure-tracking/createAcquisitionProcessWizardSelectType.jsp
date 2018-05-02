<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2>Create Acquisition/Refund</h2>

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

<p>Limits</p>
<p class="mvert05">
	Tipo de acquisição:
</p>

<form>
    <input type="hidden" name="supplier" value="${supplier}">
    <table class="btable">
        <tr>
            <td>
                <c:choose>
                    <c:when test="${suggestSimplified}">
                        <button formaction='<%= request.getContextPath() %>/acquisitionSimplifiedProcedureProcess.do?method=prepareCreateAcquisitionProcess' class="btn btn-default btn-xlarge">
                            Ajusto Direto
                            <br/>Regime Simplificado
                        </button>
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
                        <button formaction="<%= request.getContextPath() %>/consultation/prepareCreateNewMultipleSupplierConsultationProcess" class="btn btn-default btn-xlarge">
                            Consulta Prévia
                            <br/>&nbsp;
                        </button>
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
                        <button formaction="<%= request.getContextPath() %>/acquisitionRefundProcess.do?method=prepareCreateRefundProcessUnderCCP" class="btn btn-default btn-xlarge">
                            Reembolso
                            <br/>&nbsp;
                        </button>
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
</form>