function onTileClick(){
    const element = this;
    const id = element.getAttribute('real-estate-id');

    const params = new URLSearchParams();
    params.append('id', id);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onRealEstateLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'real-estate-profile?' + params.toString());
    xhr.send();
}

function onRealEstateLoad(){
    clearMessages();
    if (this.status === OK) {
        console.log(JSON.parse(this.responseText));
        onRealEstateUniquePage(JSON.parse(this.responseText));
    } else {
        onOtherResponse(containerContentDivEl, this);
    }
}

function onRealEstateUniquePage(realEstatePageDto){
    showContents(["container"]);
    removeAllChildren(containerContentDivEl);
    createRealEstateHeader(realEstatePageDto);
    loadAllPictures(realEstatePageDto);
    createRealEstateAsideEl(realEstatePageDto);
    containerContentDivEl.appendChild(onReviewsLoad(realEstatePageDto));

}


function createRealEstateHeader(realEstatePageDto) {
    createHeaderText(realEstatePageDto.realEstate);
    createHeaderImg(realEstatePageDto.realEstate, realEstatePageDto.mainPic.image);
}

function loadAllPictures(realEstatePageDto) {
    if(realEstatePageDto.pictureList.length > 1) {
        const imagesDivEl = document.createElement("div");
        imagesDivEl.id = "images";


        const imageRowDivEl = document.createElement("div");
        imageRowDivEl.id = "image-row";

        if (realEstatePageDto.pictureList.length > 5) {
            let num = realEstatePageDto.pictureList.length * 213;
            let scrollbarWidth = num + 'px';
            document.documentElement.style.setProperty('--scrollbar-width', scrollbarWidth);
        }

        for (let i = 0; i < realEstatePageDto.pictureList.length; i++) {
            const oneSixthDivEl = document.createElement("div");
            oneSixthDivEl.classList.add('one-sixth');

            const picAttr = document.createElement("id");
            picAttr.value = realEstatePageDto.pictureList[i].id;

            const imgSrc = decodeBase64(realEstatePageDto.pictureList[i].image);

            const mainImgEl = document.createElement("img");
            mainImgEl.src = 'data:image/jpg;base64,' + imgSrc;

            const ringImg = document.createElement("img");
            ringImg.src = "img/dark-angled-ring.svg";

            oneSixthDivEl.addEventListener('click', toggleBigPicture);
            oneSixthDivEl.appendChild(mainImgEl);
            oneSixthDivEl.appendChild(ringImg);
            imageRowDivEl.appendChild(oneSixthDivEl);
        }

        imagesDivEl.appendChild(imageRowDivEl);
        containerContentDivEl.appendChild(imagesDivEl);
    }
}

function toggleBigPicture() {
}

function createRealEstateAsideEl(realEstatePageDto) {
    const reservCalDivEl = document.createElement("div");
    reservCalDivEl.id = "reservation-calendar";

    const calendarDivEl = document.createElement("div");
    calendarDivEl.classList.add("calendar");

    let listOfDays = [];


    for(let i = 0; i < realEstatePageDto.availability.length; i++){
        const reservation = realEstatePageDto.availability[i];
        const begins = new Date(reservation.stringBegins);
        const ends = new Date(reservation.stringEnds);
        const tempList = getDates(begins, ends);
        console.log(tempList);
        for (let j = 0; j < tempList.length; j++){
            listOfDays.push(tempList[j]);
        }
    }

    const datepicker = new Datepickk();
    datepicker.container = calendarDivEl;
    datepicker.show();
    datepicker.range = true;
    datepicker.maxSelections = 1;
    datepicker.disabledDates = listOfDays;

    console.log(listOfDays);

    reservCalDivEl.appendChild(calendarDivEl);

    if(!realEstatePageDto.own) {
        if (realEstatePageDto.isLoggedIn) {
            const reservationButton = document.createElement("button");
            reservationButton.textContent = "Reserve";
            reservationButton.classList.add("full-width-button");
            reservationButton.id = realEstatePageDto.realEstate.id;
            reservationButton.addEventListener('click', onReservationButtonClicked);
            reservCalDivEl.appendChild(reservationButton);
        } else {
            const messagePEl = document.createElement("p");
            messagePEl.textContent = "Please log in for reservation.";
            messagePEl.classList.add("center-text");
            messagePEl.addEventListener('click', onLogInClicked);
            reservCalDivEl.appendChild(messagePEl);
        }
    } else {
        const editButton = document.createElement("button");
        editButton.textContent = "Edit";
        editButton.classList.add("full-width-button");
        editButton.addEventListener('click', function (){onEditRealEstateClicked(realEstatePageDto)});
        reservCalDivEl.appendChild(editButton);

        if(realEstatePageDto.realEstate.public){
            const unpublisButton = document.createElement("button");
            unpublisButton.textContent = "Unpublish";
            unpublisButton.classList.add("full-width-button");
            unpublisButton.id = realEstatePageDto.realEstate.id;
            unpublisButton.addEventListener('click', onPublishButtonClick);
            reservCalDivEl.appendChild(unpublisButton);
        } else {
            const publisButton = document.createElement("button");
            publisButton.textContent = "Publish";
            publisButton.classList.add("full-width-button");
            publisButton.id = realEstatePageDto.realEstate.id;
            publisButton.addEventListener('click', onPublishButtonClick);
            reservCalDivEl.appendChild(publisButton);
        }
        const checkReservationsButton = document.createElement("button");
        checkReservationsButton.textContent = "Reservations";
        checkReservationsButton.classList.add("full-width-button");
        reservCalDivEl.appendChild(checkReservationsButton);

        const deleteRealEstate = document.createElement("button");
        deleteRealEstate.textContent = "Delete";
        deleteRealEstate.classList.add("full-width-button");
        publisButton.id = realEstatePageDto.realEstate.id;
        publisButton.addEventListener('click', onDeleteRealEstateClick);
        reservCalDivEl.appendChild(deleteRealEstate);

    } containerContentDivEl.appendChild(reservCalDivEl);
}

function onReviewsLoad(realEstatePageDto) {
    const reviewContainerDivEl = document.createElement("div");
    reviewContainerDivEl.id = "reviews";
    const dividerSpanEl = insertDivider("Reviews", "divider");
    dividerSpanEl.classList.add("realestatedivider");
    reviewContainerDivEl.appendChild(dividerSpanEl);

    if(realEstatePageDto.hasReviews){
        for(let i = 0; i < realEstatePageDto.allReview.length; i++){
            if(!realEstatePageDto.allReview[i].flagged) {
                const commentDivEl = document.createElement("div");
                commentDivEl.id = "comment";

                const commentTextDivEl = document.createElement("div");
                commentTextDivEl.id = "comment-text";

                const pEl = document.createElement("p");
                pEl.classList.add("datas");
                if(!realEstatePageDto.allReview[i].hasRealEstate){
                    pEl.textContent = realEstatePageDto.allReview[i].timeStampString + "   Rate: " + realEstatePageDto.allReview[i].userRating;

                } else {
                    pEl.textContent = realEstatePageDto.allReview[i].timeStampString + "   Rate: " + realEstatePageDto.allReview[i].realEstateRating;
                }

                const pTextEl = document.createElement("p");
                pTextEl.textContent = realEstatePageDto.allReview[i].review;

                commentTextDivEl.appendChild(pEl);
                commentTextDivEl.appendChild(pTextEl);

                if(realEstatePageDto.isLoggedIn) {
                    const flagDiv = document.createElement("div");
                    flagDiv.id = realEstatePageDto.allReview[i].id;
                    flagDiv.classList.add("flag-comment");
                    flagDiv.addEventListener('click', onFlagReviewClicked);

                    const tooltipSpanEl = document.createElement("span");
                    tooltipSpanEl.textContent = "Report comment";
                    tooltipSpanEl.classList.add("tooltip");

                    commentTextDivEl.appendChild(flagDiv);
                    commentTextDivEl.appendChild(tooltipSpanEl);
                }
                commentDivEl.appendChild(commentTextDivEl);

                const commentUserDivEl = document.createElement("div");
                commentUserDivEl.id = "comment-user";

                const profilePicImgEl = document.createElement("img");
                const imgSrc = decodeBase64(realEstatePageDto.allReview[i].user.pic);
                profilePicImgEl.src = 'data:image/jpg;base64,' + imgSrc;

                const profileBoxDivEl = document.createElement("div");
                profileBoxDivEl.id = "profile-box";

                const pNameEl = document.createElement("p");
                pNameEl.textContent = "Name: " + realEstatePageDto.allReview[i].user.name;

                const pRoleEl = document.createElement("p");
                pRoleEl.textContent = "Role: " + realEstatePageDto.allReview[i].user.role;

                const pRatingEl = document.createElement("p");
                pRatingEl.textContent = "Rating: " + realEstatePageDto.allReview[i].user.avgRating;

                profileBoxDivEl.appendChild(pNameEl);
                profileBoxDivEl.appendChild(pRoleEl);
                profileBoxDivEl.appendChild(pRatingEl);

                const toProfileButtonEl = document.createElement("button");
                toProfileButtonEl.textContent = "Profile";
                toProfileButtonEl.classList.add("inverse-button");
                toProfileButtonEl.id = realEstatePageDto.allReview[i].user.name;
                toProfileButtonEl.addEventListener('click', onProfileLoad);

                commentUserDivEl.appendChild(profilePicImgEl);
                commentUserDivEl.appendChild(profileBoxDivEl);
                commentUserDivEl.appendChild(toProfileButtonEl);
                commentDivEl.appendChild(commentUserDivEl);
                reviewContainerDivEl.appendChild(commentDivEl);
            }

        }
    } else {
        const noReviewDivEl = document.createElement("div");
        newError(noReviewDivEl, "No reviews yet.")
        reviewContainerDivEl.appendChild(noReviewDivEl);
    }

    if(realEstatePageDto.isLoggedIn && !realEstatePageDto.isOwn){
        reviewContainerDivEl.appendChild(createReviewForm(realEstatePageDto.realEstateId));
    }
    return reviewContainerDivEl;
}


function getDates(startDate, endDate) {
    let dates = [],
        currentDate = startDate,
        addDays = function(days) {
            let date = new Date(this.valueOf());
            date.setDate(date.getDate() + days);
            return date;
        };
    while (currentDate <= endDate) {
        dates.push(currentDate);
        currentDate = addDays.call(currentDate, 1);
    }
    return dates;
}


function onEditRealEstateClicked() {
    
}


function onPublishButtonClick() {
    const element = this;
    const id = element.getAttribute("id");

    const params = new URLSearchParams();
    params.append('id', id);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onPublishResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('PUT', 'real-estate-handler?' + params.toString());
    xhr.send();
}

function onPublishResponse() {
    clearMessages();
    if (this.status === OK) {
        removeAllChildren(containerContentDivEl);
        setDelay(JSON.parse(this.responseText), onLoad(), 5000);
    } else {
        onOtherResponse(containerContentDivEl, this);
    }
}

function onDeleteRealEstateClick() {
    const element = this;
    const id = element.getAttribute("id");

    const params = new URLSearchParams();
    params.append('id', id);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onPublishResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('DELETE', 'real-estate-handler?' + params.toString());
    xhr.send();
}

