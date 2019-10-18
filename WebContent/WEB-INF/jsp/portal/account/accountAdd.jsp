<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	$(document).ready(function() {
		$("#resource_id").ui_select({
			url: "${path}/portal/account/resourceListInvoke",
			val: "id",
			note: "note"
		});
	});
	

	function form_cancel(){
		window.location = '${path}/portal/account/list?keep=1';
	}
	
	function form_save(){
		$.page.post({
			url : "${path}/portal/account/addInvoke",
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
				<h4><span class="glyphicon glyphicon-th"></span> 账号管理--新增</h4>
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
							<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>资源</label>
						    	<div class="col-xs-4"><select class="form-control" id="resource_id" name="account.resource_id"></select></div>
						    	<div class="col-xs-4"><label id="validate_resource_id" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>账号名称</label>
						    	<div class="col-xs-2"><input type="text" name="account.name" placeholder="如：root" class="form-control"/></div>
						    	<div class="col-xs-4"><label id="validate_name" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>密码</label>
						    	<div class="col-xs-2"><input type="password" name="account.password" class="form-control"/></div>
						    	<div class="col-xs-4"><label id="validate_password" class="control-label validate_label"></label></div>
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