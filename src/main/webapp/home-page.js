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
};

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
        ringImg.src = "img/dark-angled-ring.svg"

        const img = document.createElement("div");
        img.classList.add("one-fourth-img");


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



        oneFourthEl.appendChild(mainImgEl);
        oneFourthEl.appendChild(ringImg);
        oneFourthEl.appendChild(img);
        oneFourthEl.appendChild(h3NameEl);
        oneFourthEl.appendChild(pEl);
        oneFourthEl.appendChild(descEl);
        oneFourthEl.appendChild(buttonEl);

        rowEl.appendChild(oneFourthEl);
    }
    return rowEl;
}

function createMenu(realEstateOffersDto) {
    const ulMenuEl = document.getElementById("menu");

    for (let i = 0; i < realEstateOffersDto.menuList.length; i++){
        const liMenuEl = document.createElement("li");
        liMenuEl.textContent = realEstateOffersDto.menuList[i];
        ulMenuEl.appendChild(liMenuEl);
    }
}


function onRealEstateGuestOffers(realEstateOffersDto){
    createMenu(realEstateOffersDto);
    containerContentDivEl.appendChild(insertDivider('Newest'));
    containerContentDivEl.appendChild(createRow(realEstateOffersDto.newest));
    containerContentDivEl.appendChild(insertDivider('Best rated'));
    containerContentDivEl.appendChild(createRow(realEstateOffersDto.bestRated));
    containerContentDivEl.appendChild(insertDivider('Trending'));
    containerContentDivEl.appendChild(createRow(realEstateOffersDto.trending));
}





