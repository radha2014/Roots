<%@ page import="java.util.*,java.io.IOException,com.roots.CommonHelper" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
<!--
	function validate(){
		if(document.frm.current.value != '' ){
			return 1;//true
		} else {
			return 0;//false
		}
	}
	function requestPage(pg){
		if(validate()){
			//alert("requestPage called");
			document.frm.current.value=pg;
			openViewer();
		}
	}
	function openViewer(){
		document.frm.nextPage.value='View_Images';
		document.forms[0].submit();
	}
	function openMember(){
		document.frm.nextPage.value='';
		document.frm.nextAction.value='OpenMem';
		document.forms[0].submit();
	}
	function saveLinkage(){
		document.frm.nextAction.value='saveLinkage';
		document.forms[0].submit();
	}
	function reloadImages(){
		document.frm.nextAction.value='reload_images';
		document.forms[0].submit();
	}
//-->
</script></head>
<body>
<form name="frm" method="POST" action="mainServlet" >
<input type=hidden name="nextAction" value="zz">
<input type=hidden name="nextPage" value="View_Images">
<input type=hidden name="current" value="1">
<center>
<a href="javascript:openViewer();">Go to my Family Tree</a> | 
<a href="javascript:openMember()">Home</a>
<a href="javascript:reloadImages()">Reload Images</a>
</center>
<br>
==== Select Images ====
<table border="1">
<tr><td colspan=2 align="right">
	<%
	int blockSize = (Integer)request.getAttribute("blockSize");
	if(request.getAttribute("pg1") != null){
		int curr = (Integer)request.getAttribute("current");
		for(int i=1; i<=blockSize; i++){
			if(request.getAttribute("pg"+i) != null){
			
				int n = (Integer)request.getAttribute("pg"+i);
				if(n != curr) {
					%>
					<a href="javascript:requestPage('<%=n %>')"><%=n %></a>
					<%
				} else {
					%>
					<%=n %>
					<%
				}
			}
		}
	}
	%> Go to page 

</td></tr>
<tr>
<td>
	<table>
	<% 
		System.out.println("opening...imager.jsp"+request.getAttribute("images"));
		if(request.getAttribute("images")!=null){
			out.println("<tr><td>");
			out.println("<input type=hidden name=\"linkImage\" value=\""+request.getAttribute("images")+"\">");
			out.println("<a href=\"images\\"+request.getAttribute("images")+"\" >");
			out.println("<img src=\"images\\"+request.getAttribute("images")+"\" width=\"350\" />");
			out.println("</a>");
			out.println("</td></tr>");
		} else {
			out.println("No images to display...");
		}
	%>
	</table>
</td>
<td>
	<select multiple size="20" name="linkedMemberIDs" onChange="javascript:">
		<%
	         makeSelectOptions(out, request.getAttribute("memberlist"), request
	         .getAttribute("requestedid"));
		%>
	</select> 
	<input type=button value="Save" onClick="javascript:saveLinkage();"/>
</td></tr></table>
</form>
</body>
</html>
<%!
public void makeSelectOptions(JspWriter out, Object listObj,
        Object selectedListObj) throws IOException {

    List<String> list = (listObj != null)? (ArrayList<String>) listObj: null;
//    String selectedList = (selectedStringObj != null)? (String) selectedStringObj: null;

    String listItemID = null;
    if (list != null && !list.isEmpty()) {
        for (String listItem : list) {
            if (listItem != null) {
                listItemID = listItem.substring(0, listItem.indexOf("-"));
                out.print("<option ");
//                out.print((selectedString != null && listItemID
  //                      .equals(selectedString)) ? "selected " : "");
                out.print("value=\"" + listItemID + "\">");
                out.print(listItem);
                out.println("</option>");
            }
        }
    }
}
%>