

function startStateTypeRenderer(appContext, processId) {

	var url = appContext + "/expenditureProcesses.do?method=viewTypeDescription" + "&processId=" + processId;
	
	$(".states th").mouseenter(function() { 
		var id = $(this).attr('id');
		var classname = $(this).attr('name');
		$.getJSON(url + "&type=" + id + "&classname=" + classname ,function(data, textStatus) {dealWith(data)});
		
	});

	$(".states").mouseleave(function() {
		var id = $(".states > .selected").attr('id');
		var classname = $(".states > .selected").attr('name');
		if (id == null) {
			id = "CANCELED";
			classname = $(".states > th:first").attr('name');
		}
		$.getJSON(url + "&type=" + id + "&classname=" + classname,function(data, textStatus) {dealWith(data)});
	});
	
}

function dealWith(data) {
	
	$(".state-desc td").empty();
	$(".state-desc td").append("<h4>" + data['name'] + " </h4> " + data['description']);
}