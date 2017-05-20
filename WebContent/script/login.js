$(function() {
	$("#input-username").focus();
	//点击切换明文查看密码
	$("#psw-label").click(function() {
		//切换图标
		$(this).toggleClass("glyphicon-eye-close");
		var $psw = $("#input-psw");
		var $psw_clear = $("#input-psw-clear");
		//求换不同类型输入框
		if ($psw.css("display") == "none") {
			$psw.val($psw_clear.val());
			$psw.show();
		} else {
			$psw.hide();
		}
		if ($psw_clear.css("display") == "none") {
			$psw_clear.val($psw.val());
			$psw_clear.show();
		} else {
			$psw_clear.hide();
		}
	});
	
	$("#login").click(function(){
		var userName = $("#input-username").val();
		var password = "";
		var showPassword;
		if($("#input-psw").css("display") != "none"){
			password = $("#input-psw").val();
			showPassword = $("#input-psw");
		}else{
			password = $("#input-psw-clear").val();
			showPassword = $("#input-psw-clear");
		}
		if(!userName || !password){
			if(!userName){
				$(".login-error").html("用户名不能为空").show();
				$("#input-username").focus();
				return;
			}
			if(userName && !password){
				$(".login-error").html("密码不能为空").show();
				$("#input-psw").focus();
				return;
			}
		}
		jQuery.ajax({
			dataType: "json",
			cache:false,
			url: "login",
			data: {
				userName: userName,
				password: password
			},
			success: function(data){
				if(data.data){
					window.location.href = "showIndex";
				}else{
					$(".login-error").html("登录失败，用户名或密码错误！").show();
					showPassword.focus().select();
				}
			},
			error: function(data){
				console.log(data);
			}
			
		});
	});
	
	$("body").keydown(function(){
		if(event.keyCode == "13"){
			$("#login").click();
		}
	});
});
