if (window.top != window.self)
    window.parent.location.reload();

var regis = $(".regis");

//显示并监听窗口变化移动注册框Div
var pos = function () {
    var windowWidth = $(window).width();
    var windowHeight = $(window).height();

    regis.css("position", "absolute");
    regis.css("top", windowHeight * 0.46 - 230 + "px");
    regis.css("left", windowWidth * 0.75 - 180 + "px");
};
pos();
regis.css("display", "block");
$(window).resize(function () {
    pos();
});

$(".checkIdBtn").click(function () {
    alert("checkId");
});

$("#regisBtn").click(function () {
    alert("regis");
});