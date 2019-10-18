<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	var combobox;
	$(document).ready(function() {
		$("#method").ui_select({url: '${path}/common/routes/routesListInvoke', val: 'value', note: 'note'});
		combobox = $("#method").combobox();
	});

	function form_cancel(){
		window.location = '${path}/sys/priv/function/list?keep=1';
	}

	var f = {};
	f.methods = [];
	f.addMethod = function(m) {
		var index = "t" + new Date().getTime() + Math.round(Math.random()*10000);
		var method = m;
		if(!method){
			method = $("#method").val();
			if(method == ""){
				$("label[for='method']").text("请选择方法");
				return;
			}
		}

		var lineHtml = '';
		lineHtml+='<div class="form-group '+index+'">';
		lineHtml+='<label class="control-label col-xs-2">已添加 </label>';
		lineHtml+='<div class="col-xs-6"><input type="text" readonly="readonly" class="form-control" value="' + method + '"/></div>';
		lineHtml+='<div class="col-xs-1"><button type="button" title="删除" class="btn btn-default" onclick="f.delMethod(\''+index+'\');"><span class="glyphicon glyphicon-minus"></span></button></div>';
		lineHtml+='</div>';

		f.methods.push([index, method]);
		$("#methods_tocken").after(lineHtml);
		combobox.toggle();
	};
	f.delMethod = function(index){
		$("div."+index).remove();
		for(var i=0; i<f.methods.length; i++) {
			var m = this.methods[i];
			if(m[0] == index) {
				f.methods.splice(i,1);
			}
		}
	};
	
	function form_save(){
		$.page.post({
			url : "${path}/sys/priv/function/updateInvoke",
			data :  $("#form1").serialize() + "&f="+$.toJSON(f),
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
				<h4><span class="glyphicon glyphicon-th"></span> 功能--修改</h4>
			</div>
			<div class="col-xs-9" style="text-align: right; padding-right: 0px;">
				<button type="button" class="btn btn-default btn_loading" onclick="form_cancel()"><span class="glyphicon glyphicon-circle-arrow-left"></span> 返回</button>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-body">
						<form id="form1" method="post" class="form-horizontal">
							<input type="hidden" name="function.id" value="${function.id }"></input>
							<div class="form-group">
						    	<label class="control-label col-xs-2">所属菜单</label>
						    	<div class="col-xs-4"><input type="text" class="form-control" readonly="readonly" value="${menu.name }"/></div>
						  	</div>
						  	<div class="form-group">
						    	<label class="control-label col-xs-2"><font color="red">*</font>功能名称</label>
						    	<div class="col-xs-4"><input type="text" name="function.name" placeholder="功能名称" class="form-control" value="${function.name }"/></div>
						    	<div class="col-xs-4"><label id="validate_function_name" class="control-label validate_label"></label></div>
						  	</div>
						  	<div class="form-group">
						  		<label class="control-label col-xs-2"><font color="red">*</font>排序</label>
						    	<div class="col-xs-2"><input type="text" name="function.index" placeholder="排序，如：1,2,3" class="form-control" value="${function.index }"/></div>
						    	<div class="col-xs-4"><label id="validate_function_index" class="control-label validate_label"></label></div>
						  	</div>
						  	<hr/>
						  	<div class="form-group">
						  		<label class="control-label col-xs-2"><font color="red">*</font>关联方法</label>
						    	<div class="col-xs-6"><select class="form-control" id="method" name="method"></select></div>
						    	<div class="col-xs-1"><button type="button" class="btn btn-default" onclick="f.addMethod();"><span class="glyphicon glyphicon-plus"></span></button></div>
						    	<div class="col-xs-2"><label id="validate_method" class="control-label validate_label"></label></div>
						  	</div>
						  	<hr id="methods_tocken"/>
							<c:forEach var="method" items="${methods }">
								<script type="text/javascript">
									f.addMethod("${method.controller }--${method.method }--${method.name_cn }");
								</script>
							</c:forEach>
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