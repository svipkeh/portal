<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	$(document).ready(function() {
		$("#s_status").ui_select_enums({schema: 'FortAccStatus', defaultLabel: '全部',  selectedValue: '${qm.status}'});
		getData(0);
	});
	
	// 查询
	function getData(current) {
		$.page.post({
			url : "${path}/fort/fortacc/listInvoke?size=50&current=" + current,
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

	function f_close_session(id){
		$.page.post({
			comfirm : '注销',
			url: "${path}/fort/fortacc/closeSessionInvoke",
			data : 'acc_id='+id, 
			callback : function(data){
				$.dialog.info("注销成功");
			}
		});
	}
	
	function f_change_pwd_pre(ids){
		$("label.validate_label").text("");
		$("#acc_id").val(ids);
		$("#v_modal").modal();
	}
	
	function f_change_pwd(id){
		$.page.post({
			url: "${path}/fort/fortacc/changePwdInvoke",
			data : $("#form2").serialize(),
			callback : function(data){
				$.dialog.info("修改密码成功");
			}
		});
		$("label#validate_all").text("提交成功，等待后台调度");
		setTimeout(function(){
			$("#v_modal").modal('hide');
		}, 1000);
	}
	
	function f_change_pwd_all(id){
		$.page.post({
			comfirm : '修改密码',
			url: "${path}/fort/fortacc/changePwdAllInvoke",
			data : $("#form1").serialize(),
			callback : function(data){
				$.dialog.info("修改密码成功");
				getData(0);
			}
		});
	}
	
	function check_input_pwd(ck){
		var cked = $(ck).is(":checked");
		if(cked){
			$("#new_pwd").val("");
			$("#new_pwd").attr("readonly", "readonly");
		}else{
			$("#new_pwd").removeAttr("readonly");
		}
	}
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
	<div class="container-fluid" id="body">
		<div class="row page-header">
			<div class="col-xs-3">
				<h4><span class="glyphicon glyphicon-th"></span> 堡垒机账号监控 </h4>
			</div>
			<div class="col-xs-9 text-right" style="padding-right:0px;">
				<button type="button" class="btn btn-default btn_loading" onclick="f_change_pwd_all()"><span class="glyphicon glyphicon-cog"></span> 修改本页所有账号密码</button>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-body" style="background-color: #eee;">
				    	<form id="form1" method="post" class="form-inline">
							<div class="form-group">
								<label>IP</label>
						    	<input type="text" class="form-control" name="qm.fort_ip" value="${qm.fort_ip }"></input>
							</div>
							<div class="form-group">
								<label>账号状态</label>
						    	<select class="form-control" id="s_status" name="qm.status"></select>
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
								<th>IP</th>
								<th>账号</th>
								<th>运行时：会话名</th>
								<th>运行时：会话ID</th>
								<th>运行时：空闲时间</th>
								<th>运行时：登录时间</th>
								<th>状态</th>
								<th>最近分配使用时间</th>
								<th>最近修改密码时间</th>
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
			<td>{{data.username}}</td>
			<td>{{data.s_session_name}}</td>
			<td>{{data.s_session_id}}</td>
			<td>{{data.s_rest_time}}</td>
			<td>{{data.s_login_time}}</td>
			<td>
				{{if data.s_status=='NOTUSE'}}
    				<span class="label label-default">{{data.s_status_str}}</span>
				{{else if data.s_status=='ASSIGNED'}}
    				<span class="label label-info">{{data.s_status_str}}</span>
				{{else if data.s_status=='CONNECTED'}}
    				<span class="label label-success">{{data.s_status_str}}</span>
				{{else if data.s_status=='DISCONNECTED'}}
    				<span class="label label-success">{{data.s_status_str}}</span>
				{{/if}}
			</td>
			<td>{{data.assigned_time}}</td>
			<td>{{data.pwd_update_time}}</td>
			<td>
				<a href="#" onclick="f_close_session('{{data.id}}')"><span class="glyphicon glyphicon-log-out"></span> 注销</a>
				<a href="#" onclick="f_change_pwd_pre({{data.id}})"><span class="glyphicon glyphicon-cog"></span> 修改密码</a>
			</td>
		</tr>
		{{/each}}
	</script>
	
	<!-- 修改密码弹出框 -->
	<div class="modal fade" id="v_modal">
	  	<div class="modal-dialog">
	    	<div class="modal-content">
		      	<div class="modal-body">
		      		<form id="form2" method="post" class="form">
		      			<input type="hidden" id="acc_id" name="acc_id">
		      			<div class="form-group">
					  		<label>手动输入密码</label>
					    	<input type="password" class="form-control" id="new_pwd" name="new_pwd"></input>
					  	</div>
	      			 	<div class="checkbox">
					    	<label>
					      		<input type="checkbox" name="auto_pwd" onclick="javascript:check_input_pwd(this);"> 自动生成密码
					    	</label>
					  	</div>
					  	<div class="text-right">
					  		<label id="validate_all" class="control-label validate_label" style="float: left;"></label>
				        	<button type="button" class="btn btn-default btn_loading" onclick="f_change_pwd();"><span class="glyphicon glyphicon-cog"></span> 修改密码</button>
		  				</div>
	  				</form>
		      	</div>
		    </div>
		</div>
	</div>
</body>
</html>