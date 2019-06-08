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
    if(!realEstatePageDto.isOwn) {
        const reservCalDivEl = document.createElement("div");
        reservCalDivEl.id = "reservation-calendar";

        const calendarDivEl = document.createElement("div");
        calendarDivEl.classList.add("calendar");

        const datepicker = new Datepickk();
        datepicker.container = calendarDivEl;
        datepicker.show();
        datepicker.range = true;
        datepicker.maxSelections = 1;

        reservCalDivEl.appendChild(calendarDivEl);

        if (realEstatePageDto.isLoggedIn) {
            const reservationButton = document.createElement("button");
            reservationButton.textContent = "Reserve";
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
        containerContentDivEl.appendChild(reservCalDivEl);
    }
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