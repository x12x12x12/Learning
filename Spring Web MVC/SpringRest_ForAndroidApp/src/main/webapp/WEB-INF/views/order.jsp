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

    <title>List các hóa đơn trong nhà hàng hiện có</title>

    <!-- Bootstrap Core CSS -->
    <link href="resources/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="resources/css/plugins/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- DataTables CSS -->
    <link href="resources/css/plugins/dataTables.bootstrap.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="resources/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="resources/font-awesome-4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <! [endif]-->

</head>

<body>
  	<div id="wrapper">
		<jsp:include page="navbar.jsp" />
		<div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header"><c:out value="${data.name}"/> Restaurant</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            List order in database
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table id="dataTables-example" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%"></table>
                            </div>
                            <!-- /.table-responsive -->
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
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
                    <h4 class="modal-title" id="myModalLabel">Thông tin order</h4>
                </div>
                <div class="modal-body" id="my_modal-body">

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" id="updateButton" >Order delivery!</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.modal -->
    <input type="hidden" value="<c:out value="${data.name}"/>" id="res_name">
    <!-- jQuery Version 1.11.0 -->
    <script src="resources/js/jquery-1.11.0.js"></script>
    <!-- Bootstrap Core JavaScript -->
    <script src="resources/js/bootstrap.min.js"></script>
    <!-- Metis Menu Plugin JavaScript -->
    <script src="resources/js/plugins/metisMenu/metisMenu.min.js"></script>
    <!-- DataTables JavaScript -->
    <script src="resources/js/plugins/dataTables/jquery.dataTables.js"></script>
    <script src="resources/js/plugins/dataTables/dataTables.bootstrap.js"></script>
    <!-- Custom Theme JavaScript -->
    <script src="resources/js/sb-admin-2.js"></script>
    <!-- My JavaScript -->
    <script>
        var user_data={
            "name": "${data.name}",
            "email":"${data.email}",
            "code":"${data.code}"
        };
        var select_order=[];
    	$.getJSON("http://localhost:8080/rest/store/order/"+user_data.code,function(result){
            var data = [];
           	$.each(result, function() {
           		var element = [];
           		for (var property in this) {
           		    if (this.hasOwnProperty(property)) {
               			if(property=="createDate"){
               				var dateTime=new Date(this[property]);
               				this[property]=dateTime.toDateString();
               			}
                        if(property=="status"){
                            switch (this[property]){
                                case 0:
                                    this[property]="Đã giao";
                                    break;
                                case 1:
                                    this[property]="Đang chờ";
                                    break;
                                case 2:
                                    this[property]="Hủy";
                                    break;
                            }
                        }
           		        element.push(this[property]);
           		    }
           		}
           		data.push(element);
           	});
            $('#dataTables-example').dataTable({
                "data": data,
                "columns": [
                    { "title": "Id" },
                    { "title": "User order","class": "center" },
                    { "title": "Phone", "class": "center" },
                    { "title": "Địa chỉ giao hàng", "class": "center" },
                    { "title": "Giá đơn hàng", "class": "center" },
                    { "title": "List", "class": "center" },
                    { "title": "Ngày đặt", "class": "center" },
                    { "title": "Restaurant Code", "class": "center" },
                    { "title": "Tình trạng", "class": "center" }
                ],
                "columnDefs":[
                    {"targets":[5],"visible":false,"searchable": false},
                    {"targets":[7],"visible":false,"searchable": false}
                ]
            });
            var add=$('#dataTables-example').DataTable();
            $('#dataTables-example tbody').on('click','tr',function(){
                select_order=add.row(this).data();
                var food_data=select_order[5].split(',');
                for(var i=0;i<food_data.length;i++){
                    var food_array=food_data[i].split('.');
                    $("#my_modal-body").append("<pre>Tên món :"+food_array[0]+"- Số lượng : "+food_array[1]+"</pre>");
//                    console.log(food_array[0]+"-"+food_array[1]);
                }
                $("#myModal").modal("show");
                clearModal();
            });
    	});
        function clearModal(){
            $("#modal-body").html("");
        }
        function postData(){
            var id=$("#idUpdate").val();
            var name=$("#nameUpdate").val();
            var restaurant=$("#restaurantUpdate").val();
            var price=$("#priceUpdate").val();
            var status=$("#statusUpdate").val();
            var img_url=$("#imgUpdate").val();
            var json = {"id":id,"name":name,"restaurant_name":restaurant,"price":price,"status":status,"img_url":img_url};
	    	var url="http://localhost:8080/rest/updateitem";
            var checkUpdateOrCreate=restaurant.split('-');
            if(checkUpdateOrCreate[1]!=null){
                url="http://localhost:8080/rest/item/create";
            }
	    	$.ajax({
    	        url: url,
    	        data: JSON.stringify(json),
    	        type: "POST",
    	        beforeSend: function(xhr) {
    	            xhr.setRequestHeader("Accept", "application/json");
    	            xhr.setRequestHeader("Content-Type", "application/json");
    	        },
    	        success: function(result) {
    	        	if(result.id=='fail'){
        	            alert("Update bị lỗi !! ");
    	        	}else{
    	        		alert("Update dữ liệu mới thành công !! ");
        	            $("#myModal").modal("hide");
                        location.reload();
    	        	}
    	        }
    	    });
            clearModal();
        }
        $("#updateButton").click(function(){
            postData();
        });
    </script>
</body>
</html>
