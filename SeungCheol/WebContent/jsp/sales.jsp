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
<body onLoad="loadInit('pos', null)" onUnload="logOut()">
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
					<td><input type="text" id="goodsCode" name="goodsCode" maxlength="13"
						class="info" placeholder="상품코드" value=""  onKeyDown="isEnter()"/></td>
					<td>
						<button class="searchbtn" onClick="getGoodsInfo()"
							onMouseOver="changeButton(this, 1, 'searchbtnover')"		
							onMouseOut="changeButton(this, 1, 'searchbtn')">SALES</button>
					</td>
				</tr>
			</table>
		</div>
		<div id="message">${message }</div>
		<div id="list">${salesList }</div>
		<div id="menu">
			<div id="command">
				<button id="returns" class="action" onClick="returnGoods()" 
					onMouseOver="changeButton(this, 1, 'actionOver')"
					onMouseOut="changeButton(this, 0, 'action')">RETURN OF GOODS</button>
				<button id="hold" class="action" onClick="holdGoods()"
					onMouseOver="changeButton(this, 1, 'actionOver')"
					onMouseOut="changeButton(this, 0, 'action')">HOLD-UP OF SALE</button>
				<button id="payment" class="action" onClick="payment()"
					onMouseOver="changeButton(this, 1, 'actionOver')"
					onMouseOut="changeButton(this, 0, 'action')">PAYMENT</button>
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