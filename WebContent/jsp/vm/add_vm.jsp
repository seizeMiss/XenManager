<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../common/common.jsp" />
<script type="text/javascript" src="${pageContext.request.contextPath}/script/vm.js"></script>
<style>
#popover-name-rule {position: absolute;top: 128px;font-size: 20px;color: #337ab7;cursor: pointer;text-decoration: none;}
#popover-vm-number {position: absolute;top: 178px;font-size: 20px;color: #337ab7;cursor: pointer;text-decoration: none;}
#popover-cpu-number {position: absolute;top: 490px;font-size: 20px;color: #337ab7;cursor: pointer;text-decoration: none;}
#popover-ram-number {position: absolute;top: 545px;font-size: 20px;color: #337ab7;cursor: pointer;text-decoration: none;}
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
							<a href="showVM" data-toggle="collapse" data-parent="left-nav"> <span class="glyphicon glyphicon-cloud left-nav-icon"></span> 虚拟机  </a>
							
						</li>
						<li class="nav-li">
							<a href="#user-child" data-toggle="collapse" data-parent="left-nav"> <span class="glyphicon glyphicon-cog left-nav-icon"></span> 用户管理 <i class="glyphicon glyphicon-chevron-left pull-right arrow-sign"></i> </a>
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
									添加虚拟机
								</li>
							</ol>
						</div>
					</div>
					<div class="content-form">
						<form class="form-horizontal" role="form">

							<div class="form-group">
								<label for="name-rule" class="col-sm-2 control-label"><span class="asterisk">*</span>命名规则</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="name-rule" placeholder="VM-">
								</div>
								<a id="popover-name-rule" data-trigger="click" data-container="body" data-toggle="popover"
								data-placement="right" data-content="虚拟机名只能由英文、数字和‘横杠 (-)’构成，最长 10 字符"
								class="glyphicon glyphicon-info-sign"></a>
							</div>

							<div class="form-group">
								<label for="vm-number" class="col-sm-2 control-label"> <span class="asterisk">*</span>虚拟机数量 </label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="vm-number" placeholder="请输入虚拟机数量">
								</div>
								<a id="popover-vm-number" data-trigger="click" data-container="body" data-toggle="popover"
								data-placement="right" data-content="支持批量创建 1 ~ 500 个虚拟机"
								class="glyphicon glyphicon-info-sign"></a>
							</div>

							<div class="form-group">
								<label for="vm-colony" class="col-sm-2 control-label"> <span class="asterisk">*</span>集群 </label>
								<div class="col-sm-10">
								<div class="input-group" style="float: left;">
									<input type="text" class="form-control" id="selected-cluster" readonly="readonly">
									<div class="input-group-btn">
												<button type="button" class="btn btn-default dropdown-toggle show-cluster" data-toggle="dropdown">
													&nbsp;<span class="caret" style="font-size: 20px;"></span>
												</button>
												<ul id= "select-cluster" class="dropdown-menu pull-right" style="width: 580px;">
													<c:forEach var="cluster" items="${clusters }">
													<li>
														<a href="#">${cluster.name }</a>
													</li>
													</c:forEach>
												</ul>
											</div>
											</div>
								</div>
							</div>

							<div class="form-group">
								<label for="user_psd_conf" class="col-sm-2 control-label"><span class="asterisk">*</span>镜像</label>
								<div class="col-sm-10" style="height: 200px;">
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
													<th>镜像名称</th>
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
							</div>

							<div class="form-group">
								<label for="vm-cpu" class="col-sm-2 control-label"><span class="asterisk">*</span>CPU</label>
								<div class="col-sm-10">
									<div class="cpu-group">
										<label name="itemlabel" class="item" title="1核"> <img src="${pageContext.request.contextPath}/img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="1核" value="1" />
											1核 </label>
										<label name="itemlabel" class="item" title="2核"> <img src="${pageContext.request.contextPath}/img/selectedRadio.png" />
											<input type="radio" name="select-cpu" checked="checked" title="2核" value="2" />
											2核 </label>
										<label name="itemlabel" class="item" title="4核"> <img src="${pageContext.request.contextPath}/img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="4核" value="4" />
											4核 </label>
										<label name="itemlabel" class="item" title="8核"> <img src="${pageContext.request.contextPath}/img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="8核" value="8" />
											8核 </label>
										<label name="itemlabel" class="item" title="其他"> <img src="${pageContext.request.contextPath}/img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="其他" value="other" />
											其他
											<input type="text" id="cpu-number" maxlength="2" min="1" max="16" style="display: none" name="specialInput"/>
										</label>
									</div>
								</div>
								<a id="popover-cpu-number" data-trigger="click" data-container="body" data-toggle="popover"
								data-placement="right" data-content="每个虚拟机最多只能分配 16 个CPU核数"
								class="glyphicon glyphicon-info-sign"></a>
							</div>

							<div class="form-group">
								<label for="vm-ram" class="col-sm-2 control-label"><span class="asterisk">*</span>内存</label>
								<div class="col-sm-10">
									<div class="ram-group">
										<label name="itemlabel" class="item" title="2GB"> <img src="${pageContext.request.contextPath}/img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="2GB" value="2" />
											2GB </label>
										<label name="itemlabel" class="item" title="4GB"> <img src="${pageContext.request.contextPath}/img/selectedRadio.png" />
											<input type="radio" name="select-cpu" checked="checked" title="4GB" value="4" />
											4GB </label>
										<label name="itemlabel" class="item" title="8GB"> <img src="${pageContext.request.contextPath}/img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="8GB" value="8" />
											8GB </label>
										<label name="itemlabel" class="item" title="16GB"> <img src="${pageContext.request.contextPath}/img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="16GB" value="16" />
											16GB </label>
										<label name="itemlabel" class="item" title="其他"> <img src="${pageContext.request.contextPath}/img/selectRadio.png" />
											<input type="radio" name="select-cpu" title="其他" value="other" />
											其他
											<input type="text" id="ram-number" maxlength="2" min="1" max="16" style="display: none" name="specialInput"/>
										</label>
									</div>
								</div>
								<a id="popover-ram-number" data-trigger="click" data-container="body" data-toggle="popover"
								data-placement="right" data-content="如果选择“其他”，内存量为 1 ~ 8 GB 时，最小步进 0.5 GB；内存量为 8 ~ 16 GB 时，最小步进 1 GB；<br>不支持为虚拟机分配超过 16 GB内存"
								class="glyphicon glyphicon-info-sign"></a>
							</div>

							<div class="form-group">
								<label for="vm-storage" class="col-sm-2 control-label"><span class="asterisk">*</span>磁盘配置</label>
								<div class="col-sm-10">
									<div class="storage-group">
										<label style="line-height: 40px;float:left;">存储位置:</label>
										<div class="input-group" style="width: 85%;float: left;margin-left: 20px;">
											<input type="text" id="selected-storage-place" readonly="readonly" class="form-control" style="font-size: 18px;">
											<div class="input-group-btn">
												<button type="button" class="btn btn-default dropdown-toggle show-storage" data-toggle="dropdown">
													&nbsp;<span class="caret" style="font-size: 20px;"></span>
												</button>
												<ul id= "select-storage" class="dropdown-menu pull-right" style="width: 490px;">
													<c:forEach var="storage" items="${storages }">
													<li>
														<a href="#">
															<span class="storage-name">${storage.name }</span>
															<span class="storage-desc">总大小:${storage.storageTotal }G  可用:${storage.storageTotal-storage.storageUsed }G</span>
														</a>
													</li>
													</c:forEach>
												</ul>
											</div>
										</div>
									</div>
									<ul class="disk-list">
										<li>
											<div class="disk-details">
												<span>用户磁盘:</span>
												<input type="text" id="user-disk-size" value="40"/>
											</div>
											<div style="float: left;margin-left: 10px;line-height: 35px;">
												<span>GB</span>
											</div>
											<div class="disk-delete pull-right"><a></a>
											</div>
										</li>
									</ul>
									<button id="add-disk" class="btn btn-default add-disk-btn">
										<span class="glyphicon glyphicon-plus"></span><span style="margin-left: 10px;">添加磁盘</span>
									</button>
								</div>
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
		<script type="text/javascript">
			$(function() {
				var storageLen = $("#select-storage").children("li").length;
				if(storageLen == 1){
					$("#selected-storage-place").val($(".storage-name").html());
				}
				//添加磁盘
				$("#add-disk").click(function() {
					$(".disk-list").append("<li><div class='disk-details'><span>用户磁盘:</span><input type='text' name='user-disk' value='40' /></div><div style='float: left;margin-left: 10px;line-height: 35px;'><span>GB</span></div><div class='disk-delete pull-right'><a></a></div></li>");
					
					return false;
				});
				
				$(document).on("click", ".disk-delete a", function(){
					$(this).closest("li").remove();
				});
				
				$("#popover-name-rule").webuiPopover({
					closeable : true,
					animation : 'pop',
					placement : 'right'
				});
				$("#popover-vm-number").webuiPopover({
					closeable : true,
					animation : 'pop',
					placement : 'right'
				});
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
	</body>
</html>