<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en"> <!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1, user-scalable=no, minimal-ui"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta content="yes" name="apple-mobile-web-app-capable"/>
    <meta content="black" name="apple-mobile-web-app-status-bar-style"/>
    <meta content="telephone=no,email=no" name="format-detection"/>
    <link rel="apple-touch-fullscreen" content="YES"/>
    <title>米星登录</title>
    <link rel="stylesheet" href="/static/css/login-style.css">
    <script src="/static/js/jQuery-2.1.4.min.js"></script>
    <script src="/static/plugins/jquery.form.js"></script>
    <!--[if lt IE 9]><script src="/static/js/html5.js"></script><![endif]-->
</head>
<body>
<form name="loginForm" id="loginForm" action="/open/yogu/login.do" class="login" method="post">
  <h1>米星帐号登录</h1>
  <input type="passport" name="passport" class="login-input" placeholder="Passport" autofocus>
  <input type="password" name="password" class="login-input" placeholder="Password">
  <input type="submit" value="Login" class="login-submit">
  <input type="hidden" name="countryCode" value="86"/>
  <input type="hidden" name="appKey" value="<c:out value="${appKey}" escapeXml="true"/>"/>
  <input type="hidden" name="callback" value="<c:out value="${callback}" escapeXml="true"/>"/>
  <input type="hidden" name="token" value="<c:out value="${token}" escapeXml="true"/>"/>
    <input type="hidden" name="t" value="<c:out value="${t}" />"/>
  <p class="login-help" style="display: none; color: #f0C800;" id="errorMessage"></p>
  <p class="login-help"><a href="#">Forgot password?</a></p>
</form>

<script type="text/javascript">
    $(function () {
        $('#loginForm').ajaxForm({
            complete: function (xhr) {
              try {
                eval('json=' + xhr.responseText);
                if (json.success) {
                    window.location.href = json.object;
                }
                else {
                    $("#errorMessage").html(json.message);
                    $("#errorMessage").css("display", "block");
                }

              } catch (e) {
              }
            }
        });
    });
</script>
</body>
</html>
