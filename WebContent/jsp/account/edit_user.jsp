<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../common/common.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/user.js"></script>
<script type="text/javascript">
	$(function() {
		$("#popover-pwd").webuiPopover({
			closeable : true,
			animation : 'pop',
			placement : 'right'
		});
		$("#user_desc").val($("#user_desc").val().trim());
	});
</script>
<style>
#popover-pwd {
	position: absolute;
	top: 170px;
	font-size: 20px;
	color: #337ab7;
	cursor: pointer;
	text-decoration: none;
}
</style>

<body>
		<div class="container">
			<jsp:include page="../common/header.jsp"></jsp:include>
			<div class="row">
				<div class="left-nav">
					<ul id="nav" class="nav nav-pills nav-stacked">
						<li class="nav-li">
							<a href="showIndex" ><span class="glyphicon glyphicon-home left-nav-icon"></span>首页</a>
						</li>
						<li class="nav-li">
							<a href="showClusterAndHost"><span class="glyphicon glyphicon-th-large left-nav-icon"></span>集群和主机</a>
						</li>
						<li class="nav-li">
							<a href="showImage"><span class="glyphicon glyphicon-floppy-disk left-nav-icon"></span>镜像</a>
						</li>
						<li class="nav-li">
							<a href="showVM"> <span class="glyphicon glyphicon-cloud left-nav-icon"></span> 虚拟机 </a>
						</li>
						<li class="nav-li">
							<a href="#user-child" data-toggle="collapse" data-parent="left-nav"> <span class="glyphicon glyphicon-cog left-nav-icon"></span> 用户管理 <i class="glyphicon glyphicon-chevron-left pull-right" style="line-height: 16px;"></i> </a>
							<div id="user-child" class="panel-collapse collapse in">
								<ul class="nav nav-pills nav-stacked" style="width: auto;">
									<li class="active">
										<a href="showLocalUser"><span style="margin-left: 50px;">本地用户</span></a>
									</li>
									<li>
										<a href="showAdminUser"><span style="margin-left: 50px;">管理员</span></a>
									</li>
								</ul>
							</div>
						</li>
						<li class="nav-li">
							<a href="showAbout"><span class="glyphicon glyphicon-stats left-nav-icon"></span>关于</a>
						</li>
					</ul>
				</div>
				<div class="page-content">
					<div class="nav-content">
						<div class="nav-content">
							<ol class="breadcrumb" style="width: 100%;background: white">
								<li>
									用户管理
								</li>
								<li>
									<a href="showLocalUser">本地用户</a>
								</li>
								<li>
									编辑用户
								</li>
							</ol>
						</div>
					</div>
					<div class="content-form" tid="${localAccount.id }">
						<form class="form-horizontal" role="form">
							<div class="form-group">
								<label for="user_name" class="col-sm-2 control-label">账户名</label>
								<div class="col-sm-10">
									<span style="line-height: 27px;">${localAccount.userName }</span>
								</div>
							</div>
							<div class="form-group">
								<label for="user_real_name" class="col-sm-2 control-label">
									<span class="asterisk">*</span>真实姓名
								</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="user_real_name" value="${localAccount.realName }" placeholder="请输入真实姓名">
								</div>
								<a id="popover-pwd" data-container="body" data-toggle="popover" 
								data-placement="right" data-content="真实姓名只能由中英文、数字、空格和横杠 (-)构成，最长30字符" 
								class="glyphicon glyphicon-info-sign"></a>
							</div>
							<div class="form-group">
								<label for="user_mail" class="col-sm-2 control-label">邮箱</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="user_mail" value="${localAccount.email }" placeholder="请输入邮箱">
								</div>
							</div>
							<div class="form-group">
								<label for="user_desc" class="col-sm-2 control-label">描述</label>
								<div class="col-sm-10">
									<textarea id="user_desc" placeholder="描述">
									${localAccount.description }
									</textarea>
								</div>
							</div>
							<div class="form-group operation">
								<div class="col-sm-offset-2 col-sm-10">
									<button type="button" class="btn btn-default configure-user" style="margin-right: 30px;">
										确定
									</button>
									<button type="button" class="btn btn-default cancle-btn">
										取消
									</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="footer">

			</div>
		</div>
	</body>
</html>

