function onHomePageLoad() {
    clearMessages();
    if (this.status === OK) {
        onRealEstateGuestOffers(JSON.parse(this.responseText));
        showContents(['container']);
    } else {
        onOtherResponse(containerContentDivEl, this);
    }
}


function decodeBase64(s) {
    let e={},i,b=0,c,x,l=0,a,r='',w=String.fromCharCode,L=s.length;
    const A="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    for(i=0;i<64;i++){e[A.charAt(i)]=i;}
    for(x=0;x<L;x++){
        c=e[s.charAt(x)];b=(b<<6)+c;l+=6;
        while(l>=8){((a=(b>>>(l-=8))&0xff)||(x<(L-2)))&&(r+=w(a));}
    }
    return r;
}

function createRow(arrayList) {
    const rowEl = document.createElement("div");
    rowEl.classList.add("row");

    for (let j = 0; j < 4; j++){
        const oneFourthEl = document.createElement("div");
        oneFourthEl.classList.add("one-fourth");

        const imgSrc = decodeBase64(arrayList[j].pic);

        const mainImgEl = document.createElement("img");
        mainImgEl.src = 'data:image/jpg;base64,' + imgSrc;

        const ringImg = document.createElement("img");
        ringImg.src = "img/dark-angled-ring.svg";

        const h3NameEl = document.createElement("h3");
        h3NameEl.textContent = arrayList[j].name;

        const pEl = document.createElement("p");
        pEl.id = 'country';
        pEl.textContent = arrayList[j].country;

        const descEl = document.createElement("div");
        descEl.id = "description";
        descEl.textContent = arrayList[j].description;

        const buttonEl = document.createElement("button");
        buttonEl.textContent = "More";

        const realEstateIdAttr = document.createAttribute('real-estate-id');
        realEstateIdAttr.value = arrayList[j].id;

        oneFourthEl.setAttributeNode(realEstateIdAttr);
        oneFourthEl.appendChild(mainImgEl);
        oneFourthEl.appendChild(ringImg);
        oneFourthEl.appendChild(h3NameEl);
        oneFourthEl.appendChild(pEl);
        oneFourthEl.appendChild(descEl);
        oneFourthEl.appendChild(buttonEl);
        oneFourthEl.addEventListener('click', onTileClick);

        rowEl.appendChild(oneFourthEl);
    }
    return rowEl;
}

function createMenu(realEstateOffersDto) {
    const ulMenuEl = document.getElementById("menu");
    removeAllChildren(ulMenuEl);

    for (let i = 0; i < realEstateOffersDto.menuList.length; i++){
        const liMenuEl = document.createElement("li");
        liMenuEl.textContent = realEstateOffersDto.menuList[i];

        const mainNavAttr = document.createAttribute("menulist-id");
        mainNavAttr.value = realEstateOffersDto.menuList[i];

        const item = realEstateOffersDto.menuList[i];

        if(item === "Log in"){
            liMenuEl.addEventListener('click', onLogInClicked);
        } else if(item === "Profile"){
            const liElAttr = document.createAttribute("id");
            liElAttr.value = realEstateOffersDto.userName;
            liMenuEl.setAttributeNode(liElAttr);
            console.log(liElAttr);
            liMenuEl.addEventListener('click', onProfileLoad);
        } else if(item === "Log out"){
            liMenuEl.addEventListener('click', onLogOutLoad);
        }
        liMenuEl.classList.add("main");
        liMenuEl.setAttributeNode(mainNavAttr);

        ulMenuEl.appendChild(liMenuEl);
    }
}

function createHeaderText(realEstate){
    const realEstateIdAttr = document.createAttribute('real-estate-id');
    realEstateIdAttr.value =  realEstate.id;

    removeAllChildren(headerTextEl);
    const h2TextEl = document.createElement("h2");
    h2TextEl.textContent = realEstate.name;
    const descriptionPEl = document.createElement("p");
    descriptionPEl.textContent = realEstate.description;

    headerTextEl.addEventListener('click', onTileClick);
    headerTextEl.setAttributeNode(realEstateIdAttr);
    headerTextEl.appendChild(h2TextEl);
    headerTextEl.appendChild(descriptionPEl);
    return headerTextEl;
}

function createHeaderImg(realEstate, imgPath){
    const realEstateIdAttr = document.createAttribute('real-estate-id');
    realEstateIdAttr.value =  realEstate.id;

    removeAllChildren(headerImgEl);

    const imgSrc = decodeBase64(imgPath);

    const houseImgEl = document.createElement("img");
    houseImgEl.src = 'data:image/jpg;base64,' + imgSrc;

    const ringImg = document.createElement("img");
    ringImg.src = "img/ring.svg";
    ringImg.classList.add("ring");

    headerImgEl.addEventListener('click', onTileClick);
    headerImgEl.setAttributeNode(realEstateIdAttr);
    headerImgEl.appendChild(houseImgEl);
    headerImgEl.appendChild(ringImg);
    return headerImgEl;
}

function createRandomOffer(realEstateOffersDto) {
    createHeaderImg(realEstateOffersDto.randomOffer, realEstateOffersDto.randomOffer.pic);
    createHeaderText(realEstateOffersDto.randomOffer);

}

function onRealEstateGuestOffers(realEstateOffersDto){
    removeAllChildren(containerContentDivEl);
    createRandomOffer(realEstateOffersDto);
    createMenu(realEstateOffersDto);
    containerContentDivEl.appendChild(insertDivider('Newest', 'divider'));
    containerContentDivEl.appendChild(createRow(realEstateOffersDto.newest));
    containerContentDivEl.appendChild(insertDivider('Best rated', 'divider'));
    containerContentDivEl.appendChild(createRow(realEstateOffersDto.bestRated));
    containerContentDivEl.appendChild(insertDivider('Trending', 'divider'));
    containerContentDivEl.appendChild(createRow(realEstateOffersDto.trending));
}





