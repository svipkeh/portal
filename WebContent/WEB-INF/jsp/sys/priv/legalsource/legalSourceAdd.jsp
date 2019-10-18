<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	$(document).ready(function() {
		$("#scope_type").ui_select_enums({schema: 'LegalSourceScopeType', needDefault: false});
	});
	
	function show_target(obj){
		if(obj.value == 'FIXED'){
			$("div#scope_target_div").show();
		}else{
			$("div#scope_target_div").hide();
		}
	}
	
	function form_cancel(){
		window.location = '${path}/sys/priv/legalsource/list?keep=1';
	}
	
	function form_save(){
		$.page.post({
			url : "${path}/sys/priv/legalsource/saveInvoke",
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
				<h4><span class="glyphicon glyphicon-th"></span> 访问源--新增</h4>
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
						    	<label class="control-label col-xs-2"><font color="red">*</font>IP</label>
						    	<div class="col-xs-4"><input type="text" name="source.ip" placeholder="访问源IP" class="form-control"/></div>
						    	<div class="col-xs-4"><label id="validate_source_ip" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>适用范围</label>
						    	<div class="col-xs-2"><select class="form-control" id="scope_type" name="source.scope_type" onchange="show_target(this);"></select></div>
						    	<div class="col-xs-4"><label id="validate_scope_type" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group" id="scope_target_div">
						    	<label class="control-label col-xs-2"><font color="red">*</font>用户名</label>
						    	<div class="col-xs-4"><input type="text" class="form-control" name="source.scope_target" /></div>
						    	<div class="col-xs-4"><label id="validate_scope_target" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2">备注</label>
						    	<div class="col-xs-4"><input type="text" name="source.des" placeholder="备注" class="form-control"/></div>
						    	<div class="col-xs-4"><label id="validate_source_des" class="control-label validate_label"></label></div>
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