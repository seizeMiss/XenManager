$(function() {
	// 显示添加用户界面
	$("#add-admin-user").click(function() {
		location.href = "showAddUser";
	});
	// 添加用户
	$(".add-user-btn").bind("click", addUser);

	// 编辑管理员用户
	$("#edit-admin-user").click(function() {
		var tid;
		$("input[name='checkbox']").each(function() {
			if ($(this).is(":checked")) {
				tid = $(this).closest("tr").attr("tid");
			}
		});
		location.href = "showEditUser?tid=" + tid;
	});
	// 点击修改密码
	$("#modify_psw").click(function() {
		var tid = $("tbody").children("tr").eq(0).attr("tid");
		location.href = "showModifyPassword?tid=" + tid;
	});
	// 提交修改密码
	$(".modify-password-btn").bind("click", modifyPassword);
	// 显示编辑用户
	$("#edit_local_user").click(function() {
		var tid = $("tbody").children("tr").eq(0).attr("tid");
		location.href = "showEditUser?tid=" + tid;
	});
	// 提交用户修改
	$(".configure-user").bind("click", editUser);
	// 批量删除用户
	$("#delete-admin-user").bind("click", deleteUser);
	// 根据条件查询用户
	// $("#search-user").bind("click", searchUser);
});
function searchUser() {
	var name = $("#condition-name").val();
	$.ajax({
		dataType : "json",
		url : "searchAccount",
		data : {
			name : name
		},
		success : function(data) {
			if (!data.data) {
				$(".no-data").show();
			} else {
				$(".no-data").hide();
			}
			// location.href = "jsp/account/admin_user.jsp";
		}
	});

}
function deleteUser() {

	zeroModal.confirm({
		content : "是否删除，删除后数据将丢失？",
		ok : true,
		okFn : function() {
			var tid;
			var count = 0;
			$("input[name='checkbox']").each(function() {
				if ($(this).is(":checked")) {
					if (count == 0) {
						tid = $(this).closest("tr").attr("tid")
					} else {
						tid = $(this).closest("tr").attr("tid") + "," + tid;
					}
					count++;
					$(this).closest("tr").remove();
				}
			});
			jQuery.ajax({
				dataType : "json",
				url : "deleteUsers",
				data : {
					tid : tid
				},
				success : function(data) {
					if (data.data) {
						monitorCheckBox();
						var $len = $("input[name='checkbox']").length;
						$(".show-total").children("span").each(function() {
							$(this).html($len);
						});
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
function editUser() {
	var id = $(".content-form").attr("tid");
	var realName = $("#user_real_name").val();
	var email = $("#user_mail").val();
	var description = $("#user_desc").val();
	jQuery.ajax({
		dataType : "json",
		url : "editUser",
		data : {
			id : id,
			realName : realName,
			email : email,
			description : description
		},
		success : function(data) {
			console.log(data.data);
			if (data.data == '1') {
				location.href = "showLocalUser";
				// zeroModal.success({
				// content:"操作成功",
				// ok:true,
				// okFn:function(){
				// location.href = "showLocalUser";
				// }
				// });
				// return false;
			} else {
				location.href = "showAdminUser";
			}
		},
		error : function() {
			// alert(1);
		}
	});
}
function addUser() {
	var userName = $("#user_name").val();
	var realName = $("#user_real_name").val();
	var password = $("#user_psd").val();
	var passwordConf = $("#user_psd_conf").val();
	var email = $("#user_mail").val();
	var description = $("#user_desc").val();
	if (!userName) {
		$(".username-warming-info").show();
		$(".username-warming-info").children().children("label").html(
				"用户名不能为空！");
		return false;
	}
	if (!password || !passwordConf) {
		// 判断密码是否为空
		if (!password) {
			$(".psd-warning-info").show();
			$(".psd-warning-info").children().children("label").html("密码不能为空！");
			$(".username-warming-info").hide();
			return false;
		} else if (!passwordConf) {
			$(".psd-conf-warming-info").show();
			$(".psd-conf-warming-info").children().children("label").html(
					"确认密码不能为空！");
			$(".username-warming-info").hide();
			$(".psd-warning-info").hide();
			$("#user_psd_conf").focus();
			return false;
		}
	}
	if (password && passwordConf) {
		// 判断两次输入的密码是否一致
		if (password != passwordConf) {
			$(".psd-conf-warming-info").show();
			$(".psd-conf-warming-info").children().children("label").html(
					"两次输入的密码不一致！");
			$(".username-warming-info").hide();
			$(".psd-warning-info").hide();
			$("#user_psd_conf").focus();
			return false;
		}
		// 进行密码验证(密码中必须包含字母、数字、特称字符，至少8个字符，最多30个字符)
		var regex = new RegExp(
				'(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{6,16}');
		if (!regex.test(password)) {
			$(".warming-info").show();
			$(".warming-info").children().children("label").html(
					"密码不符合规则，请重新输入！");
			return;
		}
	}
	jQuery.ajax({
		dataType : "json",
		url : "addUser",
		data : {
			userName : userName,
			realName : realName,
			password : password,
			email : email,
			description : description
		},
		success : function(data) {
			if (data.data) {
				location.href = "showAdminUser";
			}
		},
		error : function(data) {
			alert(1);
		}

	});
}
function modifyPassword() {
	var password = $("#user_psd").val();
	var passwordConf = $("#user_psd_conf").val();
	if (!password || !passwordConf) {
		// 判断密码是否为空
		if (!password) {
			$(".warning-info").show();
			$(".warning-info").children().children("label").html("密码不能为空！");
			return;
		} else if (!passwordConf) {
			$(".warning-info").show();
			$(".warning-info").children().children("label").html("确认密码不能为空！");
			$("#user_psd_conf").focus();
			return;
		}
	}
	if (password && passwordConf) {
		// 判断两次输入的密码是否一致
		if (password != passwordConf) {
			$(".warning-info").show();
			$(".warning-info").children().children("label").html("两次输入的密码不一致！");
			$("#user_psd_conf").focus();
			return;
		}

		// 进行密码验证(密码中必须包含字母、数字、特称字符，至少8个字符，最多30个字符)
		/*
		 * var regex = new
		 * RegExp('(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{6,16}');
		 * if(!regex.test(password)){ $(".warming-info").show();
		 * $(".warming-info").children().children("label").html("密码不符合规则，请重新输入！");
		 * return; }
		 */
	}
	jQuery.ajax({
		dataType : "json",
		url : "modifyPassword",
		data : {
			password : password
		},
		success : function(data) {
			if (data.data) {
				zeroModal.success({
					content : "操作成功,点击返回登入界面",
					ok : true,
					okFn : function() {
						location.href = "login.jsp";
					}
				});
				return false;
			}
		},
		error : function(data) {
			alert(1);
		}
	});
}