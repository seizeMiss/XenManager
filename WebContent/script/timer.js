//定时刷新
setInterval(() => {
	refreshData();
}, 10000);
function refreshData() {
	var vid = "";
	var count = 0;
	$(".data-table-tbody").children("tr").each(function(){
		if($(this).children("td:first").find("img").length > 0){
			if(count == 0){
				vid = $(this).attr("vid");
			}else{
				vid = $(this).attr("vid") + ";" + vid;
			}
		}
	});
	
	$.ajax({
		dataType: "json",
		data: {
			ids: vid
		},
		url : "refreshVmData",
		success : function(data) {
			showVmInfo(data);
		}
	});
}

function showVmInfo(vmNeedInfos) {
	var targetElement = $(".data-table-tbody");
	if (vmNeedInfos) {
		for (var i = 0; i < vmNeedInfos.length; i++) {
			var vmNeedInfo = vmNeedInfos[i];
			var vmInstance = vmNeedInfo.vmInstance;
			targetElement.children("tr").each(function(){
				var vid = $(this).attr("vid");
				if(vid == vmInstance.id){
					if(vmInstance.status == 9){
						$(this).remove();
					}else{
						var status = statusMapping(vmInstance.status);
						var underWay = isUnderWay(vmInstance.status);//判断该虚拟机时候在执行中
						var td = $(this).children("td:first");
						if(underWay){
							td.html("<img src='/VMManager/img/load.gif'/>");
						}else{
							td.html("<input type='checkbox' name='checkbox'>");
						}
						$(this).children("td").eq(3).children().html(status);
						$(this).children("td").eq(3).children().attr("status",vmInstance.status);
						$("#launch-vm").attr("disabled",true);
						$("#restart-vm").attr("disabled",true);
						$("#close-vm").attr("disabled",true);
						$("#edit-vm").attr("disabled",true);
						$("#delete-vm").attr("disabled",true);
						$(".show-selected").children("span").html(getSelectedCount());
					}
				}
			});
		}
	}
}

function isUnderWay(status) {
	if(status == 0 || status == 2 || status == 3 || status == 4 || status == 5 || status == 8){
		return true;
	}else{
		return false;
	}
}
