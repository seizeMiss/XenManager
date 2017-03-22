<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="../common/common.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/user.js"></script>
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
							<a href="#user-child" data-toggle="collapse" data-parent="left-nav"> <span class="glyphicon glyphicon-cog left-nav-icon"></span> 用户管理 <i class="glyphicon glyphicon-chevron-down pull-right" style="line-height: 16px;"></i> </a>
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
						<ol class="breadcrumb" style="width: 100%;background: white">
							<li>
								用户管理
							</li>
							<li>
								<a href="loacl_user.html">本地用户</a>
							</li>
						</ol>
					</div>
					<div class="user">
						<div class="user-operate">
							<button id="modify_psw" type="button" class="btn btn-default">
								修改密码
							</button>
							<button id= "edit_local_user" type="button" class="btn btn-default">
								编辑用户
							</button>
						</div>
						<div class="data-table">
							<div class="data-table-top">
								<div class="show-total">
									已展示数/总数:<span>1</span>/<span>1</span>
								</div>
								<div class="show-selected">
									已选数:<span>0</span>
								</div>
							</div>
							<div class="data-table-content">
								<table class="table table-bordered">
									<thead>
										<tr>
											<th>用户名</th>
											<th>邮箱</th>
											<th>真实姓名</th>
											<th>创建时间</th>
											<th>描述</th>
										</tr>
									</thead>
									<tbody>
									<tr tid="${account.id }">
										<td>${account.userName }</td>
										<td>${account.email }</td>
										<td>${account.realName }</td>
										<td>${account.createTime }</td>
										<td>${account.description }</td>
									</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="footer">

			</div>
		</div>
	</body>
</html>