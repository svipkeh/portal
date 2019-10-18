<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/common/staticlibs.jsp"%>
	<script type="text/javascript">

	var tree;
	$(document).ready(function() {
		init_function_tree();
		getData(0);
	});
	
	function init_function_tree(){
		tree = $("#function_tree").ui_tree({
			url : '${path}/sys/priv/function/functionTreeInvoke',
			selectedValue : '${qm.menu_id}',
			onclick : function(node){
				if(node.type == 'menu'){
					$("#menu_id").val(node.id);
					$("#function_id").val('');
				}
				if(node.type == 'function'){
					$("#menu_id").val('');
					$("#function_id").val(node.id);
				}
				getData(0);
			}
		});
		tree.expandAll(true);
	}

	// 查询
	function getData(current) {
		$.util.hide_buttons();
		$("#check_all").get(0).checked=false;
		$.page.post({
			url : "${path}/sys/priv/function/listInvoke",
			data : "qm.menu_id="+$("#menu_id").val()+"&qm.function_id="+$("#function_id").val(),
			callback : function(data){
				var html = template('listScript', data.value);
				$('#listContainer').html(html);
			}
		});
	}

	// 新增
	function f_add(){
		window.location = "${path}/sys/priv/function/add?menu_id="+$("#menu_id").val();
	}

	// 修改
	function f_edit(id){
		window.location = "${path}/sys/priv/function/edit?id="+id;
	}

	// 改变状态
	function f_status(status, id){
		var ids = id?id:$.util.checkbox_values('ck1');
		$.page.post({
			comfirm : '操作',
			url: "${path}/sys/priv/function/statusInvoke",
			data : 'status='+status+'&ids='+ids,
			callback: function(data){
				$.dialog.info("操作成功");
				init_function_tree();
				getData(0);
			}
		});
	}
	</script>
</head>
<body>
	<input type="hidden" id="menu_id" name="qm.menu_id" value="${qm.menu_id }"></input>
	<input type="hidden" id="function_id" name="qm.function_id" value="${qm.function_id }"></input>
	<%@ include file="/WEB-INF/jsp/common/header.jsp"%>
	<div class="container-fluid">
		<div class="row page-header">
			<div class="col-xs-3">
				<h4><span class="glyphicon glyphicon-th"></span> 功能</h4>
			</div>
			<div class="col-xs-9" style="text-align: right; padding-right: 0px;">
				<button type="button" class="btn btn-default btn_loading" onclick="f_add()"><span class="glyphicon glyphicon-plus"></span> 新增</button>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="panel panel-default">
					<div class="panel-heading">
						<span class="glyphicon glyphicon-signal"></span> 功能
						<div style="float: right;">
							<a href="#" onclick="tree.expandAll(true)" title="展开"><span class="glyphicon glyphicon-plus"></span></a>
							<a href="#" onclick="tree.expandAll(false)" title="收缩"><span class="glyphicon glyphicon-minus"></span></a>
						</div>
					</div>
					<div class="panel-body" style="max-height:480px; overflow: auto; padding: 0px;">
						<ul id="function_tree" class="ztree"></ul>
					</div>
				</div>
			</div>
			<div class="col-xs-9">
				<div class="table-responsive">
					<div id="buttons_box" style="display:none; position: absolute; z-index: 100;">
						<button type="button" class="btn btn-link btn_loading" onmouseover="$.util.light_checked('ck1')" onmouseout="$.util.dark_checked('ck1')">已选中<span id="buttons_box_num"></span>条数据</button>
						<button type="button" class="btn btn-default btn_loading" onclick="f_status('ENABLED');"><span class="glyphicon glyphicon-ok"></span> 启用</button>
						<button type="button" class="btn btn-default btn_loading" onclick="f_status('DISABLED');"><span class="glyphicon glyphicon-remove"></span> 禁用</button>
						<button type="button" class="btn btn-default btn_loading" onclick="f_status('DELETED');"><span class="glyphicon glyphicon-trash"></span> 删除</button>
					</div>
					<table class="table table-hover">
						<thead>
							<tr>
								<th><input id="check_all" type="checkbox" style="margin: 0px;" onclick="$.util.checkbox_all(this,'ck1')"/></th>
								<th>功能名称</th>
								<th>排序</th>
								<th>状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="listContainer"></tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<script id="listScript" type="text/html">
		{{each list as data}}
		<tr>
			<td><input name="ck1" type="checkbox" style="margin: 0px;" value="{{data.id}}" onclick="$.util.show_buttons('ck1')"/></td>
			<td>{{data.name}}</td>
			<td>{{data.index}}</td>
			<td>
				{{if data.data_status=='ENABLED'}}
    				<span class="label label-success">{{data.data_status_str}}</span>
				{{else if data.data_status=='DISABLED'}}
    				<span class="label label-danger">{{data.data_status_str}}</span>
				{{/if}}
			</td>
			<td>
				{{if data.data_status=='ENABLED'}}
    				<a href="#" onclick="f_status('DISABLED',{{data.id}})"><span class="glyphicon glyphicon-remove"></span> 禁用</a>
				{{else if data.data_status=='DISABLED'}}
    				<a href="#" onclick="f_status('ENABLED',{{data.id}})"><span class="glyphicon glyphicon-ok"></span> 启用</a>
				{{/if}}
				<a href="#" onclick="f_edit('{{data.id}}')"><span class="glyphicon glyphicon-edit"></span> 修改</a>
			</td>
		</tr>
		{{/each}}
	</script>
</body>
</html>