<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HZ-POS :: Authentication</title>
<link href="/jsp/css/default.css" rel="stylesheet" type="text/css">
<script src="/jsp/js/default.js"></script>
</head>
<body>
	<div id="canvas">
		<div id="field">
			<div class="zone">
				<div class="title">AUTHENTICATION</div>
				<div class="userdata">
					<input type="text" name="empCode" class="box"
						placeholder="${message} E-CODE" required autofocus
						onfocus="boxHighlight(this)" onblur="boxHighlightOff(this)" />
					<input type="password" name="pwdCode" class="box"
						placeholder="${message} A-CODE" required
						onfocus="boxHighlight(this)" onblur="boxHighlightOff(this)" />
				</div>
				<div id="send" class="tail" onclick="sendAccessInfo('Access', 'post', 'empCode', 'pwdCode')"
					onmouseover="changeColor(this, 'title')"
					onmouseout="changeColor(this, 'tail')">
					Click for Access
				</div>
			</div>
		</div>
	</div>
</body>
</html>