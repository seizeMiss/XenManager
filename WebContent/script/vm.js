$(function(){
	var height = $(document.body).height();
	
	setDataTableHeight(height);
	
	$("#add-vm").click(function(){
		location.href = "showAddVm";
	});
	
	$("edit-vm").bind("click", showEditVM);
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
	
	$("#delete-vm").bind("click", deleteVms);
	$(".add-vm-btn").bind("click", addVm);
	$(".edit-vm-btn").bind("click", editVm);
	$("#launch-vm").bind("click",launchVm);
	$("#restart-vm").bind("click",restartVm);
	$("#close-vm").bind("click",closeVm);
	$("#search-image").bind("click",searchImage);
	
	//取消默认
	$(document).bind("contextmenu", function(e){
		return false;
	});
	$(".data-table-tbody").on("contextmenu","tr",function(e){
		$("body").css("height","500px","overflow-y","show");
		rightHandClick(e, $(this));
		return false;
	});
});

function showEditVM(){
	var vid = getVmIdBySelected();
	location.href = "showEditVm?vid=" + vid;
}

function rightHandClick(e, obj){
	if(!obj.find("input[name='checkbox']").is(":checked")){
		obj.parent("tbody").find("input[name='checkbox']").each(function(){
			if($(this).is(":checked")){
				$(this).prop("checked", false);
			}
		});
		obj.find("input[name='checkbox']").prop("checked", true);
	}
	$(".show-selected").find("span").html(1);
	var status = obj.children("td").eq(3).find("span").attr("status");
	status = parseInt(status);
	var items = [];
	if(status == 6){//关闭
		items = [
		         { title: '启动', icon: 'ion-plus-round', fn: launchVm},
		         { title: '重启', icon: 'ion-person', fn: restartVm, disabled: true },
		         { title: '关闭', icon: 'ion-help-buoy', fn: closeVm, disabled: true },
		         { title: '修改', icon: 'ion-minus-circled', fn: showEditVM},
		         { title: '删除', icon: 'ion-eye-disabled', fn: deleteVms},
		     ];
		$("#launch-vm").attr("disabled",false);
		$("#restart-vm").attr("disabled",true);
		$("#close-vm").attr("disabled", true);
		$("#edit-vm").attr("disabled",false);
		$("#delete-vm").attr("disabled",false);
	}else if(status == 7){//启动
		items = [
		         { title: '启动', icon: 'ion-plus-round', fn: launchVm, disabled: true},
		         { title: '重启', icon: 'ion-person', fn: restartVm },
		         { title: '关闭', icon: 'ion-help-buoy', fn: closeVm },
		         { title: '修改', icon: 'ion-minus-circled', fn: showEditVM, disabled: true },
		         { title: '删除', icon: 'ion-eye-disabled', fn: deleteVms, disabled: true },
		     ];
		$("#launch-vm").attr("disabled",true);
		$("#restart-vm").attr("disabled",false);
		$("#close-vm").attr("disabled",false);
		$("#edit-vm").attr("disabled",true);
		$("#delete-vm").attr("disabled",true);
	}else{
		items = [
		         { title: '启动', icon: 'ion-plus-round', fn: launchVm, disabled: true  },
		         { title: '重启', icon: 'ion-person', fn: restartVm , disabled: true },
		         { title: '关闭', icon: 'ion-help-buoy', fn: closeVm, disabled: true  },
		         { title: '修改', icon: 'ion-minus-circled', fn: showEditVM, disabled: true },
		         { title: '删除', icon: 'ion-eye-disabled', fn: deleteVms, disabled: true },
		     ];
	}
 
    basicContext.show(items, e.originalEvent);
}

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
		$(".cluster-name-warning-info").find("label").html("请输入集群的名称！");
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
		if(!cpuNumber){
			$(".cpu-number-edit-warning-info").show().find("label").html("cpu个数不能为空！");
			return false;
		}
	}
	var memoryNumber = $("input[name='select-ram']:checked").val();
	if(memoryNumber && memoryNumber == "other"){
		memoryNumber = $("input[name='select-ram']:checked").parent().find("#ram-number").val();
		if(!memoryNumber){
			$(".memory-number-edit-warning-info").show().find("label").html("内存大小不能为空！");
			return false;
		}
	}
	cpuNumber = parseInt(cpuNumber);
	if(cpuNumber > 16){
		$(".cpu-number-edit-warning-info").show().find("label").html("cpu个数不足，请看说明进行操作！");
		$(".memory-number-edit-warning-info").hide();
		return false;
	}
	memoryNumber = parseInt(memoryNumber);
	if(memoryNumber > 16){
		$(".memory-number-edit-warning-info").show().find("label").html("内存不足，请看说明进行操作！");
		$(".cpu-number-edit-warning-info").hide();
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
			var callbackResult = data.data;
			if(callbackResult){
				if(callbackResult == "success"){
					location.href = "showVM";
				}else if(callbackResult == "cpu_failure"){
					$(".cpu-number-edit-warning-info").show().find("label").html("系统cpu个数不足！");
				}
			}
		},
		error : function() {
			alert("error");
		}
	});
}
function deleteVms(){
	zeroModal.confirm({
		content : "确定要删除数据，将无法恢复？",
		ok : true,
		okFn : function() {
			var vid = getAllVmIdBySelected();
			jQuery.ajax({
				dataType : "json",
				url : "deleteVms",
				data : {
					ids : vid
				},
				success : function(data) {
					var vmInstances = data;
					for(var i = 0; i < vmInstances.length; i++){
						updateVmInfoBySelect(vmInstances[i]);
					}
				},
				error : function() {
					alert("error");
				}
			});
		}
	});
	return false;
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
function getAllVmIdBySelected(){
	var vid = "";
	var count = 0;
	$(".data-table-tbody").children("tr:not(.hidden-tr)").each(function(){
		if($(this).find("input[name='checkbox']").is(":checked")){
			if(count == 0){
				vid = $(this).attr("vid");
			}else{
				vid = $(this).attr("vid") + ";" + vid;
			}
			count++;
		}
	});
	return vid;
}
