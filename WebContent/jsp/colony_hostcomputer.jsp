<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="./common/common.jsp" />
<script type="text/javascript">
	var clusterCpuUsedRate = "${cluster.cpuAverage }";
	var clusterMemoryUsedRate = "${clusterMemoryUsedRate }";
	var clusterStorageUserRate = "${clusterStorageUserRate };"
	
	clusterMemoryUsedRate = parseInt(clusterMemoryUsedRate);
	clusterCpuUsedRate = parseInt(clusterCpuUsedRate);
	clusterStorageUserRate = parseInt(clusterStorageUserRate);
	$(function(){
		//集群内存使用率
		setCircleProgressColorInRange($(".cluster-memory-rate"),clusterMemoryUsedRate);
		//集群CPU使用率
		setCircleProgressColorInRange($(".cluster-cpu-rate"), clusterCpuUsedRate);
		//集群存储使用率
		setCircleProgressColorInRange($(".cluster-storage-rate"), clusterStorageUserRate);
		$(".show-hostcomputer-details").click(function(){
			if($(this).closest("tr").next().css("display") == "none"){
				$(".hide-hostcomputer-details").each(function(){
					if($(this).css("display") != "none"){
						$(this).hide();
						$(this).prev("tr").find(".show-hostcomputer-details").removeClass("glyphicon-chevron-up");
						$(this).prev("tr").find(".show-hostcomputer-details").addClass("glyphicon-chevron-down");
					}
				});
				$(this).closest("tr").next().show();
				$(this).removeClass("glyphicon-chevron-down");
				$(this).addClass("glyphicon-chevron-up");
			}else{
				$(this).closest("tr").next().hide();
				$(this).removeClass("glyphicon-chevron-up");
				$(this).addClass("glyphicon-chevron-down");
			}
		});
		
		var vmCpuUsedRate = "";
		var vmMemoryUsedRate = "";
		$(".data-table-tbody").children("tr").each(
				function() {
					vmCpuUsedRate = $(this).find(".progress-percent-cpu")
							.html();
					vmMemoryUsedRate = $(this)
							.find(".progress-percent-ram").html();
					if (vmCpuUsedRate) {
						vmCpuUsedRate = vmCpuUsedRate.split("%")[0];
						setCircleProgressColorInRange($(this).find(
								".host-cpu-rate"), vmCpuUsedRate);
					}
					if (vmMemoryUsedRate) {
						vmMemoryUsedRate = vmMemoryUsedRate.split("%")[0];
						setCircleProgressColorInRange($(this).find(
								".host-memory-rate"), vmMemoryUsedRate);
					}
				});
	});
</script>
<body>
		<div class="container">
			<jsp:include page="./common/header.jsp"></jsp:include>
			<div class="row">
				<div class="left-nav">
					<ul id="nav" class="nav nav-pills nav-stacked">
						<li class="nav-li">
							<a href="showIndex" ><span class="glyphicon glyphicon-home left-nav-icon"></span>首页</a>
						</li>
						<li class="nav-li active">
							<a href="showClusterAndHost"><span class="glyphicon glyphicon-th-large left-nav-icon"></span>集群和主机</a>
						</li>
						<li class="nav-li">
							<a href="showImage"><span class="glyphicon glyphicon-floppy-disk left-nav-icon"></span>镜像</a>
						</li>
						<li class="nav-li">
							<a href="showVM" > <span class="glyphicon glyphicon-cloud left-nav-icon"></span> 虚拟机  </a>
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
								集群和主机
							</li>
							<li>
								<a href="javascript:void(0)">集群</a>
							</li>
						</ol>
					</div>
					<div class="user">
						<div class="data-table">
							<div class="data-table-top">
								<div class="show-total">
									已展示数/总数:<span>1</span>/<span>1</span>
								</div>
							</div>
							<div class="data-table-content">
								<table class="table table-bordered table-hover">
									<thead class="data-thead">
										<tr>
											<th>名称</th>
											<th>CPU使用率</th>
											<th>内存使用率</th>
											<th>存储使用率</th>
											<th>状态</th>
											<th>主机数</th>
											<th>虚拟机数</th>
											<th>存储数</th>
											<th>地址</th>
											<th>详情</th>
										</tr>
									</thead>
									<tbody>
										<tr class="data-table-tr">
											<td><span style="line-height: 50px;">${cluster.name }</span></td>
											<td style="width: 180px;">
											<div class="progress" style="width: 70%;margin-top: 15px;margin-bottom: 0;height: 15px;background:#c3bebe">
												<div class="progress-bar progress-bar-success cluster-cpu-rate" role="progressbar" aria-valuenow="60"
												aria-valuemin="0" aria-valuemax="100">
													<span class="boyond-percent"></span>
												</div>
											</div><span class="progress-percent-cpu">${cluster.cpuAverage }%</span></td>
											<td style="width: 180px;">
												<div >${cluster.memoryUsed }/${cluster.memoryTotal }GB</div>
												<div class="progress" style="width: 70%;margin-bottom: 0;height: 15px;background:#c3bebe">
												<div class="progress-bar progress-bar-success cluster-memory-rate" role="progressbar" aria-valuenow="60"
												aria-valuemin="0" aria-valuemax="100">
													<span class="boyond-percent"></span>
												</div>
											</div><span class="progress-percent-ram">${clusterMemoryUsedRate }%</span></td>
											<td style="width: 180px;">
												<div class="">${cluster.storageUsed }/${cluster.storageTotal }GB</div>
												<div class="progress" style="width: 70%;margin-bottom: 0;height: 15px;background:#c3bebe">
												<div class="progress-bar progress-bar-success cluster-storage-rate" role="progressbar" aria-valuenow="60"
												aria-valuemin="0" aria-valuemax="100">
													<span class="boyond-percent"></span>
												</div>
											</div><span class="progress-percent-storage">${clusterStorageUserRate }%</span></td>
											<td><span style="line-height: 50px;">${cluster.status == 1 ? "可用" : "不可用" }</span></td>
											<td><span style="line-height: 50px;">${cluster.hostCount}</span></td>
											<td><span style="line-height: 50px;"><a href="showVM" style="color: green">${cluster.vmCount }</a></span></td>
											<td><span style="line-height: 50px;">${cluster.storageCount }</span></td>
											<td><span style="line-height: 50px;">${cluster.clusterIP }</span></td>
											<td><span id="show-colony-details" class="glyphicon glyphicon-chevron-down" style="cursor:pointer;line-height: 50px;margin-left: 30px;color: #337ab7"></span></td>
										</tr>
										<tr id="hide-colony-details">
											<td colspan="11">描述：${cluster.description == "" ? "无" : cluster.description}</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="nav-content">
						<ol class="breadcrumb" style="width: 100%;background: white">
							<li>
								集群和主机
							</li>
							<li>
								<a href="javascript:void(0)">主机</a>
							</li>
						</ol>
					</div>
					<div class="user">
						<div class="data-table">
							<div class="data-table-top">
								<div class="show-total">
									已展示数/总数:<span>${host_size }</span>/<span>${host_size }</span>
								</div>
							</div>
							<div class="data-table-content">
								<table class="table table-bordered table-hover">
									<thead class="data-thead">
										<tr>
											<th>名称</th>
											<th>CPU使用率</th>
											<th>内存使用率</th>
											<th>状态</th>
											<th>集群</th>
											<th>虚拟机数</th>
											<th>运行虚拟机数</th>
											<th>详情</th>
										</tr>
									</thead>
									<tbody class="data-table-tbody">
									<c:forEach var="hostNeedInfo" items="${hostNeedInfos }">
										<tr>
											<td><span style="line-height: 50px;">${hostNeedInfo.hostInstance.name }</span></td>
											<td style="width: 180px;">
											<div class="progress" style="width: 70%;margin-top: 15px;margin-bottom: 0;height: 15px;background:#c3bebe">
												<div class="progress-bar progress-bar-success host-cpu-rate" role="progressbar" aria-valuenow="60"
												aria-valuemin="0" aria-valuemax="100" style="width: 10%;">
													<span class="boyond-percent"></span>
												</div>
											</div><span class="progress-percent-cpu">${hostNeedInfo.hostInstance.cpuAverage }%</span></td>
											<td style="width: 180px;">
												<div >${hostNeedInfo.hostInstance.memoryUsed }/${hostNeedInfo.hostInstance.memoryTotal }GB</div>
												<div class="progress" style="width: 70%;margin-bottom: 0;height: 15px;background:#c3bebe">
												<div class="progress-bar progress-bar-success host-memory-rate" role="progressbar" aria-valuenow="60"
												aria-valuemin="0" aria-valuemax="100" style="width: 10%;">
													<span class="boyond-percent"></span>
												</div>
											</div><span class="progress-percent-ram">${hostNeedInfo.memoryUsedRate }%</span></td>
											<td><span style="line-height: 50px;">${hostNeedInfo.hostInstance.status == 1 ? "可用" : "不可用" }</span></td>
											<td><span style="line-height: 50px;">${hostNeedInfo.clusterName }</span></td>
											<td><span style="line-height: 50px;"><a href="showVM" style="color: green">${hostNeedInfo.hostInstance.vmTotalCount }</a></span></td>
											<td><span style="line-height: 50px;"><a href="showVM" style="color: green">${hostNeedInfo.hostInstance.vmRunningCount }</a></span></td>
											<td><span class="glyphicon glyphicon-chevron-down show-hostcomputer-details" style="line-height: 50px;cursor:pointer;margin-left: 30px;color: #337ab7"></span></td>
										</tr>
										<tr class="hide-hostcomputer-details">
											<td colspan="11">描述：${hostNeedInfo.hostInstance.description }</td>
										</tr>
									</c:forEach>
										
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