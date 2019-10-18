(function($){
	$.extend({
		
		ws : {
		  	sub : function(topicUrl, callback) {
		  		var request = {
		  	      url : topicUrl,
		  	      trackMessageLength : true,
		  	      transport: 'websocket'
		  		};
		  		request.onMessage = callback;
		  		atmosphere.subscribe(request);
		  	},
		  	unsub : function(topicUrl, callback) {
		  		var request = {
		  	  	      url : topicUrl,
		  	  	      trackMessageLength : true,
		  	  	      transport: 'websocket'
		  	  		};
		  		atmosphere.unsubscribe(request);
		  	}
		},
		
		dialog : {
		  	// 消息通知
		  	info : function(content, width, height, timeInMillis) {
		  		if(content) {
		  			var t = timeInMillis ? timeInMillis : 1500;
		  			var d = dialog({
		  				fixed: true,
		  				content: content
			  		});
		  			if(width && width>0) {
		  				d.width(width);
		  			}
		  			if(height && height>0) {
		  				d.height(height);
		  			}
			  		d.show();
			  		setTimeout(function () {
			  		    d.close().remove();
			  		}, t);
		  		} else {
		  			alert("函数参数错误，格式为：$.dialog.info(content, width, height, timeInMillis)");
		  		}
		  	},
		  	// 点击同意执行指定的回调函数
		  	confirm : function(title, content, callback, width, height) {
		  		if(title && content && callback) {
		  			var d = dialog({
		  				fixed: true,
		  				title: title,
		  				content: content,
		  				button : [
		  				  {
			  					value : '确认',
			  					callback : callback
		  				  },
		  				  {
			  					value : '取消'
		  				  }
		  				]
		  			}).showModal();
		  			if(width && width>0) {
		  				d.width(width);
		  			}
		  			if(height && height>0) {
		  				d.height(height);
		  			}
		  		} else {
		  			alert("函数参数错误，格式为：$.dialog.confirm(title, content, callback, width, height)");
		  		}
		  	}	
		},
		
		util : {
			// 复选框全选
			checkbox_all : function(ck, ck_name){
		  		var cked = $(ck).is(":checked");
		  		$(":checkbox[name="+ck_name+"]").each(function(){
	  				$(this).get(0).checked=cked;
	  			});
	  			$.util.show_buttons(ck_name);
		  	},
		  	// 显示按钮栏
		  	show_buttons : function(ck_name){
				var s = $(":checkbox[name="+ck_name+"]:checked").size();
				if(s>0){
					if(s>1){
						$("button.btn_data_1").addClass("disabled");
					}else{
						$("button.btn_data_1").removeClass("disabled");
					}
					var text = $(".table-responsive");
					$("#buttons_box_num").text(s);
					$("#buttons_box").css({
						left: "45px",
						top: "0px"
					}).width(text.width()-45+15).css("background-color", "#fff").slideDown("fast");
				}else{
					$.util.hide_buttons();
				}
			},
			//  隐藏按钮栏
			hide_buttons : function(){
				$("#buttons_box").fadeOut("fast");
			},
			// onmouseover
			light_checked : function(ck_name){
				$(":checkbox[name="+ck_name+"]:checked").each(function(){
	  				$(this).parents("tr").css("background-color", "#f5f5f5");
	  			});
			},
			// onmouseout
			dark_checked : function(ck_name){
				$(":checkbox[name="+ck_name+"]:checked").each(function(){
	  				$(this).parents("tr").css("background-color", "#fff");
	  			});
			},
			// 获取选中的checkbox的值
			checkbox_values : function(ck_name){
				var ids = [];
				$(":checkbox[name="+ck_name+"]:checked").each(function(){
					ids.push($(this).val());
				});
				return ids;
			},
			
		},
		
		// 页面异步提交方法方法
		page : {
			post : function(options){
				if(options.comfirm){
					$.dialog.confirm(options.comfirm+'确认', '是否继续'+options.comfirm+'？', function(){
						$.page.ajax_post(options);
					}, 400);
				} else {
					$.page.ajax_post(options);
				}
			},
			ajax_post : function(options){
				$("label.validate_label").text("");
				$.ajax({
					url : options.url,
					method : "post",
					data :  options.data,
					beforeSend : function() {
						$('.img_loading').show();
						$('.btn_loading').attr('disabled', true);
					},
					success : function(data) {
						// 后台正常处理，未出异常
						if(data.code=="SUCCEED"){
							options.callback(data);	
						} else {
							if(data.value){
								for(var i in data.value){
									if($("label#"+i).size()==1){
										$("label#"+i).text(data.value[i]);
									}else{
										$.dialog.confirm("错误", data.value[i], function(){}, 400);
									}
								}
							} else {
								$.dialog.confirm("错误", data.note, function(){}, 400);
							}
						}
						$('.img_loading').hide();
						$('.btn_loading').attr('disabled', false);
					},
					error : function() {
						$('.img_loading').hide();
						$('.btn_loading').attr('disabled', false);
						$.dialog.info("后台或网络异常");
					}
				});
			}
		}
		
		
		
	});
})(jQuery);