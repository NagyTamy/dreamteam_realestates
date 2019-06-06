function onFavsLoad() {

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onFavRealEstatesLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'favs');
    xhr.send();
}

function onFavRealEstatesLoad() {
    clearMessages();
    if (this.status === OK) {
        console.log(JSON.parse(this.responseText));
        onloadFavList(JSON.parse(this.responseText));
        showContents(['container']);
    } else {
        onOtherResponse(containerContentDivEl, this);
    }
}

function onloadFavList(favourites) {
    removeAllChildren(containerContentDivEl);
    markedNav("Favs");
    containerContentDivEl.appendChild(insertDivider('Favourites', 'divider'));
    if (favourites.length > 0) {
        const rowEl = document.createElement("div");
        rowEl.classList.add("row");

        for (let j = 0; j < favourites.length; j++) {
            const oneFourthDivEl = document.createElement("div");
            oneFourthDivEl.classList.add("one-fourth");

            const imgSrc = decodeBase64(favourites[j].pic);

            const mainImgEl = document.createElement("img");
            mainImgEl.src = 'data:image/jpg;base64,' + imgSrc;

            const ringImg = document.createElement("img");
            ringImg.src = "img/dark-angled-ring.svg";

            const h3NameEl = document.createElement("h3");
            h3NameEl.textContent = favourites[j].name;

            const pEl = document.createElement("p");
            pEl.id = 'country';
            pEl.textContent = favourites[j].country;

            const descEl = document.createElement("div");
            descEl.id = "description";
            descEl.textContent = favourites[j].description;

            const buttonEl = document.createElement("button");
            buttonEl.textContent = "More";

            const realEstateIdAttr = document.createAttribute('real-estate-id');
            realEstateIdAttr.value = favourites[j].id;

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
        containerContentDivEl.appendChild(rowEl);
    } else {
        newMessage(containerContentDivEl,"message","You did not mark any real estates as favourite yet.")
    }
}