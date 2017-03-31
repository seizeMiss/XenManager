<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../common/common.jsp"></jsp:include>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/script/vm.js"></script>
<script type="text/javascript">
	$(function() {
		var vmCpuUsedRate = "";
		var vmMemoryUsedRate = "";
		$(".data-table-tbody").children("tr").each(function() {
				debugger;
				vmCpuUsedRate = $(this).find(".progress-percent-cpu").html();
				vmMemoryUsedRate = $(this).find(".progress-percent-ram").html();
				if(vmCpuUsedRate){
					vmCpuUsedRate = vmCpuUsedRate.split("%")[0];
					setCircleProgressColorInRange($(this).find(".vm-cpu-used-rate"), vmCpuUsedRate);
				}
				if(vmMemoryUsedRate){
					vmMemoryUsedRate = vmMemoryUsedRate.split("%")[0];
			  		setCircleProgressColorInRange($(this).find(".vm-memory-used-rate"), vmMemoryUsedRate);
				}
		});
	});
</script>
<body>
	<div class="container">
		<jsp:include page="../common/header.jsp"></jsp:include>
		<div class="row">
			<div class="left-nav">
				<ul id="nav" class="nav nav-pills nav-stacked">
					<li class="nav-li"><a href="showIndex"><span
							class="glyphicon glyphicon-home left-nav-icon"></span>首页</a></li>
					<li class="nav-li"><a href="showClusterAndHost"><span
							class="glyphicon glyphicon-floppy-disk left-nav-icon"></span>集群和主机</a>
					</li>
					<li class="nav-li"><a href="showImage"><span
							class="glyphicon glyphicon-floppy-disk left-nav-icon"></span>镜像</a></li>
					<li class="nav-li active"><a href="showVM"> <span
							class="glyphicon glyphicon-cloud left-nav-icon"></span> 虚拟机
					</a></li>
					<li class="nav-li"><a href="#user-child"
						data-toggle="collapse" data-parent="left-nav"> <span
							class="glyphicon glyphicon-cog left-nav-icon"></span> 用户管理 <i
							class="glyphicon glyphicon-chevron-down pull-right"
							style="line-height: 16px;"></i>
					</a>
						<div id="user-child" class="panel-collapse collapse">
							<ul class="nav nav-pills nav-stacked" style="width: auto;">
								<li><a href="showLocalUser"><span
										style="margin-left: 50px;">本地用户</span></a></li>
								<li><a href="showAdminUser"><span
										style="margin-left: 50px;">管理员</span></a></li>
							</ul>
						</div></li>
					<li class="nav-li"><a href="showAbout"><span
							class="glyphicon glyphicon-stats left-nav-icon"></span>关于</a></li>
				</ul>
			</div>
			<div class="page-content">
				<div class="nav-content">
					<ol class="breadcrumb" style="width: 100%; background: white">
						<li>虚拟机</li>
						<li>虚拟机显示</li>
					</ol>
				</div>
				<div class="content-search">
				<form action="searchVm" method="post">
					<div class="content-search-condition pull-left">
						<div class="search-condition">
							<span>名称</span> <input id="condition-name" name="condition-name" type="text" />
						</div>
					</div>
					<div class="content-search-condition pull-left">
						<div class="search-condition">
								<span style="float: left;">状态</span>
								<div class="input-group" style="width: 200px;float: left;margin-left: 20px;">
									<input type="text" id="selected-state" name="selected-state" readonly="readonly" data-toggle="dropdown" class="form-control dropdown-toggle" style="font-size: 18px;">
									<div class="input-group-btn">
										<button type="button" class="btn btn-default dropdown-toggle show-state" data-toggle="dropdown">
											&nbsp;<span class="caret" style="font-size: 10px;"></span>
										</button>
										<ul id= "select-state" class="dropdown-menu pull-right" style="width: 190px;">
											<li>
												<a href="#">不可用</a>
											</li>
											<li>
												<a href="#">可用</a>
											</li>
											<li>
												<a href="#">启动</a>
											</li>
											<li>
												<a href="#">关闭</a>
											</li>
											<li>
												<a href="#">开启中</a>
											</li>
											<li>
												<a href="#">关闭中</a>
											</li>
											<li>
												<a href="#">重启中</a>
											</li>
											<li>
												<a href="#">删除中</a>
											</li>
										</ul>
									</div>
								</div>
							</div>
					</div>
					<div class="content-search-condition pull-left">
						<div class="search-condition">
							<span style="float: left;">操作系统</span>
							<div class="input-group"
								style="width: 200px; float: left; margin-left: 20px;">
								<input type="text" id="selected-os" name="selected-os" readonly="readonly"
									class="form-control" style="font-size: 18px;">
								<div class="input-group-btn">
									<button type="button"
										class="btn btn-default dropdown-toggle show-os"
										data-toggle="dropdown">
										&nbsp;<span class="caret" style="font-size: 20px;"></span>
									</button>
									<ul id="select-os" class="dropdown-menu pull-right"
										style="width: 190px;">
										<c:forEach var="vmOsType" items="${vmOsTypes }">
											<li><a href="#">${vmOsType }</a></li>
										</c:forEach>
									</ul>
								</div>
							</div>
						</div>
					</div>
					<div class="content-search-submit pull-right">
						<button type="submit" class="btn btn-success">搜索</button>
						<button type="button" class="btn btn-danger">重置</button>
					</div>
					</form>
				</div>
				<div class="user">
					<div class="user-operate">
						<button id="add-vm" type="button"  class="btn btn-default">
							添加虚拟机</button>
						<button id="delete-vm" type="button" disabled="disabled" title="只能对状态为“不可用”的系统虚拟机或者状态为“关闭”|“不可用”的用户虚拟机执行此操作" class="btn btn-default">
							删除</button>
						<button id="launch-vm" type="button" disabled="disabled" title="只能对状态为“关闭”的用户虚拟机执行此操作" class="btn btn-default">
							启动</button>
						<button id="restart-vm" type="button" disabled="disabled" title="只能对状态为“启动”的用户虚拟机执行此操作" class="btn btn-default">
							重启</button>
						<button id="close-vm" type="button" disabled="disabled" title="只能对状态为“启动”的用户虚拟机执行此操作" class="btn btn-default">
							关闭</button>
						<button id="edit-vm" type="button" disabled="disabled" title="只能对“关闭”状态的用户虚拟机执行此操作，一次只能修改一台" class="btn btn-default">
							修改配置</button>
					</div>
					<div class="data-table">
						<div class="data-table-top">
							<div class="show-total">
								已展示数/总数:<span>${vmCount }</span>/<span>${vmCount }</span>
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
										<th>IP地址</th>
										<th>状态</th>
										<th>操作系统</th>
										<th>集群</th>
										<th>主机</th>
										<th>CPU使用率</th>
										<th>内存使用率</th>
										<th>详情</th>
									</tr>
								</thead>
								<tbody class="data-table-tbody">
									<c:forEach var="vmNeedInfo" items="${vmNeedInfos }">
										<tr vid="${vmNeedInfo.vmInstance.id }">
											<td>
												<c:choose>
													<c:when test="${vmNeedInfo.vmInstance.status == 2 || vmNeedInfo.vmInstance.status == 3 || vmNeedInfo.vmInstance.status == 4 || vmNeedInfo.vmInstance.status == 5 || vmNeedInfo.vmInstance.status == 8}">
														<img src="/VMManager/img/load.gif"/>
													</c:when>
													<c:otherwise><input type="checkbox" name="checkbox"></c:otherwise>
												</c:choose>
											</td>
											<td><span style="line-height: 50px;">${vmNeedInfo.vmInstance.name }</span></td>
											<td><span style="line-height: 50px;">${vmNeedInfo.vmInstance.vmIp == "" ? "-" : vmNeedInfo.vmInstance.vmIp}</span></td>
											<td><span style="line-height: 50px;" status="${vmNeedInfo.vmInstance.status }">${vmNeedInfo.vmInstance.powerStatus == "Running" ? "启动" : "关闭" }</span></td>
											<td><span style="line-height: 50px;">${vmNeedInfo.vmInstance.osType }</span></td>
											<td><span style="line-height: 50px;">${vmNeedInfo.clusterName }</span></td>
											<td><span style="line-height: 50px;">${vmNeedInfo.hostName }</span></td>
											<c:choose>
												<c:when
													test="${vmNeedInfo.vmInstance.powerStatus == 'Running' }">
													<c:choose>
														<c:when test="${vmNeedInfo.showCpuRate == 1}">
														<td>
															<div class="progress"
																style="width: 70%; margin-top: 15px; margin-bottom: 0; height: 15px;">
																<div
																	class="progress-bar progress-bar-success vm-cpu-used-rate"
																	role="progressbar" aria-valuenow="60" aria-valuemin="0"
																	aria-valuemax="100" style="width: 10%;">
																	<span class="boyond-percent"></span>
																</div>
															</div> <span class="progress-percent-cpu">${vmNeedInfo.cpuRate }%</span>
														</td>
														</c:when>
														<c:otherwise>
														<td>-</td>
														</c:otherwise>
													</c:choose>
													<c:choose>
														<c:when test="${vmNeedInfo.showMemoryRate == 1}">
															<td>
																<div>${vmNeedInfo.memoryUsed }/${vmNeedInfo.memoryTotal }GB
																</div>
																<div class="progress"
																	style="width: 70%; margin-bottom: 0; height: 15px;">
																	<div
																		class="progress-bar progress-bar-success vm-memory-used-rate"
																		role="progressbar" aria-valuenow="60"
																		aria-valuemin="0" aria-valuemax="100"
																		style="width: 10%;">
																		<span class="boyond-percent"></span>
																	</div>
																</div> <span class="progress-percent-ram">${vmNeedInfo.memoryRate }%</span>
															</td>
														</c:when>
														<c:otherwise>
															<td>-</td>
														</c:otherwise>
													</c:choose>

												</c:when>
												<c:otherwise>
													<td>-</td>
													<td>-</td>
												</c:otherwise>
											</c:choose>
											<td><span
												class="glyphicon glyphicon-chevron-down show-details"
												style="cursor: pointer; line-height: 50px; margin-left: 30px; color: #337ab7"></span></td>

										</tr>
										<tr class="hidden-tr">
											<td colspan="10">
												<div class="vm-info">CPU:${vmNeedInfo.vmInstance.cpu }核
													&nbsp&nbsp内存:${vmNeedInfo.vmInstance.memory }GB &nbsp&nbsp
													系统盘: ${vmNeedInfo.vmInstance.systemDisk }GB &nbsp&nbsp用户盘:无</div>
												<div class="vm-explain">
												说明:<c:if test="${vmNeedInfo.showMemoryRate == 1}">无</c:if>
												<c:if test="${vmNeedInfo.showMemoryRate == 0}">未安装XenTools，无法查看内存使用率</c:if>
												</div>
											</td>
										</tr>
									</c:forEach>
									<c:if test="${vmNeedInfos == null }">
									<tr class="no-data">
										<td colspan="10">无数据</td>
									</tr>
									</c:if>
								</tbody>
								<tfoot></tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="footer"></div>
	</div>
</body>
</html>