<%@ page import="java.util.*,java.io.IOException,com.roots.CommonHelper" %>
<html><head>
<title>Sit back and enjoy sweet memories!!</title>
<script type="text/javascript">
<!--
	function openMember(){
		if(document.frm.memberID.value != ''){
			document.frm.nextAction.value='OpenMem';
			document.forms[0].submit();
		} else {
			alert('No member seleCted'); 
		}
	}
	function reloadSession(){
		document.frm.nextAction.value='Reload_Session';
		document.forms[0].submit();
	}
	function setMemberID(memberID){
		document.frm.memberID.value = memberID;
		openMember();
	}
//-->
</script></head>
<body bgcolor="FFAAAA">
<form name="frm" method="POST" action="mainServlet">
<center>
ID:<select size="1" name="memberID"
	onChange="javascript:">
	<option value=\"\" >Select...</option>
	<%
         makeSelectOptions(out, request.getAttribute("memberlist"), request
         .getAttribute("requestedid"));
	%>
</select> 
 <input type=button value="OpenMem" onClick="javascript:openMember();">
 . <input type=button value="Reload_Session" onClick="javascript:reloadSession();"> 
<input type=hidden name="nextAction" value="zz">
<input type=hidden name="nextPage" value="Load_Viewer"> <br><br>

<table border=1 >
<tr><td width="20%"> ID </td><td width="1%">:</td><td> <%=(request.getAttribute("requestedid")!=null)? request.getAttribute("requestedid"): "" %>
     <input type=hidden name="requestedID" value="<%=(request.
        getAttribute("requestedid")!=null)? request.getAttribute("requestedid"): "" %>">
</td></tr>
<tr><td>
Name</td><td width="1%">:</td><td> 
<%=(request.getAttribute("name")!=null)? request.getAttribute("name"): "" %>
</td></tr>

<% 
if(!CommonHelper.isEmpty((String)request.getAttribute("description"))) {
%>
	<tr><td colspan="3">
	<%=request.getAttribute("description") %>
	</td></tr>
<%
}
%>

<tr><td>
Age</td><td width="1%">:</td><td> <%=(request.getAttribute("age")!=null)? request.getAttribute("age"): "" %>
</td></tr>
<tr><td>
Date of Birth </td><td width="1%">:</td><td> 
	<%
		makeDOB(out, request.getAttribute("day"), request.getAttribute("month"), request.getAttribute("year"));
	%> 
</td></tr>
<tr><td>
Gender</td><td width="1%">:</td><td> <%=(request.getAttribute("gender")!=null)? (String)request.getAttribute("gender"): "" %>
</td></tr>
<tr><td>
Father's Name</td><td width="1%">:</td><td> 
<a href="javascript:setMemberID('<%=(request.getAttribute("fNameID")!=null)? request.getAttribute("fNameID"): "" %>')">
<%=(request.getAttribute("fName")!=null)? (String)request.getAttribute("fName"):"" %></a>
</td></tr>
<tr><td>
Mother's Name</td><td width="1%">:</td><td> 
<a href="javascript:setMemberID('<%=(request.getAttribute("mNameID")!=null)? request.getAttribute("mNameID"): "" %>')">
<%=(request.getAttribute("mName")!=null)? (String)request.getAttribute("mName"):"" %></a>
</td></tr>
<tr><td>
<%
	getBetterHalfLabel(out, request.getAttribute("gender"));
%>
</td><td width="1%">:</td><td> 
<a href="javascript:setMemberID('<%=(request.getAttribute("whNameID")!=null)? request.getAttribute("whNameID"): "" %>')">
<%=(request.getAttribute("whName")!=null)? (String)request.getAttribute("whName"):"" %></a>
</td></tr>
<tr><td colspan=3>Siblings</td></tr>
<tr><td colspan=3>
	<%
		makeCollectionFields(out, (ArrayList<String>)request.getAttribute("collectionlist"), (ArrayList<String>)request
		        .getAttribute("idgenderlist"), (ArrayList<String>)request.getAttribute("memberlist"), 
		        (String)request.getAttribute("siblingCollID"), "siblings", (String)request.getAttribute("name"));
	%>
</td></tr>
<tr><td colspan=3>Kids</td></tr>
<tr><td colspan=3>
	<%
		makeCollectionFields(out, (ArrayList<String>)request.getAttribute("collectionlist"), (ArrayList<String>)request
        		.getAttribute("idgenderlist"), (ArrayList<String>)request.getAttribute("memberlist"), 
        		(String)request.getAttribute("kidCollID"), "kids", null);
	%>
</td></tr>
<tr><td colspan=3>Picture</td></tr>
<tr><td colspan=3>
	<%
	ArrayList<String> pics = (ArrayList<String>) request.getAttribute("linkedPics");
	if (pics != null && pics.size() > 0){
		for(int i=0; i<pics.size(); i++){
			out.println("<a href=\"images\\"+pics.get(i)+"\" >");
			out.println("<img src=\"images\\"+pics.get(i)+"\" width=\"200\" />");
			out.println("</a>");
		}
	}
	%>
</td></tr></table>
<hr>
<a style="t"></a>
<div><font style="font-size: 14px; font-family: monospace;">
<%=(String)request.getAttribute("memberlinks") %>
</font>
</div>
<hr>
</center>
</form></body></html>
<%!
public void getBetterHalfLabel(JspWriter out, Object genderObj)
				throws IOException {

	String gender = (String)genderObj;
	if(!CommonHelper.isEmpty(gender)){
		if(gender.equalsIgnoreCase("MALE")){
			out.println("Wife's Name");
		} else if(gender.equalsIgnoreCase("FEMALE")){
			out.println("Husband's Name");
		}
	} else {
		out.println("Wife's/Husband's Name");
	}
}
public void makeSelectOptions(JspWriter out, Object listObj,
        Object selectedStringObj) throws IOException {

    List<String> list = (listObj != null)? (ArrayList<String>) listObj: null;
    String selectedString = (selectedStringObj != null)? (String) selectedStringObj: null;

    out.print("<option value=\"NEW ENTRY\">NEW ENTRY</option>");

    String listItemID = null;
    if (list != null && !list.isEmpty()) {
        for (String listItem : list) {
            if (listItem != null) {
                listItemID = listItem.substring(0, listItem.indexOf("-"));
                out.print("<option ");
                out.print((selectedString != null && listItemID
                        .equals(selectedString)) ? "selected " : "");
                out.print("value=\"" + listItemID + "\">");
                out.print(listItem);
                out.println("</option>");
            }
        }
    }
}
public void prepareLinks(JspWriter out, Object listObj) throws IOException {

    ArrayList<String> list = (listObj != null)? (ArrayList<String>) listObj: null;
    String listItemID = null;
    String listItemName = null;
    if (list != null && !list.isEmpty()) {
        for (String listItem : list) {
            if (listItem != null) {
                listItemID = listItem.substring(0, listItem.indexOf("-"));
                listItemName = listItem.substring(listItem.indexOf("-") + 1);
                out.print("<a href=\"javascript:setMemberID('" + listItemID + "')\" >");
                out.print((CommonHelper.isEmpty(listItemName))? listItemID : listItemName);
                out.print("</a> | ");
            }
        }
    }
}
public void makeDOB(JspWriter out, Object day, Object month, Object year) 
	throws IOException {
	if(day==null || month==null || year==null){
		out.print("not available");
	} else{
	    out.print(day+"/"+month+"/"+year);
	}
}
/**
 *Create three fields on the screen 1.textfield for name, dropdown for ID, dropdown for gender
 *
 */
public void makeCollectionFields(JspWriter out, ArrayList<String> colIDNamelist, ArrayList<String> colIDGenderlist,
		ArrayList<String> memberlist, String selectedString, String colType, String memberName)
	throws IOException{

     String colIdName = null;
     String colIdGender = null;

     // get the selected collection strings ID:Name and ID:Gender
     if(!CommonHelper.isEmpty(colIDNamelist) && !CommonHelper.isEmpty(colIDGenderlist) 
    		&& !CommonHelper.isEmpty(selectedString)) {
	    for(int i=0; i< colIDNamelist.size(); i++){
	        if(colIDNamelist.get(i).startsWith(selectedString)){
	        	colIdName = colIDNamelist.get(i);
	        	colIdGender = colIDGenderlist.get(i);
	        }
	    }
     }
    
     if (colIdName != null && colIdGender != null 
    		&& colIdName.substring(colIdName.indexOf("-")+1).trim().length()!=0) {
	    String[] siblings = colIdName.substring(colIdName.indexOf("-")+2).split(",");
	    String[] siblingIDGenders = colIdGender.substring(colIdName.indexOf("-")+2).split(",");
	    for(int i=0; i<siblings.length; i++){

	    	String siblingGender = siblingIDGenders[i].substring(siblingIDGenders[i].indexOf(":")+1);
	    	String id = siblings[i].substring(0, siblings[i].indexOf(":"));
	    	String name = siblings[i].substring(siblings[i].indexOf(":")+1);

	    	out.print((i+1)+". ");
	    	out.print("<a href=\"javascript:setMemberID('"+id+"')\" >");
	    	out.print(name);
	    	out.println("</a>");
	    	if(name.equals(memberName)){
	    		out.print("(self)");
	    	} else if(siblingGender != null){

	    		if("siblings".equals(colType)){
				    if("MALE".equalsIgnoreCase(siblingGender)){
			    		out.print("(brother)");
				    } else if("FEMALE".equalsIgnoreCase(siblingGender)){
			    		out.print("(sister)");
				    }
		    	} else if("kids".equals(colType)){
				    if("MALE".equalsIgnoreCase(siblingGender)){
			    		out.print("(son)");
				    } else if("FEMALE".equalsIgnoreCase(siblingGender)){
			    		out.print("(daughter)");
				    }
		    	}
	        }
	        out.print("<br>");
	    }
	 }
}
%>
