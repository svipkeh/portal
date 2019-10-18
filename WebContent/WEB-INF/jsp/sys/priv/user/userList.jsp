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
		$.util.hide_buttons();
		$("#check_all").get(0).checked=false;
		$.page.post({
			url : "${path}/sys/priv/user/listInvoke?current=" + current,
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
		window.location = "${path}/sys/priv/user/add";
	}

	// 修改
	function f_edit(id){
		window.location = "${path}/sys/priv/user/edit?id="+id;
	}
	
	function f_refresh_pwd(id){
		var ids = id?id:$.util.checkbox_values('ck1');
		$.page.post({
			comfirm : '操作',
			url: "${path}/sys/priv/user/refreshPwdInvoke",
			data : 'ids='+ids,
			callback: function(data){
				$.dialog.info("操作成功");
				getData(0);
			}
		});
	}
	
	function f_unlock(name){
		$.page.post({
			comfirm : '操作',
			url: "${path}/sys/priv/user/unlockInvoke",
			data : 'name='+name,
			callback: function(data){
				$.dialog.info("操作成功");
				getData(0);
			}
		});
	}
	
	// 改变状态
	function f_status(status, id){
		var ids = id?id:$.util.checkbox_values('ck1');
		$.page.post({
			comfirm : '操作',
			url: "${path}/sys/priv/user/statusInvoke",
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
				<h4><span class="glyphicon glyphicon-th"></span> 用户</h4>
			</div>
			<div class="col-xs-9 text-right" style="padding-right:0px;">
				<button type="button" class="btn btn-default btn_loading" onclick="f_unlock('admin')"><span class="glyphicon glyphicon-refresh"></span> 解锁admin</button>
				<button type="button" class="btn btn-default btn_loading" onclick="f_add()"><span class="glyphicon glyphicon-plus"></span> 新增</button>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-body" style="background-color: #eee;">
				    	<form id="form1" method="post" class="form-inline">
							<div class="form-group">
								<label>名称</label>
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
					<div id="buttons_box" style="display:none; position: absolute; z-index: 100;">
						<button type="button" class="btn btn-link btn_loading" onmouseover="$.util.light_checked('ck1')" onmouseout="$.util.dark_checked('ck1')">已选中<span id="buttons_box_num"></span>条数据</button>
						<button type="button" class="btn btn-default btn_loading" onclick="f_status('ENABLED');"><span class="glyphicon glyphicon-ok"></span> 启用</button>
						<button type="button" class="btn btn-default btn_loading" onclick="f_status('DISABLED');"><span class="glyphicon glyphicon-remove"></span> 禁用</button>
						<button type="button" class="btn btn-default btn_loading" onclick="f_status('DELETED');"><span class="glyphicon glyphicon-trash"></span> 删除</button>
					</div>
					<table class="table table-hover">
						<thead>
							<tr>
								<th><input id="check_all" type="checkbox" style="margin: 0px;" onclick="$.util.checkbox_all(this,'ck1')"/></th>
								<th>中文名</th>
								<th>登录名</th>
								<th>邮箱</th>
								<th>手机</th>
								<th>备注</th>
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
			<td><input name="ck1" type="checkbox" style="margin: 0px;" value="{{data.id}}" onclick="$.util.show_buttons('ck1')"/></td>
			<td>{{data.name_cn}}</td>
			<td>{{data.name}}</td>
			<td>{{data.mail}}</td>
			<td>{{data.mobile}}</td>
			<td>{{data.remark}}</td>
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
				<a href="#" onclick="f_refresh_pwd('{{data.id}}')"><span class="glyphicon glyphicon-lock"></span> 重置密码</a>
				<a href="#" onclick="f_unlock('{{data.name}}')"><span class="glyphicon glyphicon-refresh"></span> 解锁</a>
			</td>
		</tr>
		{{/each}}
	</script>
</body>
</html>