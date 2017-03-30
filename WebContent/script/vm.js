$(function(){
	var height = $(document.body).height();
	
	setDataTableHeight(height);
	
	$("#add-vm").click(function(){
		location.href = "showAddVm";
	});
	$("#edit-vm").click(function(){
		location.href = "edit_vm.html";
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
	
	$("#delete-vm").click(function(){
		$("input[name='checkbox']").each(function(){
			if($(this).is(":checked")){
				var td = $(this).parent("td");
				td.html("<img src='../img/load.gif'/>");
				
				td.html("<input type='checkbox' name='checkbox' checked>");
			}
		});
	});
	$(".add-vm-btn").bind("click", addVm);
});
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
	var vmNumber = $("#vm-number").val();
	var clusterName = $("#selected-cluster").val();
	var clusterId = "";
	$("#select-cluster").children("li").each(function(){
		if($(this).children("a").html() == clusterName){
			clusterId = $(this).attr("cid");
		}
	});
	var imageName = $("input[name='select-mirror']:checked").val();
	var imageUuid = $("input[name='select-mirror']:checked").closest("tr").attr("iid");
	var cpuNumber = $("input[name='select-cpu']:checked").val();
	var memoryNumber = $("input[name='select-ram']:checked").val();
	var storageLocation = $("#selected-storage-place").val();
	var userDisk = "";
	$("input[name='user-disk-size']").each(function(index){
		if(index == 0){
			userDisk = $(this).val();
		}else{
			userDisk = userDisk + ";" + $(this).val();
		}
	});
	var params = {
			nameRule:nameRule,
			vmNumber:vmNumber,
			clusterId:clusterId,
			imageUuid:imageUuid,
			cpuNumber:cpuNumber,
			memoryNumber:memoryNumber,
			storageLocation:storageLocation,
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
		return false;
	}
	if(!cpuNumber){
		hideWarningInfo($(".cpu-number-warning-info"),$(".vm-number-warning-info"),
				$(".cluster-name-warning-info"),$(".image-warning-info"),
				$(".memory-number-warning-info"),$(".vm-rule-warning-info"),
				$(".storage-warning-info"));
		return false;
	}
	if(!memoryNumber){
		hideWarningInfo($(".memory-warning-info"),$(".vm-number-warning-info"),
				$(".cluster-name-warning-info"),$(".image-warning-info"),
				$(".cpu-number-warning-info"),$(".vm-rule-warning-info"),
				$(".vm-rule-warning-info"));
		return false;
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
		success:function(){
			if(data.data){
				
			}
		}
	});
}

function editVm(){
	
}
function deleteVm(){
	
}
function openVm(){
	
}
function rebootVm(){
	
}
function closeVm(){
	
}

function setDataTableHeight(height){
	if(height > 768){
		$(".data-table-content").css("height", "475px");
	}else{
		$(".data-table-content").css("height", "350px");
	}
}
