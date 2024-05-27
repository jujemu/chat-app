let chatRoomId = null;

function chatPage() {
    document.querySelector("#chat-main").textContent += " (" + usernameGlobal + ")";
    getFriendsList();
    getFriendsRequest();
    getFriendRequestListConnect();
}

function getChatRoomId(event) {
    let friendName = "";
    event.currentTarget.childNodes.forEach(node => {
        if (node.nodeType === Node.TEXT_NODE) friendName += node.textContent;
    })
    const url = "/chat/rooms?";
    let params = new URLSearchParams({
        myName: usernameGlobal,
        friendName: friendName
    }).toString();
    return fetch(url + params, {
        headers: {
            'Authorization': "Basic " + getAuth(usernameGlobal, passwordGlobal),
            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
        }
    })
        .then(response => response.text())
        .then(data => {
            chatRoomId = data;
            connectChatRoom(data)
        })
        .catch(error => {
            alert("error getting chatroom, " + error);
        })
}

function connectChatRoom(chatRoomId) {
    if (chatRoomId === null) {
        alert("No chatroom found.");
        return;
    }

    stomp(chatRoomId);
    setTimeout(() => showChats(chatRoomId), 100);
}

function showChats(chatRoomId) {
    const chatContainer = document.querySelector("#chat");
    while (chatContainer.firstChild)
        chatContainer.removeChild(chatContainer.firstChild);

    getChats(chatRoomId)
        .then(chats => {
            chats["chats"]
                .sort((a, b) => a["id"].localeCompare(b["id"]))
                .forEach(
                    chat => {
                        addChatItem(chat, chatContainer);
                    }
                );
        });
}

function getChats(chatRoomId) {
    const url = "/chats?";
    const params = new URLSearchParams({
        myName: usernameGlobal,
        chatRoomId: chatRoomId
    }).toString();

    return fetch(url+params, {
        headers: {
            'Authorization': "Basic " + getAuth(usernameGlobal, passwordGlobal)
        }
    })
        .then(response => {
            return response.json();
        })
        .catch(error => {
            console.log("error on getChats"+ error);
        });
}

function addChatItem(chat) {
    let div = document.createElement("div");
    div.className = "container-fluid row pt-4";

    const contentContainer = document.createElement("div");
    contentContainer.className = "col-11";
    const content = document.createElement("div");
    content.className = "container-fluid";
    const createdAt = document.createElement("div");
    createdAt.className = "container-fluid";

    if (chat["type"] === "CONNECTED") {
        return;
        content.className = "chat-connected text-center";
        content.textContent = `${chat["sender"]}이 입장했습니다.`;
    } else if (chat["type"] === "DISCONNECTED") {
        return;
        div.className = "chat-connected text-center";
        div.textContent = `${chat["sender"]}이 퇴장했습니다.`;
    } else {
        const profileContainer = document.createElement("div");
        profileContainer.className = "col-1 p-0";
        content.textContent = chat["content"];
        createdAt.textContent = chat["createdAt"];
        if (chat["sender"] !== usernameGlobal) {
            content.className = "text-start container-fluid";
            createdAt.className = "text-start container-fluid";
            profileContainer.innerHTML = "<img  width=\"100%\" src='./profile-you.png' alt=\"\">"

            contentContainer.appendChild(content);
            contentContainer.appendChild(createdAt);
            div.appendChild(profileContainer);
            div.appendChild(contentContainer);
        } else {
            content.className = "text-end container-fluid";
            createdAt.className = "text-end container-fluid";
            profileContainer.innerHTML = "<img  width=\"100%\" src='./profile-me.png' alt=\"\">"

            contentContainer.appendChild(content);
            contentContainer.appendChild(createdAt);
            div.appendChild(contentContainer);
            div.appendChild(profileContainer);
        }
    }

    document.getElementById("chat").appendChild(div);
}

function chatPush() {
    if (chatRoomId === null) return;
    const message = document.getElementById("chat-message").value;
    const body = {
        sender: usernameGlobal,
        chatRoom: chatRoomId,
        content: message
    }
    sendMessage(body);
}

document.getElementById("chat-push")
    .addEventListener("click", chatPush);