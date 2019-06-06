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
    createContainerForUserProfile(userPageDto, "Reviews");
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

function createContainerForUserProfile(userPageDto, navId) {
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
        profileDiv.appendChild(onMessagesLoad(userPageDto))
    } else if(navId === "My reservations"){
        removeAllChildren(profileDiv);
        profileDiv.appendChild(onReservationsLoad(userPageDto))
    } else if(navId === "User's real estates"){
        removeAllChildren(profileDiv);
        profileDiv.appendChild(onOwnedRealEstateLoads(userPageDto))
    } else if(navId === "My real estates"){
        removeAllChildren(profileDiv);
        profileDiv.appendChild(onOwnedRealEstateLoads(userPageDto))
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
        liEl.addEventListener('click', function () { createContainerForUserProfile(userPageDto, liElAttr.value)});
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

    if(userPageDto.allRequest.length > 0) {
        for (let i = 0; i < userPageDto.allRequest.length; i++) {
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
            if (request.hasRealEstate) {
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
    } else {
        newError(requestContainerEl, "You do not have any requests yet.");
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

function onMessagesLoad(userPageDto) {
    /*loads all the messages sorted by time, grouped by topic*/
    const messageListContainer = document.createElement("div");
    messageListContainer.id = "messages-container";
    const dividerSpanEl = insertDivider("My messages", "divider");
    dividerSpanEl.classList.add("realestatedivider");
    messageListContainer.appendChild(dividerSpanEl);

    let receiverName;

    if(userPageDto.hasPrivateMessages) {
        for (let i = 0; i < userPageDto.messageBatches.length; i++){
            const messageContainerDivEl = document.createElement("div");
            messageContainerDivEl.id = "private-message";

            const lastMessageForDisplay = userPageDto.messageBatches[i][0];

            if(lastMessageForDisplay.iAmReceiver){
                const imageDivEl = document.createElement("div");
                imageDivEl.classList.add("image-for-message");
                imageDivEl.id = lastMessageForDisplay.senderUser.name;
                imageDivEl.addEventListener('click', onProfileLoad);

                const imgSrc = decodeBase64(lastMessageForDisplay.senderUser.profilePic);

                const usrImg = document.createElement("img");
                usrImg.src = 'data:image/jpg;base64,' + imgSrc;


                const ringImg = document.createElement("img");
                ringImg.src = "img/ring.svg";
                ringImg.classList.add("ring");

                receiverName = lastMessageForDisplay.senderUser.name;

                imageDivEl.appendChild(usrImg);
                imageDivEl.appendChild(ringImg);
                messageContainerDivEl.appendChild(imageDivEl);
            } else {
                const imageDivEl = document.createElement("div");
                imageDivEl.classList.add("image-for-message");
                imageDivEl.id = lastMessageForDisplay.receiverUser.name;
                imageDivEl.addEventListener('click', onProfileLoad);

                const imgSrc = decodeBase64(lastMessageForDisplay.receiverUser.profilePic);

                const usrImg = document.createElement("img");
                usrImg.src = 'data:image/jpg;base64,' + imgSrc;

                const ringImg = document.createElement("img");
                ringImg.src = "img/ring.svg";
                ringImg.classList.add("ring");

                receiverName = lastMessageForDisplay.receiverUser.name;

                imageDivEl.appendChild(usrImg);
                imageDivEl.appendChild(ringImg);
                messageContainerDivEl.appendChild(imageDivEl);
            }


            const messageTextPreviewDivEl = document.createElement("div");
            messageTextPreviewDivEl.id = "message-text-preview";

            const h3TitleEl = document.createElement("h3");
            h3TitleEl.textContent = lastMessageForDisplay.stringDate + "     " + lastMessageForDisplay.title;
            h3TitleEl.classList.add("link");
            h3TitleEl.addEventListener('click', function (){onLoadConversation(userPageDto.messageBatches[i])});

            const pPreviewEl = document.createElement("p");
            pPreviewEl.classList.add("message-preview");
            pPreviewEl.textContent = lastMessageForDisplay.message;
            pPreviewEl.classList.add("link");
            pPreviewEl.addEventListener('click', function (){onLoadConversation(userPageDto.messageBatches[i], userPageDto.user.name, receiverName)});

            messageTextPreviewDivEl.appendChild(h3TitleEl);
            messageTextPreviewDivEl.appendChild(pPreviewEl);

            if(lastMessageForDisplay.hasRealEstate) {
                const pRealEstateEl = document.createElement("p");
                pRealEstateEl.textContent = "Real estate: " + lastMessageForDisplay.realEstate.name;

                const realEstateAttr = document.createAttribute("real-estate-id");
                realEstateAttr.value = lastMessageForDisplay.realEstate.id;

                pRealEstateEl.setAttributeNode(realEstateAttr);
                pRealEstateEl.classList.add("link");
                pRealEstateEl.addEventListener("click", onTileClick)
                messageTextPreviewDivEl.appendChild(pRealEstateEl);
            } else{
                const pRealEstatEl = document.createElement("p");
                pRealEstatEl.textContent = "No Real Estate attached to this conversation";
                messageTextPreviewDivEl.appendChild(pRealEstatEl);
            }

            messageContainerDivEl.appendChild(messageTextPreviewDivEl);
            messageListContainer.appendChild(messageContainerDivEl);
        }
    } else {
        newMessage(messageListContainer, 'message', 'You do not have any messages yet.')
    } return messageListContainer;



    /*<div id="messages-container">
            <div id="message-text-preview">
                <h3>Title goes here</h3>
                <p class="message-preview">Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean m...</p>
                <p>Related real estate: </p>
            </div>
        </div>
    </div>*/


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

function onOwnedRealEstateLoads(userPageDto) {
    /*lists every real estate owned by the profile owner*/
    const ownRealEstateContainer = document.createElement("div");
    ownRealEstateContainer.id = "own-real-estates";

    const dividerSpanEl = insertDivider("My houses", "divider");
    dividerSpanEl.classList.add("realestatedivider");
    ownRealEstateContainer.appendChild(dividerSpanEl);

    if (userPageDto.hasRealEstates) {
        const rowEl = document.createElement("div");
        rowEl.classList.add("row");

        for (let j = 0; j < userPageDto.ownRealEstates.length; j++) {
            const oneThirdDivEl = document.createElement("div");
            oneThirdDivEl.classList.add("one-third");

            const imgSrc = decodeBase64(userPageDto.ownRealEstates[j].pic);

            const mainImgEl = document.createElement("img");
            mainImgEl.src = 'data:image/jpg;base64,' + imgSrc;

            const ringImg = document.createElement("img");
            ringImg.src = "img/dark-angled-ring.svg";

            const h3NameEl = document.createElement("h3");
            h3NameEl.textContent = userPageDto.ownRealEstates[j].name;

            const pEl = document.createElement("p");
            pEl.id = 'country';
            pEl.textContent = userPageDto.ownRealEstates[j].country;

            const descEl = document.createElement("div");
            descEl.id = "description";
            descEl.textContent = userPageDto.ownRealEstates[j].description;

            const buttonEl = document.createElement("button");
            buttonEl.textContent = "More";

            const realEstateIdAttr = document.createAttribute('real-estate-id');
            realEstateIdAttr.value = userPageDto.ownRealEstates[j].id;

            oneThirdDivEl.setAttributeNode(realEstateIdAttr);
            oneThirdDivEl.appendChild(mainImgEl);
            oneThirdDivEl.appendChild(ringImg);
            oneThirdDivEl.appendChild(h3NameEl);
            oneThirdDivEl.appendChild(pEl);
            oneThirdDivEl.appendChild(descEl);
            oneThirdDivEl.appendChild(buttonEl);
            oneThirdDivEl.addEventListener('click', onTileClick);

            rowEl.appendChild(oneThirdDivEl);
        }
        ownRealEstateContainer.appendChild(rowEl);
    } else {
        if(userPageDto.own) {
            newError(ownRealEstateContainer, "You do not have real estates uploaded yet.");
        } else {
            newError(ownRealEstateContainer, "This user does not have any public real estate, please check back later.")
        }
    } return ownRealEstateContainer;

}

function sendPrivateMessageToUser() {
    /*loads the message sending page, sender set to logged in user, receiver set to profile owner*/
}








