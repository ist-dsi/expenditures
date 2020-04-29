<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ attribute name="pagedListHolder" required="true" type="org.springframework.beans.support.PagedListHolder" %>
<%@ attribute name="formId" required="false" type="java.lang.String" %>
<c:set var="formId" value="${(empty formId) ? 'searchForm' : formId}" />

<c:if test="${pagedListHolder.pageCount > 1}">

	<div class="row">
		<div class="col-md-12 text-center">
			<ul class="pagination">
				<c:if test="${!pagedListHolder.firstPage}">
					<li>
						<a href="javascript:{}" class="paginationBtn ${formId}Btn" data-page="<%= pagedListHolder.getPage()-1 %>">
							<i class="fa fa-angle-left"></i>
						</a>

					</li>
				</c:if>
				<c:if test="${pagedListHolder.firstLinkedPage > 0}">
					<li>
						<a href="javascript:{}" class="paginationBtn ${formId}Btn" data-page="0">
							1
						</a>
					</li>
				</c:if>
				<c:if test="${pagedListHolder.firstLinkedPage > 1}">
					<li>
						<a href="javascript:{}">
							...
						</a>
					</li>
				</c:if>
				<c:forEach begin="${pagedListHolder.firstLinkedPage}" end="${pagedListHolder.lastLinkedPage}" var="i">
					<c:choose>
						<c:when test="${pagedListHolder.page == i}">
							<li class="active">
								<a href="javascript:{}">
									${i+1}
								</a>
							</li>
						</c:when>
						<c:otherwise>
							<li>
								<a href="javascript:{}" class="paginationBtn ${formId}Btn" data-page="${i}">
									${i+1}
								</a>
							</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:if test="${pagedListHolder.lastLinkedPage < pagedListHolder.pageCount - 2}">
					<li>
						<a href="javascript:{}">
							...
						</a>
					</li>
				</c:if>
				<c:if test="${pagedListHolder.lastLinkedPage < pagedListHolder.pageCount - 1}">
					<li>
						<a href="javascript:{}" class="paginationBtn ${formId}Btn" data-page="<%= pagedListHolder.getPageCount()-1 %>">
							${pagedListHolder.pageCount}
						</a>
					</li>
				</c:if>
				<c:if test="${!pagedListHolder.lastPage}">
					<li>
						<a href="javascript:{}" class="paginationBtn ${formId}Btn" data-page="<%= pagedListHolder.getPage()+1 %>">
							<i class="fa fa-angle-right"></i>
						</a>
					</li>
				</c:if>
			</ul>
		</div>
	</div>


	<script>
	$(document).ready(function() {
		$('a.paginationBtn.${formId}Btn').click(function() {
			var form = $('#${formId}');
			form.children('#page').val($(this).data('page'));
			form.submit();
		});
	});
	</script>
</c:if>
