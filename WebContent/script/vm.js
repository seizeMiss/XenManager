$(function(){
	var height = $(document.body).height();
	
	setDataTableHeight(height);
	
	$("#add-vm").click(function(){
		location.href = "showAddVm";
	});
	$("#edit-vm").click(function(){
		var vid = getVmIdBySelected();
		location.href = "showEditVm?vid=" + vid;
	});
	//点击选择存储
	$("#select-storage li").click(function(){
		$("#selected-storage-place").val($(this).find(".storage-name").html());
		$("#select-storage").hide();
	});
	$(".show-storage").click(function(){
		if($("#select-storage").css("display") == "none"){
			$("#select-storage").show();
		}else{
			$("#select-storage").hide();
		}
	});
	$("#selected-storage-place").click(function(){
		if($("#select-storage").css("display") == "none"){
			$("#select-storage").show();
		}else{
			$("#select-storage").hide();
		}
	});
	
//	$("#delete-vm").click(function(){
//		$("input[name='checkbox']").each(function(){
//			if($(this).is(":checked")){
//				var td = $(this).parent("td");
//				td.html("<img src='../img/load.gif'/>");
//				
//				td.html("<input type='checkbox' name='checkbox' checked>");
//			}
//		});
//	});
	$("#delete-vm").bind("click", deleteVms);
	$(".add-vm-btn").bind("click", addVm);
	$(".edit-vm-btn").bind("click", editVm);
	$("#launch-vm").bind("click",launchVm);
	$("#restart-vm").bind("click",restartVm);
	$("#close-vm").bind("click",closeVm);
	$("#search-image").bind("click",searchImage);
});
function searchImage(){
	var searchContent = $("#search-content").val();
	if (searchContent.length > 20) {
		$("#search-content").select();
		$(".image-warning-info").show();
		return false;
	}
	$(".image-warning-info").hide();
	$.ajax({
		dataType : "json",
		url : "searchImageByName",
		data : {
			searchContent : searchContent
		},
		success : function(data) {
			generateVMshowContent(data);
		},
		error : function(data) {
		}
	});
	
}
function hideWarningInfo(...args){
	for(var i = 0; i < args.length; i++){
		if(i == 0){
			args[i].show();
		}else{
			args[i].hide();
		}
	}
}

function addVm(){
	var nameRule = $("#name-rule").val();
	var clusterName = $("#selected-cluster").val();
	var clusterId = "";
	$("#select-cluster").children("li").each(function(){
		if($(this).children("a").html() == clusterName){
			clusterId = $(this).attr("cid");
		}
	});
	var imageName = $("input[name='select-image']:checked").val();
	var imageId = $("input[name='select-image']:checked").closest("tr").attr("iid");
	var cpuNumber = $("input[name='select-cpu']:checked").val();
	if(cpuNumber && cpuNumber == "other"){
		cpuNumber = $("input[name='select-cpu']:checked").parent().find("#cpu-number").val();
	}
	var memoryNumber = $("input[name='select-ram']:checked").val();
	if(memoryNumber && memoryNumber == "other"){
		memoryNumber = $("input[name='select-ram']:checked").parent().find("#ram-number").val();
	}
	var storageLocation = $("#selected-storage-place").val();
	var storageId = "";
	$("#select-storage").children("li").each(function(){
		if($(this).children("a").children("span:first").html() == storageLocation){
			storageId = $(this).attr("sid");
		}
	});
	var userDisk = "";
	$("input[name='user-disk-size']").each(function(index){
		var userDiskSize = $(this).val();
		if(!userDiskSize){
			$(this).focus();
			return false;
		}
		if(index == 0){
			userDisk = userDiskSize;
		}else{
			userDisk = userDisk + ";" + userDiskSize;
		}
	});
	var params = {
			nameRule:nameRule,
			clusterId:clusterId,
			imageId:imageId,
			cpuNumber:cpuNumber,
			memoryNumber:memoryNumber,
			storageId:storageId,
			userDisk:userDisk
	}
	if(!nameRule){
		hideWarningInfo($(".vm-rule-warning-info"),$(".vm-number-warning-info"),
				$(".cluster-name-warning-info"),$(".image-warning-info"),
				$(".cpu-number-warning-info"),$(".memory-number-warning-info"),
				$(".storage-warning-info"));
		return false;
	}
	if(!clusterName){
		hideWarningInfo($(".cluster-name-warning-info"),$(".vm-number-warning-info"),
				$(".vm-rule-warning-info"),$(".image-warning-info"),
				$(".cpu-number-warning-info"),$(".memory-number-warning-info"),
				$(".storage-warning-info"));
		return false;
	}
	if(!imageName){
		hideWarningInfo($(".image-warning-info"),$(".vm-number-warning-info"),
				$(".cluster-name-warning-info"),$(".vm-rule-warning-info"),
				$(".cpu-number-warning-info"),$(".memory-number-warning-info"),
				$(".storage-warning-info"));
		$(".image-warning-info").find("label").html("请选择镜像！");
		return false;
	}
	if(!cpuNumber){
		hideWarningInfo($(".cpu-number-warning-info"),$(".vm-number-warning-info"),
				$(".cluster-name-warning-info"),$(".image-warning-info"),
				$(".memory-number-warning-info"),$(".vm-rule-warning-info"),
				$(".storage-warning-info"));
		return false;
	}else{
		cpuNumber = parseInt(cpuNumber);
		if(cpuNumber > 16){
			hideWarningInfo($(".cpu-number-warning-info"),$(".vm-number-warning-info"),
					$(".cluster-name-warning-info"),$(".image-warning-info"),
					$(".memory-number-warning-info"),$(".vm-rule-warning-info"),
					$(".storage-warning-info"));
			$(".cpu-number-warning-info").find("label").html("cpu个数够多，请看说明进行操作！");
			return false;
		}
	}
	if(!memoryNumber){
		hideWarningInfo($(".memory-number-warning-info"),$(".vm-number-warning-info"),
				$(".cluster-name-warning-info"),$(".image-warning-info"),
				$(".cpu-number-warning-info"),$(".vm-rule-warning-info"),
				$(".vm-rule-warning-info"));
		return false;
	}else{
		memoryNumber = parseInt(memoryNumber);
		if(memoryNumber > 16){
			hideWarningInfo($(".memory-number-warning-info"),$(".vm-number-warning-info"),
					$(".cluster-name-warning-info"),$(".image-warning-info"),
					$(".cpu-number-warning-info"),$(".vm-rule-warning-info"),
					$(".vm-rule-warning-info"));
			$(".memory-number-warning-info").find("label").html("内存不足，请看说明进行操作！");
			return false;
		}
	}
	if(!storageLocation){
		hideWarningInfo($(".storage-warning-info"),$(".vm-number-warning-info"),
				$(".cluster-name-warning-info"),$(".image-warning-info"),
				$(".cpu-number-warning-info"),$(".memory-number-warning-info"),
				$(".vm-rule-warning-info"));
		return false;
	}
	
	$.ajax({
		dataType: "json",
		url: "addVm",
		data:params,
		success:function(data){
			var result = data.data;
			if(result){
				if(result == "success"){
					location.href = "showVM";
				}else if(result == "cpu-failure"){
					hideWarningInfo($(".cpu-number-warning-info"),$(".vm-number-warning-info"),
							$(".cluster-name-warning-info"),$(".image-warning-info"),
							$(".memory-number-warning-info"),$(".vm-rule-warning-info"),
							$(".storage-warning-info"));
					$(".cpu-number-warning-info").find("label").html("cpu个数够多，请看说明进行操作！");
				}else{
					hideWarningInfo($(".storage-warning-info"),$(".vm-number-warning-info"),
							$(".cluster-name-warning-info"),$(".image-warning-info"),
							$(".cpu-number-warning-info"),$(".memory-number-warning-info"),
							$(".vm-rule-warning-info"));
					$(".storage-warning-info").find("label").html("储存不足，请重新输入！");
				}
			}
		}
	});
}
function addVmBatch(){
	var nameRule = $("#name-rule").val();
	var vmNumber = $("#vm-number").val();
	var clusterName = $("#selected-cluster").val();
	var clusterId = "";
	$("#select-cluster").children("li").each(function(){
		if($(this).children("a").html() == clusterName){
			clusterId = $(this).attr("cid");
		}
	});
	var imageName = $("input[name='select-image']:checked").val();
	var imageId = $("input[name='select-image']:checked").closest("tr").attr("iid");
	var cpuNumber = $("input[name='select-cpu']:checked").val();
	if(cpuNumber && cpuNumber == "other"){
		cpuNumber = $("input[name='select-cpu']:checked").parent().find("#cpu-number").val();
	}
	var memoryNumber = $("input[name='select-ram']:checked").val();
	if(memoryNumber && memoryNumber == "other"){
		memoryNumber = $("input[name='select-ram']:checked").parent().find("#ram-number").val();
	}
	var storageLocation = $("#selected-storage-place").val();
	var storageId = "";
	$("#select-storage").children("li").each(function(){
		if($(this).children("a").html() == storageLocation){
			storageId = $(this).attr("sid");
		}
	});
	var userDisk = "";
	$("input[name='user-disk-size']").each(function(index){
		var userDiskSize = $(this).val();
		if(!userDiskSize){
			$(this).focus();
			return false;
		}
		if(index == 0){
			userDisk = userDiskSize;
		}else{
			userDisk = userDisk + ";" + userDiskSize;
		}
	});
	var params = {
			nameRule:nameRule,
			vmNumber:vmNumber,
			clusterId:clusterId,
			imageId:imageId,
			cpuNumber:cpuNumber,
			memoryNumber:memoryNumber,
			storageId:storageId,
			userDisk:userDisk
	}
	if(!nameRule){
		hideWarningInfo($(".vm-rule-warning-info"),$(".vm-number-warning-info"),
				$(".cluster-name-warning-info"),$(".image-warning-info"),
				$(".cpu-number-warning-info"),$(".memory-number-warning-info"),
				$(".storage-warning-info"));
		return false;
	}
	if(!vmNumber){
		hideWarningInfo($(".vm-number-warning-info"),$(".vm-rule-warning-info"),
				$(".cluster-name-warning-info"),$(".image-warning-info"),
				$(".cpu-number-warning-info"),$(".memory-number-warning-info"),
				$(".storage-warning-info"));
		return false;
	}else{
		vmNumber = parseInt(vmNumber);
		if(vmNumber > 500){
			hideWarningInfo($(".vm-number-warning-info"),$(".vm-rule-warning-info"),
					$(".cluster-name-warning-info"),$(".image-warning-info"),
					$(".cpu-number-warning-info"),$(".memory-number-warning-info"),
					$(".storage-warning-info"));
			$(".vm-number-warning-info").find("label").html("输入的虚拟机个数够多，请看说明进行操作！");
			return false;
		}
	}
	if(!clusterName){
		hideWarningInfo($(".cluster-name-warning-info"),$(".vm-number-warning-info"),
				$(".vm-rule-warning-info"),$(".image-warning-info"),
				$(".cpu-number-warning-info"),$(".memory-number-warning-info"),
				$(".storage-warning-info"));
		return false;
	}
	if(!imageName){
		hideWarningInfo($(".image-warning-info"),$(".vm-number-warning-info"),
				$(".cluster-name-warning-info"),$(".vm-rule-warning-info"),
				$(".cpu-number-warning-info"),$(".memory-number-warning-info"),
				$(".storage-warning-info"));
		$(".image-warning-info").find("label").html("请选择镜像！");
		return false;
	}
	if(!cpuNumber){
		hideWarningInfo($(".cpu-number-warning-info"),$(".vm-number-warning-info"),
				$(".cluster-name-warning-info"),$(".image-warning-info"),
				$(".memory-number-warning-info"),$(".vm-rule-warning-info"),
				$(".storage-warning-info"));
		return false;
	}else{
		cpuNumber = parseInt(cpuNumber);
		if(cpuNumber > 16){
			hideWarningInfo($(".cpu-number-warning-info"),$(".vm-number-warning-info"),
					$(".cluster-name-warning-info"),$(".image-warning-info"),
					$(".memory-number-warning-info"),$(".vm-rule-warning-info"),
					$(".storage-warning-info"));
			$(".cpu-number-warning-info").find("label").html("cpu个数够多，请看说明进行操作！");
			return false;
		}
	}
	if(!memoryNumber){
		hideWarningInfo($(".memory-number-warning-info"),$(".vm-number-warning-info"),
				$(".cluster-name-warning-info"),$(".image-warning-info"),
				$(".cpu-number-warning-info"),$(".vm-rule-warning-info"),
				$(".vm-rule-warning-info"));
		return false;
	}else{
		memoryNumber = parseInt(memoryNumber);
		if(memoryNumber > 16){
			hideWarningInfo($(".memory-number-warning-info"),$(".vm-number-warning-info"),
					$(".cluster-name-warning-info"),$(".image-warning-info"),
					$(".cpu-number-warning-info"),$(".vm-rule-warning-info"),
					$(".vm-rule-warning-info"));
			$(".memory-number-warning-info").find("label").html("内存不足，请看说明进行操作！");
			return false;
		}
	}
	if(!storageId){
		hideWarningInfo($(".storage-warning-info"),$(".vm-number-warning-info"),
				$(".cluster-name-warning-info"),$(".image-warning-info"),
				$(".cpu-number-warning-info"),$(".memory-number-warning-info"),
				$(".vm-rule-warning-info"));
		return false;
	}
	
	$.ajax({
		dataType: "json",
		url: "addVm",
		data:params,
		success:function(data){
			var result = data.data;
			if(result){
				if(result == "success"){
					location.href = "showVM";
				}else if(result == "cpu-failure"){
					hideWarningInfo($(".cpu-number-warning-info"),$(".vm-number-warning-info"),
							$(".cluster-name-warning-info"),$(".image-warning-info"),
							$(".memory-number-warning-info"),$(".vm-rule-warning-info"),
							$(".storage-warning-info"));
					$(".cpu-number-warning-info").find("label").html("cpu个数够多，请看说明进行操作！");
				}else{
					hideWarningInfo($(".storage-warning-info"),$(".vm-number-warning-info"),
							$(".cluster-name-warning-info"),$(".image-warning-info"),
							$(".cpu-number-warning-info"),$(".memory-number-warning-info"),
							$(".vm-rule-warning-info"));
					$(".storage-warning-info").find("label").html("储存不足，请重新输入！");
				}
			}
		}
	});
}

function editVm(){
	var cpuNumber = $("input[name='select-cpu']:checked").val();
	if(cpuNumber && cpuNumber == "other"){
		cpuNumber = $("input[name='select-cpu']:checked").parent().find("#cpu-number").val();
	}
	var memoryNumber = $("input[name='select-ram']:checked").val();
	if(memoryNumber && memoryNumber == "other"){
		memoryNumber = $("input[name='select-ram']:checked").parent().find("#ram-number").val();
	}
	cpuNumber = parseInt(cpuNumber);
	if(cpuNumber > 16){
		$(".cpu-number-warning-info").find("label").html("cpu个数够多，请看说明进行操作！");
		return false;
	}
	memoryNumber = parseInt(memoryNumber);
	if(memoryNumber > 16){
		hideWarningInfo($(".memory-number-warning-info"),$(".vm-number-warning-info"),
				$(".cluster-name-warning-info"),$(".image-warning-info"),
				$(".cpu-number-warning-info"),$(".vm-rule-warning-info"),
				$(".vm-rule-warning-info"));
		$(".memory-number-warning-info").find("label").html("内存不足，请看说明进行操作！");
		return false;
	}
	jQuery.ajax({
		dataType : "json",
		url : "editVmBySelected",
		data : {
			cpuNumber : cpuNumber,
			memoryNumber : memoryNumber
		},
		success : function(data) {
			if(data.data){
				location.href = "showVM";
			}
		},
		error : function() {
			alert("error");
		}
	});
}
function deleteVms(){
}

function launchVm(){
	zeroModal.confirm({
		content : "确定要执行开机？",
		ok : true,
		okFn : function() {
			var vid = getVmIdBySelected();
			jQuery.ajax({
				dataType : "json",
				url : "openVmByselected",
				data : {
					vid : vid
				},
				success : function(data) {
					var vmInstance = data;
					updateVmInfoBySelect(vmInstance);
				},
				error : function() {
					alert("error");
				}
			});
		}
	});
	return false;
}
function restartVm(){
	zeroModal.confirm({
		content : "确定要执行重启？",
		ok : true,
		okFn : function() {
			var vid = getVmIdBySelected();
			jQuery.ajax({
				dataType : "json",
				url : "restartVmByselected",
				data : {
					vid : vid
				},
				success : function(data) {
					var vmInstance = data;
					updateVmInfoBySelect(vmInstance);
				},
				error : function() {
					alert("error");
				}
			});
		}
	});
	return false;
}
function closeVm(){
	zeroModal.confirm({
		content : "确定要执行关机？",
		ok : true,
		okFn : function() {
			var vid = getVmIdBySelected();
			jQuery.ajax({
				dataType : "json",
				url : "closeVmByselected",
				data : {
					vid : vid
				},
				success : function(data) {
					var vmInstance = data;
					updateVmInfoBySelect(vmInstance);
				},
				error : function() {
					alert("error");
				}
			});
		}
	});
	return false;
}

function setDataTableHeight(height){
	if(height > 768){
		$(".data-table-content").css("height", "475px");
	}else{
		$(".data-table-content").css("height", "350px");
	}
}
function updateVmInfoBySelect(data){
	var vid = data.id;
	var vmStatus = statusMapping(data.status);
	$(".data-table-tbody").children("tr:not(.hidden-tr)").each(function(){
		if($(this).attr("vid") == vid){
			var td = $(this).children("td:first");
			td.html("<img src='/VMManager/img/load.gif'/>");
			$(this).children("td").eq(3).children().html(vmStatus);
		}
	});
}
function statusMapping(vmStatus){
	if(vmStatus){
		if(vmStatus == -1){
			return "不可用";
		}else if(vmStatus == 1){
			return "可用";
		}else if(vmStatus == 2){
			return "创建中";
		}else if(vmStatus == 3){
			return "重启中";
		}else if(vmStatus == 4){
			return "关闭中";
		}else if(vmStatus == 5){
			return "删除中";
		}else if(vmStatus == 6){
			return "关闭";
		}else if(vmStatus == 7){
			return "启动";
		}else if(vmStatus == 8){
			return "启动中";
		}
	}
}
function getVmIdBySelected(){
	var vid = "";
	$(".data-table-tbody").children("tr:not(.hidden-tr)").each(function(){
		if($(this).find("input[name='checkbox']").is(":checked")){
			vid = $(this).attr("vid");
		}
	});
	return vid;
}
