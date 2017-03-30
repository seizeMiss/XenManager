$(function() {
	$("#image_desc").val("");

	$("#add-image").click(function() {
		location.href = "showAddImage";
	});
	$("#search-vm").click(function() {
		var searchContent = $("#search-content").val();
		if (searchContent.length > 20) {
			$("#search-content").select();
			$(".search-warning-info").show();
			return false;
		}
		$(".search-warning-info").hide();
		$.ajax({
			dataType : "json",
			url : "searchVmByName",
			data : {
				searchContent : searchContent
			},
			success : function(data) {
				generateVMshowContent(data);
			},
			error : function(data) {
				alert(1);
			}
		});
	});
	$(".add-image-btn").bind("click",addImage);
});
function addImage(){
	var clusterName = $("#selected-cluster").val();
	var clusterId = "";
	$("#select-cluster").children("li").each(function(){
		if($(this).children("a").html() == clusterName){
			clusterId = $(this).attr("cid");
		}
	});
	var imageName = $("#image-name").val();
	var imageDecs = $("#image_desc").val();
	var imageVmName = $("input[name='select-mirror']:checked").val();
	var imageVmUuid = $("input[name='select-mirror']:checked").closest("tr").attr("vid");
	
	if(!clusterName){
		$(".search-warning-info").hide();
		$(".image-name-warning-info").hide();
		$(".cluster-warning-info").show();
		return false;
	}
	if(!imageName){
		$(".search-warning-info").hide();
		$(".image-name-warning-info").show();
		$(".cluster-warning-info").hide();
		$("#image-name").focus();
		return false;
	}
	if(!imageVmName){
		$(".search-warning-info").show();
		$(".search-warning-info").children("span").html("请选择虚拟机！");
		$(".image-name-warning-info").hide();
		$(".cluster-warning-info").hide();
		return false;
	}
	
	$.ajax({
		dataType:"json",
		url:"addImage",
		type:"post",
		data:{
			clusterId:clusterId,
			imageName:imageName,
			imageDecs:imageDecs,
			imageVmUuid:imageVmUuid
		},
		success:function(data){
			if(data.data){
				location.href = "showImage";
				/*zeroModal.confirm({
					content:"是否添加",
					ok:true,
					okFn:function(){
						
					}
				});*/
			}
		}
	
	});
	
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
			$vmContentContainer.append("<tr vid='" + vmInstancesJson[i].id
					+ "'><td><input type='radio' "
					+ "name='select-mirror' value='" + vmInstancesJson[i].name
					+ "'/></td><td>" + vmInstancesJson[i].name + "</td></tr>");
		}
		
		$(".vm-no-data").hide();
	} else {
		$(".vm-no-data").show();
	}
}
