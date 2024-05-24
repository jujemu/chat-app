function chatPage() {
    document.querySelector("#chat-main").textContent += " (" + usernameGlobal + ")";
    getFriendsList();
    getFriendsRequest();
}

function getFriendListConnect() {
    const url = "/friend/requests/connect";
    const sse = new EventSource(url);

    sse.addEventListener('connect', (e) => {
        const { data: receivedConnectData } = e;
    })
}
