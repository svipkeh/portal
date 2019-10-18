<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	$(document).ready(function() {
		getData(0);
	});
	
	function form_cancel(){
		window.location = '${path}/portal/account/list?keep=1';
	}
	
	// 查询
	function getData(current) {
		$.page.post({
			url : "${path}/portal/accountuser/listInvoke?current=" + current,
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
	
	function f_status(status, id){
		var ids = id?id:$.util.checkbox_values('ck1');
		$.page.post({
			comfirm : '请确认操作',
			url: "${path}/portal/accountuser/statusInvoke?resid=${resource.id}&accid="+$("#accid").val(),
			data : 'status='+status+'&ids='+ids,
			callback: function(data){
				$.dialog.info("操作成功");
				getData(0);
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
				<h4><span class="glyphicon glyphicon-th"></span> 授权管理 <small>(${resource.name_cn } / ${account.name })</small></h4>
			</div>
			<div class="col-xs-9 text-right" style="padding-right: 0px;">
				<button type="button" class="btn btn-default btn_loading" onclick="form_cancel()"><span class="glyphicon glyphicon-circle-arrow-left"></span> 返回</button>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-body" style="background-color: #eee;">
				    	<form id="form1" method="post" class="form-inline">
							<input type="hidden" id="accid" name="qm.accid" value="${account.id }"/>
							<div class="form-group">
								<label>用户名</label>
						    	<input type="text" class="form-control" name="qm.name" value="${qm.name }"></input>
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
								<th>中文名</th>
								<th>登录名</th>
								<th>手机</th>
								<th>用户状态</th>
								<th>授权状态</th>
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
			<td>{{data.mobile}}</td>
			<td>
				{{if data.data_status=='ENABLED'}}
    				<span class="label label-success">{{data.data_status_str}}</span>
				{{else if data.data_status=='DISABLED'}}
    				<span class="label label-danger">{{data.data_status_str}}</span>
				{{/if}}
			</td>
			<td>
				{{if data.user_id}}
    				<span class="label label-success">已授权</span>
				{{else}}
    				<span class="label label-default">未授权</span>
				{{/if}}
			</td>
			<td>
				{{if data.user_id}}
    				<a href="#" onclick="f_status('DISABLED',{{data.id}})"><span class="glyphicon glyphicon-minus"></span> 取消授权</a>
				{{else}}
    				<a href="#" onclick="f_status('ENABLED',{{data.id}})"><span class="glyphicon glyphicon-plus"></span> 添加授权</a>
				{{/if}}
			</td>
		</tr>
		{{/each}}
	</script>
</body>
</html>