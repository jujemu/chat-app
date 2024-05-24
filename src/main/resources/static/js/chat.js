function chatPage() {
    document.querySelector("#chat-main").textContent += " (" + usernameGlobal + ")";
    getFriendsList();
    getFriendsRequest();
}


