<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	$(document).ready(function() {
		getData(0);
	});
	
	// 查询
	function getData(current) {
		$.page.post({
			url : "${path}/fort/fortres/listInvoke?current=" + current,
			data : '',
			callback : function(data){
				var html = template('listScript', data.value);
				$('#listContainer').html(html);
			}
		});
	}

	// 新增
	function f_add(){
		window.location = "${path}/fort/fortres/add";
	}

	// 修改
	function f_edit(id){
		window.location = "${path}/fort/fortres/edit?id="+id;
	}
	
	// 改变状态
	function f_status(status, id){
		var ids = id?id:$.util.checkbox_values('ck1');
		$.page.post({
			comfirm : '操作',
			url: "${path}/fort/fortres/statusInvoke",
			data : 'status='+status+'&ids='+ids,
			callback: function(data){
				$.dialog.info("操作成功");
				getData(0);
			}
		});
	}
	
	function f_fortmgr_service_start(id){
		$.page.post({
			comfirm : '启动该服务',
			url: "${path}/fort/fortres/servmgrServiceStartInvoke",
			callback : function(data){
				$.dialog.info("启动服务成功");
			}
		});
	}
	
	function f_fortmgr_service_stop(id){
		$.page.post({
			comfirm : '停止该服务',
			url: "${path}/fort/fortres/servmgrServiceStopInvoke",
			callback : function(data){
				$.dialog.info("停止服务成功");
			}
		});
	}
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
	<div class="container-fluid" id="body">
		<div class="row page-header">
			<div class="col-xs-3">
				<h4><span class="glyphicon glyphicon-th"></span> 堡垒机配置 </h4>
			</div>
			<div class="col-xs-9 text-right" style="padding-right:0px;">
				<button type="button" class="btn btn-default btn_loading" onclick="f_add()"><span class="glyphicon glyphicon-plus"></span> 新增</button>
<!-- 				<button type="button" class="btn btn-default btn_loading" onclick="f_fortmgr_service_start()"><span class="glyphicon glyphicon-ok"></span> 启动图堡管理服务</button> -->
<!-- 				<button type="button" class="btn btn-default btn_loading" onclick="f_fortmgr_service_stop()"><span class="glyphicon glyphicon-remove"></span> 停止图堡管理服务</button> -->
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="table-responsive">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>IP</th>
								<th>外网IP</th>
								<th>运行状态</th>
								<th>运维端口</th>
								<th>管理端口</th>
								<th>最近成功通讯时间</th>
								<th>状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="listContainer"></tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 text-right" id="page"></div>
		</div>
	</div>
	<script id="listScript" type="text/html">
		{{each list as data}}
		<tr>
			<td>{{data.fort_ip}}</td>
			<td>{{data.fort_ip_wan}}</td>
			<td>
				{{if data.running_status=='ZHENGCHANG'}}
    				<span class="label label-success">{{data.running_status_str}}</span>
				{{else if data.running_status=='SHILIAN'}}
    				<span class="label label-danger">{{data.running_status_str}}</span>
				{{else if data.running_status=='UNKNOWN'}}
    				<span class="label label-default">{{data.running_status_str}}</span>
				{{/if}}
			</td>
			<td>{{data.mstsc_port}}</td>
			<td>{{data.servmgr_port}}</td>
			<td>{{data.servmgr_syn_time}}</td>
			<td>
				{{if data.data_status=='ENABLED'}}
    				<span class="label label-success">{{data.data_status_str}}</span>
				{{else if data.data_status=='DISABLED'}}
    				<span class="label label-danger">{{data.data_status_str}}</span>
				{{/if}}
			</td>
			<td>
				{{if data.data_status=='ENABLED'}}
    				<a href="#" onclick="f_status('DISABLED',{{data.id}})"><span class="glyphicon glyphicon-remove"></span> 禁用</a>
				{{else if data.data_status=='DISABLED'}}
    				<a href="#" onclick="f_status('ENABLED',{{data.id}})"><span class="glyphicon glyphicon-ok"></span> 启用</a>
				{{/if}}
				<a href="#" onclick="f_edit('{{data.id}}')"><span class="glyphicon glyphicon-edit"></span> 修改</a>
				<a href="#" onclick="f_status('DELETED',{{data.id}})"><span class="glyphicon glyphicon-trash"></span> 删除</a>
			</td>
		</tr>
		{{/each}}
	</script>
</body>
</html>