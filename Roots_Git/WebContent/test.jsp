<html>
<head>
<script type="text/javascript">
<!--
	function save(){
	if(document.frm.T1.value = 'AAA')
	return true;
	return false;
	}

		if(document.frm.T1.value = 'AAA'){
		alert("validate true");
		} else {
		alert("validate false");
		}
		if(document.frm.T1.value <> '' && 
				document.frm.D5.value <> ''){
			document.frm.nextAction.value='Save';
			//document.forms[0].submit();
			}
function open(){
		if(document.frm.memberID.value <> ''){
			document.frm.nextAction.value='Open';
			document.forms[0].submit();
			}
	}
function save(){
document.frm.nextAction.value='Save';
document.forms[0].submit();
}
function setAction(obj){
alert(obj.value);
document.frm.nextAction.value='Save';
document.forms[0].submit();
}
//-->
</script>
</head>
<body>
<form name="frm" action="testServlet" method="post">Name : <input
	type=text value="<%=request.getAttribute("membername") %>"
	name="membername"> Kid1 : <input type=text
	value="<%=request.getAttribute("membername") %>" name="kid">
Kid2 : <input type=text value="<%=request.getAttribute("membername") %>"
	name="kid"> Kid3 : <input type=text
	value="<%=request.getAttribute("membername") %>" name="kid">
Kid4 : <input type=text value="<%=request.getAttribute("membername") %>"
	name="kid"> <input type=hidden name="nextAction" value="">
IsAge : <input type="radio" name="R1" value="V1">Age Or
<input type="radio" name="R1" value="V2">Date of Birth : <input
	type="text" name="T14" size="20"> <input type=button
	name="Saver" value="Save" onClick="javascript:save()"> <input
	type=submit name="Submit"></form>
</body>
</html>
