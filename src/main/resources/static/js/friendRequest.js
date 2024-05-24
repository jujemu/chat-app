function friendRequest_modalShow() {
    return function(event) {
        event.preventDefault();
        document.querySelector("#friendRequest-warning")
            .classList.add("hidden");
        const modal = new bootstrap.Modal(document.getElementById('friendRequest-modal'));
        modal.show();
    }
}

function sendRequest(event) {
    event.preventDefault();
    const friendName = document.getElementById('friendRequestName').value;
    const url = '/friend/request/' + usernameGlobal + '/' + friendName;

    fetch(url, {
        method: 'POST',
        headers: {
            'Authorization': "Basic " +
                btoa(usernameGlobal + ":" + passwordGlobal)
        }
    })
        .then(response => {
            if (response.status === 404) {
                document.querySelector("#friendRequest-warning")
                    .classList.remove("hidden");
            } else {
                const modal = bootstrap.Modal.getInstance('#friendRequest-modal');
                modal.hide();
            }
        })
        .catch(error => alert("Error occurs."));
}

function getFriendsRequest() {
    const params = {
        myName: usernameGlobal
    }
    const queryString = new URLSearchParams(params).toString();
    const url = `/friend/requests?${queryString}`;
    fetch(url, {
        headers: {
            'Authorization': "Basic " +
                btoa(usernameGlobal + ":" + passwordGlobal)
        }
    })
        .then(response => response.json())
        .then(data => {
            getFriendRequestItem(data);
        })
        .catch(error => {
            console.error("Error fetching user list:", error);
        });
}

function getFriendRequestItem(data) {
    const userListElement = document.getElementById("friend-request");
    data["friends"]
        .sort((a, b) => a["otherName"].localeCompare(b["otherName"]))
        .forEach(friend => {
            const listItem = document.createElement("li");
            listItem.id = "friend-request-container-" + friend["otherName"];
            const textNode = document.createElement("div");
            textNode.textContent = friend["otherName"];

            const acceptDenyContainer = document.createElement("div");
            const accept = document.createElement("span");
            accept.className = "badge badge-pill";
            accept.style.backgroundColor = "#198754";
            accept.textContent = "수락";
            accept.style.cursor = "pointer";

            const blank = document.createElement("span");
            blank.textContent = "  ";

            const deny = document.createElement("span");
            deny.className = "badge badge-pill";
            deny.style.backgroundColor = "#FE2E64";
            deny.textContent = "거절";
            deny.style.cursor = "pointer";

            listItem.appendChild(textNode);
            acceptDenyContainer.appendChild(accept);
            acceptDenyContainer.appendChild(blank);
            acceptDenyContainer.appendChild(deny);
            listItem.appendChild(acceptDenyContainer);
            listItem.className = "list-group-item d-flex justify-content-between align-items-center";
            userListElement.appendChild(listItem);

            accept
                .addEventListener("click", requestEventHandler("accept", listItem));

            deny
                .addEventListener("click", requestEventHandler("deny", listItem));
        });
}

function requestEventHandler(acceptOrDeny, listItem) {
    return function () {
        const receiveName = getPureText(listItem);
        const url = '/friend/request/' + acceptOrDeny + "/" + usernameGlobal + "/" + receiveName;
        fetch(url, {
            method: 'POST',
            headers: {
                'Authorization': "Basic " +
                    btoa(usernameGlobal + ":" + passwordGlobal)
            }
        })
            .then(() => {
                listItem.remove();
                if (acceptOrDeny === "accept") {
                    const data = {
                        friends: [{
                            type: "NORMAL",
                            otherName: receiveName
                        }]
                    }
                    getFriendItem(data)
                }
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
            });
    };
}

function getPureText(li) {
    const div = li.querySelector('div');
    return div ? div.textContent.trim() : '';
}

function getFriendRequestListConnect() {
    const url = "/friend/requests/connect" + `?myName=${usernameGlobal}`;
    const sse = new EventSource(url);

    sse.addEventListener('friendRequest', (e) => {
        const { data: requestUserName } = e;
        const data = {
            friends: [{
                type: "NORMAL",
                otherName: requestUserName
            }]
        }
        getFriendRequestItem(data);
    })

    sse.addEventListener('requestAccept', (e) => {
        const { data: username } = e;
        const data = {
            friends: [{
                type: "NORMAL",
                otherName: username
            }]
        }
        getFriendItem(data);
    })
}

document.getElementById('friendRequest-btn')
    .addEventListener('submit', friendRequest_modalShow());

document.querySelector("#friendRequest-modal")
    .addEventListener('keypress', (event) => {
        if (event.key === 'Enter') {
            sendRequest(event);
        }
    });

document.querySelector("#friendRequestModalBtn")
    .addEventListener('click', sendRequest);