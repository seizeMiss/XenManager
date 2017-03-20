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
	radiaObjCPU.animate(51);
	radiaObjCPU.option("barColor", "blue");
	$("#cpu-use-rate").next().children("a")
		.html(51).css("color", "blue");
	
	$("#cpu-use-rate").next().children("span").css("color", "blue");
	console.log(radiaObjCPU.value());
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
	$("#ram-use-rate").next().children("a")
		.html(20).css("color", "#9c3");
	
	var radiaObjStorage = radialIndicator("#storage-use-rate", {
		radius : 80,
		barWidth : 8,
		percentage : true,
		interpolate: true,
		initValue : 20,
		maxValue : 100,
		minValue : 0,
		barBgColor: "#c3bebe"
	});
});
