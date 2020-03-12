<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<%@ attribute name="processList" required="true" type="java.util.List" %>
<%@ attribute name="viewUrl" required="false" type="java.lang.String" %>

<c:choose>
    <c:when test="${processList.isEmpty()}">
        <p>
            <spring:message code="info.search.noResults" />
        </p>
    </c:when>
    <c:otherwise>
        <ul class="operations mtop0">
            <c:forEach items="${processList}" var="process">
                <li>
                    <c:choose>
                        <c:when test="${empty viewUrl}">
                            <c:out value="${process.presentationName}"/>
                        </c:when>
                        <c:otherwise>
                            <a href='${viewUrl}${process.externalId}'>
                                <c:out value="${process.presentationName}"/>
                            </a>
                        </c:otherwise>
                    </c:choose>
                </li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>
