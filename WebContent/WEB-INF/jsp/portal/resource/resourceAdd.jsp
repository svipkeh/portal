<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	$(document).ready(function() {
		$("#res_type").ui_select_enums({schema: 'ResType', needDefault: false});
	});
	

	function form_cancel(){
		window.location = '${path}/portal/resource/list?keep=1';
	}
	
	function form_save(){
		$.page.post({
			url : "${path}/portal/resource/addInvoke",
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
				<h4><span class="glyphicon glyphicon-th"></span> 资源管理--新增</h4>
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
						    	<label class="control-label col-xs-2"><font color="red">*</font>资源名称</label>
						    	<div class="col-xs-4"><input type="text" name="resource.name_cn" placeholder="通常为中文，用于用户识别" class="form-control"/></div>
						    	<div class="col-xs-4"><label id="validate_name_cn" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>资源标识</label>
						    	<div class="col-xs-4"><input type="text" name="resource.name" placeholder="通常为英文，资源的唯一标识，用于程序识别" class="form-control"/></div>
						    	<div class="col-xs-4"><label id="validate_name" class="control-label validate_label"></label></div>
						  	</div>
							<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>资源类型</label>
						    	<div class="col-xs-2"><select class="form-control" id="res_type" name="resource.res_type"></select></div>
						  	</div>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>IP</label>
						    	<div class="col-xs-2"><input type="text" name="resource.ip" class="form-control"/></div>
						    	<div class="col-xs-4"><label id="validate_ip" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2">备注</label>
						    	<div class="col-xs-4"><input type="text" name="resource.remark" class="form-control"/></div>
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