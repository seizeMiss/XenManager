<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="header">
	<div class="pull-left">
		<img src="${pageContext.request.contextPath}/img/logo3.png" ondragstart="return false;" />
	</div>
	<div class="pull-right">
		<ul class="head-ul">
			<li title="任务消息"><a href="#"> <span id="head-main"
					class="glyphicon glyphicon-th"></span>
			</a></li>
			<li><span id="head-user" class="glyphicon glyphicon-user"></span>
				<a id="popover-local-user" href="#" data-container="body"
				data-toggle="popover" data-placement="bottom"
				data-content="<a href='local_user.html' style='color:#605757'>用户信息</a>">
					admin </a></li>
			<li title="注销"><a href="login.html"> <span id="loginout"
					class="glyphicon glyphicon-log-out"></span>
			</a></li>
		</ul>
	</div>
</div>