<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HoonZzang :: Goods Managements JOB</title>
<link href="/jsp/css/manager.css" rel="stylesheet" type="text/css">
<script src="/jsp/js/manager.js"></script>
</head>
<body onLoad="loadInit('goods', null)" onUnload="logOut()">
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
					<td><input type="text" name="goodsInfo" maxlength="4" class="info" placeholder="GOODS CODE"/></td>
					<td><input type="text" name="goodsInfo" class="info" placeholder="GOODS NAME" /></td>
					<td><input type="text" name="goodsInfo" class="info" placeholder="Min Price" /></td>
					<td><input type="text" name="goodsInfo" class="info" placeholder="Max Price" /></td>
					<td rowspan="2">
						<button class="searchbtn" onClick="searchGoods()" onMouseOver="changeButton(this, 1, 'searchbtnover')" onMouseOut="changeButton(this, 1, 'searchbtn')">SEARCH</button>
					</td>
				</tr>
				<tr>
					<td colspan="4">
						<button class="regbtn" onClick="ctlLightsBox('lightBoxCanvas', 'lightBoxView', 'goods', 1, null)" onMouseOver="changeButton(this, 1, 'regbtnover')" onMouseOut="changeButton(this, 1, 'regbtn')">GOODS REGISTRATION</button>
					</td>
				</tr>
			</table>
		</div>
		<div id="message"></div>
		<div id="list">${goodsList }</div>
		<div id="menu">
			<div id="command">
				<button id="emp" class="action" onClick="moveEmpMgr()" onMouseOver="changeButton(this, 1, 'actionOver')" onMouseOut="changeButton(this, 0, 'action')">직원관리</button>
				<button id="member"class="action" onClick="moveMemberMgr()" onMouseOver="changeButton(this, 1, 'actionOver')" onMouseOut="changeButton(this, 0, 'action')">회원관리</button>
				<button id="goods"class="action">상품관리</button>
				<button id="sales"class="action" onClick="moveSalesMgr()" onMouseOver="changeButton(this, 1, 'actionOver')" onMouseOut="changeButton(this, 0, 'action')">판매통계</button>
			</div>
		</div>
	</div>
	<div id="lightBoxCanvas" class="bglight"></div>
	<div id="lightBoxView" class="light">
		<div class="titlezone">
			<div id="title" class="title">GOODS REGISTRATION</div>
		</div>
		<div class="contentszone">
			<div id="datazone" class="datazone"></div>
			<div id="submitzone" class="submitzone"></div>
		</div>
		<div class="footer">
			<div class="close" onClick="ctlLightsBox('lightBoxCanvas', 'lightBoxView', 'goods', 0, null)" onMouseOver="changeClose(this, 1)" onMouseOut="changeClose(this, 0)">CLOSE</div>
		</div>
	</div>
</body>
</html>