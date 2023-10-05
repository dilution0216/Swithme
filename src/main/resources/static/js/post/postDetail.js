// URL을 "/"로 분할하여 배열로 저장합니다.
let currentURL = window.location.href;
let urlParts = currentURL.split("/");

// 배열에서 마지막 요소를 가져옵니다.
let lastPart = urlParts[urlParts.length - 1];

//  JavaScript에서 변수 가져오기
var currentNickname = document.getElementById('currentNickname').textContent;
// 변수 사용 예제

// 삭제 버튼 클릭 시 게시물 삭제
document.getElementById("deleteButton").addEventListener("click", function() {
    let postId = this.getAttribute("data-postid");

    fetch("/api/post/" + postId, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then(data => { // 서버의 응답 데이터 처리
            // 여기서 필요한 동작 수행 (예: 페이지 새로고침)
            alert(data.message);
            window.location.href = "/view/posts";
        })
        .catch(error => {
        });
});

// 댓글, 대댓글 관련 JS
$(document).ready(function () {

    // 댓글 작성
    $("#commentSubmitBtn").click(function () {
        const commentText = $("#commentText").val();
        if (commentText.trim() === "") {
            alert("댓글 내용을 입력하세요.");
            return;
        }

        const requestData = {
            content: commentText,
        };

        fetch(`/api/post/comment/` + lastPart, {
        // fetch(`/api/post/comment/${postId}` + lastPart, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestData),
        })
            .then((response) => response.json())
            .then((data) => {
                alert("댓글이 작성되었습니다.");
                $("#commentText").val("");
                loadComments(lastPart);
            })
            .catch((error) => {
                alert("댓글 작성 중 오류가 발생했습니다.");
            });
    });

    // 대댓글 작성 버튼 클릭
    $(document).on("click", ".replyBtn", function () {
        const commentId = $(this).data("comment");
        const replyText = prompt("대댓글 내용을 입력하세요.");
        if (replyText === null || replyText.trim() === "") {
            return;
        }

        const requestData = {
            content: replyText,
        };

        fetch(`/api/post/comment/${commentId}/reply`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestData),
        })
            .then((response) => response.json())
            .then((data) => {
                alert("대댓글이 작성되었습니다.");
                loadReplies(commentId);
            })
            .catch((error) => {
                alert("대댓글 작성 중 오류가 발생했습니다.");
            });
    });

    // 댓글 조회
    function loadComments(lastPart) {
        fetch(`/api/post/comment/`+ lastPart)
            .then((response) => response.json())
            .then((comments) => {
                displayComments(comments);
                loadInitialReplies();
            })
            .catch((error) => {
                alert("댓글을 불러오는 중 오류가 발생했습니다.");
            });
    }

    // 대댓글 조회
    function loadReplies(commentId) {
        fetch(`/api/post/comment/reply/${commentId}`)
            .then((response) => response.json())
            .then((replies) => {
                displayReplies(commentId, replies);
            })
            .catch((error) => {
                alert("대댓글을 불러오는 중 오류가 발생했습니다.");
            });
    }

    // 댓글을 화면에 표시하는 함수
    function displayComments(comments) {

        const commentList = $("#commentList");
        commentList.html(""); // 댓글 목록 초기화
        comments.forEach(function (comment) {

            const commentElement = document.createElement("div");
            commentElement.id = `comment-${comment.id}`;

            if (currentNickname == comment.userNickname) {
                commentElement.innerHTML = `
                <hr>
                <p>${comment.userNickname}</p>
                <p>${comment.content}</p>
                <button class="replyBtn" data-comment="${comment.id}">대댓글 작성</button>
                <button class="edit-comment" data-comment="${comment.id}">댓글 수정</button>
                <button class="delete-comment" data-comment="${comment.id}">댓글 삭제</button>
                <div id="replyList-${comment.id}" style="margin-left: 5%;">
                    <!-- 대댓글이 여기에 동적으로 추가됩니다 -->
                </div>
            `;
            } else {
                commentElement.innerHTML = `
                <hr>
                <p>${comment.userNickname}</p>
                <p>${comment.content}</p>
                <button class="replyBtn" data-comment="${comment.id}">대댓글 작성</button>
                <div id="replyList-${comment.id}" style="margin-left: 5%;">
                    <!-- 대댓글이 여기에 동적으로 추가됩니다 -->
                </div>
            `;
            }

            commentList.append(commentElement);
        });
    }

    // 대댓글을 화면에 표시하는 함수
    function displayReplies(commentId, replies) {
        if (replies==null) {
            return;
        }
        const replyList = $(`#replyList-${commentId}`);
        replyList.html(""); // 대댓글 목록 초기화

        replies.forEach(function (reply) {
            const replyElement = document.createElement("div");

            if (currentNickname == reply.userNickname) {
                replyElement.innerHTML = `
                <hr>
                <p>${reply.userNickname}</p>
                <p id="reply-${reply.id}">${reply.content} </p>
                <button class="edit-reply" data-reply="${reply.id}">대댓글 수정</button>
                <button class="delete-reply" data-reply="${reply.id}" data-comment="${commentId}">대댓글 삭제</button>
            `;
            } else {
            replyElement.innerHTML = `
                <hr>
                <p>${reply.userNickname}</p>
                <p id="reply-${reply.id}">${reply.content} </p>
            `;
            }
            replyList.append(replyElement);
        });
    }
    // 대댓글 수정만 하는 방법
    function updateReply(reply) {
        const replyContent = $(`#reply-${reply.id}`);
        replyContent.html(reply.content);
    }

    // 댓글 수정
    $(document).on("click", ".edit-comment", function () {
        const commentId = $(this).data("comment");
        const editedText = prompt("수정된 댓글 내용을 입력하세요.");

        if (editedText === null || editedText.trim() === "") {
            return;
        }

        const requestData = {
            content: editedText,
        };

        fetch(`/api/post/comment/${commentId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestData),
        })
            .then((response) => response.json())
            .then((data) => {
                alert("댓글이 수정되었습니다.");
                loadComments(lastPart);
            })
            .catch((error) => {
                alert("댓글 수정 중 오류가 발생했습니다.");
            });
    });

    // 대댓글 수정
    $(document).on("click", ".edit-reply", function () {
        const replyId = $(this).data("reply");
        const editedText = prompt("수정된 대댓글 내용을 입력하세요.");

        if (editedText === null || editedText.trim() === "") {
            return;
        }

        const requestData = {
            content: editedText,
        };

        fetch(`/api/post/comment/reply/${replyId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestData),
        })
            .then((response) => response.json())
            .then((data) => {
                alert("대댓글이 수정되었습니다.");
                const commentId = $(this).data("comment"); // 댓글 ID 가져오기
                const reply = {
                    id:replyId,
                    content:editedText
                }
                updateReply(reply)
            })
            .catch((error) => {
                alert("대댓글 수정 중 오류가 발생했습니다.");
            });
    });

    // 댓글 삭제
    $(document).on("click", ".delete-comment", function () {
        const commentId = $(this).data("comment");

        if (!confirm("댓글을 삭제하시겠습니까?")) {
            return;
        }

        fetch(`/api/post/comment/${commentId}`, {
            method: "DELETE",
        })
            .then((response) => {
                if (response.ok) {
                    alert("댓글이 삭제되었습니다.");
                    loadComments(lastPart);
                } else {
                    throw new Error("댓글 삭제 중 오류가 발생했습니다.");
                }
            })
            .catch((error) => {
                alert("댓글 삭제 중 오류가 발생했습니다.");
            });
    });

    // 대댓글 삭제
    $(document).on("click", ".delete-reply", function () {
        const replyId = $(this).data("reply");

        if (!confirm("대댓글을 삭제하시겠습니까?")) {
            return;
        }

        fetch(`/api/post/comment/reply/${replyId}`, {
            method: "DELETE",
        })
            .then((response) => {
                if (response.ok) {
                    alert("대댓글이 삭제되었습니다.");
                    const commentId = $(this).data("comment"); // 댓글 ID 가져오기
                    loadReplies(commentId);
                } else {
                    throw new Error("대댓글 삭제 중 오류가 발생했습니다.");
                }
            })
            .catch((error) => {
                alert("대댓글 삭제 중 오류가 발생했습니다.");
            });
    });

    // 초기 댓글 로드
    loadComments(lastPart);

    // 페이지 로드 시 대댓글 불러오기
    loadInitialReplies();

    // 페이지 로드 시 대댓글 불러오기 함수
    function loadInitialReplies() {
        const commentButtons = $(".replyBtn");
        commentButtons.each(function () {
            const commentId = $(this).data("comment");
            loadReplies(commentId);
        });
    }
});
// 맨 위로 보내기 스크롤 버튼
$(function(){
    $('#back-to-top').on('click',function(e){
        e.preventDefault();
        $('html,body').animate({scrollTop:0});
    });

    $(window).scroll(function() {
        if ($(document).scrollTop() > 100) {
            $('#back-to-top').addClass('show');
        } else {
            $('#back-to-top').removeClass('show');
        }
    });
});
