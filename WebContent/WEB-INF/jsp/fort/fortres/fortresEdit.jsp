<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	$(document).ready(function() {
	});
	

	function form_cancel(){
		window.location = '${path}/fort/fortres/list?keep=1';
	}
	
	function form_save(){
		$.page.post({
			url : "${path}/fort/fortres/editInvoke",
			data :  $("#form1").serialize(),
			callback: function(data){
				$.dialog.info("保存成功");
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
				<h4><span class="glyphicon glyphicon-th"></span> 堡垒机配置--修改</h4>
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
							<input type="hidden" name="res.id" value="${res.id }"/>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>IP</label>
						    	<div class="col-xs-4"><input type="text" name="res.fort_ip" placeholder="内网IP地址" class="form-control" value="${res.fort_ip }"/></div>
						    	<div class="col-xs-4"><label id="validate_fort_ip" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>外网IP</label>
						    	<div class="col-xs-4"><input type="text" name="res.fort_ip_wan" placeholder="外网IP地址" class="form-control" value="${res.fort_ip_wan }"/></div>
						    	<div class="col-xs-4"><label id="validate_fort_ip_wan" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>运维端口</label>
						    	<div class="col-xs-2"><input type="text" name="res.mstsc_port" class="form-control" value="${res.mstsc_port }"/></div>
						    	<div class="col-xs-4"><label id="validate_mstsc_port" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>管理端口</label>
						    	<div class="col-xs-2"><input type="text" name="res.servmgr_port" class="form-control" value="${res.servmgr_port }"/></div>
						    	<div class="col-xs-4"><label id="validate_servmgr_port" class="control-label validate_label"></label></div>
						  	</div>
			  				<button type="button" class="btn btn-default btn_loading" onclick="form_save();"><span class="glyphicon glyphicon-ok"></span> 保存</button>
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