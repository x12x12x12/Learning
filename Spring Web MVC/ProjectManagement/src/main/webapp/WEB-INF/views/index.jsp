<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Project management</title>

    <!-- Bootstrap Core CSS -->
    <link href="/resources/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="/resources/css/plugins/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- DataTables CSS -->
    <link href="/resources/css/plugins/dataTables.bootstrap.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="/resources/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="/resources/font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">


</head>

<body>
  	<div id="wrapper">
		<jsp:include page="navbar.jsp" />
		<div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header"> <c:out value="${account.name}"/>
                        <button type="button" class="btn btn-primary btn-circle btn-xl" onclick="formProject()" data-toggle="tooltip" data-placement="top" title="" data-original-title="Create Project"><i class="fa fa-list " ></i> </button>
                    </h1>
                </div>
            </div>
            <c:if test="${not empty listProject}">
                <c:forEach var="listValue" items="${listProject}">
                    <div class="col-lg-3 col-md-6">
                        <div class="panel panel-green">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-xs-3">
                                        <i class="fa fa-tasks fa-5x"></i>
                                    </div>
                                    <div class="col-xs-9 text-right">
                                        <div class="huge" onclick="updateProject('${listValue.id}','${listValue.name}','${listValue.startDate}','${listValue.dueDate}')">${listValue.name}</div>
                                        <div>${listValue.createDate}</div>
                                    </div>
                                </div>
                            </div>
                            <a href="/project/${listValue.id}">
                                <div class="panel-footer">
                                    <span class="pull-left">View Details</span>
                                    <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                    <div class="clearfix"></div>
                                </div>
                            </a>
                        </div>
                    </div>
                </c:forEach>

            </c:if>

        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- Modal -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">Create Project </h4>
                </div>
                <div class="modal-body">
                        <fieldset>
                            <div class="form-group">
                                <label>Project name</label>
                                <input class="form-control" placeholder="Enter text" id="nameProject">
                            </div>
                            <div class="form-group">
                                <label>Start date</label>
                                <input class="form-control" placeholder="Start date" id="startProject">
                            </div>
                            <div class="form-group">
                                <label>Due date</label>
                                <input class="form-control" placeholder="Due date" id="dueProject">
                            </div>
                            <input type="hidden" name="${_csrf.parameterName}"value="${_csrf.token}" />
                        </fieldset>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" id="submitButton" onclick="createProject()">Submit</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="myUpdateModal" tabindex="-1" role="dialog" aria-labelledby="myUpdateModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myUpdateModalLabel">Update Project </h4>
                </div>
                <div class="modal-body">
                    <fieldset>
                        <div class="form-group">
                            <label>Project name</label>
                            <input class="form-control" placeholder="Enter text" id="updatenameProject">
                        </div>
                        <div class="form-group">
                            <label>Start date</label>
                            <input class="form-control" placeholder="Start date" id="updatestartProject">
                        </div>
                        <div class="form-group">
                            <label>Due date</label>
                            <input class="form-control" placeholder="Due date" id="updatedueProject">
                        </div>
                        <input type="hidden" id="id_project" value="" />
                        <input type="hidden" name="${_csrf.parameterName}"value="${_csrf.token}" />
                    </fieldset>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" onclick="postUpdateProject()">Submit</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
    </div>

    <link rel="stylesheet" href="/resources/jquery-ui.css">
    <!-- jQuery Version 1.11.0 -->
    <script src="/resources/js/jquery-1.11.0.js"></script>
    <script src="/resources/moment.js"></script>
    <script src="/resources/moment-with-locales.js"></script>
    <script src="/resources/jquery-ui.js"></script>
    <!-- Bootstrap Core JavaScript -->
    <script src="/resources/js/bootstrap.min.js"></script>
    <!-- Metis Menu Plugin JavaScript -->
    <script src="/resources/js/plugins/metisMenu/metisMenu.min.js"></script>
    <!-- Custom Theme JavaScript -->
    <script src="/resources/js/sb-admin-2.js"></script>
    <!-- My JavaScript -->
    <script>

        $("#startProject").datepicker({ dateFormat: 'yy-mm-dd' }).val();
        $("#dueProject").datepicker({ dateFormat: 'yy-mm-dd' }).val();
        $("#updatestartProject").datepicker({ dateFormat: 'yy-mm-dd' }).val();
        $("#updatedueProject").datepicker({ dateFormat: 'yy-mm-dd' }).val();

        $('.row').tooltip({
            selector: "[data-toggle=tooltip]",
            container: "body"
        })
        var user_data={
            "name": "${account.name}",
            "email":"${account.email}"
        };
        var urlPostProject="http://localhost:8080/rest/project/create/";
        var urlUpdateProject="http://localhost:8080/rest/project/update/";

        function formProject(){
            clearModal();
            $("#myModal").modal("show");
        }

        function createProject(){
            var account=user_data.email;
            var name=$("#nameProject").val();
            var startDate = new Date($("#startProject").val()).toISOString();
            var dueDate = new Date($("#dueProject").val()).toISOString();
            var json = {"name":name,"accountOwner":account,"startDate":startDate,"dueDate":dueDate};
            $.ajax({
                url: urlPostProject,
                data: JSON.stringify(json),
                type: "POST",
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("Accept", "application/json");
                    xhr.setRequestHeader("Content-Type", "application/json");
                },
                success: function(result) {
                    if(result.id=='fail'){
                        alert("Submit bị lỗi !! ");
                    }else{
                        alert("Submit dữ liệu mới thành công !! ");

                    }
                }
            });
            $("#myModal").modal("hide");
            location.reload();
            clearModal();
        }

        function updateProject(id,name,start,due){
            clearModal();
            $("#id_project").val(id);
            $("#updatenameProject").val(name);
            $("#updatestartProject").val(parseDate(start));
            $("#updatedueProject").val(parseDate(due));
            $("#myUpdateModal").modal("show");
        }

        function postUpdateProject(){
            var id=$("#id_project").val();
            var account=user_data.email;
            var name=$("#updatenameProject").val();
            var startDate = new Date($("#updatestartProject").val()).toISOString();
            var dueDate = new Date($("#updatedueProject").val()).toISOString();
            var json = {"id":id,"name":name,"accountOwner":account,"startDate":startDate,"dueDate":dueDate};
            $.ajax({
                url: urlUpdateProject,
                data: JSON.stringify(json),
                type: "POST",
                beforeSend: function(xhr) {
                    xhr.setRequestHeader("Accept", "application/json");
                    xhr.setRequestHeader("Content-Type", "application/json");
                },
                success: function(result) {
                    if(result.id=='fail'){
                        alert("Submit bị lỗi !! ");
                    }else{
                        alert("Submit dữ liệu mới thành công !! ");

                    }
                }
            });
            $("#myModal").modal("hide");
            location.reload();
            clearModal();
        }

        function clearModal(){
            $("#nameProject").val("");
            $("#startProject").val("");
            $("#dueProject").val("");
            $("#id_project").val("");
            $("#updatenameProject").val("");
            $("#updatestartProject").val("");
            $("#updatedueProject").val("");
        }

        function parseDate(date){
            var data=date.split(' ');
            switch (data[1]) {
                case 'Jan':
                    data[1]=1;
                    break;
                case 'Feb':
                    data[1]=2;
                    break;
                case 'Mar':
                    data[1]=3;
                    break;
                case 'Apr':
                    data[1]=4;
                    break;
                case 'May':
                    data[1]=5;
                    break;
                case 'Jun':
                    data[1]=6;
                    break;
                case 'Jul':
                    data[1]=7;
                    break;
                case 'Aug':
                    data[1]=8;
                    break;
                case 'Sep':
                    data[1]=9;
                    break;
                case 'Oct':
                    data[1]=10;
                    break;
                case 'Nov':
                    data[1]=11;
                    break;
                case 'Dec':
                    data[1]=12;
                    break;
                default :
                    break;
            }
            return data[5]+"-"+data[1]+"-"+data[2];
        }

    </script>
</body>
</html>
