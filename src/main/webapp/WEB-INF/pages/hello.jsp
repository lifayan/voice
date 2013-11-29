<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
    width: 880px;
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
<h1>${businessName} Phone Booking Record</h1>

<div id="centerList">
    <form:form method="post" action="businessName">
        <input name="businessName" value="${businessName}"/>
        <input type="submit" value="Update businessName"/>
    </form:form>

    <table id="hor-zebra" summary="Booking record">
    <thead>
    <tr>
    <th scope="col">Unique Session ID</th>
    <th scope="col">From Number</th>
    <th scope="col">TO Number</th>
    <th scope="col">Booking Date</th>
    <th scope="col">Calling time</th>
    <th scope="col">CALL BACK</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${items}" var="item" varStatus="loopStatus">
    <tr class="${loopStatus.index % 2 == 0 ? 'odd' : ''}">
    <td>${item.id}</td>
    <td>${item.from}</td>
    <td>${item.to}</td>
    <td>TBA</td>
    <td><fmt:formatDate type="both"  dateStyle="short" timeStyle="short"   value="${item.createDate}" /></td>
    <td><input type="button" value="call back to Remind"/> </td>
    </tr>
    </c:forEach>
    </tbody>
    </table>

</div>
</body>
</html>