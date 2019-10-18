<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	$(document).ready(function() {
		$("#app_id").ui_select({
			url: "${path}/portal/resourceapp/appListInvoke",
			val: "id",
			note: "note",
			selectedValue: "${app.id}"
		});
	});
	
	function select_app(){
		window.location = '${path}/portal/resourceapp/add?resid=${resource.id }&appid='+$("#app_id").val();
	}
	

	function form_cancel(){
		window.location = '${path}/portal/resourceapp/list?resid=${resource.id}';
	}
	
	function form_save(){
		$.page.post({
			url : "${path}/portal/resourceapp/addInvoke",
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
				<h4><span class="glyphicon glyphicon-th"></span> 工具管理--新增 <small>(${resource.name_cn })</small></h4>
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
							<input type="hidden" name="resapp.resource_id" value="${resource.id }"/>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>运维工具</label>
						    	<div class="col-xs-4"><select class="form-control" id="app_id" name="resapp.app_id" onchange="javascript:select_app();"></select></div>
						    	<div class="col-xs-4"><label id="validate_app_id" class="control-label validate_label"></label></div>
						  	</div>
							<h5 class="bg-info">输入参数</h5>
						  	${input_html }
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