<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:include page="../common/common.jsp" />
<script type="text/javascript">
	$(function() {
		$("#popover-image-name").webuiPopover({
			closeable : true,
			animation : 'pop'
		});
		$("#popover-select-vm").webuiPopover({
			closeable : true,
			animation : 'pop'
		});
	});
</script>
<style>
#popover-image-name {position: absolute;top: 178px;font-size: 20px;color: #337ab7;cursor: pointer;text-decoration: none;}
#popover-select-vm {position: absolute;top: 343px;font-size: 20px;color: #337ab7;cursor: pointer;text-decoration: none;}
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
						<li class="nav-li active">
							<a href="showImage"><span class="glyphicon glyphicon-floppy-disk left-nav-icon"></span>镜像</a>
						</li>
						<li class="nav-li">
							<a href="showVM"> <span class="glyphicon glyphicon-cloud left-nav-icon"></span> 虚拟机 </a>
						</li>
						<li class="nav-li">
							<a href="#user-child" data-toggle="collapse" data-parent="left-nav"> <span class="glyphicon glyphicon-cog left-nav-icon"></span> 用户管理 <i class="glyphicon glyphicon-chevron-left pull-right" style="line-height: 16px;"></i> </a>
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
						<div class="nav-content">
							<ol class="breadcrumb" style="width: 100%;background: white">
								<li>
									<a href="virtual_machine.html">镜像</a>
								</li>
								<li>
									添加镜像
								</li>
							</ol>
						</div>
					</div>
					<div class="content-form">
						<form class="form-horizontal" role="form">

							


							<div class="form-group">
								<label for="vm-colony" class="col-sm-2 control-label"> <span class="asterisk">*</span>集群 </label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="vm-colony" value="Xen" disabled="disabled">
								</div>
							</div>
							
							<div class="form-group">
								<label for="image-name" class="col-sm-2 control-label"><span class="asterisk">*</span>名称</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="image-name">
								</div>
								<a id="popover-image-name" data-trigger="click" data-container="body" data-toggle="popover"
								data-placement="right" data-content="镜像名只能由中英文、数字和横杠 (-) 构成，最长30字符"
								class="glyphicon glyphicon-info-sign"></a>
							</div>
							
							<div class="form-group">
								<label for="image_desc" class="col-sm-2 control-label">描述</label>
								<div class="col-sm-10">
									<textarea id="image_desc" placeholder="描述">
									</textarea>
								</div>
							</div>

							<div class="form-group">
								<label for="user_psd_conf" class="col-sm-2 control-label"><span class="asterisk">*</span>选择虚拟机</label>
								<div class="col-sm-10" style="height: 300px;">
									<div class="input-group">
										<input type="text" class="form-control" id="search-content">
										<span class="input-group-btn">
											<button class="btn btn-default" type="button" id="search">
												搜索
											</button> </span>
									</div>
									<div class="search-content">
										<table class="table table-bordered">
											<thead style="background-color: #808080">
												<tr>
													<th></th>
													<th>虚拟机名称</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td>
													<input type="radio" name="select-mirror" value="windows 7镜像"/>
													</td>
													<td>windows 7镜像</td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
								<a id="popover-select-vm" data-trigger="click" data-container="body" data-toggle="popover"
								data-placement="right" data-content="选择 1 个可用虚拟机创建镜像；搜索框最多输入 127 个字符"
								class="glyphicon glyphicon-info-sign"></a>
							</div>


							<div class="form-group operation">
								<div class="col-sm-offset-2 col-sm-10">
									<button type="submit" class="btn btn-default ok-btn" style="margin-right: 30px;">
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