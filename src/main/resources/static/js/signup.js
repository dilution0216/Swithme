const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');

signInButton.addEventListener('click', () => {
    container.classList.remove("right-panel-active");
});
signUpButton.addEventListener('click', () => {
    container.classList.add("right-panel-active");
});

// 구글 로그인 시도
function redirectToGoogleLogin() {
    const clientId = "964296755360-5db3hsu4jhn7dvtsbig72f4r6316obf0.apps.googleusercontent.com";
    const redirectUri = "http://localhost:8080/api/users/google/callback";
    const scopes = encodeURIComponent("https://www.googleapis.com/auth/userinfo.profile email");

    const googleLoginUrl = `https://accounts.google.com/o/oauth2/v2/auth?client_id=${clientId}&redirect_uri=${redirectUri}&response_type=code&scope=${scopes}`;

    location.href = googleLoginUrl;
}

// URL 매개변수에서 error 값을 읽어와서 경고창을 띄우는 함수
function showAlertFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    const content = urlParams.get('content');

    if (content === 'loginFail') {
        alert('아이디 또는 비밀번호가 틀립니다.');
    }

    if (content === 'email') {
        alert('이메일 인증 메일이 발송되었습니다. 확인 후 회원가입을 완료해주세요')
    }
}

// 페이지 로드 시 showAlertFromUrl 함수 호출
window.onload = showAlertFromUrl;