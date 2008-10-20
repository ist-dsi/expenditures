	function calculate(maxElements, writeTo, maxValue, outOfLabel) {
	
			var sum = parseFloat("0");	
			var	sumValue ="";
			var maxValueFloat = parseFloat(maxValue); 
			var i = 0;
			for(i=0; i<maxElements;i++) {
				element = document.getElementById("tr" + i);
				if (element != null) {
					var checked = element.cells[0].getElementsByTagName("input")[0].checked;
					var value = element.cells[2].getElementsByTagName("input")[0].value;
					if (checked && value.match('\\d+')) {
						sum += parseFloat(value.replace(',','.'));
					}
				}
			}
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
			document.getElementById(writeTo).innerHTML=sumValue.replace('.',',') + ' (' + outOfLabel + ' ' + maxValue.replace('.',',') + ')';
	}