<%@ page import="java.util.*,java.io.IOException,com.roots.CommonHelper" %>
<html><head>
<title>Construct your family links and relatives, friends. Add details with rich pictures..</title>
<script type="text/javascript">
<!--
	function validate(){
		if(document.frm.T1.value != '' ){
			return 1;//true
		} else {
			return 0;//false
		}
	}
	function save(){
		if(validate()){
			//alert("save called");
			document.frm.nextAction.value='Save';
			document.forms[0].submit();
		}
	}
	function saveSession(){
		if(validate()){
			alert("save Session called");
			document.frm.nextAction.value='Save_Session';
			document.forms[0].submit();
		}
	}
	function openMember(){
		if(document.frm.memberID.value != ''){
			document.frm.nextAction.value='OpenMem';
			document.forms[0].submit();
		} else {
			alert('No member seleted'); 
		}
	}
	function reloadSession(){
		document.frm.nextAction.value='Reload_Session';
		document.forms[0].submit();
	}
	function openViewer(){
		document.frm.nextAction.value='';
		document.frm.nextPage.value='Load_Viewer';
		document.forms[0].submit();
	}
	function linkImages(){
		document.frm.nextAction.value='reload_images';
		document.frm.nextPage.value='Link_Images';
		document.forms[0].submit();
	}
//--------- UI validations --------------------
	function resetFieldValue(selectObj, txtField){
		if(selectObj.value=='NEW ENTRY'){
			document.getElementById (txtField).value = '';
		} else {
			val = selectObj.options [selectObj.selectedIndex].text;
			val = val.substr(val.indexOf('-') + 1, val.length);
			document.getElementById (txtField).value = val;
		}
	}
//---------- UI Validations ---------------------

//-->
</script></head>
<body>
<form name="frm" method="POST" action="mainServlet" >
<center>
ID:<select size="1" name="memberID"
	onChange="javascript:">
	<option value=\"\" >Select...</option>
	<%
         makeSelectOptions(out, request.getAttribute("memberlist"), request
         .getAttribute("requestedid"));
	%>
</select> 
o<input type=button value="OpenMem" onClick="javascript:openMember();">
s<input type=button value="Save" onClick="javascript:save();"> <br>
<a href="javascript:openViewer();">Go to my Family Tree</a> | 
<a href="javascript:linkImages();">View & Link Images</a>
<input type=hidden name="nextAction" value="zz">
<input type=hidden name="nextPage" value="load_application">
<table border=1 width="80%"><tr><td>
Name: <input value="<%=(request.getAttribute("name")!=null)? request.getAttribute("name"): "" %>" type="text" name="T1" size="20">

<font size=2 color="4444ff">
 ( ID : <%=(request.getAttribute("requestedid")!=null)? request.getAttribute("requestedid"): "Does nor exist" %>
     <input type=hidden name="requestedID" value="<%=(request.
        getAttribute("requestedid")!=null)? request.getAttribute("requestedid"): "Not Available" %>"> )
</font>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
Gender: <select size="1" name="D5">
	<option value="">Select..</option>
	<option <%=(request.getAttribute("gender") !=null && "MALE".equalsIgnoreCase((String)request.getAttribute("gender"))? 
            "selected" : "") %>>Male</option>
	<option <%=(request.getAttribute("gender") !=null && "FEMALE".equalsIgnoreCase((String)request.getAttribute("gender"))?
			"selected" : "")%>>Female</option>
</select><br>

Age : <input type="text" name="T14" size="20" value="<%=(request.getAttribute("age")!=null)? request.getAttribute("age"): "" %>"> 

Date of Birth : 
Day<select size="1" name="D2">
	<%
		makeDayOptions(out, request.getAttribute("day"));
	%>
</select>
-Month<select size="1" name="D3">
	<%
		makeMonthOptions(out, request.getAttribute("month"));
	%>
</select>
-Year<select size="1" name="D4">
	<%
		makeYearOptions(out, request.getAttribute("year"));
	%>
</select><br>

Some short description <font size=2 color="4444ff">*you may use html tags in your text</font>:<br>
 &nbsp;<textarea name="description" rows="2" cols="100">
<%=(!CommonHelper.isEmpty((String)request.getAttribute("description")))? ((String)request.getAttribute("description")).replaceAll("<br>","\n"): "" %>
</textarea><br>

Father's Name: <input type="text" name="T3" size="20"
	value="<%=(request.getAttribute("fName")!=null)? request.getAttribute("fName"):"" %>" >
	&lt;==<select size="1" name="D23" onChange="javascript:resetFieldValue(this,'T3')">
	<%
        makeSelectOptions(out, request.getAttribute("memberlist"), request
        .getAttribute("fNameID"));
	%>
</select><font size=2 color="4444ff">*check if member already exists in the member collection</font>
<br>

Mother's Name: <input type="text" name="T4" size="20"
	value="<%=(request.getAttribute("mName")!=null)? request.getAttribute("mName"):"" %>" >
	&lt;==<select size="1" name="D24" onChange="javascript:resetFieldValue(this,'T4')">
	<%
        makeSelectOptions(out, request.getAttribute("memberlist"), request
        .getAttribute("mNameID"));
	%>
</select><font size=2 color="4444ff">*check if member already exists in the member collection</font>
<br>

Wife's/Husband's Name: <input type="text" name="T5" value="<%=(request.getAttribute("whName")!=null)?
        request.getAttribute("whName"):"" %>" size="20">
	&lt;==<select size="1" name="D25" onChange="javascript:resetFieldValue(this,'T5')">
	<%
        makeSelectOptions(out, request.getAttribute("memberlist"), request
        .getAttribute("whNameID"));
	%>
</select><br>

Siblings&lt;==<select size="1" name="D26">
	<%
        makeSelectOptions(out, request.getAttribute("collectionlist"), request
        .getAttribute("siblingCollID"));
	%>
</select>
<table border=0>
	<%
		makeCollectionFields(out, (ArrayList<String>) request.getAttribute("collectionlist"), (ArrayList<String>) request
		        .getAttribute("idgenderlist"), (ArrayList<String>) request.getAttribute("memberlist"), 
		        (String)request.getAttribute("siblingCollID"), "siblings");
	%>
</table>
Kids&lt;==<select size="1" name="D27">
	<%
        makeSelectOptions(out, request.getAttribute("collectionlist"), request
                .getAttribute("kidCollID"));
	%>
</select>
<table>
	<%
		makeCollectionFields(out, (ArrayList<String>)request.getAttribute("collectionlist"), (ArrayList<String>)request
        		.getAttribute("idgenderlist"), (ArrayList<String>)request.getAttribute("memberlist"), 
        		(String)request.getAttribute("kidCollID"), "kids");
	%>
</table>
	<%
	ArrayList<String> pics = (ArrayList<String>) request.getAttribute("linkedPics");
	if (pics != null && pics.size() > 0){
		for(int i=0; i<pics.size(); i++){
			out.println("<a href=\"images\\"+pics.get(i)+"\" >");
			out.println("<img src=\"images\\"+pics.get(i)+"\" width=\"350\" />");
			out.println("</a>");
		}
	}
	%>
<table>
<tr></tr>
</table>
</td></tr></table>
 <input type=button value="Save Session" onClick="javascript:saveSession();"> 
 <input type=button value="Reload Session" onClick="javascript:reloadSession();">
</center>
</form></body></html>
<%!
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
public void makeDayOptions(JspWriter out, Object selectDayObj) 
	throws IOException {

    String selectedDay = "";
    if (selectDayObj != null) {
        selectedDay = "" + selectDayObj;
    }

    for (int day = 1; day <= 31; day++) {
        out.print("<option ");
        out.print((selectedDay.equals(""+day)) ? "selected " : "");
        out.print("value=\"" + day + "\">");
        out.print(day);
        out.println("</option>");
    }
}
public void makeMonthOptions(JspWriter out, Object selectMonthObj) 
	throws IOException {

    String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String selectedMonth ="";
    if (selectMonthObj != null) {
        selectedMonth = "" + selectMonthObj;
    }

    for (int monthIdx = 0; monthIdx<months.length; monthIdx++) {
        out.print("<option ");
        out.print((selectedMonth.equals(""+(monthIdx+1))) ? "selected " : "");
        out.print("value=\"" + (monthIdx+1) + "\">");
        out.print(months[monthIdx]);
        out.println("</option>");
    }
}
public void makeYearOptions(JspWriter out, Object selectedYearObj) 
	throws IOException {

    String selectedYear = "";
    if (selectedYearObj != null) {
        selectedYear = "" + selectedYearObj;
    }

    for (int year = 1900; year<=2010; year++) {
        out.print("<option ");
        out.print((selectedYear.equals(""+year)) ? "selected " : "");
        out.print("value=\"" + year + "\">");
        out.print(year);
        out.println("</option>");
    }
}
/**
 *Create three fields on the screen 1.textfield for name, dropdown for ID, dropdown for gender
 *
 */
public void makeCollectionFields(JspWriter out, ArrayList<String> colIDNamelist, ArrayList<String> colIDGenderlist,
		ArrayList<String> memberlist, String selectedString, String colType)
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
    
    int startIdx = 0;
    if (colIdName != null && colIdGender != null 
    		&& colIdName.substring(colIdName.indexOf("-")+1).trim().length()!=0) {
	    String[] siblings = colIdName.substring(colIdName.indexOf("-")+2).split(",");
	    String[] siblingIDGenders = colIdGender.substring(colIdName.indexOf("-")+2).split(",");
	    for(int i=0; i<siblings.length; i++){
	        String siblingID = siblingIDGenders[i].substring(0,siblingIDGenders[i].indexOf(":"));
	        String siblingGender = siblingIDGenders[i].substring(siblingIDGenders[i].indexOf(":")+1);
	        out.println("<tr><td>");
	        out.println((i+1)+".</td><td><input type=\"text\" name=\""+colType+"\" size=\"20\" value=\""+siblings[i]+"\" ></td>");
		    
		    out.print("<td><select size=\"1\" name=\""+colType+"ID\">");
		    makeSelectOptions(out, memberlist, siblingID);
	        out.print("</select></td>");
		    
		    out.print("<td><select size=\"1\" name=\""+colType+"Gender\">");
		    out.print("<option value=\"\" >Select..</option>");
		    out.print("<option "+ (siblingGender !=null && "MALE".equalsIgnoreCase(siblingGender)? 
		            "selected" : "") +" >Male</option>");
	        out.print("<option "+ (siblingGender!=null && "FEMALE".equalsIgnoreCase(siblingGender)? 
	                "selected" : "") +" >Female</option>");
	        out.print("</select></td>");
	        out.print("</tr>");
	    }
	    startIdx = siblings.length;
	}
    for(int i=startIdx; i<( startIdx == 0? 4 : startIdx+2) ; i++){
        out.println("<tr><td>");
	    out.println((i+1)+".</td><td><input type=\"text\" name=\""+colType+"\" size=\"20\"></td>");
	    
	    out.print("<td><select size=\"1\" name=\""+colType+"ID\">");
	    makeSelectOptions(out, memberlist, "");
        out.print("</select></td>");
	    
	    out.print("<td><select size=\"1\" name=\""+colType+"Gender\">");
	    out.print("<option value=\"\" >Select..</option>");
	    out.print("<option>Male</option>");
        out.print("<option>Female</option>");
        out.print("</select></td>");
        out.println("<tr>");
    }
}
%>
