<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="./common/common.jsp"></jsp:include>
<script type="text/javascript">
	var cpuUsedRate = "${cpuUsedRate }";
	var memoryUsedRate = "${memoryUsedRate }";
	var storageUsedRate = "${storageUsedRate }"
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/radialIndicator.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/script/index.js"></script>
<body>
		<div class="container">
			<jsp:include page="./common/header.jsp"></jsp:include>
			<div class="row">
				<div class="left-nav">
					<ul id="nav" class="nav nav-pills nav-stacked">
						<li class="nav-li active">
							<a href="showIndex" ><span class="glyphicon glyphicon-home left-nav-icon"></span>首页</a>
						</li>
						<li class="nav-li">
							<a href="showClusterAndHost"><span class="glyphicon glyphicon-floppy-disk left-nav-icon"></span>集群和主机</a>
						</li>
						<li class="nav-li">
							<a href="showImage"><span class="glyphicon glyphicon-floppy-disk left-nav-icon"></span>镜像</a>
						</li>
						<li class="nav-li">
							<a href="showVM" >
								<span class="glyphicon glyphicon-cloud left-nav-icon"></span>
								虚拟机 
							</a>
						</li>
						<li class="nav-li">
							<a href="#user-child" data-toggle="collapse" data-parent="left-nav">
								<span class="glyphicon glyphicon-cog left-nav-icon"></span> 
								用户管理
								<i class="glyphicon glyphicon-chevron-left pull-right" style="line-height: 16px;"></i>
							</a>
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
					<div class="show-vm-total">
						<div class="vm-title"><span>虚拟机状态</span></div>
						<div class="vm-number">
							<div class="vm-close-number">
								<a href="showVM">${activeVmCount }</a>
								<h3>已启动</h3>
							</div>
							<div class="vm-line"></div>
							<div class="vm-open-number">
								<a href="showVM">${vmCount }</a>
								<h3>总数</h3>
							</div>
						</div>
					</div>
					<div class="show-resource-use-rate">
						<div class="resource-name"><span>CPU使用率</span></div>
						<div id="cpu-use-rate"></div>
						<div class="show-percentage">
							<a href="showClusterAndHost"></a>
							<span>%</span>
						</div>
					</div>
					<div class="show-resource-use-rate">
						<div class="resource-name"><span>内存使用率</span></div>
						<div id="ram-use-rate"></div>
						<div class="show-percentage">
							<a href="showClusterAndHost"></a>
							<span>%</span>
						</div>
					</div>
					<div class="show-resource-use-rate">
						<div class="resource-name"><span>存储使用率</span></div>
						<div id="storage-use-rate">
						</div>
						<span class="total-storage">总${storageTotal }G</span>
					</div>
				</div>
			</div>
			<div class="footer"></div>
		</div>
	</body>
</html>