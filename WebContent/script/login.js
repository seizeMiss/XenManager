$(function() {
	//点击切换明文查看密码
	$("#psw-label").click(function() {
		//求换图标
		$(this).toggleClass("glyphicon-eye-close");
		var $psw = $("#input-psw");
		var $psw_clear = $("#input-psw-clear");
		//求换不同类型输入框
		if ($psw.css("display") == "none") {
			$psw.css("display", "block");
		} else {
			$psw.css("display", "none");
		}
		if ($psw_clear.css("display") == "none") {
			$psw_clear.css("display", "block");
		} else {
			$psw_clear.css("display", "none");
		}
		//赋值
		if ($psw.val()) {
			$psw_clear.val($psw.val());
		}
		if ($psw_clear.val()) {
			$psw.val($psw_clear.val());
		}
	});
	
	$("#login").click(function(){
		$(".login-error").show();
//		location.href = "index.html";
	});
});
