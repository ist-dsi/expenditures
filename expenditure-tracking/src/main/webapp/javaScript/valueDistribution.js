function getShares(maxValue,outOfLabel,url) {
				var maxValueFloat = parseFloat(maxValue);
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
									$("#" + trId + " td:last").find("input").attr("value",value);
									writeSum(maxValue,outOfLabel);
							    }
					 	    });
				}else {
					writeSum(maxValue,outOfLabel);
				}
					 			
			}
			
			function writeSum(maxValue, outOfLabel) {
				var sum = parseFloat("0");	
				var maxValueFloat = parseFloat(maxValue);
				
				jQuery.each($("#assign input[type='checkbox']:checked"), function() {
					var value = $(this).parent("td").siblings("td:last").find("input").val()
					sum += parseFloat(value.replace('.','').replace(',','.'));
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
				$("#sum").append(sumValue.replace('.',',') + ' (' + outOfLabel + ' ' + maxValue.replace('.',',') + ')');
			}
