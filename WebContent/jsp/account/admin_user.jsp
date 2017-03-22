<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
							<a href="showVM"> <span class="glyphicon glyphicon-cloud left-nav-icon"></span> 虚拟机 </a>
						</li>
						<li class="nav-li">
							<a href="#user-child" data-toggle="collapse" data-parent="left-nav"> <span class="glyphicon glyphicon-cog left-nav-icon"></span> 用户管理 <i class="glyphicon glyphicon-chevron-down pull-right" style="line-height: 16px;"></i> </a>
							<div id="user-child" class="panel-collapse collapse in">
								<ul class="nav nav-pills nav-stacked" style="width: auto;">
									<li>
										<a href="showLocalUser"><span style="margin-left: 50px;">本地用户</span></a>
									</li>
									<li class="active">
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
								<a href="#">管理员</a>
							</li>
						</ol>
					</div>
					<div class="content-search">
						<form action="searchAccount">
						<div class="content-search-condition pull-left">
							<div class="search-condition">
								<span>名称</span>
								<input id="condition-name" name="condition-name" type="text" />
							</div>
						</div>
						<div class="content-search-submit pull-right">
							<button type="submit" id="search-user" class="btn btn-success">
								搜索
							</button>
							<button type="button" class="btn btn-danger">
								重置
							</button>
						</div>
						</form>
					</div>
					<div class="user">
						<div class="user-operate">
							<button id="add-admin-user" type="button" class="btn btn-default">
								添加管理员
							</button>
							<button id="edit-admin-user" disabled="disabled" type="button" class="btn btn-default">
								编辑用户
							</button>
							<button id="delete-admin-user" disabled="disabled" type="button" class="btn btn-default">
								删除
							</button>
						</div>
						<div class="data-table">
							<div class="data-table-top">
								<div class="show-total">
									已展示数/总数:<span>${size == null ? 0 : size }</span>/<span>${size == null ? 0 : size }</span>
								</div>
								<div class="show-selected">
									已选数:<span>0</span>
								</div>
							</div>
							<div class="data-table-content">
								<table class="table table-bordered table-hover">
									<thead class="data-thead">
										<tr>
											<th><input type="checkbox" id="all_cb"></th>
											<th>名称</th>
											<th>邮箱</th>
											<th>真实姓名</th>
											<th>创建时间</th>
											<th>描述</th>
										</tr>
									</thead>
									<tbody>
									<c:choose>
										<c:when test="${accounts != null }">
											<c:forEach var="adminAccount" items="${accounts }">
												<tr tid="${adminAccount.id }">
													<td><input type="checkbox" name="checkbox"></td>
													<td>${adminAccount.userName }</td>
													<td>${adminAccount.email }</td>
													<td>${adminAccount.realName }</td>
													<td>${adminAccount.createTime }</td>
													<td>${adminAccount.description }</td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr class="no-data">
												<td colspan="10">无数据</td>
											</tr>
										</c:otherwise>
									</c:choose>
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