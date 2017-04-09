<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:include page="../common/common.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/user.js"></script>
<script type="text/javascript">
	$(function() {
		$("#popover-pwd").webuiPopover({
			closeable : true,
			animation : 'pop',
			placement : 'right'
		});
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
							<a href="showClusterAndHost"><span class="glyphicon glyphicon-floppy-disk left-nav-icon"></span>集群和主机</a>
						</li>
						<li class="nav-li">
							<a href="showImage"><span class="glyphicon glyphicon-floppy-disk left-nav-icon"></span>镜像</a>
						</li>
						<li class="nav-li">
							<a href="showVM"> <span class="glyphicon glyphicon-cloud left-nav-icon"></span> 虚拟机  </a>
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
									<a href="javascript:void(0)">本地用户</a>
								</li>
								<li>
									修改密码
								</li>
							</ol>
						</div>
					</div>
					<div class="content-form">
						<form class="form-horizontal" role="form">
							<div class="form-group">
								<label for="user_name" class="col-sm-2 control-label">账户名</label>
								<div class="col-sm-10">
									<span style="line-height: 27px;">${localAccount.userName }</span>
								</div>
							</div>
							<div class="form-group">
								<label for="user_psd" class="col-sm-2 control-label">
									<span class="asterisk">*</span>密码
								</label>
								<div class="col-sm-10">
									<input type="password" class="form-control" id="user_psd" placeholder="请输入密码">
								</div>
								<a id="popover-pwd" data-container="body" data-toggle="popover" 
								data-placement="right" data-content="1.密码长度为6~18位<br>2.密码必须包含英文大写、英文小写、数字、特殊字符中的至少3类字符<br>3.密码中不能包含账户名和真实姓名" 
								class="glyphicon glyphicon-info-sign"></a>
								<div class="warning-info" style="float: left">
									<span class="glyphicon glyphicon-warning-sign"><label style="margin-left: 5px;">密码不能为空！</label></span>
								</div>
							</div>
							<div class="form-group">
								<label for="user_psd_conf" class="col-sm-2 control-label"><span class="asterisk">*</span>确认密码</label>
								<div class="col-sm-10">
									<input type="password" class="form-control" id="user_psd_conf" placeholder="确认密码">
								</div>
							</div>
							<div class="form-group operation">
								<div class="col-sm-offset-2 col-sm-10">
									<button type="button" class="btn btn-default modify-password-btn" style="margin-right: 30px;">
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