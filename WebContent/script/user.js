$(function(){
	$("#add-admin-user").click(function(){
		location.href = "add_user.html";
	});
	$("#edit-admin-user").click(function(){
		location.href = "edit_user.html";
	});
	$("#modify_psw").click(function(){
		
		var tid = $("tbody").children("tr").eq(0).attr("tid");
		console.log(tid);
//		jQuery.ajax({
//			dataType: "json",
//			data: {
//				tid:tid
//			},
//			url: "showModifyPassword"
//		});
		location.href = "showModifyPassword?tid="+tid;
	});
	$(".modify-password-btn").click(function(){
		var password = $("#user_psd").val();
		var passwordConf = $("#user_psd_conf").val();
		if(!password || !passwordConf){
			if(!password){
				$(".warming-info").show();
				$(".warming-info").children().children("label").html("密码不能为空！");
				return;
			}else if(!passwordConf){
				$(".warming-info").show();
				$(".warming-info").children().children("label").html("确认密码不能为空！");
				return;
			}
		}
		if(password && passwordConf){
			if(password != passwordConf){
				$(".warming-info").show();
				$(".warming-info").children().children("label").html("两次输入的密码不一致！");
				return;
			}
			
			//进行密码验证(密码中必须包含字母、数字、特称字符，至少8个字符，最多30个字符)
			/*var regex = new RegExp('(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{6,16}');
			if(!regex.test(password)){
				$(".warming-info").show();
				$(".warming-info").children().children("label").html("密码不符合规则，请重新输入！");
				return;
			}*/
		}
		jQuery.ajax({
			dataType: "json",
			url: "modifyPassword",
			data:{
				password:password
			},
			success: function(data){
				if(data.data){
					console.log(data.data);
					zeroModal.success({
						content:"操作成功,点击返回登入界面",
						ok:true,
						okFn:function(){
							location.href = "login.jsp";
						}
					});
					return false;
				}
			},
			error:function(data){
				alert(data.data);
			}
			
		});
	});
	
	$("#edit_local_user").click(function(){
		var tid = $("tbody").children("tr").eq(0).attr("tid");
		location.href = "showEditUser?tid="+tid;
	});
	
	$(".configure-user").click(function(){
		var id = $(".content-form").attr("tid");
		var realName = $("#user_real_name").val();
		var email = $("#user_mail").val();
		var description = $("#user_desc").val();
		jQuery.ajax({
			dataType: "json",
			url: "editUser",
			data:{
				id:id,
				realName:realName,
				email:email,
				description:description
			},
			success:function(data){
				console.log(data.data);
				if(data.data){
					location.href = "showLocalUser";
//					zeroModal.success({
//						content:"操作成功",
//						ok:true,
//						okFn:function(){
//							location.href = "showLocalUser";
//						}
//					});
					return false;
				}
			},
			error:function(){
				alert(1);
			}
		});
	});
});
