<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <style>
        #centerList {
            width: 700px;
            margin-left: auto;
            margin-right: auto;
        }
    </style>
</head>
<body>
<h1>${message}</h1>

<div id="centerList">
    <c:forEach items="${items}" var="item">
        <ul>
            <li><span>${item.id}</span> <span>${item.createDate}</span></li>
        </ul>
    </c:forEach>
</div>
</body>
</html>