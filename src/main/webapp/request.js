function onDeleteRequestClick() {
    const element = this;
    const id = element.getAttribute("id");

    const params = new URLSearchParams();
    params.append('id', id);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSentRequestResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('DELETE', 'request?' + params.toString());
    xhr.send();
}

function onPermitPendingRequest(){

}

function onRefusePendingRequest() {
    
}

function onSentRequestResponse() {
    clearMessages();
    if (this.status === OK) {
        removeAllChildren(containerContentDivEl);
        setDelay(JSON.parse(this.responseText), onLoad(), 5000);
    } else {
        onOtherResponse(containerContentDivEl, this);
    }
}