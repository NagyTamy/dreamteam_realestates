function onAdminPageClick() {

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onAdminLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'admin');
    xhr.send();
}

function onAdminLoad(){
    clearMessages();
    if (this.status === OK) {
        onAdminPageLoad(JSON.parse(this.responseText));
        showContents(['container']);
    } else {
        onOtherResponse(containerContentDivEl, this);
    }
}

function onAdminPageLoad(adminPageDto) {
}