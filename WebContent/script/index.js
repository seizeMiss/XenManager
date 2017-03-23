$(function() {
	var radiaObjCPU = radialIndicator("#cpu-use-rate", {
		radius : 80,
		barWidth : 8,
		interpolate: true,
		initValue : 50,
		maxValue : 100,
		minValue : 0,
		displayNumber:false,
		barBgColor: "#c3bebe"
	});
	radiaObjCPU.animate(cpuUsedRate);
	if(parseInt(cpuUsedRate) > 70){
		radiaObjCPU.option("barColor", "#f8d346");
		$("#cpu-use-rate").next().children("a")
			.html(cpuUsedRate).css("color", "#f8d346");
		$("#cpu-use-rate").next().children("span").css("color", "#f8d346");
	}else{
		radiaObjCPU.option("barColor", "#5bb85d");
		$("#cpu-use-rate").next().children("a")
			.html(cpuUsedRate).css("color", "#5bb85d");
		$("#cpu-use-rate").next().children("span").css("color", "#5bb85d");
	}
	
	
//	console.log(radiaObjCPU.value());
	var radiaObjRam = radialIndicator("#ram-use-rate", {
		radius : 80,
		barWidth : 8,
		percentage : true,
		interpolate: true,
		initValue : 20,
		maxValue : 100,
		minValue : 0,
		displayNumber:false,
		barBgColor: "#c3bebe"
	});
	radiaObjRam.animate(memoryUsedRate);
	if(parseInt(memoryUsedRate) > 70){
		radiaObjRam.option("barColor", "#f8d346");
		$("#ram-use-rate").next().children("a")
			.html(memoryUsedRate).css("color", "#f8d346");
		$("#ram-use-rate").next().children("span").css("color", "#f8d346");
	}else{
		radiaObjRam.option("barColor", "#5bb85d");
		$("#ram-use-rate").next().children("a")
			.html(memoryUsedRate).css("color", "#5bb85d");
		$("#ram-use-rate").next().children("span").css("color", "#5bb85d");
	}
	
	var radiaObjStorage = radialIndicator("#storage-use-rate", {
		radius : 80,
		barWidth : 8,
		percentage : true,
		interpolate: true,
		initValue : storageUsedRate,
		maxValue : 100,
		minValue : 0,
		barBgColor: "#c3bebe"
	});
});
