			function getShares(outOfLabel,url) {
				var maxValueFloat = parseFloat($("#maxValue").text().replace('.','').replace(',','.')) || 0;
				var activeRows = "";
				jQuery.each($("#assign input[type='checkbox']:checked"), function() {
					var value = $(this).parent("td").parent("tr").attr("id")
					if (activeRows.length > 0) {
						activeRows += ",";
					}
					activeRows += value;
				});
				
				if (activeRows.length > 0) {
				$.getJSON(url,
					 	    { money: maxValueFloat, requestors: activeRows },
					    	function(data) { 
						    	size = data.length;
						    	for (i = 0; i < size; i++) {
									var trId = data[i]['id'];
									var value = data[i]['share'].replace(".",",");
									$("#" + trId + " td:last").find("input").val(value);
									writeSum(outOfLabel);
							    }
					 	    });
				}else {
					writeSum(outOfLabel);
				}
					 			
			}
			function writeSum(outOfLabel) {
				var maxValueFloat = parseFloat($("#maxValue").text().replace('.','').replace(',','.')) || 0;
				var sum = parseFloat("0");

				jQuery.each($("#assign input[type='checkbox']:checked"), function() {
					var value = $(this).parent("td").siblings("td:last").find("input").val()
					sum += parseFloat(value.replace('.','').replace(',','.')) || 0;
				});

				sum = Math.round(sum*100)/100;
				if (sum > maxValueFloat) {
					sumValue = "<span class=\"invalid\">" + sum + "</span>";
				}
				else if (sum == maxValueFloat) {
					sumValue = "<span class=\"valid\">" + sum + "</span>";
				}
				else {
					sumValue = sum + "";
				}

				$("#sum").empty();
				$("#sum").append(sumValue.replace('.',',') + ' (' + outOfLabel + ' ' + maxValueFloat + ')');
			}
			
			function getMaxValue(itemId,outOfLabel,url) {
				var activeRows = "";
				jQuery.each($("#assign input[type='checkbox']:checked"), function() {
					var value = $(this).parent("td").attr("id")
					if (activeRows.length > 0) {
						activeRows += ",";
					}
					activeRows += value;
				});
				
				if (activeRows.length > 0) {
				$.getJSON(url,
					 	    { requestItemId: itemId, payingUnits: activeRows },
					    	function(data) { 
					    		$("#maxValue").empty();
								$("#maxValue").append(data['maxValue'].replace(".",","));
								writeSum(outOfLabel);
					 	    });
				} else {
					writeSum(outOfLabel);
				}
			}
