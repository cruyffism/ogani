$(document).ready(function () {
    // 로그인 폼 null 체크 문구 나오게
    const loginForm = document.querySelectorAll('#loginForm')
    Array.from(loginForm).forEach(form => {
        form.addEventListener('submit', event => {
            // event.preventDefault()랑 event.stopPropagation() 는 로그인 실행이 안되게 막는 기능
            if (!form.checkValidity()) { // input에 값을 안 넣었을 때
                event.preventDefault()
                event.stopPropagation()
            }
            form.classList.add('was-validated') // input창에 디자인 넣는곳 에러인지 성공인지
        }, false)
    })
})