function onProfileLoad(){
    const element = this;
    const id = element.getAttribute('id');

    const params = new URLSearchParams();
    params.append('id', id);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onUserPageLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'user-profile?' + params.toString());
    xhr.send();
}

function onUserPageLoad(){
    clearMessages();
    if (this.status === OK) {
        onUserProfileLoad(JSON.parse(this.responseText));
        showContents(['container']);
    } else {
        onOtherResponse(containerContentDivEl, this);
    }
}

function onUserProfileLoad(userPageDto) {
    console.log(userPageDto);
    markedNav("Profile");
    removeAllChildren(containerContentDivEl);
    addUserPhoto(userPageDto);
    addUserData(userPageDto);
    createAsideEl(userPageDto);
    createContainerForUserReviews(userPageDto, "Reviews");
}

function addUserPhoto(userPageDto) {
    removeAllChildren(headerImgEl);

    const imgSrc = decodeBase64(userPageDto.user.profilePic);

    const profileImgEl = document.createElement("img");
    profileImgEl.src = 'data:image/jpg;base64,' + imgSrc;

    const ringImg = document.createElement("img");
    ringImg.src = "img/ring.svg";
    ringImg.classList.add("ring");

    headerImgEl.removeEventListener('click', onTileClick);
    headerImgEl.removeAttribute('real-estate-id');
    headerImgEl.appendChild(profileImgEl);
    headerImgEl.appendChild(ringImg);
    return headerImgEl;
}

function addUserData(userPageDto) {
    removeAllChildren(headerTextEl);
    const h2TextEl = document.createElement("h2");
    h2TextEl.textContent = userPageDto.user.name;

    const eMailEl = document.createElement("p");
    if(userPageDto.loggedIn){
        eMailEl.textContent = "E-mail address: " + userPageDto.user.eMail;
    } else {
        eMailEl.textContent = "Please log in to see contact information.";
        eMailEl.addEventListener("click", onLogInClicked);
    }

    const membershipEl = document.createElement("p");
    membershipEl.textContent = "Member since: " + userPageDto.user.stringRegDate;


    headerTextEl.removeAttribute('real-estate-id');
    headerTextEl.removeEventListener('click', onTileClick);
    headerTextEl.appendChild(h2TextEl);
    headerTextEl.appendChild(eMailEl);
    headerTextEl.appendChild(membershipEl);
    return headerTextEl;
}

function createContainerForUserReviews(userPageDto, navId) {
    removeAllChildren(profileDiv);
    markedAside(navId);
    if(navId === "Reviews") {
        profileDiv.appendChild(onReviewsLoad(userPageDto));
    } else if(navId === "My requests"){
        profileDiv.appendChild(onOwnRequestsLoad(userPageDto))
    } else if(navId === "My reviews"){
        profileDiv.appendChild(onSentReviewsLoad())
    } else if(navId === "Messages"){
        profileDiv.appendChild(onMessagesLoad())
    } else if(navId === "My reservations"){
        profileDiv.appendChild(onReservationsLoad())
    } else if(navId === "User's real estates"){
        profileDiv.appendChild(onOwnedRealEstateLoads())
    } else if(navId === "My real estates"){
        profileDiv.appendChild(onOwnedRealEstateLoads())
    } else if(navId === "Send message"){
        profileDiv.appendChild(sendPrivateMessageToUser())
    } else {
        newError(profileDiv, "Ooops, something went wrong. Please chose an option from the menu!");
    }

    containerContentDivEl.appendChild(profileDiv);
}

function createAsideEl(userPageDto) {
    const menuList = userPageDto.asideMenu;
    const asideEl = document.createElement("aside");
    const navEl = document.createElement("nav");
    const ulEl = document.createElement("ul");

    if(menuList.length > 1) {
        let num = menuList.length * 49;
        let asideHeight = num + 'px';
        document.documentElement.style.setProperty('--aside-height', asideHeight);
    }

    for(let i = 0; i < menuList.length; i++){
        const liEl = document.createElement("li");
        const liElAttr = document.createAttribute("nav-id");
        liElAttr.value = menuList[i];
        liEl.classList.add("aside-nav");
        liEl.textContent = menuList[i];
        liEl.setAttributeNode(liElAttr);
        liEl.addEventListener('click', function () { createContainerForUserReviews(userPageDto, liElAttr.value)});
        ulEl.appendChild(liEl);
    }

    navEl.appendChild(ulEl);
    asideEl.appendChild(navEl);
    containerContentDivEl.appendChild(asideEl);
}


function onOwnRequestsLoad(userPageDto){
    /*loads all the requests the user sent sorted by time*/
    const requestContainerEl = document.createElement("div");
    requestContainerEl.id = "reviews";

    const dividerSpanEl = insertDivider("Requests", "divider");
    dividerSpanEl.classList.add("realestatedivider");
    requestContainerEl.appendChild(dividerSpanEl);

    for (let i = 0; i < userPageDto.allRequest.length; i++){
        const request = userPageDto.allRequest[i];

        const requestDivEl = document.createElement("div");
        requestDivEl.id = "request";

        const requestDataDivEl = document.createElement("div");
        requestDataDivEl.id = "request-data";

        const pTypeEl = document.createElement("p");
        pTypeEl.textContent = request.title;
        const pDateEl = document.createElement("p");
        pDateEl.textContent = request.stringDate;
        const pMessageEl = document.createElement("p");
        pMessageEl.textContent = request.message;
        const pRealEstateEl = document.createElement("p");
        if(request.hasRealEstate){
            pRealEstateEl.textContent = request.realEstateId;
            const pAttr = document.createAttribute("real-estate-id");
            pAttr.value = request.realEstateId;
            pRealEstateEl.classList.add("link");
            pRealEstateEl.setAttributeNode(pAttr);
            pRealEstateEl.addEventListener("click", onTileClick);
        } else {
            pRealEstateEl.textContent = "No Real Estate related to this request";
        }
        requestDataDivEl.appendChild(pTypeEl);
        requestDataDivEl.appendChild(pDateEl);
        requestDataDivEl.appendChild(pMessageEl);
        requestDataDivEl.appendChild(pRealEstateEl);


        const requestButtonEl = document.createElement("button");
        requestButtonEl.classList.add("request-button");
        requestButtonEl.textContent = "Delete";
        requestButtonEl.addEventListener('click', onDeleteRequestClick);

        requestDivEl.appendChild(requestDataDivEl);
        requestDivEl.appendChild(requestButtonEl);
        requestContainerEl.appendChild(requestDivEl);
    }

    return requestContainerEl;
}

function onSentReviewsLoad() {
    /*loads all the reviews the user sent, sorted by time*/
}

function onMessagesLoad() {
    /*loads all the messages sorted by time, grouped by topic*/
}

function onReservationsLoad() {
    /*loads users all sent reservations*/
}

function onOwnedRealEstateLoads() {
    /*lists every real estate owned by the profile owner*/
}

function sendPrivateMessageToUser() {
    /*loads the message sending page, sender set to logged in user, receiver set to profile owner*/
}








