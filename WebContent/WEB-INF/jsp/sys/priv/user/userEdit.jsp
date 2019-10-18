<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	$(document).ready(function() {
		$("#role_ids").ui_checkbox({name: 'role_ids', data: '${roles}', val: 'id', note: 'name', selectedValue: '${role_ids}'});
	});

	function form_cancel(){
		window.location = '${path}/sys/priv/user/list?keep=1';
	}
	
	function form_save(){
		$.page.post({
			url : "${path}/sys/priv/user/updateInvoke",
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
				<h4><span class="glyphicon glyphicon-th"></span> 用户--修改</h4>
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
							<input type="hidden" name="user.id" value="${user.id }"/>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>登录名</label>
						    	<div class="col-xs-4"><input type="text" name="user.name" placeholder="如：zhangsan" class="form-control" value="${user.name }"/></div>
						    	<div class="col-xs-4"><label id="validate_user_name" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						  		<label class="control-label col-xs-2"><font color="red">*</font>中文名</label>
						    	<div class="col-xs-4"><input type="text" name="user.name_cn" placeholder="如：张三" class="form-control" value="${user.name_cn }"/></div>
						    	<div class="col-xs-4"><label id="validate_user_name_cn" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						  		<label class="control-label col-xs-2"><font color="red">*</font>角色</label>
						    	<div class="col-xs-4 checkbox" id="role_ids"></div>
						    	<div class="col-xs-4"><label id="validate_role_id" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						  		<label class="control-label col-xs-2">邮箱</label>
						    	<div class="col-xs-4"><input type="text" name="user.mail" class="form-control" value="${user.mail }"/></div>
						    	<div class="col-xs-4"><label id="validate_user_mail" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						  		<label class="control-label col-xs-2">手机号</label>
						    	<div class="col-xs-4"><input type="text" name="user.mobile" class="form-control" value="${user.mobile }"/></div>
						    	<div class="col-xs-4"><label id="validate_user_mobile" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						  		<label class="control-label col-xs-2">备注</label>
						    	<div class="col-xs-4"><textarea rows="4" id="task_ips" name="user.remark" class="form-control" style="resize:none;">${user.remark }</textarea></div>
						    	<div class="col-xs-4"><label id="validate_user_remark" class="control-label validate_label"></label></div>
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