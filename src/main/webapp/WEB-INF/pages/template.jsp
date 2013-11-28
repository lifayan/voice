<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
<h1>Response template</h1>
<div id="centerList">
    <form:form method="post" action="template">
        <textarea name="template" cols="100" rows="50">${template}</textarea>
        <input type="submit" value="Update template"/>
    </form:form>
</div>
</body>
</html>