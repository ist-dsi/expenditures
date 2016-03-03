<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="form-group">
    <label class="control-label col-sm-2" for="title">
        <spring:message code="label.internalBilling.billableService.title" text="Service" />
    </label>
    <div class="col-sm-10">
        <input name="title" type="text" class="form-control" id="title" required="required" value=""/>
    </div>
</div>
<div class="form-group">
    <label class="control-label col-sm-2" for="type">
        <spring:message code="label.internalBilling.billableService.description" text="Description" />
    </label>
    <div class="col-sm-10">
        <textarea name="description" class="form-control" id="description" required="required" rows="4"></textarea>
    </div>
</div>
