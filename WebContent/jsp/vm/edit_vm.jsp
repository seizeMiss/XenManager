<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:include page="../common/common.jsp"></jsp:include>
<script type="text/javascript">
	$(function() {
		$("#popover-cpu-number").webuiPopover({
			closeable : true,
			animation : 'pop',
			placement : 'right'
		});
		$("#popover-ram-number").webuiPopover({
			closeable : true,
			animation : 'pop',
			placement : 'right'
		});
	});
</script>
<style>
#popover-cpu-number {position: absolute;top: 230px;font-size: 20px;color: #337ab7;cursor: pointer;text-decoration: none;}
#popover-ram-number {position: absolute;top: 280px;font-size: 20px;color: #337ab7;cursor: pointer;text-decoration: none;}
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
						<li class="nav-li active">
							<a href="showVM"> <span class="glyphicon glyphicon-cloud left-nav-icon"></span> 虚拟机  </a>
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
									<a href="virtual_machine.html">虚拟机</a>
								</li>
								<li>
									修改配置
								</li>
							</ol>
						</div>
					</div>
					<div class="content-form">
						<form class="form-horizontal" role="form">

							<div class="form-group">
								<label for="name-rule" class="col-sm-2 control-label"><span class="asterisk">*</span>命名规则</label>
								<div class="col-sm-10">
									<span style="line-height: 35px;font-size: 20px;">vm-cbl</span>
								</div>
							</div>


							<div class="form-group">
								<label for="user_mirror" class="col-sm-2 control-label"><span class="asterisk">*</span>镜像</label>
								<div class="col-sm-10">
									<span style="line-height: 35px;font-size: 20px;">mirror</span>
								</div>
							</div>

							<div class="form-group">
								<label for="vm-cpu" class="col-sm-2 control-label"><span class="asterisk">*</span>CPU</label>
								<div class="col-sm-10">
									<div class="cpu-group">
										<label name="itemlabel" class="item" title="1核"> <img src="../img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="1核" value="1" />
											1核 </label>
										<label name="itemlabel" class="item" title="2核"> <img src="../img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="2核" value="2" />
											2核 </label>
										<label name="itemlabel" class="item" title="4核"> <img src="../img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="4核" value="4" />
											4核 </label>
										<label name="itemlabel" class="item" title="8核"> <img src="../img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="8核" value="8" />
											8核 </label>
										<label name="itemlabel" class="item" title="其他"> <img src="../img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="其他" value="other" />
											其他
											<input type="text" id="cpu-number" maxlength="2" min="1" max="16" style="display: none" name="specialInput"/>
										</label>
									</div>
								</div>
								<a id="popover-cpu-number" data-trigger="hover" data-container="body" data-toggle="popover"
								data-placement="right" data-content="每个虚拟机最多只能分配 16 个CPU核数"
								class="glyphicon glyphicon-info-sign"></a>
							</div>

							<div class="form-group">
								<label for="vm-ram" class="col-sm-2 control-label"><span class="asterisk">*</span>内存</label>
								<div class="col-sm-10">
									<div class="ram-group">
										<label name="itemlabel" class="item" title="2GB"> <img src="../img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="2GB" value="2" />
											2GB </label>
										<label name="itemlabel" class="item" title="4GB"> <img src="../img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="4GB" value="4" />
											4GB </label>
										<label name="itemlabel" class="item" title="8GB"> <img src="../img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="8GB" value="8" />
											8GB </label>
										<label name="itemlabel" class="item" title="16GB"> <img src="../img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="16GB" value="16" />
											16GB </label>
										<label name="itemlabel" class="item" title="其他"> <img src="../img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="其他" value="other" />
											其他
											<input type="text" id="ram-number" maxlength="2" min="1" max="16" style="display: none" name="specialInput"/>
										</label>
									</div>
								</div>
								<a id="popover-ram-number" data-trigger="hover" data-container="body" data-toggle="popover"
								data-placement="right" data-content="如果选择“其他”，内存量为 1 ~ 8 GB 时，最小步进 0.5 GB；内存量为 8 ~ 16 GB 时，最小步进 1 GB；<br>不支持为虚拟机分配超过 16 GB内存"
								class="glyphicon glyphicon-info-sign"></a>
							</div>

							<div class="form-group operation">
								<div class="col-sm-offset-2 col-sm-10">
									<button type="submit" class="btn btn-default" style="margin-right: 30px;">
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