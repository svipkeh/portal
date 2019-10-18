<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp" %>
	<script type="text/javascript">
	
	$(document).ready(function() {
	});
	
	function form_cancel(){
		window.location = '${path}/fort/fortview/view';
	}
	
	function f_fortmgr_service_start(id){
		$.page.post({
			comfirm : '启动该服务',
			url: "${path}/fort/fortres/servmgrServiceStartInvoke",
			callback : function(data){
				$.dialog.info("启动服务成功");
				setInterval(form_cancel, 500);
			}
		});
	}
	
	function f_fortmgr_service_stop(id){
		$.page.post({
			comfirm : '停止该服务',
			url: "${path}/fort/fortres/servmgrServiceStopInvoke",
			callback : function(data){
				$.dialog.info("停止服务成功");
				setInterval(form_cancel, 500);
			}
		});
	}
	
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
	<div class="container-fluid">
		<div class="row page-header">
			<div class="col-xs-3">
				<h4><span class="glyphicon glyphicon-th"></span> 堡垒机总览 </h4>
			</div>
			<div class="col-xs-9 text-right" style="padding-right:0px;">
				<button type="button" class="btn btn-default btn_loading" onclick="f_fortmgr_service_start()"><span class="glyphicon glyphicon-ok"></span> 启动图堡管理服务</button>
				<button type="button" class="btn btn-default btn_loading" onclick="f_fortmgr_service_stop()"><span class="glyphicon glyphicon-remove"></span> 停止图堡管理服务</button>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="panel panel-default">
					<div class="panel-body text-center">
						<h3 class="text-danger">${apps_num } <small>种</small></h3>
						<h5>运维工具</h5>
					</div>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="panel panel-default">
					<div class="panel-body text-center">
						<h3 class="text-danger">${fort_res_num } <small>台</small></h3>
						<h5>堡垒机集群成员</h5>
					</div>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="panel panel-default">
					<div class="panel-body text-center">
						<h3 class="text-danger">${fort_acc_num } <small>个</small></h3>
						<h5>运维账号</h5>
					</div>
				</div>
			</div>
			<div class="col-xs-3">
				<div class="panel panel-default">
					<div class="panel-body text-center">
						<h3 class="text-danger">${fort_serv_deamon_service_stauts }</h3>
						<h5>堡垒机服务</h5>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<h4 class="bg-info">堡垒机运行状态 </h4>
				<div class="row">
					<c:forEach items="${fortResList }" var="data">
						<div class="col-xs-3">
							<div class="panel panel-default">
								<div class="panel-body" style="padding-bottom: 8px;">
									<h4><span class="label label-${data.bg_class }">${data.fort_ip } - (${data.data_status_str })</span></h4>
									<p>未使用账号：${data.acc_notuse_num } 个</p>
									<p>已分配账号：${data.acc_assigned_num } 个</p>
									<p>使用中账号：${data.acc_connected_num } 个</p>
									<p>已断开账号：${data.acc_disconnected_num } 个</p>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
				<h4><small>状态说明：</small><span class="label label-success">正常</span>  <span class="label label-danger">失联</span>  <span class="label label-default">未知</span></h4>
				<div class="text-right" style="border-top: solid 1px #ddd; padding-top: 10px;"></div>
			</div>
		</div>
	</div>
</body>
</html>