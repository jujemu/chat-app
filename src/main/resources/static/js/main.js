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