<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<html>
<head>
	<title>集中安全运维管理系统</title>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="renderer" content="ie-comp">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${static_path}/login/css/login.css" rel="stylesheet" type="text/css">
    <link href="${static_path}/login/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <script src="${static_path}/login/js/jquery.min.js"></script>
	<script type="text/javascript">
	$(document).ready(function() {
		$('#form1 input').bind('keypress', function(event){
            if(event.keyCode == "13"){
            	$(".loginBtn").click();
            }
        });
	});
	
	function f_login() {
		$.ajax({
			url : "${path}/bin/loginInvoke",
			method : "post",
			data : $("#form1").serialize(),
			success : function(data){
				// 后台正常处理，未出异常
				if(data.code=="SUCCEED"){
					window.location = "${path}/bin/dashboard";
				} else {
					if(data.value){
						showErrorMessage(data.note);
					}
				}
			}
		});
	}
	
	function refresh_code(){
		$("#checkCodeImg").attr("src", "${path }/bin/checkCodeImg/" + new Date().getTime());
	}
	</script>
</head>
<div class="main">
    <div class="top">
        <div class="top_bg"></div>
        <img id="top_logo" src="${static_path}/login/img/LOGO.png">
        <h5 id="top_title">集中安全运维管理系统</h5>
        <div class="top_border"></div>
    </div>
    <div class="login">
        <div class="loginType">
            <div id="id_login" class="loginTypeTitle loginTypeTitleAction">
          		帐号登陆
            </div>
            <div class="fgLine"></div>
            <div id="cert_login" class="loginTypeTitle">
				 手机登录
            </div>
        </div>
        <div class="message">
            <img src="${static_path}/login/img/warn.png">
            <span>错误信息</span>
        </div>
        <form id="form1" method="post">
	        <div id="idContent" class="loginContent">
	            <div id="userNameForm" class="inputDiv">
	                <div class="showIcon">
	                    <img id="userNameIcon_nor" src="${static_path}/login/img/user_nor.png">
	                    <img id="userNameIcon_press" src="${static_path}/login/img/user_press.png">
	                </div>
	                <input type="text" placeholder="请输入用户名" id="userName" name="username">
	            </div>
	            <div id="userPwdForm" class="inputDiv">
	                <div class="showIcon">
	                    <img id="userPwdIcon_nor" src="${static_path}/login/img/pw_nor.png">
	                    <img id="userPwdIcon_press" src="${static_path}/login/img/pw_press.png">
	                    <img id="userPwdIcon_warn" src="${static_path}/login/img/pw_warn.png">
	                </div>
	                <input type="password" placeholder="请输入密码" id="userPwd" name="password">
	            </div>
	            <div id="checkCodeForm" class="inputDiv">
	                <div class="showIcon">
	                    <img id="checkCodeIcon_nor" src="${static_path}/login/img/yanzheng.png">
	                    <img id="checkCodeIcon_press" src="${static_path}/login/img/yanzheng_press.png">
	                    <img id="checkCodeIcon_warn" src="${static_path}/login/img/yanzheng_warn.png">
	                </div>
	                <input type="text" placeholder="请输入验证码" id="checkCode" name="checkcode">
	                <img src="${path }/bin/checkCodeImg" id="checkCodeImg" onclick="javascript:refresh_code();">
	            </div>
	            <div class="psText">
                	<div id="saveId" class="left">
                   		<i class="fa fa-square-o" aria-hidden="true"></i>
						记住账号
	                </div>
	            </div>
	            <div class="loginBtn">
	               	 登&nbsp;&nbsp;录
	            </div>
	        </div>
        </form>
        <div id="certContent" class="loginContent">
        </div>
    </div>
    <div class="bottom">
        <div class="center">
            <p>Copyright © 2017 Trustmo All Rights Reserved<br/>技术支持：广州天懋信息系统股份有限公司</p>
        </div>
    </div>
</div>
<script src="${static_path}/login/js/login.js"></script>
</body>
</html>