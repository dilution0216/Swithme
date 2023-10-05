document.addEventListener('DOMContentLoaded', function () {
    var modeSwitch = document.querySelector('.mode-switch');

    modeSwitch.addEventListener('click', function () {
        document.documentElement.classList.toggle('dark');
        modeSwitch.classList.toggle('active');
    });

// 카테고리 불러오기
    $.ajax({
        type: 'GET',
        url: '/api/categories',
        success: function(response) {
            let form_category = $('#category');
            form_category.empty();
            let data = response['data'];
            for(let i=0;i<data.length;i++) {
                let category = data[i]
                let category_id = category['id'];
                let name = category['name'];
                let option_value = i+1;
                form_category.append(`<option value=${option_value}>${name}</option>`);
            }
        }
    })
});

function updatePost() {
    var urlParts = window.location.href.split("/");
    var postId = urlParts[urlParts.length - 1]; // 맨 마지막 부분이 게시글 ID일 것으로 가정

    var title = document.getElementById("title").value;
    var content = document.getElementById("content").value;
    var categoryId = document.getElementById("category").value; // 카테고리 ID를 가져오는 코드
    var imageFile = document.getElementById("image").files[0]; // 파일 업로드

    // 파일 크기 확인 (1MB 이상인 경우 처리)
    if (imageFile && imageFile.size > 1048576) { // 1MB 이상인 경우
        alert("파일 크기가 너무 큽니다. 1MB 보다 작은 파일을 선택해주세요.");
        return; // 업로드 중단
    }

    // AWS S3 업로드
    // (AWS SDK를 사용하여 이미지를 S3로 업로드하고 이미지의 S3 URL을 얻는 로직 필요)

    // 이미지 업로드를 위한 FormData 객체 생성
    const imageFormData = new FormData();
    imageFormData.append('image', imageFile);

    // 업데이트할 데이터 객체를 구성
    var postData = {
        title: title,
        content: content,
        category_id: categoryId,
    };

    // JSON 데이터를 문자열로 변환한 후 Blob으로 감싸기
    var jsonDataBlob = new Blob([JSON.stringify(postData)], { type: 'application/json' });

    // FormData 객체 생성하고 JSON 데이터 추가
    var dataFormData = new FormData();
    dataFormData.append('data', jsonDataBlob);

    // 두 개의 FormData 객체를 합치기
    const formData = new FormData();
    formData.append('image', imageFile);
    formData.append('data', jsonDataBlob);

    // AJAX 요청 설정
    $.ajax({
        type: "PUT",
        url: "/api/post/" + postId,
        data: formData,
        contentType: false, // Content-Type 자동 설정 비활성화
        processData: false, // 데이터 처리 비활성화
        // dataType: 'json', // 서버에서 JSON 데이터를 반환하는 경우
        success: function(response) {
            // 업데이트 성공 시 실행할 코드
            alert(response.message);
            window.location.href = "/view/post/detail/" + postId;
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 업데이트 실패 시 실행할 코드
            alert("게시글 수정이 실패했습니다.");
        }
    });
}