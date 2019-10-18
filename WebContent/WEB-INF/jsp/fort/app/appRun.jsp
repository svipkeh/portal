<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	$(document).ready(function() {
	});
	

	function form_cancel(){
		window.location = '${path}/fort/app/list?keep=1';
	}
	
	function form_save(r){
		$.page.post({
			url : "${path}/fort/app/runInvoke",
			data :  $("#form1").serialize() + "&r="+r,
			callback: function(data){
				$("#app").get(0).src = "Trustmo://" + data.value;
			}
		});
	}
	</script>
</head>
<body>
	<iframe id="app" width="0" height="0" style="display:none;"></iframe>
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
	<div class="container-fluid">
		<div class="row page-header">
			<div class="col-xs-3">
				<h4><span class="glyphicon glyphicon-th"></span> 运维工具--测试运行</h4>
			</div>
			<div class="col-xs-9 text-right" style="padding-right: 0px;">
				<button type="button" class="btn btn-default btn_loading" onclick="form_cancel()"><span class="glyphicon glyphicon-circle-arrow-left"></span> 返回</button>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-body">
						<form id="form1" method="post" class="form-horizontal">
							<input type="hidden" name="app_id" value="${app.id }"/>
							<h5 class="bg-info">一、工具概述</h5>
							<div class="form-group">
						    	<label class="control-label col-xs-2">工具</label>
						    	<div class="col-xs-2"><input type="text" class="form-control" value="${app.name_cn }" readonly="readonly"/></div>
						  	</div>
							<div class="form-group">
						    	<label class="control-label col-xs-2">命令行参数</label>
						    	<div class="col-xs-6"><input type="text" class="form-control" value="${app.parameter }" readonly="readonly"/></div>
						  	</div>
						  	<h5 class="bg-info">二、测试参数</h5>
							${app.html }
			  				<button type="button" class="btn btn-default btn_loading" onclick="form_save(0);"><span class="glyphicon glyphicon-play"></span> 运行(不录像)</button>
			  				<button type="button" class="btn btn-default btn_loading" onclick="form_save(1);"><span class="glyphicon glyphicon-play"></span> 运行(要录像)</button>
			  				<span class="img_loading" style="display: none;"><img src="${static_path}/bin/img/loading.gif" /></span>
			  				<button type="button" class="btn btn-default btn_loading" onclick="form_cancel();"><span class="glyphicon glyphicon-remove"></span> 取消</button>
			  			</form>
		  			</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>