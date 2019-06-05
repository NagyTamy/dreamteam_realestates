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
    markedAside(navId);
    if(navId === "Reviews") {
        removeAllChildren(profileDiv);
        profileDiv.appendChild(onReviewsLoad(userPageDto));
    } else if(navId === "My requests"){
        removeAllChildren(profileDiv);
        profileDiv.appendChild(onOwnRequestsLoad(userPageDto))
    } else if(navId === "My reviews"){
        removeAllChildren(profileDiv);
        profileDiv.appendChild(onSentReviewsLoad(userPageDto))
    } else if(navId === "Messages"){
        removeAllChildren(profileDiv);
        profileDiv.appendChild(onMessagesLoad())
    } else if(navId === "My reservations"){
        removeAllChildren(profileDiv);
        profileDiv.appendChild(onReservationsLoad(userPageDto))
    } else if(navId === "User's real estates"){
        removeAllChildren(profileDiv);
        profileDiv.appendChild(onOwnedRealEstateLoads())
    } else if(navId === "My real estates"){
        removeAllChildren(profileDiv);
        profileDiv.appendChild(onOwnedRealEstateLoads())
    } else if(navId === "Send message"){
        removeAllChildren(profileDiv);
        profileDiv.appendChild(sendPrivateMessageToUser())
    } else {
        removeAllChildren(profileDiv);
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


function onSentReviewsLoad(userPageDto) {
    /*loads all the reviews the user sent, sorted by time*/
    const sentReviewContainerDivEl = document.createElement("div");
    sentReviewContainerDivEl.id = "reviews";
    const dividerSpanEl = insertDivider("My reviews", "divider");
    dividerSpanEl.classList.add("realestatedivider");
    sentReviewContainerDivEl.appendChild(dividerSpanEl);

    if(userPageDto.allSentReview.length > 0){
        for(let i = 0; i < userPageDto.allSentReview.length; i++){
            const commentDivEl = document.createElement("div");
            commentDivEl.id = "comment";

            const commentTextDivEl = document.createElement("div");
            commentTextDivEl.id = "comment-text";

            const pEl = document.createElement("p");
            pEl.classList.add("datas");
            pEl.textContent = userPageDto.allSentReview[i].timeStampString + "   Rate: " + userPageDto.allSentReview[i].realEstateRating;

            const pTextEl = document.createElement("p");
            pTextEl.textContent = userPageDto.allSentReview[i].review;

            commentTextDivEl.appendChild(pEl);
            commentTextDivEl.appendChild(pTextEl);

            if(userPageDto.allSentReview[i].flagged) {
                const flagDiv = document.createElement("div");
                flagDiv.classList.add("flag-comment");

                const tooltipSpanEl = document.createElement("span");
                tooltipSpanEl.textContent = "Reported comment";
                tooltipSpanEl.classList.add("tooltip");

                commentTextDivEl.appendChild(flagDiv);
                commentTextDivEl.appendChild(tooltipSpanEl);
            }

            commentDivEl.appendChild(commentTextDivEl);

            if(!userPageDto.allSentReview[i].hasRealEstate) {
                const commentUserDivEl = document.createElement("div");
                commentUserDivEl.id = "comment-user";

                const profilePicImgEl = document.createElement("img");
                const imgSrc = decodeBase64(userPageDto.allSentReview[i].reviewedUser.profilePic);
                profilePicImgEl.src = 'data:image/jpg;base64,' + imgSrc;

                const profileBoxDivEl = document.createElement("div");
                profileBoxDivEl.id = "profile-box";

                const pNameEl = document.createElement("p");
                pNameEl.textContent = "Name: " + userPageDto.allSentReview[i].reviewedUser.name;

                const pRoleEl = document.createElement("p");
                pRoleEl.textContent = "Role: " + userPageDto.allSentReview[i].reviewedUser.role;

                const pRatingEl = document.createElement("p");
                pRatingEl.textContent = "Rating: " + userPageDto.allSentReview[i].reviewedUser.avgRating;

                profileBoxDivEl.appendChild(pNameEl);
                profileBoxDivEl.appendChild(pRoleEl);
                profileBoxDivEl.appendChild(pRatingEl);

                const toProfileButtonEl = document.createElement("button");
                toProfileButtonEl.textContent = "Profile";
                toProfileButtonEl.classList.add("inverse-button");
                toProfileButtonEl.id = userPageDto.allSentReview[i].reviewedUser.name;
                toProfileButtonEl.addEventListener('click', onProfileLoad);

                commentUserDivEl.appendChild(profilePicImgEl);
                commentUserDivEl.appendChild(profileBoxDivEl);
                commentUserDivEl.appendChild(toProfileButtonEl);
                commentDivEl.appendChild(commentUserDivEl);
                sentReviewContainerDivEl.appendChild(commentDivEl);
            } else {
                const commentUserDivEl = document.createElement("div");
                commentUserDivEl.id = "comment-user";

                const profilePicImgEl = document.createElement("img");
                const imgSrc = decodeBase64(userPageDto.allSentReview[i].realEstate.pic);
                profilePicImgEl.src = 'data:image/jpg;base64,' + imgSrc;

                const profileBoxDivEl = document.createElement("div");
                profileBoxDivEl.id = "profile-box";

                const pNameEl = document.createElement("p");
                pNameEl.textContent = "Name: " + userPageDto.allSentReview[i].realEstate.name;

                const pRoleEl = document.createElement("p");
                pRoleEl.textContent = "Country: " + userPageDto.allSentReview[i].realEstate.country;

                const pRatingEl = document.createElement("p");
                pRatingEl.textContent = "Rating: " + userPageDto.allSentReview[i].realEstate.avgRating;

                profileBoxDivEl.appendChild(pNameEl);
                profileBoxDivEl.appendChild(pRoleEl);
                profileBoxDivEl.appendChild(pRatingEl);

                const toProfileButtonEl = document.createElement("button");
                toProfileButtonEl.textContent = "Page";
                toProfileButtonEl.classList.add("inverse-button");
                const realEstAttr = document.createAttribute("real-estate-id");
                realEstAttr.value = userPageDto.allSentReview[i].realEstate.id;
                toProfileButtonEl.setAttributeNode(realEstAttr);
                toProfileButtonEl.addEventListener('click', onTileClick);

                commentUserDivEl.appendChild(profilePicImgEl);
                commentUserDivEl.appendChild(profileBoxDivEl);
                commentUserDivEl.appendChild(toProfileButtonEl);
                commentDivEl.appendChild(commentUserDivEl);
                sentReviewContainerDivEl.appendChild(commentDivEl);
            }

        }
    } else {
        const noReviewDivEl = document.createElement("div");
        newError(noReviewDivEl, "You did not send any reviews yet.")
        sentReviewContainerDivEl.appendChild(noReviewDivEl);
    }

    return sentReviewContainerDivEl;
}

function onMessagesLoad() {
    /*loads all the messages sorted by time, grouped by topic*/
}

function onReservationsLoad(userPageDto) {
    /*loads users all sent reservations*/
    const sentReservationContainer = document.createElement("div");
    sentReservationContainer.id = "reviews";
    const dividerSpanEl = insertDivider("My reservations", "divider");
    dividerSpanEl.classList.add("realestatedivider");
    sentReservationContainer.appendChild(dividerSpanEl);

    if(userPageDto.hasCurrentReservation){
        sentReservationContainer.appendChild(createReservationBox(userPageDto.currentReservation, "current-reservation"));
    }

    if(userPageDto.allUpcomingReservation.length > 0) {
        const listOfReservations = userPageDto.allUpcomingReservation;
        for (let i = 0; i < listOfReservations.length; i++) {
            const reservationObject = listOfReservations[i];
            sentReservationContainer.appendChild(createReservationBox(reservationObject, ""));
        }
    }

    if(userPageDto.allPastReservation.length > 0) {
        const listOfReservations = userPageDto.allPastReservation;
        for (let i = 0; i < listOfReservations.length; i++) {
            const reservationObject = listOfReservations[i];
            sentReservationContainer.appendChild(createReservationBox(reservationObject, "past-reservation"));
        }
    }

    if(!userPageDto.hasCurrentReservation && userPageDto.allUpcomingReservation.length <= 0 && userPageDto.allPastReservation.length <= 0){
        newError(sentReservationContainer, "No reservations yet.")
    }

    return sentReservationContainer;

}


function createReservationBox(reservationObject, cssClassForBg) {
    const commentDivEl = document.createElement("div");
    commentDivEl.id = "comment";

    const commentTextDivEl = document.createElement("div");
    commentTextDivEl.id = "comment-text";
    commentTextDivEl.classList.add(cssClassForBg);

    const pEl = document.createElement("p");
    pEl.classList.add("datas");
    pEl.textContent = "From " + reservationObject.stringBegins + " to " + reservationObject.stringEnds;

    const confEl = document.createElement("p");

    if(reservationObject.isConfirmed){
        confEl.textContent = "Reservation confirmed";
    } else {
        confEl.textContent = "Reservation pending";
    }

    commentTextDivEl.appendChild(pEl);

    const commentUserDivEl = document.createElement("div");
    commentUserDivEl.id = "comment-user";

    const profilePicImgEl = document.createElement("img");
    const imgSrc = decodeBase64(reservationObject.realEstate.pic);
    profilePicImgEl.src = 'data:image/jpg;base64,' + imgSrc;

    const profileBoxDivEl = document.createElement("div");
    profileBoxDivEl.id = "profile-box";

    const pNameEl = document.createElement("p");
    pNameEl.textContent = "Name: " + reservationObject.realEstate.name;

    const pRoleEl = document.createElement("p");
    pRoleEl.textContent = "Country: " + reservationObject.realEstate.country;

    const pRatingEl = document.createElement("p");
    pRatingEl.textContent = "Rating: " + reservationObject.realEstate.avgRating;

    profileBoxDivEl.appendChild(pNameEl);
    profileBoxDivEl.appendChild(pRoleEl);
    profileBoxDivEl.appendChild(pRatingEl);

    const toProfileButtonEl = document.createElement("button");
    toProfileButtonEl.textContent = "Page";
    toProfileButtonEl.classList.add("inverse-button");
    const realEstAttr = document.createAttribute("real-estate-id");
    realEstAttr.value = reservationObject.realEstate.id;
    toProfileButtonEl.setAttributeNode(realEstAttr);
    toProfileButtonEl.addEventListener('click', onTileClick);

    commentUserDivEl.appendChild(profilePicImgEl);
    commentUserDivEl.appendChild(profileBoxDivEl);
    commentUserDivEl.appendChild(toProfileButtonEl);
    commentDivEl.appendChild(commentTextDivEl);
    commentDivEl.appendChild(commentUserDivEl);

    return commentDivEl;

}

function onOwnedRealEstateLoads() {
    /*lists every real estate owned by the profile owner*/
}

function sendPrivateMessageToUser() {
    /*loads the message sending page, sender set to logged in user, receiver set to profile owner*/
}








