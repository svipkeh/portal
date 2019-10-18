<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	$(document).ready(function() {
		$("#is_record").ui_select_enums({schema: 'YesNo', defaultLabel: '全部',  selectedValue: '${qm.is_record}'});
		$("#ex_status").ui_select_enums({schema: 'FortSessionExStatus', defaultLabel: '全部',  selectedValue: '${qm.ex_status}'});
		//
		$("#date1").ui_day_of_year();
		$("#time1").ui_time({defaultTime: '00:00'});
		//
		$("#date2").ui_day_of_year({dayPlus: 1});
		$("#time2").ui_time({defaultTime: '00:00'});
		getData(0);
	});
	
	// 查询
	function getData(current) {
		$.page.post({
			url : "${path}/fort/fortsessionrecord/listInvoke?current=" + current,
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

	// 运行
	function f_run(id){
		$.page.post({
			url : "${path}/fort/fortsessionrecord/playInvoke",
			data :  "sessionid="+id,
			callback: function(data){
				$("#app").get(0).src = "Trustmo://" + data.value;
			}
		});
	}
	
	// 删除
	function f_del(id){
		var ids = id?id:$.util.checkbox_values('ck1');
		$.page.post({
			url : "${path}/fort/fortsessionrecord/delRecordInvoke",
			data :  "ids="+ids,
			callback: function(data){
				$.dialog.info("操作成功");
				getData(0);
			}
		});
	}
	</script>
</head>
<body>
	<iframe id="app" width="0" height="0" style="display:none;"></iframe>
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
	<div class="container-fluid" id="body">
		<div class="row page-header">
			<div class="col-xs-3">
				<h4><span class="glyphicon glyphicon-th"></span> 历史运维会话</h4>
			</div>
			<div class="col-xs-9 text-right" style="padding-right:0px;">
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-body" style="background-color: #eee;">
				    	<form id="form1" method="post" class="form-inline">
				    		<div class="form-group">
								<label>会话ID</label>
						    	<input type="text" class="form-control" name="qm.session_id" value="${qm.session_id }"></input>
							</div>
				    		<div class="form-group">
								<label>是否录像</label>
						    	<select class="form-control" id="is_record" name="qm.is_record"></select>
							</div>
				    		<div class="form-group">
								<label>执行状态</label>
						    	<select class="form-control" id="ex_status" name="qm.ex_status"></select>
							</div>
							<div class="form-group">
								<label>时间</label>
								<div class="input-group">
							  		<input type="text" id="date1" name="qm.date1" class="form-control" style="width: 94px;">
							  		<div class="input-group-btn">
							  			<select id="time1" name="qm.time1" class="form-control"></select>
							  		</div>
								</div> -
								<div class="input-group">
							  		<input type="text" id="date2" name="qm.date2" class="form-control" style="width: 94px;">
							  		<div class="input-group-btn">
							  			<select id="time2" name="qm.time2" class="form-control"></select>
							  		</div>
								</div>
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
						<button type="button" class="btn btn-default btn_loading" onclick="f_del();"><span class="glyphicon glyphicon-trash"></span> 批量删除录像</button>
					</div>
					<table class="table table-hover">
						<thead>
							<tr>
								<th><input id="check_all" type="checkbox" style="margin: 0px;" onclick="$.util.checkbox_all(this,'ck1')"/></th>
								<th>会话ID</th>
								<th>图堡IP</th>
								<th>图堡账号</th>
								<th>工具</th>
								<th>是否录像</th>
								<th>执行状态</th>
								<th>操作时间</th>
								<th>录像</th>
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
			<td><input name="ck1" type="checkbox" style="margin: 0px;" value="{{data.session_id}}" onclick="$.util.show_buttons('ck1')"/></td>
			<td>{{data.session_id}}</td>
			<td>{{data.fort_ip}}</td>
			<td>{{data.acc_name}}</td>
			<td>{{data.app_name}}</td>
			<td>{{data.is_record_str}}</td>
			<td>
				{{if data.ex_status=='ASSIGNED'}}
    				<span class="label label-info">{{data.ex_status_str}}</span>
				{{else if data.ex_status=='RUNNING'}}
    				<span class="label label-info">{{data.ex_status_str}}</span>
				{{else if data.ex_status=='OK'}}
    				<span class="label label-success">{{data.ex_status_str}}</span>
				{{else if data.ex_status=='ERROR'}}
    				<span class="label label-danger">{{data.ex_status_str}}</span>
				{{/if}}
			</td>
			<td>{{data.ex_time}}</td>
			<td><a href="javascript:f_run('{{data.session_id}}')"><span class="glyphicon glyphicon-facetime-video"></span> 播放录像</a></td>
			<td><a href="javascript:f_del('{{data.session_id}}')"><span class="glyphicon glyphicon-trash"></span> 删除录像</a></td>
		</tr>
		{{/each}}
	</script>
</body>
</html>