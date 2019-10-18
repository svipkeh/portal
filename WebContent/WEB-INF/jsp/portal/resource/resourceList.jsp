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
			url : "${path}/portal/resource/listInvoke?current=" + current,
			data : $("#form1").serialize(),
			callback : function(data){
				var html = template('listScript', data.value);
				$('#listContainer').html(html);
				$('#page').paginator({
					current_page : data.value.pageNumber,
					page_count : data.value.totalPage,
					total_count : data.value.totalRow,
					page_size : data.value.pageSize,
					pager_click : function(page){ 
						getData(page);
					}
				});
			}
		});
	}

	// 新增
	function f_add(){
		window.location = "${path}/portal/resource/add";
	}

	// 修改
	function f_edit(id){
		window.location = "${path}/portal/resource/edit?id="+id;
	}
	
	// 改变状态
	function f_status(status, id){
		var ids = id?id:$.util.checkbox_values('ck1');
		$.page.post({
			comfirm : '操作',
			url: "${path}/portal/resource/statusInvoke",
			data : 'status='+status+'&ids='+ids,
			callback: function(data){
				$.dialog.info("操作成功");
				getData(0);
			}
		});
	}
	
	// 资源工具
	function f_resapp_list(id){
		window.location = "${path}/portal/resourceapp/list?resid="+id;
	}
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
	<div class="container-fluid" id="body">
		<div class="row page-header">
			<div class="col-xs-3">
				<h4><span class="glyphicon glyphicon-th"></span> 资源管理</h4>
			</div>
			<div class="col-xs-9 text-right" style="padding-right:0px;">
				<button type="button" class="btn btn-default btn_loading" onclick="f_add()"><span class="glyphicon glyphicon-plus"></span> 新增</button>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-body" style="background-color: #eee;">
				    	<form id="form1" method="post" class="form-inline">
							<div class="form-group">
								<label>资源名称</label>
						    	<input type="text" class="form-control" name="qm.name_cn" value="${qm.name_cn }"></input>
							</div>
							<div class="form-group">
								<label>资源标识</label>
						    	<input type="text" class="form-control" name="qm.name" value="${qm.name }"></input>
							</div>
							<div class="form-group">
								<label>IP</label>
						    	<input type="text" class="form-control" name="qm.ip" value="${qm.ip }"></input>
							</div>
							<button type="button" class="btn btn-default btn_loading" onclick="getData(1);"><span class="glyphicon glyphicon-search"></span> 搜索</button>
							<span class="img_loading" style="display: none;"><img src="${static_path}/bin/img/loading.gif" /></span>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="table-responsive">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>资源名称</th>
								<th>资源标识</th>
								<th>IP</th>
								<th>资源类型</th>
								<th>备注</th>
								<th>工具管理</th>
								<th>工具列表</th>
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
			<td>{{data.name_cn}}</td>
			<td>{{data.name}}</td>
			<td>{{data.ip}}</td>
			<td>{{data.res_type_str}}</td>
			<td>{{data.remark}}</td>
			<td><a href="#" onclick="f_resapp_list('{{data.id}}')"><span class="glyphicon glyphicon-edit"></span> 工具管理</a></td>
			<td>{{data.resapps}}</td>
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