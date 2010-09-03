

function startStateTypeRenderer(appContext, processId) {
	$(".states th").mouseover(function() { 
		var id = $(this).attr('id');
		$.getJSON("/" + appContext + "/expenditureProcesses.do?method=viewTypeDescription&type=" + id + "&processId=" + processId,function(data, textStatus) {dealWith(data)});
		
	});

	$(".states").mouseout(function() {
		var id = $(".states > .selected").attr('id');
		if (id == null) {
			id = "CANCELED";
		}
		$.getJSON("/" + appContext + "/expenditureProcesses.do?method=viewTypeDescription&type=" + id + "&processId=" + processId,function(data, textStatus) {dealWith(data)});
	});
	
}

function dealWith(data) {
	
	$(".state-desc td").empty();
	$(".state-desc td").append("<h4>" + data['name'] + " </h4> " + data['description']);
}