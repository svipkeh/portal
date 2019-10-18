(function($){
	
	//========================================================================
	// 弹出对话框
	$.fn.ui_modal = function(title, url, callback) {
		$("#modal_dialog_main").modal({});
		$("#modal_dialog_label").text(title);
		$("#modal_dialog_iframe").attr("src", url);
		$("#btn_modal_dialog_ok").unbind("click").text("确定");
		$("#btn_modal_dialog_ok").bind("click", function(){
			$("#modal_dialog_main").modal('hide');
			var s = top.frames['modal_dialog_iframe'].btn_modal_dialog_ok();
			if(callback){
				callback(s);
			}
		});
	};
	
	
	//========================================================================
	// 日期和时间
	// options.dayPlus:在当前日期上加减天数
	// options.defaultDay:默认显示日期
	$.fn.ui_day_of_year = function(options) {
		var options = $.extend({}, $.fn.ui_time.defaults, options);
		if('' != options.defaultDay){
			$(this).val(options.defaultDay);
		}else{
			var d = new Date(new Date().getTime() + (options.dayPlus * 24 * 60 * 60 * 1000));
			var time = d.getFullYear() + "-" + fill(d.getMonth()+1) + "-" + fill(d.getDate());
			$(this).val(time);
		}
		$(this).datetimepicker({
			format: 'yyyy-mm-dd',
	        language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  0,
			autoclose: 1,
			todayHighlight: 0,
			startView: 2,
			minView: 2,
			maxView: 4,
			forceParse: 1,
	        showMeridian: 0
	    });
	};
	
	// options.defaultDay: 日期1-31
	$.fn.ui_day_of_month = function(options){
		$(this).empty();
		var options = $.extend({}, $.fn.ui_time.defaults, options);
		for(var i=1; i<=31; i++){
			var item = $("<option value='"+i+"'>"+i+"号</option>");
			if(i == options.defaultDay){
				item.attr("selected", "selected");
			}
			$(this).append(item);
		}
	}
	
	// options.defaultDay: 星期 1-7
	$.fn.ui_day_of_week = function(options){
		$(this).empty();
		var options = $.extend({}, $.fn.ui_time.defaults, options);
		var item1 =$('<option value="1">星期一</option>');
		var item2 =$('<option value="2">星期二</option>');
		var item3 =$('<option value="3">星期三</option>');
		var item4 =$('<option value="4">星期四</option>');
		var item5 =$('<option value="5">星期五</option>');
		var item6 =$('<option value="6">星期六</option>');
		var item7 =$('<option value="7">星期日</option>');
		$(this).append(item1);
		$(this).append(item2);
		$(this).append(item3);
		$(this).append(item4);
		$(this).append(item5);
		$(this).append(item6);
		$(this).append(item7);
		if('' != options.defaultDay){
			var index = eval(options.defaultDay-1);
			$(this).children().eq(index).attr("selected", "selected");
		}
	}
	
	// options.defaultTime: 09:30
	$.fn.ui_time = function(options){
		$(this).empty();
		var options = $.extend({}, $.fn.ui_time.defaults, options);
		for(var i=0; i<24; i++){
			var time1 = fill(i)+':00';
			var time2 = fill(i)+':30';
			var option1 = $("<option value='"+time1+"'>"+time1+"</option>");
			var option2 = $("<option value='"+time2+"'>"+time2+"</option>");
			if(time1 == options.defaultTime){
				option1.attr("selected", "selected");
			}
			if(time2 == options.defaultTime){
				option2.attr("selected", "selected");
			}
			$(this).append(option1);
			$(this).append(option2);
		}
	}
	
	function fill(num){
		if(num<10){
			return "0" + num;
		}
		return num;
	}
	
	$.fn.ui_time.defaults = {
		dayPlus : 0,
		defaultDay : '',
		defaultTime : '00:00'
	};
	
	
	//========================================================================
	// 颜色
	$.fn.ui_colorpicker = function(options) {
		var options = $.extend({}, $.fn.ui_colorpicker.defaults, options);
		$(this).val(options.defaultColor);
		$(this).css("background-color", options.defaultColor);
		$(this).colorpicker().on('changeColor', function(ev){
			$(this).css("background-color", ev.color.toHex());
		});
	};
	
	$.fn.ui_colorpicker.defaults = {
		defaultColor : '#ffffff'
	}
	
	
	//========================================================================
	// 多选checkbox（前台json和后台json）
	$.fn.ui_checkbox = function(options) {
		var selectRoot = $(this);
		var options = $.extend({}, $.fn.ui_checkbox.defaults, options);
		if(options.data == '' && options.url != ''){
			$.ajax({
				url: options.url,
				method:"post",
				async: false,
				success: function(data){
					options.data = data;
				},
			});
		}
		$.each(eval(options.data), function(i, status){
			var item = $('<label><input type="checkbox" name="'+options.name+'" value="'+eval('status.'+options.val)+'">'+eval('status.'+options.note)+'</label>');
			var checkedNodes = eval(options.selectedValue);
			if(checkedNodes){
				for(var j=0; j<checkedNodes.length; j++){
					var temp = checkedNodes[j];
					if(eval('status.'+options.val) == temp) {
						item.children("input").attr("checked", "checked");
					}
				}
			}
			selectRoot.append(item);
		});
	};
	
	$.fn.ui_checkbox.defaults = {
		name : '',
		data : '',
		url : '',
		val : 'id',
		note : 'name',
		selectedValue : ""
	};
	
	
	//========================================================================
	// 下拉列表（前台json和后台json）
	$.fn.ui_select = function(options) {
		var selectRoot = $(this);
		selectRoot.empty();
		var options = $.extend({}, $.fn.ui_select.defaults, options);
		if(options.data == '' && options.url != ''){
			$.ajax({
				url: options.url,
				method:"post",
				async: false,
				success: function(data){
					options.data = data;
				},
			});
		}
		if(options.needDefault) {
			var option = $('<option value=\'\'>' + options.defaultLabel + '</option>');
			selectRoot.append(option);
		}
		$.each(eval(options.data), function(i, status){
			var option = $('<option value=' + eval('status.'+options.val) + '>' + eval('status.'+options.note) + '</option>');
			if(eval('status.'+options.val) == options.selectedValue) {
				option.attr("selected", "selected");
			}
			selectRoot.append(option);
		});
	};
	
	$.fn.ui_select.defaults = {
		data : '',
		url : '',
		val : 'id',
		note : 'name',
		selectedValue : "",
		needDefault : true,
		defaultLabel : "请选择"
	};
	
	
	//========================================================================
	// 下拉列表（枚举）
	$.fn.ui_select_enums = function(options) {
		var selectRoot = $(this);
		var options = $.extend({}, $.fn.ui_select_enums.defaults, options);
		$.ajax({
			url: path + "/common/enum/enumList/" + options.schema,
			method:"post",
			async: false,
			success: function(data){
				options.data = data;
			},
		});
		if(options.needDefault) {
			var option = $('<option value=\'\'>' + options.defaultLabel + '</option>');
			selectRoot.append(option);
		}
		$.each(eval(options.data), function(i,status){
			var option = $('<option value=' + status.val + '>' + status.note + '</option>');
			if(status.val == options.selectedValue) {
				option.attr("selected", "selected");
			}
			selectRoot.append(option);
		});
	};
	
	$.fn.ui_select_enums.defaults = {
		schema : "",
		selectedValue : "",
		needDefault : true,
		defaultLabel : "请选择"
	};
	
	
	//========================================================================
	// 树
	$.fn.ui_tree = function(options) {
		var selectRoot = $(this);
		var options = $.extend({}, $.fn.ui_tree.defaults, options);
		if(options.data == '' && options.url != ''){
			$.ajax({
				url: options.url,
				method:"post",
				async: false,
				success: function(data){
					options.data = data;
				},
			});
		}
		var setting = {
				check: {
					enable: options.checkbox
				},
				callback: {
					onClick: function(event, treeId, node) {
						if(options.onclick){
							options.onclick(node);
						}
					}
				}
			};
		var treeObj = $.fn.zTree.init(selectRoot, setting, eval(options.data));
		if(!options.checkbox){
			// 没有多选框
			var selecteds = treeObj.getNodesByParam("id", options.selectedValue, null);
			if(selecteds.length>0){
				treeObj.selectNode(selecteds[0]);
				treeObj.expandNode(selecteds[0]);
			}
		} else {
			var checkedNodes = eval(options.selectedValue);
			if(checkedNodes){
				for(var j=0; j<checkedNodes.length; j++){
					var temp = checkedNodes[j];
					var matcheds = treeObj.getNodesByParam("id", temp, null);
					if(matcheds.length>0){
						treeObj.checkNode(matcheds[0], true, false);
					}
				}
			}
		}
		return treeObj;
	};
	
	$.fn.ui_tree.defaults = {
		data : '',
		url : '',
		checkbox : false,
		selectedValue : '',
		onclick : ''
	};
	
	
	//========================================================================
	// 多级联动下拉列表
	$.fn.ui_selects = function(options) {
		var selectRoot = $(this);
		var options = $.extend({}, $.fn.ui_selects.defaults, options);
		if(options.data == '' && options.url != ''){
			$.ajax({
				url: options.url,
				method:"post",
				async: false,
				success: function(data){
					options.data = data;
				},
			});
		}
		var nodeList = eval(options.data);
		if(nodeList.length == 1){
			nodeList = nodeList[0].children;
		}
		appendSelect(options, selectRoot, nodeList);
		// 设置默认选中
		var allNodePath = [];
		getAllNodePath(allNodePath, nodeList, []);
		var selectedNodePath = [];
		for(var i=0; i<allNodePath.length; i++){
			var id = allNodePath[i][allNodePath[i].length-1];
			if(id == options.selectedValue){
				selectedNodePath = allNodePath[i];
				break;
			}
		}
		for(var i=0; i<selectedNodePath.length; i++){
			selectRoot.children("select").eq(i).val(selectedNodePath[i]);
			selectRoot.children("select").eq(i).change();
		}
	};
	
	function appendSelect(options, selectRoot, nodeList){
		var select = $('<select class="form-control" style="width: auto; display: inline-block;"></select>');
		var defaultOption = $('<option value=\'\'>' + options.defaultLabel + '</option>');
		select.append(defaultOption);
		$.each(nodeList, function(i,status){
			var option = $('<option value=' + status.id + '>' + status.name + '</option>');
			select.append(option);
		});
		select.change(function(){
			select.nextAll().remove();
			var selectId = select.val();
			var childrenNodeList = [];
			for(var i=0; i<nodeList.length; i++){
				if(nodeList[i].id == selectId){
					childrenNodeList = nodeList[i].children;
					break;
				}
			}
			if(childrenNodeList != undefined && childrenNodeList.length >= 1){
				appendSelect(options, selectRoot, childrenNodeList);
			}
			if(options.onchange){
				var size = selectRoot.children("select").size();
				var selectNode = selectRoot.children("select").eq(size-1);
				if(!selectNode.val()){
					selectNode = selectRoot.children("select").eq(size-2);
				}
				options.onchange(selectNode.val(), selectNode.find("option:selected").text());
			}
		});
		selectRoot.append(select);
	}
	
	function getAllNodePath(allNodePath, nodeList, parentPath){
		for(var i=0; i<nodeList.length; i++){
			var thisPath = parentPath.concat(nodeList[i].id);
			allNodePath.push(thisPath);
			var childrenNodeList = nodeList[i].children;
			if(childrenNodeList != undefined && childrenNodeList.length >= 1){
				getAllNodePath(allNodePath, childrenNodeList, thisPath);
			}
		}
	}
	
	$.fn.ui_selects.defaults = {
		data : '',
		url : '',
		selectedValue : '',
		needDefault : true,
		defaultLabel : "请选择",
		onchange : ''
	};
	
})(jQuery);