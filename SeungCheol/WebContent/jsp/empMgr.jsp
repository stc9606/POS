<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HoonZzang :: MANAGER JOB</title>
<link href="/jsp/css/manager.css" rel="stylesheet" type="text/css">
<script src="/jsp/js/manager.js"></script>
</head>
<body onLoad="loadInit('emp', '${newEmpCode}')" onUnload="logOut()">
	<div id="canvas">
		<div id="navigation">
			<div id="infozone">
				<span>${name}(${ecode}:${lev})</span><span class="subinfo">${hip }(${hdate})</span>
				${moveJob }
			</div>
		</div>
		<div id="search">
			<table id="searchT">
				<tr>
					<td><input type="text" name="empInfo" maxlength="4"
						class="info" placeholder="EMPOYEE CODE" value=""/></td>
					<td><input type="text" name="empInfo" maxlength="5"
						class="info" placeholder="EMPOYEE NAME" value=""/></td>
					<td><select name="empLev" class="info">
							<option selected disabled value="">LEVEL</option>
							<option value="M">Manager</option>
							<option value="E">PartTime</option>
					</select></td>
					<td rowspan="2">
						<button class="searchbtn" onClick="empSearch()"
							onMouseOver="changeButton(this, 1, 'searchbtnover')"
							onMouseOut="changeButton(this, 1, 'searchbtn')">SEARCH</button>
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<button class="regbtn"
							onClick="getNewEmpCode()"
							onMouseOver="changeButton(this, 1, 'regbtnover')"
							onMouseOut="changeButton(this, 1, 'regbtn')">EMPLOYEE
							REGISTRATION</button>
					</td>
				</tr>
			</table>
		</div>
		<div id="message">${message }</div>
		<div id="list">${empList }</div>
		<div id="menu">
			<div id="command">
				<button id="emp" class="action">직원관리</button>
				<button id="member" class="action" onClick="moveMemberMgr()"
					onMouseOver="changeButton(this, 1, 'actionOver')"
					onMouseOut="changeButton(this, 0, 'action')">회원관리</button>
				<button id="goods" class="action" onClick="moveGoodsMgr()"
					onMouseOver="changeButton(this, 1, 'actionOver')"
					onMouseOut="changeButton(this, 0, 'action')">상품관리</button>
				<button id="sales" class="action" onClick="moveSalesMgr()"
					onMouseOver="changeButton(this, 1, 'actionOver')"
					onMouseOut="changeButton(this, 0, 'action')">판매통계</button>
			</div>
		</div>
	</div>
	<div id="lightBoxCanvas" class="bglight"></div>
	<div id="lightBoxView" class="light">
		<div class="titlezone">
			<div id="title" class="title">EMPLOYEE REGISTRATION</div>
		</div>
		<div class="contentszone">
			<div id="datazone" class="datazone"></div>
			<div id="submitzone" class="submitzone"></div>
		</div>
		<div class="footer">
			<div class="close" onClick="ctlLightsBox('lightBoxCanvas', 'lightBoxView', 'emp', 0, null)" onMouseOver="changeClose(this, 1)" onMouseOut="changeClose(this, 0)">CLOSE</div>
		</div>
	</div>
</body>
</html>