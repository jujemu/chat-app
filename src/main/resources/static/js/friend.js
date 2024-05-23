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
    return async function(event) {
        event.preventDefault();
        const modal = new bootstrap.Modal(document.getElementById('friendRequest-modal'));
        modal.show();
    }
}

function sendRequest() {
    const friendName = document.getElementById('friendRequestName').value;
    const url = '/friend/request/' + usernameGlobal + '/' + friendName;

    fetch(url, {
        method: 'POST',
        headers: {
            'Authorization': "Basic " +
            btoa(usernameGlobal + ":" + passwordGlobal)
        }
    })
        .catch(error => alert("Error occurs."));
}

document.querySelector("#friendRequest-modal")
    .addEventListener('keypress', (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            sendRequest();
        }
});

document.querySelector("#friendRequestModalBtn")
    .addEventListener('click', sendRequest);