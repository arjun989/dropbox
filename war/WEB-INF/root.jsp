<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>This is Example 001</title>
</head>

<body>
	<c:choose>
		<c:when test="${user != null}">
			<p style="text-align:center">Welcome ${user.email} <br /></p>
		    <p style="text-align:center"> You can signout <a href="${logout_url}">here</a><br /></p>
		</c:when>
		<c:otherwise>
			<p style="text-align:center">Welcome! <a href="${login_url}">Sign in or register</a></p>
		</c:otherwise>
	</c:choose>
</body>
</html>