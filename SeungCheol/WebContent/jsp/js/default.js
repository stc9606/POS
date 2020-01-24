function moveMain(){
	var message = decodeURIComponent(location.search.split("=")[1]);
	if(message != "" && message != "undefined"){
		var temp;
		temp = message.split('+');
		message = "";
		for(i=0;i<temp.length;i++){
			message += temp[i]+" ";
		}
		alert(message);
	}
	var form = getForm("MoveMain", "post");
	form.submit();
}

function sendAccessInfo(mapping, method, objName1, objName2){
	var ecode = document.getElementsByName(objName1)[0];
	var acode = document.getElementsByName(objName2)[0];

	var form = getForm(mapping, method);
	form.appendChild(ecode);
	form.appendChild(acode);

	form.submit();
}

function moveManagerJob(){
	var form = getForm("ManagerView", "post");
	form.submit();
}

function moveSalesJob(){
	var form = getForm("SalesView", "post");
	form.submit();
}

function moveEmpmgr(pecode, pip){
	var form = getForm("Empmgr", "post");
	var ecode = getObject("hidden", "ecode", pecode);
	var ip = getObject("hidden", "ip", pip);

	form.appendChild(ecode);
	form.appendChild(ip);
	form.submit();
}

function moveEmpReg(light, fade){
	document.getElementById(light).style.display="block";
	document.getElementById(fade).style.display="block";
}

function moveEmpUpd(pecode, pip){
	var form = getForm("EmpUpd", "post");
	var ecode = getObject("hidden", "ecode", pecode);
	var ip = getObject("hidden", "ip", pip);

	form.appendChild(ecode);
	form.appendChild(ip);
	form.submit();
}

function moveEmpDel(pecode, pip){
	var form = getForm("EmpDel", "post");
	var ecode = getObject("hidden", "ecode", pecode);
	var ip = getObject("hidden", "ip", pip);

	form.appendChild(ecode);
	form.appendChild(ip);
	form.submit();
}

function moveMemmgr(pecode, pip){
	var form = getForm("Memmgr", "post");
	var ecode = getObject("hidden", "ecode", pecode);
	var ip = getObject("hidden", "ip", pip);

	form.appendChild(ecode);
	form.appendChild(ip);
	form.submit();
}

function moveMemReg(pecode, pip){
	var form = getForm("MemReg", "post");
	var ecode = getObject("hidden", "ecode", pecode);
	var ip = getObject("hidden", "ip", pip);

	form.appendChild(ecode);
	form.appendChild(ip);
	form.submit();
}

function moveMemUpd(pecode, pip){
	var form = getForm("MemUpd", "post");
	var ecode = getObject("hidden", "ecode", pecode);
	var ip = getObject("hidden", "ip", pip);

	form.appendChild(ecode);
	form.appendChild(ip);
	form.submit();
}

function moveMemDel(pecode, pip){
	var form = getForm("MemDel", "post");
	var ecode = getObject("hidden", "ecode", pecode);
	var ip = getObject("hidden", "ip", pip);

	form.appendChild(ecode);
	form.appendChild(ip);
	form.submit();
}
function moveGoodsmgr(pecode, pip){
	var form = getForm("Goodsmgr", "post");
	var ecode = getObject("hidden", "ecode", pecode);
	var ip = getObject("hidden", "ip", pip);

	form.appendChild(ecode);
	form.appendChild(ip);
	form.submit();
}

function moveGoodsReg(pecode, pip){
	var form = getForm("GoodsReg", "post");
	var ecode = getObject("hidden", "ecode", pecode);
	var ip = getObject("hidden", "ip", pip);

	form.appendChild(ecode);
	form.appendChild(ip);
	form.submit();
}

function moveGoodsUpd(pecode, pip){
	var form = getForm("GoodsUpd", "post");
	var ecode = getObject("hidden", "ecode", pecode);
	var ip = getObject("hidden", "ip", pip);

	form.appendChild(ecode);
	form.appendChild(ip);
	form.submit();
}

function moveGoodsDel(pecode, pip){
	var form = getForm("GoodsDel", "post");
	var ecode = getObject("hidden", "ecode", pecode);
	var ip = getObject("hidden", "ip", pip);

	form.appendChild(ecode);
	form.appendChild(ip);
	form.submit();
}
function moveSalesmgr(pecode, pip){
	var form = getForm("Salesmgr", "post");
	var ecode = getObject("hidden", "ecode", pecode);
	var ip = getObject("hidden", "ip", pip);

	form.appendChild(ecode);
	form.appendChild(ip);
	form.submit();
}

function moveSalesReg(pecode, pip){
	var form = getForm("SalesReg", "post");
	var ecode = getObject("hidden", "ecode", pecode);
	var ip = getObject("hidden", "ip", pip);

	form.appendChild(ecode);
	form.appendChild(ip);
	form.submit();
}


function moveSalesUpd(pecode, pip){
	var form = getForm("SalesUpd", "post");
	var ecode = getObject("hidden", "ecode", pecode);
	var ip = getObject("hidden", "ip", pip);

	form.appendChild(ecode);
	form.appendChild(ip);
	form.submit();
}


function moveSalesDel(pecode, pip){
	var form = getForm("SalesDel", "post");
	var ecode = getObject("hidden", "ecode", pecode);
	var ip = getObject("hidden", "ip", pip);

	form.appendChild(ecode);
	form.appendChild(ip);
	form.submit();
}



function logOut(){
	var form = getForm("LogOut", "post");
	form.submit();
}

function getForm(action, method){
	var form = document.createElement("form");
	form.setAttribute("action", action);
	form.setAttribute("method", method);
	document.body.appendChild(form);
	return form;
}

function getObject(type, name, value){
	var input = document.createElement("input");
	input.setAttribute("type", type);
	input.setAttribute("name", name);
	input.setAttribute("value", value);

	return input;
}

function changeColor(obj, name){
	obj.style.cursor = "pointer";
	obj.className = name;
}

function boxHighlight(obj){
	obj.style.border = "3px solid rgba(255, 187, 0, 1.0)";
}

function boxHighlightOff(obj){
	obj.style.border = "1px solid rgba(76, 76, 76, 1.0)";
}

function changeButton(obj, event){
	if(event == 1){
		obj.style.cursor = "pointer";
		obj.className = "btnOver";
	} else{
		obj.style.cursor = "default";
		obj.className = "btn";
	}
}

function changeSButton(obj, event){
	if(event == 1){
		obj.style.cursor = "pointer";
		obj.className = "btnSOver";
	} else{
		obj.style.cursor = "default";
		obj.className = "btnS";
	}

}
