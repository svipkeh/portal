if (window.top != window.self)
    window.parent.location.reload();

var login = $(".login");
var idContentDom = $("#idContent");
var certContentDom = $("#certContent");
var idLoginBtnDom = $("#id_login");
var certLoginBtnDom = $("#cert_login");
var userNameDom = $("#userName");
var userNameFormDom = $("#userNameForm");
var userNameIconNorDom = $("#userNameIcon_nor");
var userNameIconPressDom = $("#userNameIcon_press");
var userPwdDom = $("#userPwd");
var userPwdFormDom = $("#userPwdForm");
var userPwdIconNorDom = $("#userPwdIcon_nor");
var userPwdIconPressDom = $("#userPwdIcon_press");
var userPwdIconWarnDom = $("#userPwdIcon_warn");
var checkCodeDom = $("#checkCode");
var checkCodeFormDom = $("#checkCodeForm");
var checkCodeIconNorDom = $("#checkCodeIcon_nor");
var checkCodeIconPressDom = $("#checkCodeIcon_press");
var checkCodeIconWarnDom = $("#checkCodeIcon_warn");
var saveIdDom = $("#saveId");
var messageDom = $(".message");
var saveUserName = false;

//密码及验证码输入框组状态和当前登录模式
var userPwdInputState = true;
var checkCodeInputState = true;
var nowType = "id";

userNameIconNorDom.show();
userPwdIconNorDom.show();
checkCodeIconNorDom.show();
//显示并监听窗口变化移动登录框Div
var pos = function () {
    var windowWidth = $(window).width();
    var windowHeight = $(window).height();

    login.css("position", "absolute");
    login.css("top", windowHeight * 0.48 - 225 + "px");
    login.css("left", windowWidth * 0.75 - 180 + "px");
};
pos();
login.css("display", "block");
$(window).resize(function () {
    pos();
});

userNameDom.focus(function () {
    userNameFormDom.attr("class", "inputDiv inputDivFocus");
    userNameIconNorDom.fadeOut(200);
    userNameIconPressDom.fadeIn(200);
});

userNameDom.blur(function () {
    userNameFormDom.attr("class", "inputDiv");
    userNameIconPressDom.fadeOut(200);
    userNameIconNorDom.fadeIn(200);
});

userPwdDom.focus(function () {
    if (userPwdInputState) {
        userPwdFormDom.attr("class", "inputDiv inputDivFocus");
        userPwdIconNorDom.fadeOut(200);
        userPwdIconPressDom.fadeIn(200);
    } else {
        userPwdFormDom.attr("class", "inputDiv warnInputDivFocus");
        userPwdIconNorDom.fadeOut(200);
        userPwdIconWarnDom.fadeIn(200);
    }
});

userPwdDom.blur(function () {
    userPwdFormDom.attr("class", "inputDiv");
    userPwdIconPressDom.fadeOut(200);
    userPwdIconWarnDom.fadeOut(200);
    userPwdIconNorDom.fadeIn(200);
});

checkCodeDom.focus(function () {
    if (checkCodeInputState) {
        checkCodeFormDom.attr("class", "inputDiv inputDivFocus");
        checkCodeIconNorDom.fadeOut(200);
        checkCodeIconPressDom.fadeIn(200);
    } else {
        checkCodeFormDom.attr("class", "inputDiv warnInputDivFocus");
        checkCodeIconNorDom.fadeOut(200);
        checkCodeIconWarnDom.fadeIn(200);
    }
});

checkCodeDom.blur(function () {
    checkCodeFormDom.attr("class", "inputDiv");
    checkCodeIconPressDom.fadeOut(200);
    checkCodeIconWarnDom.fadeOut(200);
    checkCodeIconNorDom.fadeIn(200);
});

//帐号登录模式
idLoginBtnDom.click(function () {
    if (nowType == "id") return;
    certLoginBtnDom.attr("class", "loginTypeTitle");
    idLoginBtnDom.attr("class", "loginTypeTitle loginTypeTitleAction");
    idContentDom.fadeIn(220);
    certContentDom.fadeOut(220);
    nowType = "id";
    hiddenErrorMessage();
});

//证书登录模式
certLoginBtnDom.click(function () {
    certLoginBtnDom.attr("class", "loginTypeTitle loginTypeTitleAction");
    idLoginBtnDom.attr("class", "loginTypeTitle");
    idContentDom.fadeOut(220);
    certContentDom.fadeIn(220);
    nowType = "cert";
    hiddenErrorMessage();
});

//记住账号按钮点击事件
saveIdDom.click(function () {
    if (saveUserName) {
        saveUserName = false;
        saveIdDom.find("i").attr("class", "fa fa-square-o");
    } else {
        saveUserName = true;
        saveIdDom.find("i").attr("class", "fa fa-check-square-o");
    }
});

//找回密码事件
$("#findId").click(function () {
    alert("不存在的");
});

//注册事件
$("#regis").click(function () {
    alert("告诉你,不可能");
});

//登录
$(".loginBtn").click(function () {
    userPwdInputState = true;
    checkCodeInputState = true;
    hiddenErrorMessage();
    if (userNameDom.val() == "") {
        showErrorMessage("用户名不能为空");
        userNameDom.focus();
        return;
    }
    if (userPwdDom.val() == "") {
        showErrorMessage("密码不能为空");
        userPwdInputState = false;
        userPwdDom.focus();
        return;
    }
    if (checkCodeDom.val() == "") {
        showErrorMessage("验证码不能为空");
        checkCodeInputState = false;
        checkCodeDom.focus();
        return;
    }
    if (saveUserName) {
        cookieFun.delCookie("TrustmoUserName");
        cookieFun.setCookie("TrustmoUserName", userNameDom.val());
    } else {
        cookieFun.delCookie("TrustmoUserName");
    }
    f_login();
});

//显示错误信息
function showErrorMessage(info) {
    messageDom.css("visibility", "visible");
    messageDom.find("span").text(info);
    messageDom.css("opacity", "1");
}

//隐藏显示错误信息
function hiddenErrorMessage() {
    messageDom.css("opacity", "0.00000001");
    messageDom.css("visibility", "hidden");
}

var cookieFun = {
    setCookie: function (name, value) {
        var Days = 30;
        var exp = new Date();
        exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
        document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
    },
    getCookie: function getCookie(name) {
        var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg))
            return unescape(arr[2]);
        else
            return null;
    },
    delCookie: function (name) {
        var exp = new Date();
        exp.setTime(exp.getTime() - 1);
        var cval = cookieFun.getCookie(name);
        if (cval != null)
            document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
    }
};

var UN = cookieFun.getCookie("TrustmoUserName");

if (UN != null) {
    userNameDom.val(UN);
    saveIdDom.click();
}