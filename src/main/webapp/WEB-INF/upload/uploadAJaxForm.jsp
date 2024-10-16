<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
table {
	border-collapse: collapse;
}

th, td {
	padding: 5px;
}
</style>
</head>
<body>
	<form id="uploadAJaxForm">
		<table border="1">
			<tr>
				<th>상품명</th>
				<td><input type="text" name="imageName" size="35"></td>
			</tr>

			<tr>
				<td colspan="2"><textarea name="imageContent" rows="10"
						cols="50"></textarea></td>
			</tr>
			
			<!-- 한번에 1개 또는 여려개(드래그)를 선택 서버에서 List로 받는다-->
			<tr>
				<td colspan="2">
				<img id="camera" src="../image/camera.png" alt="camera" width="70" height="50">
				<span id="showImageList"></span>
				<!-- <input id="img" type="file" name="img[]" multiple="multiple"></td> -->
				<input id="img" type="file" name="img[]" multiple="multiple" style="visibility:hidden"></td>
			</tr>

			<tr>
				<td colspan="2" align="center">
					<input type="button" value="이미지 업로드">
					<!-- <input type="button" value="이미지" id="uploadAJaxBtn"> -->
					<button type="button" id="uploadAJaxBtn">이미지</button>
					<input type="reset" value="취소">
			</tr>

		</table>
	</form>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.min.js"></script> 
<script type="text/javascript" src="../js/uploadAJax.js"></script>
<script type="text/javascript">
$('#camera').click(function(){
	$('#img').trigger('click'); //강제 이벤트 발생
});

$('#img').change(function(){
	$('#showImageList').empty();
	
	for(var i =0; i<this.files.length; i++){
		readURL(this.files[i]);
	}
});
function readURL(file){ //파일 미리보기 함수
	var reader = new FileReader(); //FileReader 객체생성
	var show;
	
	reader.onload = function(e){
		var img = document.createElement('img'); //img 요소를 생성해서 img 변수에 할당. 이 요소는 미리보기 이미지로 사용.
		img.src = e.target.result; //미리보기 이미지 설정
		img.width=70;
		img.height=70;
		$('#showImageList').append(img); //미리보기 이미지 추가.
	}
	
	reader.readAsDataURL(file); //파일을 데이터 URL로 읽기
}
</script>
</body>
</html>

<!-- 
FileReader 란?
FileReader는 type이 file인 input 태그 또는 API 요청과 같은 인터페이스를 통해 
File 또는 Blob 객체를 편리하게 처리할수있는 방법을 제공하는 객체이며
abort, load, error와 같은 이벤트에서 발생한 프로세스를 처리하는데 주로 사용되며,
File 또는 Blob 객체를 읽어서 result 속성에 저장한다.

FileReader도 비동기로 동작한다.

FileReader.onload()
load 이벤트의 핸들러. 이 이벤트는 읽기 동작이 성공적으로 완료되었을 때마다 발생한다.
 -->