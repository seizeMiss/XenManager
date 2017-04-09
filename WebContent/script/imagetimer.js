//定时刷新
setInterval(() => {
	refreshImageData();
}, 1000);
function refreshImageData() {
	var vid = "";
	var count = 0;
	$(".data-table-tbody").children("tr").each(function(){
		if($(this).children("td:first").find("img").length > 0){
			if(count == 0){
				vid = $(this).attr("iid");
			}else{
				vid = $(this).attr("iid") + ";" + vid;
			}
		}
	});
	
	$.ajax({
		dataType: "json",
		data: {
			ids: vid
		},
		url : "refreshImageData",
		success : function(data) {
			showImageInfo(data);
		}
	});
}

function showImageInfo(images) {
	var targetElement = $(".data-table-tbody");
	if (images) {
		for (var i = 0; i < images.length; i++) {
			var image = images[i];
			targetElement.children("tr").each(function(){
				var id = $(this).attr("iid");
				if(id == image.id){
					if(image.status == 5){
						$(this).remove();
					}else{
						var status = imageStatusMapping(image.status);
						var underWay = isImageUnderWay(image.status);// 判断该虚拟机时候在执行中
						var td = $(this).children("td:first");
						if(underWay){
							td.html("<img src='/VMManager/img/load.gif'/>");
						}else{
							td.html("<input type='checkbox' name='checkbox'>");
						}
						$(this).children("td").eq(3).children().html(status);
						$(this).children("td").eq(4).children().html(image.imageSize + "GB");
						$("#delete-image").attr("disabled",true);
						$(".show-selected").children("span").html(getSelectedCount());
					}
				}
			});
		}
	}
}
function imageStatusMapping(status){
	switch (status) {
	case -1:
		status = "不可用";
		break;
	case 2:
		status = "创建中";
		break;
	case 1:
		status = "可用";
		break;
	case 4:
		status = "删除中";
		break;
	case 5:
		status = "已删除";
		break;
	default:
		break;
	}
	return status;
}
function isImageUnderWay(status) {
	if(status == 2 ||status == 4){
		return true;
	}
	return false;
}