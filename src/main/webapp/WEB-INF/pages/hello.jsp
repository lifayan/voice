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

    #hor-zebra
    {
    font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
    font-size: 14px;
    margin: 55px;
    width: 680px;
    text-align: left;
    border-collapse: collapse;
    }
    #hor-zebra th
    {
    font-size: 16px;
    font-weight: normal;
    padding: 12px 10px;
    color: #039;
    }
    #hor-zebra td
    {
    padding: 10px;
    color: #669;
    }
    #hor-zebra .odd
    {
    background: #e8edff;
    }
    </style>
</head>
<body>
<h1>Sensis Fantastic Resort Phone Booking Record</h1>

<div id="centerList">

    <table id="hor-zebra" summary="Booking record">
    <thead>
    <tr>
    <th scope="col">Unique Session ID</th>
    <th scope="col">From Number</th>
    <th scope="col">TO Number</th>
    <th scope="col">Booking Date</th>
    <th scope="col">Calling time</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${items}" var="item" varStatus="loopStatus">
    <tr class="${loopStatus.index % 2 == 0 ? 'odd' : ''}">
    <td>${item.id}</td>
    <td>${item.from}</td>
    <td>${item.to}</td>
    <td>${item.bookingDate}</td>
    <td><fmt:formatDate type="both"  dateStyle="short" timeStyle="short"   value="${item.createDate}" /></td>
    </tr>
    </c:forEach>
    </tbody>
    </table>

</div>
</body>
</html>