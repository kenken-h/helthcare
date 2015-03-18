google.load("visualization", "1.1", {
	packages : [ "corechart" ]
});
$(document).ready(function() {
	$("#bsButton").click(showBsChart);
	$("#bpButton").click(showBpChart);
	$("#btButton").click(showBtChart);
	$("#wtButton").click(showWtChart);
});
// 血糖値
function showBsChart() {
	$.post("showBsChart", // URL
	{}, // User データ
	function(result) { // 成功時のコールバック
		$("#myModalLabel").html("血糖値グラフ");
		$('#myModal').modal('show');
		var data = google.visualization.arrayToDataTable(result);
		var options = {
			hAxis : {
				title : "日付"
			},
			seriesType : "bars",
			series : {
				4 : {
					type : "line"
				}
			},
			width : 820,
		};
		var chart = new google.visualization.ComboChart(document
				.getElementById('chart_div'));
		chart.draw(data, options);
	});
}
// 血圧
function showBpChart() {
	$.post("showBpChart", // URL
	{}, // User データ
	function(result) { // 成功時のコールバック
		$("#myModalLabel").html("血圧グラフ");
		$('#myModal').modal('show');
		var data = google.visualization.arrayToDataTable(result);
		var options = {
			hAxis : {
				title : "日付"
			},
			width : 820,
		};
		var chart = new google.visualization.ColumnChart(document
				.getElementById('chart_div'));
		chart.draw(data, options);
	});
}
// 体温
function showBtChart() {
	$.post("showBtChart", // URL
	{}, // User データ
	function(result) { // 成功時のコールバック
		$("#myModalLabel").html("体温グラフ");
		$('#myModal').modal('show');
		var data = google.visualization.arrayToDataTable(result);
		var options = {
			vAxis : {
				gridlines : {
					color : '#ccf',
					count : 8
				},
				baseline : 37,
				viewWindow : {
					min : 35.5,
					max : 38.5
				},
			},
			hAxis : {
				title : "日付"
			},
			width : 820,
		};
		//var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
		var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
		chart.draw(data, options);
	});
}
// 体重
function showWtChart() {
	$.post("showWtChart", // URL
	{}, // User データ
	function(result) { // 成功時のコールバック
		$("#myModalLabel").html("体重グラフ");
		$('#myModal').modal('show');
		var data = google.visualization.arrayToDataTable(result);
		var options = {
			vAxis : {
				gridlines : {
					color : '#ccf'
				},
			},
			baseline : 60,
			viewWindow : {
				min : 56,
				max : 66
			},
			hAxis : {
				title : "日付"
			},
			width : 820,
		};
		var chart = new google.visualization.ColumnChart(document
				.getElementById('chart_div'));
		chart.draw(data, options);
	});
}
