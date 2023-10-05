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
    $.ajax({
        type: 'GET',
        url: '/chat/content',
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
    stompClient.subscribe(`/topic/public/1`, onMessageReceived);
};


function sendMessage(event) {
    if(stompClient) {
        var chatMessage = {
            sender: "guest",
            content: messageInput.value
        };
        stompClient.send("/app/allChat.sendMessage/1", {}, JSON.stringify(chatMessage));
    }
    event.preventDefault();
    document.getElementById('message').value = '';
}

messageForm.addEventListener('submit', sendMessage, true)

function onError(error) {

}

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