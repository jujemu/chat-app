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
