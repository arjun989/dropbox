<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Dropbox Clone</title>
</head>

<body>
	<c:choose>
		<c:when test="${user != null}">
			<p style="text-align: center">
				Welcome ${user.email} <br />
			</p>
			<div align="center">
				<form method="post">
					<input value="signout" name="signout" type="submit"/>
				</form>
			</div>
			<h2>Path : ${path}</h2>
			<h3>Sub-Directories :</h3>
			
			<script>
				if (document.getElementById("app")) {
					document.getElementById("app").parentNode
							.removeChild(document.getElementById("app"));
				}
			</script>
			
			<%
				ArrayList<String> arr = (ArrayList<String>) request.getAttribute("dir");
						if (arr != null) {
							//out.write("");
							out.write("<div id= \"app\" >");
							out.write("<form method=\"post\">");
							for (int i = 0; i < arr.size(); i++) {

								out.write("<lable>" + arr.get(i) + "</lable>");
								out.write("<input type=\"submit\" name=\"" + (i + "ent") + "\" value=\"Enter\" \\>");
								out.write("<input type=\"submit\" name=\"" + (i + "dlt") + "\" value=\"Delete\" \\>");
								out.write("<br/><br/>");

							}
							out.write("</form>");
							out.write("</div>");
						}
			%>
			
			<p>
				<a href="${addDir}">ADD DIRECTORY</a><br />
			</p>
			<br />
			
			<%-- <h3>Files</h3>
			<!-- The list of blobs passed in that will be made available -->
			<c:forEach items="${keys}" var="key" begin="0" varStatus="loop">
				<form method="get" action="/upanddown">
					<input type="hidden" name="key_value"
						value="${loop.index}" />
					<input type="submit" value="Download" />
				</form>
			</c:forEach>
			
			<h1>File Upload</h1>
			<form enctype="multipart/form-data" method="post"
				action="<%=BlobstoreServiceFactory.getBlobstoreService().createUploadUrl("/upanddown")%>">
				<input type="file" name="file" size="30" /> <input type="submit" />
			</form> --%>
		</c:when>
		
		<c:otherwise>
			<p style="text-align: center">
				Welcome! <a href="${login_url}">Sign in or register</a>
			</p>
			
		</c:otherwise>
	</c:choose>
</body>
</html>