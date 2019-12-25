<%@page language="java" import="cn.dendarii.ivan.api.WebServer"
    pageEncoding="utf-8"%>
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>ivan-helloworld</title>
<link href="bootstrap.min.css" rel="stylesheet" />
</head>

<body style="font-family: Consolas, 'Microsoft YaHei';">
    <div class="container">
        <div class="row">
            <div class="col-xs-4">服务器状态：</div>
            <div class="col-xs-8">
                <%=WebServer.server.getState()%>
            </div>
            <div class="row">
                <div class="col-xs-4">测试内嵌代码</div>
                <%
                    for (int i = 0; i < 6; i++) {
                %>
                <div class="col-xs-1"><%=i%></div>
                <%
                    }
                %>
            </div>
            <div class="row">
                <div class="col-xs-4">测试获取服务器消息</div>
                <div class="col-xs-8" id="test-div"></div>
            </div>
        </div>
    </div>
</body>

<script type="text/javascript" src="jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="bootstrap.min.js"></script>
<script type="text/javascript">
  $(function() {
    $.get("/test", {
      a : 1,
      b : 2
    }, function(randNum) {
      $('#test-div').html(randNum);
      console.log(randNum);
    });
  });
</script>
</html>

