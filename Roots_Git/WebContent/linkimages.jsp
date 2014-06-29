<%@ page import="java.util.*,java.io.IOException,com.roots.CommonHelper" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Enrich your family member and friend details with pictures</title>
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
			document.forms[0].submit();
		}
	}
	function openViewer(){
		document.frm.nextPage.value='Load_Viewer';
		document.forms[0].submit();
	}
	function openMember(){
		document.frm.nextPage.value='load_application';
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
	function saveSession(){
		document.frm.nextAction.value='Save_Session';
		document.forms[0].submit();
	}
	function updateSelectedMembers(){
		len = document.frm.linkedMemberIDs.length;
		var count = 0;
		//alert(len);
		document.frm.txt1.value='';
		for(i=0; i<len; i++){
			if(document.frm.linkedMemberIDs.options[i].selected){
				//alert(document.frm.linkedMemberIDs.options[i].text);
				document.frm.txt1.value+=document.frm.linkedMemberIDs.options[i].text+'\n';
				count++;
			}
		}
		document.frm.selectedImageCount.value = " " + count + " member(s) selected...";
	}
//-->
</script></head>
<body>
<form name="frm" method="POST" action="mainServlet" >
<input type=hidden name="nextAction" value="zz">
<input type=hidden name="nextPage" value="Link_Images">
<input type=hidden name="current" 
	value="<%=(request.getAttribute("currentImage")!=null)? request.getAttribute("currentImage") : "zz" %>">
<center>
<a href="javascript:openViewer();">Go to my Family Tree</a> | 
<a href="javascript:openMember()">Home</a> |
<a href="javascript:reloadImages()">Reload Images</a>
</center>
<br>
==== Select Images ====
<table border="1">
<tr><td colspan=2 align="right">
	<%
	Integer prev = (Integer)request.getAttribute("prevImage");
	Integer curr = (Integer)request.getAttribute("currentImage");
	Integer next = (Integer)request.getAttribute("nextImage");
	if(prev != null){
		%>
		<a href="javascript:requestPage('<%=prev %>')">&lt;</a>
		<%
	} else {
		%>
		&lt; 
		<%
	}
	if(curr != null){
		%>
		<%=curr+1 %>
		<%
	} else {
		%>
		not available
		<%
	}
	if(next != null){
		%>
		<a href="javascript:requestPage('<%=next %>')">&gt;</a>
		<%
	} else {
		%>
		&gt;
		<%
	}
	Integer imageCount = (Integer)request.getAttribute("imageCount");
	ArrayList<String> availableImgList = (ArrayList<String>) request.getAttribute("availableImgList");
	if(imageCount != null){
		%>
		<select onChange="javascript:requestPage(this.value)">
		<option>Jump To</option>
		<%
		for(int i=0; i<imageCount ; i++){
			%>
			<option value=<%=i %> ><%=(i+1)+"."+availableImgList.get(i) %></option>
			<%
		}
		%>
		</select>
		<%
	}
	%>
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
	<table>
	<tr><td>
	<select multiple size="20" name="linkedMemberIDs" onChange="javascript:updateSelectedMembers()">
		<%
	         makeSelectOptions(out, request.getAttribute("memberlist"), request
	         .getAttribute("linkedMemberList"));
		%>
	</select>
	</td>
	<td>
		<table><tr><td>
		<textarea readonly="readonly" rows="19" cols="30" name="txt1"></textarea>
		</td></tr>
		<tr><td>
		<input type=button value="Save" onClick="javascript:saveLinkage();"/>
		<input type=button value="Save Session" title="Save all data permanently." onClick="javascript:saveSession();"/>
		</td></tr></table>
	</td></tr></table>
</td></tr>
<tr><td colspan=2 align="right"><input style=" background-color: lime; " type=text name="selectedImageCount" /></td></tr>
</table>
</form>
<script type="text/javascript">
<!--
	updateSelectedMembers();
//-->
</script>

</body>
</html>
<%!
public void makeSelectOptions(JspWriter out, Object listObj,
        Object selectedListObj) throws IOException {

    List<String> list = (listObj != null)? (ArrayList<String>) listObj: null;
    List<String> selectedList = (selectedListObj != null)? (List<String>) selectedListObj
    		: new ArrayList<String>();

    String listItemID = null;
    if (list != null && !list.isEmpty()) {
        for (String listItem : list) {
            if (listItem != null) {
                listItemID = listItem.substring(0, listItem.indexOf("-"));
                out.print("<option ");
                out.print(selectedList.contains(listItemID) ? "selected " : "");
                out.print("value=\"" + listItemID + "\">");
                out.print(listItem);
                out.println("</option>");
            }
        }
    }
}
%>