function getFriendsList() {
    fetch("/friend/all/" + usernameGlobal, {
        headers: {
            'Authorization': "Basic " +
                btoa(usernameGlobal + ":" + passwordGlobal)
        }
    })
        .then(response => response.json())
        .then(data => {
            getFriendItem(data);
        })
        .catch(error => {
            console.error("Error fetching user list:", error);
        });
}

function getFriendItem(data) {
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
                badge.style.backgroundColor = "#AC58FA";
                badge.textContent = "plus";
                listItem.appendChild(badge);
            }
            listItem.className = "list-group-item d-flex justify-content-between align-items-center";
            userListElement.appendChild(listItem);
        });
    selected('friends');
}

function selected(element) {
    const ul = document.getElementById(element);
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
