// 채팅방 생성 자바 스크립트
function create_chat() {
    $.ajax({
        method: 'POST',
        url: '/chat/personal/room',
        contentType: "application/json",
        dataType: "text",
        success: function (data) {
            document.location.reload();
        },
        error: function (xhr, status, error) {
            // 에러 시 처리할 코드
            console.error(error);
        }
    });
}

// 채팅 연결 관련 코드
const urlParams = new URLSearchParams(window.location.search);
const url = urlParams.get("url");

var stompClient = null;

var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');

window.onload = function connect() {
    receiveMessage()
    var socket = new SockJS('/swithme');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
}

// 저장된 메시지를 출력
function receiveMessage() {
    let data = {'chatUrl': url};

    $.ajax({
        method: 'POST',
        url: '/chat/group/content',
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                let sender = response[i]['sender'];
                let content = response[i]['content'];

                receiveMessageHtml(sender, content);
            }
        }
    });
}

function receiveMessageHtml(sender, content) {
    let html = `
                <div class="message-box">
                    <div class="message-content">
                        <div class="message-header">
                            <div class="name">${sender}</div>
                        </div>
                        <p class="message-line">
                            ${content}
                        </p>
                    </div>
                </div>`;
    $('#chat-container').append(html);
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe(`/topic/public/${url}`, onMessageReceived);
};


// 채팅 전달
function sendMessage(event) {
    if (stompClient) {
        var chatMessage = {
            sender: "guest",
            content: messageInput.value
        };
        stompClient.send(`/app/chat.sendMessage/${url}`, {}, JSON.stringify(chatMessage));
    }
    event.preventDefault();
    document.getElementById('message').value = '';
}

messageForm.addEventListener('submit', sendMessage, true)

function onError(error) {
}

//메시지 수신
function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    var sender = message.sender;
    var content = message.content;

    // 새로운 메시지를 생성하고 내용을 설정합니다.
    var newMessage = document.createElement("div");
    newMessage.className = "message-box";

    var messageContent = document.createElement("div");
    messageContent.className = "message-content";

    var messageHeader = document.createElement("div");
    messageHeader.className = "message-header";

    var name = document.createElement("div");
    name.className = "name";
    name.textContent = sender;
    messageHeader.appendChild(name);

    var messageLine = document.createElement("p");
    messageLine.className = "message-line";
    messageLine.textContent = content;

    messageContent.appendChild(messageHeader);
    messageContent.appendChild(messageLine);
    newMessage.appendChild(messageContent);

    // 기존의 메시지 컨테이너에 새로운 메시지를 추가합니다.
    var messagesContainer = document.querySelector(".messages");
    messagesContainer.appendChild(newMessage);

    const chatContainer = document.getElementById('chat-container');
    chatContainer.scrollTop = chatContainer.scrollHeight;
}

// 채팅방 접속 메시지
function changeChat() {
    alert("채팅방에 접속합니다.")
}

// 팝업 창을 열기 위한 JavaScript 함수
function openPopup(link) {
    // 클래스로 엘리먼트를 선택하고 href 속성을 사용하여 팝업 창을 엽니다.
    window.open(link.getAttribute('href'), '팝업창이름', 'width=500,height=700,top=300,left=1300');
}


// 클래스가 "popup-link"인 모든 "a" 태그를 찾아 이벤트 리스너를 추가합니다.
var popupLinks = document.querySelectorAll('.popup-link');
popupLinks.forEach(function(link) {
    link.addEventListener('click', function (e) {
        e.preventDefault(); // 기본 동작 방지
        openPopup(link);
    });
});