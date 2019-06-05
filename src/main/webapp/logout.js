function onLogOutLoad(){
    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onLogOutPageLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'logout');
    xhr.send();
}

function onLogOutPageLoad(){
    clearMessages();
    if (this.status === OK) {
        setDelay(JSON.parse(this.responseText));
        showContents(['container']);
    } else {
        onOtherResponse(containerContentDivEl, this);
    }
}


function setDelay(message) {
    removeAllChildren(containerContentDivEl);
    newMessage(containerContentDivEl, "message", message);

    let timeOut = 5000;
    setTimeout(onLoad, timeOut);
}