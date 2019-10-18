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
			url : "${path}/portal/account/listInvoke?current=" + current,
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
		window.location = "${path}/portal/account/add";
	}

	// 改变状态
	function f_status(status, id){
		var ids = id?id:$.util.checkbox_values('ck1');
		$.page.post({
			comfirm : '操作',
			url: "${path}/portal/account/statusInvoke",
			data : 'status='+status+'&ids='+ids,
			callback: function(data){
				$.dialog.info("操作成功");
				getData(0);
			}
		});
	}
	
	function f_set_pwd_pre(id){
		$("label.validate_label").text("");
		$("#accid").val(id);
		$("#modal_submit").off('click').on('click', f_set_pwd);
		$("#v_modal").modal();
	}
	
	function f_set_pwd(){
		$.page.post({
			url: "${path}/portal/account/setPwdInvoke",
			data : $("#form2").serialize(), 
			callback: function(data){
				$.dialog.info("操作成功");
				$("#v_modal").modal('hide');
				getData(0);
			}
		});
	}
	
	function f_user_list(id){
		window.location = "${path}/portal/accountuser/list?accid="+id;
	}
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
	<div class="container-fluid" id="body">
		<div class="row page-header">
			<div class="col-xs-3">
				<h4><span class="glyphicon glyphicon-th"></span> 账号管理</h4>
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
						    	<input type="text" class="form-control" name="qm.res_name_cn" value="${qm.res_name_cn }"></input>
							</div>
							<div class="form-group">
								<label>资源标识</label>
						    	<input type="text" class="form-control" name="qm.res_name" value="${qm.res_name }"></input>
							</div>
							<div class="form-group">
								<label>IP</label>
						    	<input type="text" class="form-control" name="qm.ip" value="${qm.ip }"></input>
							</div>
							<div class="form-group">
								<label>账号名称</label>
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
								<th>资源名称</th>
								<th>资源标识</th>
								<th>IP</th>
								<th>资源类型</th>
								<th>账号</th>
								<th>授权管理</th>
								<th>密码</th>
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
			<td>{{data.res_name_cn}}</td>
			<td>{{data.res_name}}</td>
			<td>{{data.ip}}</td>
			<td>{{data.res_type_str}}</td>
			<td>{{data.name}}</td>
			<td><a href="#" onclick="f_user_list('{{data.id}}')"><span class="glyphicon glyphicon-edit"></span> 授权管理</a> (目前{{data.user_num}}人)</td>
			<td><a href="#" onclick="f_set_pwd_pre('{{data.id}}')"><span class="glyphicon glyphicon-edit"></span> 设置密码</a></td>
			<td><a href="#" onclick="f_status('DELETED',{{data.id}})"><span class="glyphicon glyphicon-trash"></span> 删除</a></td>
		</tr>
		{{/each}}
	</script>
	<div class="modal fade" id="v_modal">
	  	<div class="modal-dialog">
	    	<div class="modal-content">
		      	<div class="modal-body">
		      		<form id="form2" method="post" class="form">
		      			<input type="hidden" id="accid" name="accid">
					  	<div class="form-group">
					  		<label>输入密码</label>
					    	<input type="password" name="password" class="form-control"/>
					  	</div>
					  	<div class="text-right">
					  		<label id="validate_all" class="control-label validate_label" style="float: left;"></label>
				        	<button id="modal_submit" type="button" class="btn btn-default btn_loading"><span class="glyphicon glyphicon-ok"></span> 确定</button>
		  				</div>
	  				</form>
		      	</div>
		    </div>
		</div>
	</div>
</body>
</html>