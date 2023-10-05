document.addEventListener('DOMContentLoaded', function () {
    var modeSwitch = document.querySelector('.mode-switch');

    modeSwitch.addEventListener('click', function () {
        document.documentElement.classList.toggle('dark');
        modeSwitch.classList.toggle('active');
    });

    var listView = document.querySelector('.list-view');
    var gridView = document.querySelector('.grid-view');
    var projectsList = document.querySelector('.project-boxes');

    listView.addEventListener('click', function () {
        gridView.classList.remove('active');
        listView.classList.add('active');
        projectsList.classList.remove('jsGridView');
        projectsList.classList.add('jsListView');
    });

    gridView.addEventListener('click', function () {
        gridView.classList.add('active');
        listView.classList.remove('active');
        projectsList.classList.remove('jsListView');
        projectsList.classList.add('jsGridView');
    });
});

$(document).ready(function () {
    showPage();
});

function showPage() {
//게시글 출력 페이지
    setPosts();

//User 식별 & 포맷 설정
// setUserType();
}

function setPosts() {
    $.ajax({
        type: 'GET',
        url: '/api/posts',
        success: function (response) {
            $('#post-cards').empty();
            for (let i = 0; i < response.length; i++) {
                let post_title = response[i]['title'];
                let post_nickname = response[i]['userNickname'];
                let post_date = response[i]['modifiedAt']
                let post_id = response[i]['id'];
                let category_name = response[i]['category_name']

                setHtml(post_title, post_id, post_nickname, post_date, category_name);
            }
        }
    });
}

function setHtml(post_title, post_id, post_nickname, post_date, category_name) {
    let html = `
<div class="project-box-wrapper" onclick="location.href='/view/post/detail/' + ${post_id}">
<div class="project-box" style="background-color: #e9e7fd;">
                        <div class="project-box-header">
                            <span>${post_date}</span>
                            <div class="more-wrapper">
                                <button class="project-btn-more">
                                </button>
                            </div>
                        </div>
        <p class="box-content-header">${post_title}</p>
        <div class="project-box-footer">
        <div class="box-content-subheader">${post_nickname}</div> <div class="days-left" style="color: #4f3ff0;">${category_name}</div>
        <span hidden="hidden">${post_id}</span>
        </div>
</div>
</div>`;
    $('#post-cards').append(html);
}

function setCategory(category_id) {
// 카테고리별 조회
    $.ajax({
        type: 'GET',
        url: `/api/posts/category/${category_id}`,
        success: function (response) {
            $('#post-cards').empty();
            let data = response['data']
            for (let i = 0; i < data.length; i++) {
                let post = data[i]
                let post_title = post['title'];
                let post_id = post['id'];
                let post_nickname = post['userNickname'];
                let category_name = post['category_name']
                let post_date = post['modifiedAt']
                setHtml(post_title, post_id, post_nickname, post_date, category_name);
            }
        }
    });
}
