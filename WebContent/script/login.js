$(function() {
	
	//点击切换明文查看密码
	$("#psw-label").click(function() {
		//求换图标
		$(this).toggleClass("glyphicon-eye-close");
		var $psw = $("#input-psw");
		var $psw_clear = $("#input-psw-clear");
		//求换不同类型输入框
		if ($psw.css("display") == "none") {
			$psw.val($psw_clear.val());
			$psw.css("display", "block");
		} else {
			$psw.css("display", "none");
		}
		if ($psw_clear.css("display") == "none") {
			$psw_clear.val($psw.val());
			$psw_clear.css("display", "block");
		} else {
			$psw_clear.css("display", "none");
		}
	});
	
	$("#login").click(function(){
		var userName = $("#input-username").val();
		var password = $("#input-psw").val();
		if(!userName || !password){
			if(!userName){
				$(".login-error").html("用户名不能为空").show();
				return;
			}
			if(userName && !password){
				$(".login-error").html("密码不能为空").show();
				return;
			}
		}
		jQuery.ajax({
			dataType: "json",
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
				}
			},
			error: function(data){
				console.log(data);
			}
			
		});
		
//		location.href = "index.html";
	});
	
	$("body").keydown(function(){
		if(event.keyCode == "13"){
			$("#login").click();
		}
	});
});
