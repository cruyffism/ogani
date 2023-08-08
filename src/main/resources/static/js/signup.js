$(document).ready(function () {
    console.log("ready!");
    signupAjax(1);
});

function signupAjax(type) {
    console.log("type : ", type);
    const innerHtml = $("#signupForm")
    $.ajax({
        url: "/user/signupAjax/" + type,
        type: 'GET',
        cache: false,
        dataType: "html",
        async: false,
        success: function (data) {
            $('.nav-link').removeClass('active');
            document.getElementById("signupTab" + type).className += " active"
            $(innerHtml).html(data)
            setTimeout(function () {
            }, 1000)
        },
        error: function (e) {
            $(innerHtml).html("")
        }
    })
}
