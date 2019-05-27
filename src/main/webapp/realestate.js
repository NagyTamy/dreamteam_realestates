function onTileClick(){
    const element = this;
    const id = element.getAttribute('id');

    const params = new URLSearchParams();
    params.append('id', id);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onNotOwnedRealEstateLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'real-estate-profile?' + params.toString());
    xhr.send();
}

function onNotOwnedRealEstateLoad() {
    showContents(['container']);
    removeAllChildren(containerContentDivEl);


}