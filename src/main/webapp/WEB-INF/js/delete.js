$(function () {
    $('#deleteBtn').click(function () {
        $('#pwdDiv').empty();

        if ($('#pwd').val() == '') {
            $('#pwdDiv').html('먼저 비밀번호를 입력하세요');
        } else {
            $.ajax({
                type: 'post',
                url: '/spring/user/getExistPwd',
                data: 'id=' + $('#id').val(),
                dataType: 'json',
                success: function (data) {
                    if (data.pwd == $('#pwd').val()) {
                        if (confirm('정말 삭제하시겠습니까?')) {
                            $.ajax({
                                type: 'post',
                                url: '/spring/user/delete',
                                data: 'id=' + $('#id').val(),
                                success: function () {
                                    alert('회원정보 삭제 완료');
                                    location.href = '/spring/user/list';
                                },
                                error: function (e) {
                                    console.log(e);
                                }
                            });
                        }
                    } else {
                        $('#pwdDiv').html('비밀번호가 틀렸습니다.');
                    }
                },
                error: function (e) {
                    console.log(e);
                }
            });
        }
    });
});