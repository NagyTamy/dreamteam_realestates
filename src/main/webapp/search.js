function searchByUserName() {
    
}

function searchByRealEstate(){

}

function doSimpleSearch() {
    const searchKeysEl = document.querySelector('input[name=seach-key]');
    const searchKey = searchKeysEl.value;

    const params = new URLSearchParams();
    params.append('searchKey', searchKey);


    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSearchResultResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'search?' + params.toString());
    xhr.send();
}

function onSearchResultResponse() {
    clearMessages();
    if (this.status === OK) {
        removeAllChildren(containerContentDivEl);
        onSearchResultLoad(JSON.parse(this.responseText));
    } else {
        onOtherResponse(containerContentDivEl, this);
    }
}

function onSearchResultLoad(searchResultDto){
    if(searchResultDto.hasResult){
        const searchResultContainer = document.createElement("div");
        searchResultContainer.id = "search-result";

        const rowEl = document.createElement("div");
        rowEl.classList.add("row");

        for (let j = 0; j < searchResultDto.searchResult.length; j++) {

            const realEstate = searchResultDto.searchResult[j];

            const oneFourthDivEl = document.createElement("div");
            oneFourthDivEl.classList.add("one-fourth");

            const imgSrc = decodeBase64(realEstate.pic);

            const mainImgEl = document.createElement("img");
            mainImgEl.src = 'data:image/jpg;base64,' + imgSrc;

            const ringImg = document.createElement("img");
            ringImg.src = "img/dark-angled-ring.svg";

            const h3NameEl = document.createElement("h3");
            h3NameEl.textContent = realEstate.name;

            const pEl = document.createElement("p");
            pEl.id = 'country';
            pEl.textContent = realEstate.country;

            const descEl = document.createElement("div");
            descEl.id = "description";
            descEl.textContent = realEstate.description;

            const buttonEl = document.createElement("button");
            buttonEl.textContent = "More";

            const realEstateIdAttr = document.createAttribute('real-estate-id');
            realEstateIdAttr.value = realEstate.id;

            oneFourthDivEl.setAttributeNode(realEstateIdAttr);
            oneFourthDivEl.appendChild(mainImgEl);
            oneFourthDivEl.appendChild(ringImg);
            oneFourthDivEl.appendChild(h3NameEl);
            oneFourthDivEl.appendChild(pEl);
            oneFourthDivEl.appendChild(descEl);
            oneFourthDivEl.appendChild(buttonEl);
            oneFourthDivEl.addEventListener('click', onTileClick);

            rowEl.appendChild(oneFourthDivEl);
        }
        searchResultContainer.appendChild(rowEl);
        containerContentDivEl.appendChild(searchResultContainer);
    }
    else {
            newMessage(containerContentDivEl,"message",  "No match in our database :( ")
    }


}