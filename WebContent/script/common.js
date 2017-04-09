$(function() {
	//设置中间部分的高度
	var height = $(document.body).height();
	var width = $(document.body).width();
	$(".left-nav").css("height", height-55);
	//滚动滚动条的时候，设置边框
	$(".data-table-content").scroll(function(){
		if($(".data-table-content").scrollTop() > 0){
			//$(".data-thead").css({"position": "fixed","width": "96%"});
			$(".data-table-top").css("border-bottom", "1px solid white");
		}else{
			$(".data-table-top").css("border-bottom", "0");
		}
	});
	//点击选择cpu
	$(".cpu-group .item").click(function(){
		$(".cpu-group .item").each(function(){
			$(this).children("img").attr("src", "/VMManager/img/selectRadio.png");
		});
		$(this).children("img").attr("src", "/VMManager/img/selectedRadio.png");
		if($(this).children("input").val() == "other"){
			$("#cpu-number").show().focus();
			
		}else{
			$("#cpu-number").hide();
		}
	});
	$(".ram-group .item").click(function(){
		$(".ram-group .item").each(function(){
			$(this).children("img").attr("src", "/VMManager/img/selectRadio.png");
		});
		$(this).children("img").attr("src", "/VMManager/img/selectedRadio.png");
		if($(this).children("input").val() == "other"){
			$("#ram-number").show().focus();
		}else{
			$("#ram-number").hide();
		}
	});
	
//	setVMLineLeft(width);
	
	$("#hide-hostcomputer-details").hide();
	$("#hide-colony-details").hide();
	$(".hidden-tr").hide();
	
	$("#popover-local-user").popover({html:true});
	
	//展现被隐藏的内容
	$("#show-colony-details").click(function(){
		if($("#hide-colony-details").css("display") == "none"){
			$("#hide-colony-details").show();
		}else{
			$("#hide-colony-details").hide();
		}
	});
	$("#show-hostcomputer-details").click(function(){
		if($(this).closest("tr").next().css("display") == "none"){
			$("#hide-hostcomputer-details").show();
		}else{
			$("#hide-hostcomputer-details").hide();
		}
	});
	
	$(".show-details").click(function(){
		if($(this).closest("tr").next().css("display") == "none"){
			$(".hidden-tr").each(function(){
				if($(this).css("display") != "none"){
					$(this).hide();
					$(this).prev("tr").find(".show-details").removeClass("glyphicon-chevron-up");
					$(this).prev("tr").find(".show-details").addClass("glyphicon-chevron-down");
				}
			});
			$(this).closest("tr").next().show();
			$(this).removeClass("glyphicon-chevron-down");
			$(this).addClass("glyphicon-chevron-up");
		}else{
			$(this).closest("tr").next().hide();
			$(this).removeClass("glyphicon-chevron-up");
			$(this).addClass("glyphicon-chevron-down");
		}
	});
	//选中下拉框的内容，赋值给input
	$("#select-os li").click(function(){
		$("#selected-os").val($(this).children("a").html());
		$("#select-os").hide();
	});
	$("#selected-os").click(function(){
		if($("#select-os").css("display") == "none"){
			$("#select-os").show();
		}else{
			$("#select-os").hide();
		}
	});
	$(".show-os").click(function(){
		if($("#select-os").css("display") == "none"){
			$("#select-os").show();
		}else{
			$("#select-os").hide();
		}
	});
	//选中下拉框的内容，赋值给input
	$("#select-state li").click(function(){
		$("#selected-state").val($(this).children("a").html());
		$("#select-state").hide();
	});
	$("#selected-state").click(function(){
		if($("#select-state").css("display") == "none"){
			$("#select-state").show();
		}else{
			$("#select-state").hide();
		}
	});
	$(".show-state").click(function(){
		if($("#select-state").css("display") == "none"){
			$("#select-state").show();
		}else{
			$("#select-state").hide();
		}
	});
	
	//使所有的textarea标签都是空值
//	$("textarea").val("");
	var li_record;
	
	$(".nav-li").click(function() {
		$(".nav-li").each(function(i) {
			if($(this).hasClass("active")){
				li_record = i;
				// $(this).children("div").css("display", "none");
				$(this).removeClass("active");
			}
		});
		$(this).addClass("active");
		$(this).css("background","#d2d0d0");
		// $(this).children("div").css("display", "block");
		if($(this).children("div").length > 0){
			$(this).children().children("i")
					.css("color","white").addClass("glyphicon-chevron-down")
					.removeClass("glyphicon-chevron-left");
		}else{
			$(this).children().children("i")
					.css("color","white").addClass("glyphicon-chevron-left")
					.removeClass("glyphicon-chevron-down");
		}
	});
	
	//点击全选，下面用户全部被选中
	$("#all_cb").click(function() {
		var length = $("input[name='checkbox']").length;
		if ($(this).is(":checked")) {
			$("input[name='checkbox']").prop("checked", true);
			$(".show-selected").children("span").html(length);
			$("#delete-admin-user").attr("disabled",false);
			$("#delete-vm").attr("disabled",false);
		} else {
			$("input[name='checkbox']").prop("checked", false);
			$(".show-selected").children("span").html(0);
			$("#delete-admin-user").attr("disabled",true);
			$("#delete-vm").attr("disabled",true);
		}
		if(length == 1){
			$("#edit-admin-user").attr("disabled",false);
			setVmOperationShowSituation($(".data-table-tbody tr"));
		}
	});
	//点击checkbox
	$(".select-td").on("click", "input[name='checkbox']", function(){
		var len = $("input[name='checkbox']").length;
		var count = getSelectedCount();
		var status;
		if(count == len){
			$("#all_cb").prop("checked", true);
		}else{
			$("#all_cb").prop("checked", false);
		}
		$(".show-selected").children("span").html(count);
		if(count == 1){
			$("#edit-admin-user").attr("disabled",false);
			$("#delete-admin-user").attr("disabled",false);
			//判断该点击事件是选中还是取消，若取消则选择唯一一个被选中的tr
			if($(this).is(":checked")){
				setVmOperationShowSituation($(this).closest("tr"));
			}else{
				setVmOperationShowSituation($("input[name='checkbox']:checked").closest("tr"));
			}
		}else{
			if(count == 0){
				$("#delete-admin-user").attr("disabled",true);
				$("#delete-vm").attr("disabled",true);
			}else{
				//点击事件是被选中事件
				if($(this).is(":checked")){
					status = getStatusByTr($(this).closest("tr"));
					status = parseInt(status);
					if(status == -1 || status == 6){
						$("#delete-vm").attr("disabled",false);
					}else{
						$("#delete-vm").attr("disabled",true);
					}
				}else{
					count = 0;
					var showDeleteCount = 0;//被选中的selectbox的虚拟机是“关闭”或者“不可用”状态的总个数
					$("input[name='checkbox']:checked").each(function(){
						count++;
						status = parseInt(getStatusByTr($(this).closest("tr")));
						if(status != -1 && status != 6){
							$("#delete-vm").attr("disabled",true);
						}else{
							showDeleteCount++;
						}
					});
					if(showDeleteCount == count){
						$("#delete-vm").attr("disabled",false);
					}
				}
			}
			$("#launch-vm").attr("disabled",true);
			$("#restart-vm").attr("disabled",true);
			$("#close-vm").attr("disabled",true);
			$("#edit-vm").attr("disabled",true);
			$("#edit-admin-user").attr("disabled",true);
		}
	});
	//重置
	$(".content-search-submit").children("button").eq(1).click(function(){
		$(".search-condition").find("input").each(function(){
			$(this).val("");
		});
		$("#selected-os").val("");
	});
	//设置取消按钮的点击事件
	$(".cancle-btn").click(function(){
		window.history.back(-1);
	});
	//设置确定按钮的点击事件
	$(".ok-btn").click(function(){
		zeroModal.confirm({
			content:"是否提交修改",
			ok:true,
			okFn:function(){
				alert("ok");
			}
		});
		return false;
	});
	//点击hear出现任务消息
	$("#head-main").parent("a").webuiPopover({
			animation:'pop',
			placement:'bottom',
			width: 150,
			content:"无任务消息"
	});
	
	//点击选择集群
	$("#select-cluster li").click(function(){
		$("#selected-cluster").val($(this).children("a").html());
		$("#select-cluster").hide();
	});
	$(".show-cluster").click(function(){
		if($("#select-cluster").css("display") == "none"){
			$("#select-cluster").show();
		}else{
			$("#select-cluster").hide();
		}
	});
	$("#selected-cluster").click(function(){
		if($("#select-cluster").css("display") == "none"){
			$("#select-cluster").show();
		}else{
			$("#select-cluster").hide();
		}
	});
	//如果集群为一个默认被填入input
	var clusterLen = $("#select-cluster").children("li").length;
	if(clusterLen == 1){
		$("#selected-cluster").val($("#select-cluster li").eq(0).children("a").html());
	}
});
/*function setVMLineLeft(width){
	if(width < 1370){
		$(".vm-line").css("left", "37%");
	}else if(width > 1370){
		$(".vm-line").css("left", "35%");
	}
}*/


//监控checkBox被选中的个数
function monitorCheckBox(){
	var count = 0;
	var len = $("input[name='checkbox']").length;
	$("input[name='checkbox']").each(function(){
		if($(this).is(":checked")){
			count++;
		}
	});
	$(".show-selected").children("span").html(count);
	if(count == 1){
		$("#edit-admin-user").attr("disabled",false);
		$("#delete-admin-user").attr("disabled",false);
	}else{
		if(count == 0){
			$("#delete-admin-user").attr("disabled",true);
		}
		$("#edit-admin-user").attr("disabled",true);
	}
}

function generateVMshowContent(data) {
	var $vmContentContainer = $(".show-vm-info");
	$vmContentContainer.children("tr").each(function() {
		if ($(this).attr("class") != "vm-no-data") {
			$(this).remove();
		}
	});
	if (data.length > 0) {
		var vmInstancesJson = data;
		for (var i = 0; i < vmInstancesJson.length; i++) {
			$vmContentContainer.append("<tr vid='" + vmInstancesJson[i].uuid
					+ "'><td><input type='radio' "
					+ "name='select-mirror' value='" + vmInstancesJson[i].name
					+ "'/></td><td>" + vmInstancesJson[i].name + "</td></tr>");
		}
		
		$(".vm-no-data").hide();
	} else {
		$(".vm-no-data").show();
	}
}

function setCircleProgressColorInRange(setObj, val){
	if( val > 70 && val <= 90){
		setObj.css({
			"width": val+"%"}).toggleClass("progress-bar-warning");
	}else if(val > 90){
		setObj.css({
			"width": val+"%"}).toggleClass("progress-bar-danger");
	}else{
		setObj.css({
			"width": val+"%"}).toggleClass("progress-bar-success");
	}
}

function getStatusByTr(tr){
	return tr.children("td").eq(3).children().attr("status");
}
function getSelectedCount(){
	var count = 0;
	$("input[name='checkbox']").each(function(){
		if($(this).is(":checked")){
			count++;
		}
	});
	return count;
}
function setVmOperationShowSituation(obj){
	var status = getStatusByTr(obj);
	status = parseInt(status);
	switch (status) {
	case -1: //不可用
		$("#delete-vm").attr("disabled",false);
		break;
	case 1: //可用
		break;
	case 2: //创建中
		break;
	case 3: //重启中
		break;
	case 4: //关闭中
		break;
	case 5: //删除中
		break;
	case 6: //关闭
		$("#delete-vm").attr("disabled",false);
		$("#launch-vm").attr("disabled",false);
		$("#edit-vm").attr("disabled",false);
		break;
	case 7: //启动
		$("#close-vm").attr("disabled",false);
		$("#restart-vm").attr("disabled",false);
		break;
	case 8: //启动中
		break;
	default:
		break;
	}
}