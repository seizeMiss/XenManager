<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE >
<html>
	<head>
		<title>VMManager 登录</title>
		<meta content="text/html" charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=Edge">
		<link rel='icon' href='${pageContext.request.contextPath}/img/pic.ico ' type=‘image/x-ico’ />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/dist/css/bootstrap.min.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/normalize.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/scroll.css" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery-2.1.0.js"></script>
		<script src="${pageContext.request.contextPath}/bootstrap/dist/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/script/login.js"></script>
	</head>
	<body style="background-image: url('${pageContext.request.contextPath}/img/1.jpg')">
		<div class="login">
			<div class="logo"><img src="${pageContext.request.contextPath}/img/logo3.png" ondragstart="return false"/></div>
			<div class="login-error">登录失败，用户名或密码错误！</div>
			<div class="input-group username">
				<span class="input-group-addon glyphicon glyphicon-user" style="color: rgb(0, 0, 0);background: transparent;font-size: 20px;
				border-left: 0;border-bottom: 1px solid gray;border-right: 1px solid gray; border-top: 0"></span>
				<input id="input-username" name="input-username" type="text" placeholder="用户名" maxlength="50px" />
			</div>
			<div class="input-group password">
				<span class="input-group-addon glyphicon glyphicon-lock" style="color: rgb(0, 0, 0);background: transparent;font-size: 20px;
				border-left: 0;border-bottom: 1px solid gray;border-right: 1px solid gray; border-top: 0"></span>
				<input id="input-psw-clear" name="input-psw" type="text" placeholder="密码" maxlength="50px" style="display: none"/>
				<input id="input-psw" name="input-psw" type="password" placeholder="密码" maxlength="50px" />
				<label id="psw-label" class="glyphicon glyphicon-eye-open" for="input-psw"></label>
			</div>
			<div class="login-submit">
				<input type="button" id="login" class="btn btn-default" value="登录"/>
			</div>
		</div>
	</body>
</html>