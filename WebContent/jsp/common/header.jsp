<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="header">
	<div class="pull-left">
		<img src="${pageContext.request.contextPath}/img/logo3.png" ondragstart="return false;" />
	</div>
	<div class="pull-right">
		<ul class="head-ul">
			<li title="任务消息"><a href="javascript:void(0)"> <span id="head-main"
					class="glyphicon glyphicon-th"></span>
			</a>
			<span class="badge message-number">1</span>
			</li>
			<li><span id="head-user" class="glyphicon glyphicon-user"></span>
				<a id="popover-local-user" href="javascript:void(0)" data-container="body"
				data-toggle="popover" data-placement="bottom"
				data-content="<a href='showLocalUser' style='color:#605757'>用户信息</a>">
					${account.userName } </a></li>
			<li title="注销"><a href="loginOut"> <span id="loginout"
					class="glyphicon glyphicon-log-out"></span>
			</a></li>
		</ul>
	</div>
</div>