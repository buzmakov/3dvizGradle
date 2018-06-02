function addDataPoints(chart, circleDiagramList) {
	for(var i = 0; i < circleDiagramList.length; ++i) {
		chart.options.data[0].dataPoints.push({
		    x: circleDiagramList[i].radius,
		    y: circleDiagramList[i].value
		});
	}

	return chart
}

function vizCircleDiagram(circleDiagram) {
    var chart = new CanvasJS.Chart("circle-chart", {
    	theme: "light2", // "light1", "light2", "dark1", "dark2"
    	animationEnabled: true,
    	zoomEnabled: true,
    	title: {
    		text: "Circle chart"
    	},
    	data: [{
    		type: "area",
    		dataPoints: []
    	}]
    });

    chart = addDataPoints(chart, circleDiagram);
    chart.render();

}


function vizCircleDiagramIfNeeded() {
    if (!document.getElementById("circleDiagramViz").checked) {
            return;
    }

    document.getElementById("circle-chart").style.display = "none";
    document.getElementById("chartloader").style.display = "block";


    var req = getXmlHttp()
    req.open("GET", host + "/objects/current/slice/"+ idSlice +"/circleDiagram/?r=" + Math.random(), true);
    req.onreadystatechange = function() {
        if (req.readyState == 4) {
           if(req.status == 200 && req.responseURL.includes(idSlice)) {
                document.getElementById("chartloader").style.display = "none";
                document.getElementById("circle-chart").style.display = "block";
                vizCircleDiagram(JSON.parse(req.responseText))
           }
        }
    };
    req.send(null);
}