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
        setDelay(JSON.parse(this.responseText), onLoad(), 5000);
        showContents(['container']);
    } else {
        onOtherResponse(containerContentDivEl, this);
    }
}


function setDelay(message, callbackFunction, timeout) {
    markedNav("Log out");
    removeAllChildren(containerContentDivEl);
    newMessage(containerContentDivEl, "message", message);

    setTimeout(callbackFunction, timeout);
}