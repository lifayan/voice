<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
<h1>Fantastic Resort Phone Booking</h1>

<div id="centerList">
    <ul>
             <li><span>UUID</span>,<span>From</span>, <span>TO</span>, <span>Booking Date</span>,  <span>Calling time</span> </li>
    <c:forEach items="${items}" var="item">
            <li><span>${item.id}</span>, <span>${item.from}</span>, <span>${item.to}</span>, <span>${item.bookingDate}</span>,
    <span><fmt:formatDate type="both"  dateStyle="short" timeStyle="short"   value="${item.createDate}" /></span> </li>
    </c:forEach>
    </ul>
</div>
</body>
</html>