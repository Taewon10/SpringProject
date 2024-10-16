// 전체 선택 또는 전체 해제
$('#all').click(function() {
    if ($(this).prop('checked')) // all 체크박스가 체크되었는지 확인
        $('input[name="check"]').prop('checked', true); // 모든 체크박스를 체크 상태로 설정
    else
        $('input[name="check"]').prop('checked', false); // 모든 체크박스를 체크 해제 상태로 설정
});

// 전체 선택 후, 하나라도 체크 해제 시 전체 선택 해제
$('input[name="check"]').click(function() {
    console.log('체크박스 개수 = ' + $('input[name="check"]').length); // 전체 체크박스 개수 출력
    console.log('선택된 개수 = ' + $('input[name="check"]:checked').length); // 선택된 체크박스 개수 출력
    $('#all').prop('checked', $('input[name="check"]').length === $('input[name="check"]:checked').length); // 전체 선택 상태 업데이트
});

// 이미지 삭제 버튼 클릭 시 AJAX 요청
$('#uploadDeleteBtn').click(function() {
    $.ajax({
        type: 'post',
        url: '/spring/user/uploadDelete', // 서버로 보낼 URL
        data: $('#uploadListForm').serialize(), // 폼 데이터를 직렬화하여 서버에 전송
        success: function() { // 성공 시 콜백 함수
            alert('이미지 삭제 완료');
            location.href = '/spring/user/uploadList'; // 목록 페이지로 리디렉션
        },
        error: function(e) { // 에러 발생 시 콜백 함수
            console.log(e); // 에러 로그 출력
        }
    }); // $.ajax 종료
});