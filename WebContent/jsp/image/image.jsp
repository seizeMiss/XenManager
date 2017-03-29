<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	
<jsp:include page="../common/common.jsp" />
<script type="text/javascript" src="${pageContext.request.contextPath}/script/image.js"></script>
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
						<li class="nav-li active">
							<a href="showImage"><span class="glyphicon glyphicon-floppy-disk left-nav-icon"></span>镜像</a>
						</li>
						<li class="nav-li">
							<a href="showVM"> <span class="glyphicon glyphicon-cloud left-nav-icon"></span> 虚拟机 </a>
						</li>
						<li class="nav-li">
							<a href="#user-child" data-toggle="collapse" data-parent="left-nav"> <span class="glyphicon glyphicon-cog left-nav-icon"></span> 用户管理 <i class="glyphicon glyphicon-chevron-down pull-right" style="line-height: 16px;"></i> </a>
							<div id="user-child" class="panel-collapse collapse">
								<ul class="nav nav-pills nav-stacked" style="width: auto;">
									<li>
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
								镜像
							</li>
							<li>镜像显示</li>
						</ol>
					</div>
					<div class="content-search">
						<div class="content-search-condition pull-left">
							<div class="search-condition">
								<span>名称</span>
								<input id="condition-name" type="text" />
							</div>
						</div>
						<div class="content-search-condition pull-left">
							<div class="search-condition">
								<span>状态</span>
								<input id="condition-state" type="text" />
							</div>
						</div>
						<div class="content-search-condition pull-left">
							<div class="search-condition">
								<span style="float: left;">操作系统</span>
								<div class="input-group" style="width: 200px;float: left;margin-left: 20px;">
									<input type="text" id="selected-os" readonly="readonly" data-toggle="dropdown" class="form-control dropdown-toggle" style="font-size: 18px;">
									<div class="input-group-btn">
										<button type="button" class="btn btn-default dropdown-toggle show-os" data-toggle="dropdown">
											&nbsp;<span class="caret" style="font-size: 10px;"></span>
										</button>
										<ul id= "select-os" class="dropdown-menu pull-right" style="width: 190px;">
											<li>
												<a href="#">Windows 7</a>
											</li>
											<li>
												<a href="#">CentOS7</a>
											</li>
											<li>
												<a href="#">Ubuntu 15</a>
											</li>
										</ul>
									</div>
								</div>
							</div>
						</div>
						<div class="content-search-submit pull-right">
							<button type="button" class="btn btn-success">
								搜索
							</button>
							<button type="button" class="btn btn-danger">
								重置
							</button>
						</div>
					</div>
					<div class="user">
						<div class="user-operate">
							<button id="add-image" type="button" class="btn btn-default">
								添加镜像
							</button>
							<button id="delete-image" type="button" class="btn btn-default">
								删除镜像
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
								<table class="table table-bordered table-hover">
									<thead class="data-thead">
									<tr>
										<th>
										<input type="checkbox" id="all_cb">
										</th>
										<th>名称</th>
										<th>操作系统</th>
										<th>状态</th>
										<th>镜像大小</th>
										<th>创建时间</th>
										<th>详情</th>
									</tr>
								</thead>
									<tbody class="data-table-tbody">
										<tr>
											<td>
											<input type="checkbox" name="checkbox">
											</td>
											<td><span style="line-height: 50px;">Tanmay</span></td>
											<td><span style="line-height: 50px;">Windows 7</span></td>
											<td><span style="line-height: 50px;">可用</span></td>
											<td><span style="line-height: 50px;">10GB</span></td>
											<td><span style="line-height: 50px;">2017.03.15</span></td>
											<td>
												<span class="glyphicon glyphicon-chevron-down show-details" style="cursor:pointer;line-height: 50px;margin-left: 30px;color: #09f"></span>
											</td>
										</tr>
										<tr class="hidden-tr">
											<td colspan="10">11</td>
										</tr>

									</tbody>
									<tfoot></tfoot>
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