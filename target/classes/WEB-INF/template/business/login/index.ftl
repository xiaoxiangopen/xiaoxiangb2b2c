<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="format-detection" content="telephone=no">
    <meta name="author" content="小象SHOP Team">
    <meta name="copyright" content="小象SHOP">
    <title>${message("business.login.title")} - Powered By 小象SHOP</title>
    <link href="${base}/favicon.ico" rel="icon">
    <link href="${base}/resources/common/css/bootstrap.css" rel="stylesheet">
    <link href="${base}/resources/common/css/iconfont.css" rel="stylesheet">
    <link href="${base}/resources/common/css/font-awesome.css" rel="stylesheet">
    <link href="${base}/resources/common/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
    <link href="${base}/resources/common/css/base.css" rel="stylesheet">
    <link href="${base}/resources/business/css/base.css" rel="stylesheet">
    <link href="${base}/resources/business/css/login.css" rel="stylesheet">
    <!--[if lt IE 9]>
		<script src="${base}/resources/common/js/html5shiv.js"></script>
		<script src="${base}/resources/common/js/respond.js"></script>
	<![endif]-->
    <script src="${base}/resources/common/js/jquery.js"></script>
    <script src="${base}/resources/common/js/bootstrap.js"></script>
    <script src="${base}/resources/common/js/bootstrap-growl.js"></script>
    <script src="${base}/resources/common/js/jquery.validate.js"></script>
    <script src="${base}/resources/common/js/jquery.validate.additional.js"></script>
    <script src="${base}/resources/common/js/jquery.form.js"></script>
    <script src="${base}/resources/common/js/jquery.cookie.js"></script>
    <script src="${base}/resources/common/js/underscore.js"></script>
    <script src="${base}/resources/common/js/url.js"></script>
    <script src="${base}/resources/common/js/base.js"></script>
    <script src="${base}/resources/business/js/base.js"></script>
    [#noautoesc]
        [#escape x as x?js_string]
            <script>
                $().ready(function() {

                    var $document = $(document);
                    var $loginForm = $("#loginForm");
                    var $username = $("#username");
                    var $password = $("#password");
                    var $captcha = $("#captcha");
                    var $captchaImage = $("[data-toggle='captchaImage']");
                    var $rememberUsername = $("#rememberUsername");
                    var rememberedUsernameLocalStorageKey = "rememberedBusinessUsername";
                    var loginSuccessUrl = "${base}${businessLoginSuccessUrl}";

                    // 记住用户名
                    if (localStorage.getItem(rememberedUsernameLocalStorageKey) != null) {
                        $username.val(localStorage.getItem(rememberedUsernameLocalStorageKey));
                        $password.focus();
                        $rememberUsername.prop("checked", true);
                    } else {
                        $username.focus();
                        $rememberUsername.prop("checked", false);
                    }

                    // 表单验证
                    $loginForm.validate({
                        rules: {
                            username: "required",
                            password: {
                                required: true,
                                normalizer: function(value) {
                                    return value;
                                }
                            },
                            captcha: "required"
                        },
                        messages: {
                            username: {
                                required: "${message("business.login.usernameRequired")}"
                            },
                            password: {
                                required: "${message("business.login.passwordRequired")}"
                            },
                            captcha: {
                                required: "${message("business.login.captchaRequired")}"
                            }
                        },
                        submitHandler: function(form) {
                            $(form).ajaxSubmit({
                                successMessage: false,
                                successRedirectUrl: function(redirectUrlParameterName) {
                                    var redirectUrl = Url.queryString(redirectUrlParameterName);

                                    return $.trim(redirectUrl) != "" ? redirectUrl : loginSuccessUrl;
                                }
                            });
                        },
                        invalidHandler: function(event, validator) {
                            $.bootstrapGrowl(validator.errorList[0].message, {
                                type: "warning"
                            });
                        },
                        errorPlacement: $.noop
                    });

                    // 用户登录成功、记住用户名
                    $loginForm.on("success.xiaoxiangshop.ajaxSubmit", function() {
                        $document.trigger("loggedIn.xiaoxiangshop.user", [{
                            type: "business",
                            username: $username.val()
                        }]);

                        if ($rememberUsername.prop("checked")) {
                            localStorage.setItem(rememberedUsernameLocalStorageKey, $username.val());
                        } else {
                            localStorage.removeItem(rememberedUsernameLocalStorageKey);
                        }
                    });

                    // 验证码图片
                    $loginForm.on("error.xiaoxiangshop.ajaxSubmit", function() {
                        $captchaImage.captchaImage("refresh");
                    });

                    // 验证码图片
                    $captchaImage.on("refreshed.xiaoxiangshop.captchaImage", function() {
                        $captcha.val("");
                    });

                });
            </script>
        [/#escape]
    [/#noautoesc]
</head>
<body class="business login">
<main>
    <div class="container">
        <div class="panel panel-default">
            <div class="panel-heading">${message("business.login.title")}</div>
            <div class="panel-body">
                <form id="loginForm" action="${base}/business/login" method="post">
                    <div class="form-group">
                        <div class="input-group">
								<span class="input-group-addon">
									<i class="iconfont icon-people"></i>
								</span>
                            <input id="username" name="username" class="form-control" type="text" maxlength="200" placeholder="${message("business.login.usernamePlaceholder")}" autocomplete="off">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group">
								<span class="input-group-addon">
									<i class="iconfont icon-lock"></i>
								</span>
                            <input id="password" name="password" class="form-control" type="password" maxlength="200" placeholder="${message("business.login.passwordPlaceholder")}" autocomplete="off">
                        </div>
                    </div>
                    [#if setting.captchaTypes?? && setting.captchaTypes?seq_contains("BUSINESS_LOGIN")]
                        <div class="form-group">
                            <div class="input-group">
									<span class="input-group-addon">
										<i class="iconfont icon-pic"></i>
									</span>
                                <input id="captcha" name="captcha" class="captcha form-control" type="text" maxlength="4" placeholder="${message("common.captcha.name")}" autocomplete="off">
                                <div class="input-group-btn">
                                    <img class="captcha-image" src="${base}/resources/common/images/transparent.png" title="${message("common.captcha.imageTitle")}" data-toggle="captchaImage">
                                </div>
                            </div>
                        </div>
                    [/#if]
                    <div class="form-group">
                        <div class="checkbox checkbox-inline">
                            <input id="rememberUsername" name="rememberUsername" type="checkbox" value="true">
                            <label for="rememberUsername">${message("business.login.rememberUsername")}</label>
                        </div>
                        <a class="text-white pull-right" href="${base}/password/forgot?type=BUSINESS">${message("business.login.forgotPassword")}</a>
                    </div>
                    <div class="form-group">
                        <button class="btn btn-primary btn-lg btn-block" type="submit">${message("business.login.submit")}</button>
                    </div>
                    <p>
                        <a class="text-orange" href="${base}/business/register">${message("business.login.register")}</a>
                    </p>
                </form>
            </div>
        </div>
    </div>
</main>
</body>
</html>