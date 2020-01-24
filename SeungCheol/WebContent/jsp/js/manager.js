/**
 *  manager.js 
 */

/* 로그아웃 */
function logOut(){
	var form = getForm("LogOut", "post");
	form.submit();
}

/* 페이지 초기화 */
function loadInit(page, code){

	if(page == "emp"){
		empInit(page,code);
	} else if(page == "goods"){
		goodsInit(page, code);
	} else if(page == "pos"){
		posInit(page);
	}

}

/* Begin :: 상품판매를 위한 추가 스크립트 */
	/* sales.jsp - LightBox 개체 초기화 */
function posInit(page){
	
	/* 결제정보 입력 양식 영역 동적 생성 */
	var div = document.getElementById("datazone");

	/* input 타입의 객체를 동적 생성하기위한 속성값 지정 */
	var type = ["text", "text","text"];
	var placeholder = ["결제 금액", "받은 돈", "거스름돈"];
	/* input 타입 객체 동적 생성 */
	var input = [];
	for(i=0; i<placeholder.length; i++){
		input[i] = getInputObject(type[i], "payment", placeholder[i], "data");
		input[i].setAttribute("onFocus", "changeBorder(this, 1)");
		input[i].setAttribute("onBlur", "changeBorder(this, 0)");
		input[i].setAttribute("onKeyPress", "isNumber()");
		div.appendChild(input[i]);
	}
	/* 결제금액 박스는 훼손되지 않게 ReadOnly 처리 */
	input[0].readOnly = true;

	/* 서버전송을 위한  button 객체 생성*/
	var submitzone = document.getElementById("submitzone");
	var buttonInfo = 
		[["del", "data", "paymentDel()", "changeButton(this, 1, 'dataOver')", "changeButton(this, 1, 'data')", "INIT"],
		 ["pay", "data", "paymentComplete()", "changeButton(this, 1, 'dataOver')", "changeButton(this, 1, 'data')", "COMPLETE"]];

	var btn = [];
	for(j=0; j<buttonInfo.length; j++){
		btn[j] = getButtonObject(buttonInfo[j]);
		submitzone.appendChild(btn[j]);
	}

}



	/* 결제 윈도우(LightBox)의 데이터 삭제*/
function paymentDel(){
	document.getElementsByName("payment")[1].value = "";
	document.getElementsByName("payment")[2].value = "";
	document.getElementsByName("payment")[1].focus();
}

	/* 결제 완료 :: 서버 전송 */
function paymentComplete(){
	var payment = document.getElementsByName("payment");
	var result = 0;
	
	result = Number(payment[1].value) - Number(payment[0].value);
	payment[2].value = result;	
}
	
	/* 결제 윈도우(LightBox) 호출  */
function payment(){
	var amount = document.getElementsByName("amount");
	var payment = document.getElementsByName("payment");
	var result = 0;
	for(i=0; i<amount.length; i++){
		result += Number(amount[i].value);
	}
	
	payment[0].setAttribute("value", result);
	ctlLightsBox('lightBoxCanvas', 'lightBoxView', 'pay', 1, null);
}

	/* 숫자만 입력 받기 */
function isNumber(){
	if(event.keyCode<48 || event.keyCode>57){
		event.returnValue=false;
	}
}
	/* 상품 검색 시 엔터키 인지 */
function isEnter(){
	if(event.keyCode == 13){getGoodsInfo();}
}

	/* 상품코드를 서버 전송 */
function getGoodsInfo(){
	var gCode = document.getElementsByName("goodsCode");
	var ea = document.getElementsByName("ea");
	
	var form = getForm("GoodsInfo", "post");
	form.appendChild(gCode[0]);
	if(ea.length>0){
		for(i = 0;i<ea.length;i++){
			form.appendChild(getObject("hidden","goodsCode", gCode[0].innerHTML));
			gCode[0].remove();
			form.appendChild(ea[0]);	
		}
	}
	
	form.submit();
}

/* End :: 상품판매를 위한 추가 스크립트 */

function salesSum(obj, index){
	var money = document.getElementsByName("money")[index].innerHTML;
	var amount = document.getElementsByName("amount")[index];
	
	amount.value = money*obj.value;
}



/* empMgr.jsp 개체 초기화 */
function empInit(page, code){
	/* 상품판매를 위한 업데이트 : 페이지 하단 관리 메뉴 중 현재 페이지 메뉴에 대한 CSS 지정 */
	var obj = document.getElementById(page);
	obj.className = "actionOver";
	obj.style.cursor="default";

	/* 직원등록을 위한 등록정보 입력 양식 동적 생성 */
	var div = document.getElementById("datazone");
	/* input 타입의 객체를 동적 생성하기위한 속성값 지정 */
	var type = ["text", "password","text"];
	var placeholder = ["EMPLOYEE CODE", "ACCESS CODE", "EMPLOYEE NAME"];
	/* input 타입 객체 동적 생성 */
	var input = [];
	for(i=0; i<placeholder.length; i++){
		input[i] = getInputObject(type[i], "emp", placeholder[i], "data");
		input[i].setAttribute("onFocus", "changeBorder(this, 1)");
		input[i].setAttribute("onBlur", "changeBorder(this, 0)");
		div.appendChild(input[i]);
	}

	/* select 타입 객체 동적 생성 */
	var select;
	var options = [["EMPLOYEE LEVEL", ""], [" Manager", "M"], ["PartTime", "E"]];
	select = getSelectObject("empLevel", "data", options);
	select.setAttribute("onFocus", "changeBorder(this, 1)");
	select.setAttribute("onBlur", "changeBorder(this, 0)");
	div.appendChild(select);

	/* 서버전송을 위한  button 객체 생성*/
	var submitzone = document.getElementById("submitzone");
	var buttonInfo = 
		[["reg", "data", "empReg('emp', 'empLevel')", "changeButton(this, 1, 'dataOver')", "changeButton(this, 1, 'data')", "REGISTRATION"], 
			["upd", "data", "empUpd('emp', 'empLevel')", "changeButton(this, 1, 'dataOver')", "changeButton(this, 1, 'data')", "CORRECTION"], 
			["del", "data", "empDel('emp', 'empLevel')", "changeButton(this, 1, 'dataOver')", "changeButton(this, 1, 'data')", "ELIMINATION"]];

	var btn = [];
	for(j=0; j<buttonInfo.length; j++){
		btn[j] = getButtonObject(buttonInfo[j]);
		submitzone.appendChild(btn[j]);
	}

	if(code != null && code != ""){
		input[0].value = code;
		input[0].setAttribute("readOnly", true);
		btn[1].setAttribute("disabled", true);
		btn[2].setAttribute("disabled", true);
		ctlLightsBox('lightBoxCanvas', 'lightBoxView', 'emp', 1, null);
	}
}


/* goodsMgr.jsp 개체 초기화 */
function goodsInit(page, code){
	/* 상품판매를 위한 업데이트 : 페이지 하단 관리 메뉴 중 현재 페이지 메뉴에 대한 CSS 지정 */
	var obj = document.getElementById(page);
	obj.className = "actionOver";
	obj.style.cursor="default";

	/* 상품등록을 위한 등록정보 입력 양식 동적 생성 */
	var div = document.getElementById("datazone");
	/* input 타입의 객체를 동적 생성하기위한 속성값 지정 */
	var type = ["text", "text","text", "text"];
	var placeholder = ["GOODS CODE", "GOODS NAME", "GOODS PRICE", "GOODS STOCK"];
	/* input 타입 객체 동적 생성 */
	var input = [];
	for(i=0; i<placeholder.length; i++){
		input[i] = getInputObject(type[i], "goods", placeholder[i], "data");
		input[i].setAttribute("onFocus", "changeBorder(this, 1)");
		input[i].setAttribute("onBlur", "changeBorder(this, 0)");
		div.appendChild(input[i]);
	}


	/* 서버전송을 위한  button 객체 생성*/
	var submitzone = document.getElementById("submitzone");
	var buttonInfo = 
		[["reg", "data", "goodsReg('goods')", "changeButton(this, 1, 'dataOver')", "changeButton(this, 1, 'data')", "REGISTRATION"], 
			["upd", "data", "goodsUpd('goods')", "changeButton(this, 1, 'dataOver')", "changeButton(this, 1, 'data')", "CORRECTION"], 
			["del", "data", "goodsDel('goods')", "changeButton(this, 1, 'dataOver')", "changeButton(this, 1, 'data')", "ELIMINATION"]];

	var btn = [];
	for(j=0; j<buttonInfo.length; j++){
		btn[j] = getButtonObject(buttonInfo[j]);
		submitzone.appendChild(btn[j]);
	}

	btn[1].setAttribute("disabled", true);
	btn[2].setAttribute("disabled", true);

}

/**********수정한부분***********************************************************/

/* REGISTRATION : 상품등록 */
function goodsReg(obj1){
	/* HTML Object 가져오기 */
	var info1 = document.getElementsByName(obj1);

	/* HTML Object 값 유효성 검사 */
	if(!isWordRange(info1[0].value, 1, 4)){	info1[0].value = ""; info1[0].focus(); return;}
	if(!isWordRange(info1[1].value, 1, 5)){	info1[1].value = ""; info1[1].focus(); return;}
	if(!isWordRange(info1[2].value, 1, 5)){	info1[2].value = ""; info1[2].focus(); return;}
	if(!isWordRange(info1[3].value, 1, 3)){	info1[3].value = ""; info1[3].focus(); return;}

	/* 서버로 전송하기 위해 폼객체 생성 후 HTML Object를 자식노드에 추가 */
	var form = getForm("MoveGoodsMgr", "post");
	for(i=0; i<info1.length; i++){
		form.appendChild(info1[0]);
	}
	var obj = getInputObject("hidden", "JobCode", "", "");
	obj.setAttribute("value", "RG");
	form.appendChild(obj);
	form.submit();
}

/* CORRECTION   : 상품정보수정 */
function goodsUpd(obj1){
	/* HTML Object 가져오기 */
	var info1 = document.getElementsByName(obj1);

	/* HTML Object 값 유효성 검사 */
	if(!isWordRange(info1[0].value, 1, 4)){	info1[0].value = ""; info1[0].focus(); return;}
	if(!isWordRange(info1[1].value, 1, 5)){	info1[1].value = ""; info1[1].focus(); return;}
	if(!isWordRange(info1[2].value, 1, 5)){	info1[2].value = ""; info1[2].focus(); return;}
	if(!isWordRange(info1[3].value, 1, 3)){	info1[3].value = ""; info1[3].focus(); return;}

	/* 서버로 전송하기 위해 폼객체 생성 후 HTML Object를 자식노드에 추가 */
	var form = getForm("MoveGoodsMgr", "post");
	for(i=0; i<info1.length; i++){
		form.appendChild(info1[0]);
	}
	var obj = getInputObject("hidden", "JobCode", "", "");
	obj.setAttribute("value", "UG");
	form.appendChild(obj);
	form.submit();
}

/* ELIMINATION  : 상품삭제 */
function goodsDel(obj1){
	/* HTML Object 가져오기 */
	var info1 = document.getElementsByName(obj1);

	/* 서버로 전송하기 위해 폼객체 생성 후 HTML Object를 자식노드에 추가 */
	var form = getForm("MoveGoodsMgr", "post");
	for(i=0; i<info1.length; i++){
		form.appendChild(info1[0]);
	}
	var obj = getInputObject("hidden", "JobCode", "", "");
	obj.setAttribute("value", "DG");
	form.appendChild(obj);
	form.submit();

}
/**************수정한부분*******************************************************/

/* REGISTRATION : 직원등록 */
function empReg(obj1, obj2){

	/* HTML Object 가져오기 */
	var info1 = document.getElementsByName(obj1);
	var info2 = document.getElementsByName(obj2);

	/* HTML Object 값 유효성 검사 */
	if(!isWordRange(info1[1].value, 1, 4)){	info1[1].value = ""; info1[1].focus(); return;}
	if(!isWordRange(info1[2].value, 1, 5)){	info1[2].value = ""; info1[2].focus(); return;}
	if(info2[0].options[info2[0].selectedIndex].value == ""){alert("EMPLOYEE LEVEL을 선택해 주세요"); return;}

	/* 서버로 전송하기 위해 폼객체 생성 후 HTML Object를 자식노드에 추가 */
	var form = getForm("EmpReg", "post");
	for(i=0; i<info1.length; i++){
		form.appendChild(info1[0]);
	}
	form.appendChild(info2[0]);

	form.submit();
}

function getNewEmpCode(){
	var form = getForm("NewEmpCode", "post");
	form.submit();
}

/* CORRECTION   : 직원정보수정 */
function empUpd(obj1, obj2){
	/* HTML Object 가져오기 */
	var info1 = document.getElementsByName(obj1);
	var info2 = document.getElementsByName(obj2);

	/* HTML Object 값 유효성 검사 */
	if(!isWordRange(info1[1].value, 1, 4)){	info1[1].value = ""; info1[1].focus(); return;}
	if(!isWordRange(info1[2].value, 1, 5)){	info1[2].value = ""; info1[2].focus(); return;}
	if(info2[0].options[info2[0].selectedIndex].value == ""){alert("EMPLOYEE LEVEL을 선택해 주세요"); return;}

	/* 서버로 전송하기 위해 폼객체 생성 후 HTML Object를 자식노드에 추가 */
	var form = getForm("EmpUpd", "post");
	for(i=0; i<info1.length; i++){
		form.appendChild(info1[0]);
	}
	form.appendChild(info2[0]);

	form.submit();
}

/* ELIMINATION  : 직원삭제 */
function empDel(obj1, obj2){
	/* HTML Object 가져오기 */
	var info1 = document.getElementsByName(obj1);
	var info2 = document.getElementsByName(obj2);

	/* 서버로 전송하기 위해 폼객체 생성 후 HTML Object를 자식노드에 추가 */
	var form = getForm("EmpDel", "post");
	for(i=0; i<info1.length; i++){
		form.appendChild(info1[0]);
	}
	form.appendChild(info2[0]);

	form.submit();

}

/* 관리화면 이동 */
function moveManagerJob(){
	var form = getForm("ManagerView", "post");
	form.submit();
}

/* 판매화면 : sales.jsp 호출*/
function moveSalesJob(){
	var form = getForm("SalesView", "post");
	form.submit();
}

/* 특정 직원 검색 */
function empSearch(){
	var empInfo = document.getElementsByName("empInfo");
	var empLev =  document.getElementsByName("empLev");

	var check = true;
	for(i=0; i<empInfo.length;i++){
		if(empInfo[i].value != ""){
			check = false;
			break;
		}
	}

	if(empLev[0].value != ""){check = false;}

	if(check){
		moveManagerJob();
	}else{
		if(isWordRange(empInfo[0].value, 0, 0) || isWordRange(empInfo[0].value, 4, 4)){
			var form = getForm( 'EmpSearch', 'post');

			for(i=0; i<empInfo.length;i++){
				form.appendChild(empInfo[0]);
			}

			form.appendChild(empLev[0]);
			form.submit();
		}else{
			empInfo[0].value = "";
			empInfo[0].focus();
			return;
		}
	}
}

/* 검색어에 대한 문자 길이 유효성 검사 */
function isWordRange(data, min, max){
	var result = false;
	if(data.length >= min && data.length <= max){
		result = true;
	}
	return result;
}

/* Dynamic Form Creation */
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

/* Dynamic Html Input Object Creation */
function getInputObject(type, name, placeholder, className){
	var input = document.createElement("input");
	input.setAttribute("type", type);
	input.setAttribute("name", name);
	input.setAttribute("placeholder", placeholder);
	input.setAttribute("class", className);


	return input;
}

/* Dynamic Html Select Object Creation */
function getSelectObject(name, className, options){
	var select = document.createElement("select");
	var option = [];

	select.setAttribute("name", name); 
	select.setAttribute("class", className);

	for(i=0; i<options.length; i++){
		option[i] = new Option(options[i][0], options[i][1]);
		option[i].setAttribute("class", "data");
		if(i==0){
			option[i].setAttribute("selected", true);
			option[i].setAttribute("disabled", true);
		}
		select.appendChild(option[i]);
	}

	return select;
}

/* Dynamic Html Button Object Creation */
function getButtonObject(btnInfo){
	var button = document.createElement("button");
	var attName = ["id", "class", "onClick", "onMouseOver", "onMouseOut", "innerHTML"];

	for(i=0; i<attName.length-1; i++){
		button.setAttribute(attName[i], btnInfo[i]);
	}
	button.innerHTML = btnInfo[btnInfo.length-1];

	return button;
}

/* 페이지 이동 */
function moveEmpMgr(){

}

function moveMemberMgr(){

}

function moveGoodsMgr(){
	/* form 생성과 전송 */
	var form = getForm( "MoveGoodsMgr", "post");
	var obj = getInputObject("hidden", "JobCode", "", "");
	obj.setAttribute("value", "AG");
	form.appendChild(obj);
	form.submit();
}

function searchGoods(){
	/* HTML 개체 가져오기 */
	var goodsInfo = document.getElementsByName("goodsInfo");

	/* 사용자 입력 데이터 존재여부 확인 */
	var check = false;
	for(i=0; i<goodsInfo.length; i++){
		if(goodsInfo[i].value != ""){
			check = true;
			break;
		}
	}

	/* 전체 상품 검색 or 조건 검색 선택*/
	if(!check){	moveGoodsMgr();	}

	var form = getForm( "MoveGoodsMgr", "post");
	var obj = getInputObject("hidden", "JobCode", "", "");
	obj.setAttribute("value", "SG");	
	form.appendChild(obj);

	for(i=0; i<goodsInfo.length; i++){
		form.appendChild(goodsInfo[0]);
	}

	form.submit();
}


/* List Table Point */
function selectedRow(row, colorCode){
	row.style.backgroundColor = colorCode; 
}

/* BUTTON EVENT */
function changeButton(obj, event, value){
	if(event == 1){
		obj.className = value;
	} else{
		obj.className = value;
	}
}

/* LIGHT BOX */
function ctlLightsBox(canvasName, viewName, job, action, info){
	var canvas = document.getElementById(canvasName);
	var view = document.getElementById(viewName);

	if(info != null){
		document.getElementById("upd").disabled = false;
		document.getElementById("del").disabled = false;
	}

	if(job == "emp"){
		if(action == 1){
			canvas.style.display = "block";
			view.style.display = "block";
			if(info != null){
				isEmpReadOnly(info, true);
			}
		} else{
			canvas.style.display = "none";
			view.style.display = "none";
			isEmpReadOnly(null, false)
		}
	}else if(job == "goods"){
		if(action == 1){
			canvas.style.display = "block";
			view.style.display = "block";
			goodsObjectControl(info, true);
		} else{
			canvas.style.display = "none";
			view.style.display = "none";
			goodsObjectControl(null, false)
		}
	}else if(job == "pay"){
		if(action == 1){
			canvas.style.display = "block";
			view.style.display = "block";
			
		} else{
			canvas.style.display = "none";
			view.style.display = "none";
			
		}
	}
}

function isEmpReadOnly(empinfo, check){
	var emp = document.getElementsByName("emp");
	var empLevel = document.getElementsByName("empLevel");
	var reg = document.getElementById("reg");

	reg.setAttribute("disabled", check);

	if(check){
		var j = 0;
		emp[0].setAttribute("readOnly", check);

		for(i=0; i<empinfo.length-1; i++){
			j = (i==1)? i+1: i;
			emp[j].value = empinfo[i];
		}
		empLevel[0].selectedIndex = empinfo[empinfo.length-1];
	}else{
		for(i=0; i<emp.length; i++){
			emp[i].value = "";
		}
		empLevel[0].selectedIndex = 0;
	}
}

/* 상품수정 및 삭제 관련 HTML 개체 제어 */
function goodsObjectControl(info, check){
	var reg = document.getElementById("reg");
	var upd = document.getElementById("upd");
	var del = document.getElementById("del");
	var obj = document.getElementsByName("goods");

	if(info != null){	
		for(i=0; i< info.length; i++){
			obj[i].value = info[i];
		}
		obj[0].readOnly = check;
		reg.disabled = check;
		upd.disabled = !check;
		del.disabled = !check;
	}else{
		for(i=0; i< obj.length; i++){
			obj[i].value = "";
		}
		obj[0].readOnly = !check;
		reg.disabled = !check;
		upd.disabled = check;
		del.disabled = check;
	}
}

/* LIGHT BOX : CLOSE */
function changeClose(obj, value){
	if(value == 1){
		obj.style.backgroundColor = "rgba(255, 228, 0, 1.0)";
		obj.style.color = "rgba(76, 76, 76, 1.0)";
	}else{
		obj.style.backgroundColor = "";
		obj.style.color = "rgba(255, 228, 0, 1.0)";
	}
}

/* LIGHT BOX : 폼요소 테두리 */
function changeBorder(obj, value){
	if(value == 1){
		obj.style.borderColor = "rgba(255, 0, 221, 1.0)";
	}else{
		obj.style.borderColor = "rgba(255, 228, 0, 1.0)";
	}	
}
