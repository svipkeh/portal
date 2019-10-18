<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	$(document).ready(function() {
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
			url : "${path}/sys/priv/syslog/listInvoke?current=" + current,
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
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
	<div class="container-fluid" id="body">
		<div class="row page-header">
			<div class="col-xs-3">
				<h4><span class="glyphicon glyphicon-th"></span> 操作日志</h4>
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
								<label>用户</label>
						    	<input type="text" class="form-control" name="qm.username" value="${qm.username }"></input>
							</div>
							<div class="form-group">
								<label>访问源</label>
						    	<input type="text" class="form-control" name="qm.source_ip" value="${qm.source_ip }"></input>
							</div>
							<div class="form-group">
								<label>操作内容</label>
						    	<input type="text" class="form-control" name="qm.action" value="${qm.action }"></input>
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
					<table class="table table-hover">
						<thead>
							<tr>
								<th>用户</th>
								<th>访问源</th>
								<th>操作内容</th>
								<th>执行时间</th>
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
			<td>{{data.source_ip}}</td>
			<td>{{data.action}}</td>
			<td>{{data.ex_time}}</td>
		</tr>
		{{/each}}
	</script>
</body>
</html>