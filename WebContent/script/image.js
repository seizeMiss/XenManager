$(function() {
	$("#image_desc").val("");
	
	$("#add-image").click(function() {
		location.href = "showAddImage";
	});
	$("#search-vm").bind("click", searchVmByName);
	$(".add-image-btn").bind("click",addImage);
	$("#delete-image").bind("click", deleteImages);
});
function searchVmByName(){
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
		}
	});
}
function deleteImages(){
	zeroModal.confirm({
		content : "是否删除，删除后数据将丢失？",
		ok : true,
		okFn : function() {
			var tid = "";
			$("input[name='checkbox']").each(function() {
				if ($(this).is(":checked")) {
					tid = $(this).closest("tr").attr("iid");
				}
			});
			jQuery.ajax({
				dataType : "json",
				url : "deleteImages",
				data : {
					tid : tid
				},
				success : function(data) {
					if (data.data) {
						location.href = "showImage"
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

