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
	
	$("#delete-vm").click(function(){
		$("input[name='checkbox']").each(function(){
			if($(this).is(":checked")){
				var td = $(this).parent("td");
				td.html("<img src='../img/load.gif'/>");
				
				td.html("<input type='checkbox' name='checkbox' checked>");
			}
		});
	});
});

function addVm(){
	
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
