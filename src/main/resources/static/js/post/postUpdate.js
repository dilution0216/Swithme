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
            console.log(response);
            console.log("게시글 업데이트 성공");
            window.location.href = "/view/post/detail/" + postId;
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 업데이트 실패 시 실행할 코드
            console.error("게시글 업데이트 실패");
            console.log("에러 상태 코드:", textStatus);
            console.log("에러 메시지:", errorThrown);
        }
    });
}

// 게시물 수정 버튼
// function updatePost() {
//     // var postId = /* 게시글 ID를 가져오는 코드 */;
//     var urlParts = window.location.href.split("/");
//     var postId = urlParts[urlParts.length - 1]; // 맨 마지막 부분이 게시글 ID일 것으로 가정
//     console.log(postId)
//     var title = document.getElementById("title").value;
//     var content = document.getElementById("content").value;
//     var categoryId = document.getElementById("category").value;/* 카테고리 ID를 가져오는 코드 */;
//     var imageFile = document.getElementById("image").value;
//
//     // 이미지 업로드를 위한 FormData 객체 생성
//     var imageFormData = new FormData();
//     imageFormData.append('image', imageFile);
//
//     // 업데이트할 데이터 객체를 구성
//     var postData = {
//         title: title,
//         content: content,
//         category_id: categoryId
//     };
//
//     // JSON 데이터를 문자열로 변환한 후 Blob으로 감싸기
//     var jsonDataBlob = new Blob([JSON.stringify(postData)], { type: 'application/json' });
//
//     // FormData 객체 생성하고 JSON 데이터 추가
//     var dataFormData = new FormData();
//     dataFormData.append('data', jsonDataBlob);
//
//     // 두 개의 FormData 객체를 합치기
//     var formData = new FormData();
//     formData.append('image', imageFile);
//     formData.append('data', jsonDataBlob);
//
//     console.log(dataFormData)
//     console.log(imageFormData)
//     console.log(jsonDataBlob)
//
//     // AJAX 요청 설정
//     $.ajax({
//         type: "PUT",
//         url: "/api/post/" + postId,
//         data: formData,
//         contentType: false, // Content-Type 자동 설정 비활성화
//         processData: false, // 데이터 처리 비활성화
//         success: function(response) {
//             // 업데이트 성공 시 실행할 코드
//             console.log(response)
//             console.log("게시글 업데이트 성공");
//             window.location.href = "/view/post/detail/" + postId;
//         },
//         error: function(xhr, status, error) {
//             // 업데이트 실패 시 실행할 코드
//             console.error("게시글 업데이트 실패");
//             console.log("에러 상태 코드:", status);
//             console.log("에러 메시지:", error);
//         }
//     });
// }