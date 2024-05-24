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

document.getElementById('friendRequest-btn')
    .addEventListener('submit', friendRequest_modalShow());

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

document.querySelector("#friendRequest-modal")
    .addEventListener('keypress', (event) => {
        if (event.key === 'Enter') {
            sendRequest(event);
        }
});

document.querySelector("#friendRequestModalBtn")
    .addEventListener('click', sendRequest);