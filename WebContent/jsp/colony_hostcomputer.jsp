<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="./common/common.jsp" />
<script type="text/javascript">
	var clusterCpuUsedRate = "${cluster.cpuAverage }";
	var clusterMemoryUsedRate = "${clusterMemoryUsedRate }";
	var clusterStorageUserRate = "${clusterStorageUserRate };"
	var hostCpuUsedRate = "${hostInstance.cpuAverage }"
	var hostMemoryUsedRate = "${hostMemoryUsedRate }";
	clusterMemoryUsedRate = parseInt(clusterMemoryUsedRate);
	clusterCpuUsedRate = parseInt(clusterCpuUsedRate);
	clusterStorageUserRate = parseInt(clusterStorageUserRate);
	hostCpuUsedRate = parseInt(hostCpuUsedRate);
	hostMemoryUsedRate = parseInt(hostMemoryUsedRate);
	function setCircleProgressColorInRange(setObj, val){
		debugger;
		if( val > 70 && val <= 90){
			$(setObj).css({
				"width": val+"%"}).toggleClass("progress-bar-warning");
		}else if(val > 90){
			$(setObj).css({
				"width": val+"%"}).toggleClass("progress-bar-danger");
		}else{
			$(setObj).css({
				"width": val+"%"}).toggleClass("progress-bar-success");
		}
	}
	$(function(){
		//集群内存使用率
		setCircleProgressColorInRange(".cluster-memory-rate",clusterMemoryUsedRate);
		//集群CPU使用率
		setCircleProgressColorInRange(".cluster-cpu-rate", clusterCpuUsedRate);
		//集群存储使用率
		setCircleProgressColorInRange(".cluster-storage-rate", clusterStorageUserRate);
		//主机内存使用率
		setCircleProgressColorInRange(".host-memory-rate", hostMemoryUsedRate);
		//主机CPU使用率
		setCircleProgressColorInRange(".host-cpu-rate", hostCpuUsedRate);
		
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
							<a href="showClusterAndHost"><span class="glyphicon glyphicon-floppy-disk left-nav-icon"></span>集群和主机</a>
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
								<a href="#">集群</a>
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
											<td colspan="11">${cluster.description == "" ? "无" : cluster.description}</td>
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
								<a href="#">主机</a>
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
											<th>状态</th>
											<th>集群</th>
											<th>虚拟机数</th>
											<th>运行虚拟机数</th>
											<th>详情</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><span style="line-height: 50px;">${hostInstance.name }</span></td>
											<td style="width: 180px;">
											<div class="progress" style="width: 70%;margin-top: 15px;margin-bottom: 0;height: 15px;background:#c3bebe">
												<div class="progress-bar progress-bar-success host-cpu-rate" role="progressbar" aria-valuenow="60"
												aria-valuemin="0" aria-valuemax="100" style="width: 10%;">
													<span class="boyond-percent"></span>
												</div>
											</div><span class="progress-percent-cpu">${hostInstance.cpuAverage }%</span></td>
											<td style="width: 180px;">
												<div >${hostInstance.memoryUsed }/${hostInstance.memoryTotal }GB</div>
												<div class="progress" style="width: 70%;margin-bottom: 0;height: 15px;background:#c3bebe">
												<div class="progress-bar progress-bar-success host-memory-rate" role="progressbar" aria-valuenow="60"
												aria-valuemin="0" aria-valuemax="100" style="width: 10%;">
													<span class="boyond-percent"></span>
												</div>
											</div><span class="progress-percent-ram">${hostMemoryUsedRate }%</span></td>
											<td><span style="line-height: 50px;">${hostInstance.status == 1 ? "可用" : "不可用" }</span></td>
											<td><span style="line-height: 50px;">${clusterName }</span></td>
											<td><span style="line-height: 50px;"><a href="showVM" style="color: green">${hostInstance.vmTotalCount }</a></span></td>
											<td><span style="line-height: 50px;"><a href="showVM" style="color: green">${hostInstance.vmRunningCount }</a></span></td>
											<td><span id="show-hostcomputer-details" class="glyphicon glyphicon-chevron-down" style="line-height: 50px;cursor:pointer;margin-left: 30px;color: #337ab7"></span></td>
										</tr>
										<tr id="hide-hostcomputer-details">
											<td colspan="11">${hostInstance.description }</td>
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