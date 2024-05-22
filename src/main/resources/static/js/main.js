let usernameGlobal = null;
let passwordGlobal = null;

function registerAndLogin() {
    return async function (event) {
        event.preventDefault();
        username = document.getElementById('username').value;
        let password = document.getElementById('password').value;
        const response = await fetch(`/create/available?username=${username}`);
        const isNeededRegister = await response.text()

        if (isNeededRegister === "true") {
            registerUser(username, password);
        } else {
            login(username, password);
        }
    };
}

function registerUser(username, password) {
    fetch('/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(
            {
                username: username,
                password: password
            }
        )
    })
        .then(() => {
            login(username, password);
        })
        .catch(error => {
            alert(error);
        });
}

function login(username, password) {
    fetch("/login", {
        method: 'GET',
        headers: {
            'Authorization': "Basic " +
                btoa(username + ":" + password)
        }
    })
        .then(response => {
            if (response.ok) {
                usernameGlobal = username
                passwordGlobal = password
                document.querySelector('.login-page').classList.add('hidden');
                document.querySelector('.chat-page').classList.remove('hidden');
                chatPage();
            } else {
                var myModal = new bootstrap.Modal(document.getElementById('alert-modal'));
                myModal.show();
            }
        })
        .catch(error =>
            alert("error occurs in login")
        );
}

document.getElementById('loginForm')
    .addEventListener('submit', registerAndLogin());

// -------------------------------  chat page  -------------------------------

function chatPage() {
    fetch("/friend/all/" + usernameGlobal, {
        headers: {
            'Authorization': "Basic " +
                btoa(usernameGlobal + ":" + passwordGlobal)
        }
    })
        .then(response => response.json())
        .then(data => {
            const userListElement = document.getElementById("friends");
            data["friends"]
                .sort((a, b) => a["otherName"].localeCompare(b["otherName"]))
                .forEach(friend => {
                    const name = friend["otherName"];
                    const type = friend["type"];
                    const listItem = document.createElement("li");
                    listItem.textContent = name;
                    if (type === "PLUS") {
                        const badge = document.createElement("span");
                        badge.className = "badge badge-pill";
                        badge.style.backgroundColor = "#198754"
                        badge.textContent = "plus";
                        listItem.appendChild(badge);
                    }
                    listItem.className = "list-group-item d-flex justify-content-between align-items-center";
                    userListElement.appendChild(listItem);
                });
            selected();
        })
        .catch(error => {
            console.error("Error fetching user list:", error);
        });
}

function selected() {
    const ul = document.getElementById('friends');
    const items = ul.getElementsByTagName('li');
    for (let i = 0; i < items.length; i++) {
        items[i].addEventListener('mouseover', function () {
            this.classList.add('active');
        });
        items[i].addEventListener('mouseout', function () {
            this.classList.remove('active');
        });
    }
}

function participantsList() {
    fetch("/users")
        .then(response => response.json())
        .then(data => {
            // 받은 데이터를 사용하여 HTML 리스트를 만듭니다.
            const userListElement = document.getElementById("userList");
            data["usernames"].forEach(username => {
                const listItem = document.createElement("li");
                listItem.textContent = username;
                userListElement.appendChild(listItem);
            });
        })
        .catch(error => {
            console.error("Error fetching user list:", error);
        });
}