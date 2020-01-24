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
<body onLoad="loadInit('sales')" onUnload="logOut()">
	<div id="canvas">
		<div id="info"><span>${name}(${ecode}:${lev}) ${hip }(${hdate}) ${moveJob }</span></div>
		<div id="search">
			<table id="searchT">
				<tr>
					<td><input type="text" name="empInfo" maxlength="4" class="info" placeholder="EMPOYEE CODE"/></td>
					<td><input type="text" name="empInfo" maxlength="5" class="info" placeholder="EMPOYEE NAME" /></td>
					<td><select name="empInfo" class="info">
							<option selected disabled>LEVEL</option>
							<option> MANAGER </option>
							<option>PART-TIME</option>
						</select>
					</td>
					<td rowspan="2">
						<button class="searchbtn" onClick="" onMouseOver="changeButton(this, 1, 'searchbtnover')" onMouseOut="changeButton(this, 1, 'searchbtn')">SEARCH</button>
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<button class="regbtn" onClick="ctlLightsBox('lightBoxCanvas', 'lightBoxView', 1)" onMouseOver="changeButton(this, 1, 'regbtnover')" onMouseOut="changeButton(this, 1, 'regbtn')">EMPLOYEE REGISTRATION</button>
					</td>
				</tr>
			</table>
		</div>
		<div id="message"></div>
		<div id="list">
			<table id="empList">
				<tr>
					<th>직원코드</th>
					<th>직원성명</th>
					<th>직원등급</th>
					<th>최근접속기록</th>
				</tr>
				<tr class="odd" onClick="ctlLightsBox('lightBoxCanvas', 'lightBoxView', 1)" onMouseOver="selectedRow(this, 'rgba(255, 228, 0, 0.8)')" onMouseOut="selectedRow(this, 'rgba(246, 246, 246, 0.8)')">
					<td>0000</td>
					<td>훈짱</td>
					<td>매니저</td>
					<td>2019-08-30 14:50:10</td>
				</tr>
				<tr class="even" onClick="ctlLightsBox('lightBoxCanvas', 'lightBoxView', 1)" onMouseOver="selectedRow(this, 'rgba(255, 228, 0, 0.8)')" onMouseOut="selectedRow(this, 'rgba(217, 229, 255, 0.8)')">
					<td>1111</td>
					<td>알바짱</td>
					<td>알바</td>
					<td>2019-08-30 14:50:10</td>
				</tr>
			</table>
		</div>
		<div id="menu">
			<div id="command">
				<button id="emp" class="action"  onClick="moveEmpMgr()" onMouseOver="changeButton(this, 1, 'actionOver')" onMouseOut="changeButton(this, 0, 'action')">직원관리</button>
				<button id="member" class="action" onClick="moveMemberMgr()" onMouseOver="changeButton(this, 1, 'actionOver')" onMouseOut="changeButton(this, 0, 'action')">회원관리</button>
				<button id="goods" class="action" onClick="moveGoodsMgr()" onMouseOver="changeButton(this, 1, 'actionOver')" onMouseOut="changeButton(this, 0, 'action')">상품관리</button>
				<button id="sales" class="action">판매관리</button>
			</div>
		</div>
	</div>
	<div id="lightBoxCanvas" class="canvas"></div>
	<div id="lightBoxView" class="light">
		<div onClick="ctlLightsBox('lightBoxCanvas', 'lightBoxView', 0)">CLOSE</div>
	</div>
</body>
</html>