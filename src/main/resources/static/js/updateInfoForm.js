let isEmailCheck = 1; //이메일 체크 여부 확인 (아이디 중복일 경우 = 0 , 중복이 아닐 경우 = 1)

//첫 화면은 기본값 1(일반 사용자 회원가입)로해서 바로 화면 보여주기
$(document).ready(function () {
    console.log("ready!");
    // 회원가입 폼 null 체크 및 아이디 중복체크 문구 나오게
    //userSignupAjax.html 안에 id = " updateInfoForm인 안에 내용들을 모두 불러와서 const updateInfoForm 변수명에 담은거다!
    const updateInfoForm = document.querySelectorAll('#updateInfoForm')
    Array.from(updateInfoForm).forEach(form => {
        form.addEventListener('submit', event => {
            // event.preventDefault()랑 event.stopPropagation() 는 회원가입 저장이 안되게 막는 기능
            if (!form.checkValidity()) { // input에 값을 안 넣었을 때
                event.preventDefault()
                event.stopPropagation()
            }
            if(isEmailCheck === 0){ // 이메일 중복 체크를 안했을 경우
                alert("이메일 중복체크를 해주세요.")
                event.preventDefault()
                event.stopPropagation()
            }
            form.classList.add('was-validated') // input창에 디자인 넣는곳 에러인지 성공인지
        }, false)
    })
})

// 이메일 중복 체크하는 function
function emailCheckAjax() { //userSignupAjax.html의 onclick="emailCheckAjax()"와 연결됨
    const email = $("#email").val();  //
    if(email !== ''){ //
        $.ajax({
            url: "/user/emailCheckAjax",  //우리가 만든 백엔드 url
            type: 'GET',//get매핑이니까 get 타입
            cache: false,
            data: {email : email}, //프론트엔드에서 백엔드로 보내주는 값(signupForm.html안에 있는 userSignupAjax.html에서 값을 보내면 UserController의 RequestParam이 받는다.)
            dataType:'json',   //return 타입을 의미한다!
            // 여기까지 api 쏠 준비 완료
            async: true,
            success: function (cnt) {
                if (cnt > 0) { // 아이디 중복
                    alert("이메일이 존재합니다. 다른 이메일을 입력해주세요.");
                    //아이디가 존재할 경우 빨깡으로 , 아니면 초록으로 처리하는 디자인
                    $("#email").addClass("is-invalid")
                    $("#email").removeClass("is-valid")
                    $("#email").attr("readonly",false)
                    $("#emailCheck").attr("disabled",false)
                    $("#email").focus();
                    isEmailCheck = 0;
                } else { // 아이디 사용 가능
                    alert("사용가능한 이메일입니다.");
                    //아이디가 존재할 경우 빨깡으로 , 아니면 초록으로 처리하는 디자인
                    $("#email").addClass("is-valid")
                    $("#email").removeClass("is-invalid")
                    $("#email").attr("readonly",true)
                    $("#emailCheck").attr("disabled",true)
                    $("#postcode").focus(); //이메일 사용 가능 시 다음 창 넘어가는거
                    isEmailCheck = 1;
                }
                setTimeout(function () {
                }, 1000)
            },
            error: function (e) {
                console.log("error : "+e)
            }
        })
    }else {
        alert("이메일을 입력하세요.");
        $("#email").focus();
    }
}


// 폰번호 유효성 체크하는 function
function phoneCheck(number){
    const regExp = /^[0-9]{11}$/; // 0-9로 이루어진 11자
    if(!regExp.test(number)){ // regExp 조건에 맞지 않으면
        $('#phone-error').show(); // 에러문구 보여줘  : login.html의 id 가 password-error인  애꺼를 보여줘라!
        $("#updateBtn").attr("disabled",true) // 버튼 비활성화
    }else { // regExp 조건에 맞으면
        $('#phone-error').hide(); // 에러문구 없애줘
        $("#updateBtn").attr("disabled",false) // 버튼 활성화
    }
}

// 이메일 유효성 체크하는 function
function emailRealCheck(email) {
    const ori_email = $('#oriEmail').val()

    if (ori_email === email) {
        $("#emailCheck").attr("disabled", true)
        isEmailCheck = 1
    } else {
        $("#emailCheck").attr("disabled", false)
        isEmailCheck = 0
    }

}

// 주소 api
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                document.getElementById("extraAddress").value = extraAddr;

            } else {
                document.getElementById("extraAddress").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('postcode').value = data.zonecode;
            document.getElementById("address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("detailAddress").focus();
        }
    }).open();
}