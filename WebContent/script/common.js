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
			$(this).children("img").attr("src", "../img/selectRadio.png");
		});
		$(this).children("img").attr("src", "../img/selectedRadio.png");
		if($(this).children("input").val() == "other"){
			$("#cpu-number").show().focus();
			
		}else{
			$("#cpu-number").hide();
		}
	});
	$(".ram-group .item").click(function(){
		$(".ram-group .item").each(function(){
			$(this).children("img").attr("src", "../img/selectRadio.png");
		});
		$(this).children("img").attr("src", "../img/selectedRadio.png");
		if($(this).children("input").val() == "other"){
			$("#ram-number").show().focus();
		}else{
			$("#ram-number").hide();
		}
	});
	
	setVMLineLeft(width);
	
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
			$(this).closest("tr").next().show();
			$(this).removeClass("glyphicon-chevron-down");
			$(this).addClass("glyphicon-chevron-up");
		}else{
			$(this).closest("tr").next().hide();
			$(this).removeClass("glyphicon-chevron-up");
			$(this).addClass("glyphicon-chevron-down");
		}
	});
	
	$("#select-os li").click(function(){
		$("#selected-os").val($(this).children("a").html());
	});
	
	
	
	//使所有的textarea标签都是空值
	$("textarea").val("");
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
		$(this).children().children("i").toggleClass("glyphicon-chevron-down").css("color","white");
	});
	
	//点击全选，下面用户全部被选中
	$("#all_cb").click(function() {
		var length = $("input[name='checkbox']").length;
		if ($(this).is(":checked")) {
			$("input[name='checkbox']").prop("checked", true);
			$(".show-selected").children("span").html(length);
		} else {
			$("input[name='checkbox']").prop("checked", false);
			$(".show-selected").children("span").html(0);
		}
		
	});
	$("input[name='checkbox']").click(function(){
		var count = 0;
		var len = $("input[name='checkbox']").length;
		$("input[name='checkbox']").each(function(){
			if($(this).is(":checked")){
				count++;
			}
		});
		if(count == len){
			$("#all_cb").prop("checked", true);
		}else{
			$("#all_cb").prop("checked", false);
		}
		$(".show-selected").children("span").html(count);
		if(count != 1){
			
		}else{
			
		}
	});
	//重置
	$(".content-search-submit").children("button").eq(1).click(function(){
		$(".search-condition").children("input").each(function(){
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
			content:"任务消息"
	});
});
function setVMLineLeft(width){
	if(width < 1370){
		$(".vm-line").css("left", "37%");
	}else if(width > 1370){
		$(".vm-line").css("left", "35%");
	}
}
