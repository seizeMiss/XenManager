$(function(){
	var height = $(document.body).height();
	
	setDataTableHeight(height);
	
	$("#add-vm").click(function(){
		location.href = "add_vm.html";
	});
	$("#edit-vm").click(function(){
		location.href = "edit_vm.html";
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

function setDataTableHeight(height){
	if(height > 768){
		$(".data-table-content").css("height", "475px");
	}else{
		$(".data-table-content").css("height", "350px");
	}
}
