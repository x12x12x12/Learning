<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head lang="en">
    <meta charset="UTF-8">
    <title> Code Validate</title>
    <link rel="stylesheet" type="text/css" href="resources/extra/Style.css">
    <link rel="stylesheet" type="text/css" href="resources/extra/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="resources/extra/bootstrap-theme.min.css">

    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.26/angular.min.js"></script>
    <script src="resources/extra/jquery-1.11.0.min.js"></script>
    <script src="resources/extra/bootstrap.min.js"></script>
</head>
<body>
    <div class="col-md-4"></div>
    <div class=" my-form col-md-4">
        <div id="my-form-title" class="row" align="center">
            Please Validate Your Code
        </div>
        <br/>
        <div id="my-form-body" class="row">
            <form class="form-horizontal">
                <div class="form-group" align="center">
                    <label for="inputEmail" class="col-md-3 control-label">Email</label>
                    <div class="col-md-8">
                        <input type="email" class="form-control" id="inputEmail" placeholder="Email">
                    </div>
                </div>
                <div class="form-group" align="center">
                    <label for="inputCode" class="col-md-3 control-label">Your Code</label>
                    <div class="col-md-8">
                        <input type="password" class="form-control" id="inputCode" placeholder="Code">
                    </div>
                </div>
                <div class="form-group" align="center">
                    <label for="inputCapcha" class="col-md-3 control-label">Capcha</label>
                    <div class="col-md-8">
                        <input type="password" class="form-control" id="inputCapcha" placeholder="Capcha">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-md-1"></div>
                    <div class="col-md-10" align="center">
                        <button type="submit" class="btn btn-primary" ng-click="validateYourCode()"
                                style="width: 100%; border-radius: 10px;">
                            Sign in
                        </button>
                    </div>
                    <div class="col-md-1"></div>
                </div>
            </form>
        </div>
    </div>
    <div class="col-md-4"></div>

</body>
</html>