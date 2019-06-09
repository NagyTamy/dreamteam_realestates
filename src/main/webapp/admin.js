function onAdminPageClick() {

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onAdminLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'admin');
    xhr.send();
}

function onAdminLoad(){
    clearMessages();
    if (this.status === OK) {
        console.log(JSON.parse(this.responseText));
        onAdminPageLoad(JSON.parse(this.responseText));
        showContents(['container']);
    } else {
        onOtherResponse(containerContentDivEl, this);
    }
}

function onAdminPageLoad(adminPageDto) {
    removeAllChildren(containerContentDivEl);
    createRandomOffer(adminPageDto);
    createAdminAside(adminPageDto);
    markedNav("Admin");
    markedAside("Requests");
    const adminContainerDivEl = document.createElement("div");
    adminContainerDivEl.id = "admin";
    adminContainerDivEl.appendChild(onPendingRequestsLoad(adminPageDto));
    containerContentDivEl.appendChild(adminContainerDivEl);
}

function createAdminAside(adminPageDto) {
    const menuList = adminPageDto.asideMenu;
    const asideEl = document.createElement("aside");
    const navEl = document.createElement("nav");
    const ulEl = document.createElement("ul");

    if (menuList.length > 1) {
        let num = menuList.length * 49;
        let asideHeight = num + 'px';
        document.documentElement.style.setProperty('--aside-height', asideHeight);
    }

    for (let i = 0; i < menuList.length; i++) {
        const liEl = document.createElement("li");
        const liElAttr = document.createAttribute("nav-id");
        liElAttr.value = menuList[i];
        liEl.classList.add("aside-nav");
        liEl.textContent = menuList[i];
        liEl.setAttributeNode(liElAttr);
        liEl.addEventListener('click', function () {
            createContainerForAdmin(adminPageDto, liElAttr.value)
        });
        ulEl.appendChild(liEl);
    }

    navEl.appendChild(ulEl);
    asideEl.appendChild(navEl);
    containerContentDivEl.appendChild(asideEl);

    const asideUserSearchForm = document.createElement("form");
    asideUserSearchForm.id = "admin-user-search";

    const searchInputEl = document.createElement("input");
    searchInputEl.type = 'text';
    searchInputEl.placeholder = 'Search user by name...';
    searchInputEl.name = 'find_user';

    const searchButtonUserSearchEl = document.createElement("button");
    searchButtonUserSearchEl.textContent = 'Search';
    searchButtonUserSearchEl.classList.add("link");
    searchButtonUserSearchEl.addEventListener('click', searchByUserName);

    asideUserSearchForm.appendChild(searchInputEl);
    asideUserSearchForm.appendChild(searchButtonUserSearchEl);
    containerContentDivEl.appendChild(asideUserSearchForm);

    const asideRealEstateSearchForm = document.createElement("form");
    asideRealEstateSearchForm.id = "admin-re-search";

    const searchREInputEl = document.createElement("input");
    searchREInputEl.type = 'text';
    searchREInputEl.placeholder = 'Search real estate by name...';
    searchREInputEl.name = 'find_real_estate';

    const searchButtonRESearchEl = document.createElement("button");
    searchButtonRESearchEl.textContent = 'Search';
    searchButtonRESearchEl.classList.add("link");
    searchButtonRESearchEl.addEventListener('click', searchByRealEstate);

    asideRealEstateSearchForm.appendChild(searchREInputEl);
    asideRealEstateSearchForm.appendChild(searchButtonRESearchEl);
    containerContentDivEl.appendChild(asideRealEstateSearchForm);

}

function createContainerForAdmin(adminPageDto, navId) {
    markedAside(navId);
    const adminContainerDivEl = document.getElementById("admin");
    removeAllChildren(adminContainerDivEl);
    if(navId === "Requests") {
        adminContainerDivEl.appendChild(onPendingRequestsLoad(adminPageDto));
    } else if(navId === "Logs"){
        adminContainerDivEl.appendChild(onLogLoad(adminPageDto));
    } else if(navId === "System real estates"){
        adminContainerDivEl.appendChild(onCompanyRealEstateLoad(adminPageDto));
    } else if(navId === "Flagged comments"){
        adminContainerDivEl.appendChild(onFlaggedCommentsClick(adminPageDto));
    } else {
        removeAllChildren(adminContainerDivEl);
        newError(adminContainerDivEl, "Ooops, something went wrong. Please chose an option from the menu!");
    }
}

function onPendingRequestsLoad(adminPageDto) {
    /*loads and displays all unanswered user request with button for confirm/deny*/
    const pendingRequestContEl = document.createElement("div");
    pendingRequestContEl.id = "reviews";

    const dividerSpanEl = insertDivider("Pending requests", "divider");
    dividerSpanEl.classList.add("realestatedivider");
    pendingRequestContEl.appendChild(dividerSpanEl);

    const tableElement = document.createElement("table");
    const tableHeaderRow = document.createElement("tr");
    const tableHeaderDate = document.createElement("td");
    tableHeaderDate.textContent = "Date";
    tableHeaderRow.appendChild(tableHeaderDate);
    const tableHeaderSender = document.createElement("td");
    tableHeaderSender.textContent = "Sender";
    tableHeaderRow.appendChild(tableHeaderSender);
    const tableHeaderMessageType = document.createElement("td");
    tableHeaderMessageType.textContent = "Request type";
    tableHeaderRow.appendChild(tableHeaderMessageType);
    const tableHeaderRealEstate = document.createElement("td");
    tableHeaderRealEstate.textContent = "Real Estate";
    tableHeaderRow.appendChild(tableHeaderRealEstate);
    const buttonOne = document.createElement("td");
    tableHeaderRow.appendChild(buttonOne);
    const buttonTwo = document.createElement("td");
    tableHeaderRow.appendChild(buttonTwo);
    tableElement.appendChild(tableHeaderRow);

    for(let i = 0; i < adminPageDto.allPendingUserRequest.length; i++){
        const item = adminPageDto.allPendingUserRequest[i];
        const tableRow = document.createElement("tr");

        const tdDateEl = document.createElement("td");
        tdDateEl.textContent = item.stringDate;
        tableRow.appendChild(tdDateEl);

        const tdSenderEl = document.createElement("td");
        tdSenderEl.textContent = item.sender;
        tdSenderEl.id = item.sender;
        tdSenderEl.classList.add("link");
        tdSenderEl.addEventListener('click', onProfileLoad);
        tableRow.appendChild(tdSenderEl);

        const tdTypeEl = document.createElement("td");
        tdTypeEl.textContent = item.title;
        tableRow.appendChild(tdTypeEl);

        const tdRealEstateEl = document.createElement("td");
        if(item.hasRealEstate) {
            tdRealEstateEl.textContent = item.realEstateId;
            const tableREAttr = document.createAttribute("real-estate-id");
            tableREAttr.value = item.realEstateId;
            tdRealEstateEl.setAttributeNode(tableREAttr);
            tdRealEstateEl.classList.add("link");
            tdRealEstateEl.addEventListener('click', onTileClick);
        } else {
            tdRealEstateEl.textContent = "None";
        }
        tableRow.appendChild(tdRealEstateEl);

        const buttonOneTdEl = document.createElement("td");
        const buttonOne = document.createElement("button");
        buttonOne.textContent = "Permit";
        buttonOne.classList.add("link");
        buttonOne.id = item.id;
        buttonOne.addEventListener('click', function (){onAnswerPendingRequest("accept", item.id)});
        buttonOneTdEl.appendChild(buttonOne);
        tableRow.appendChild(buttonOneTdEl);

        const buttonTwoTdEl = document.createElement("td");
        const buttonTwo = document.createElement("button");
        buttonTwo.textContent = "Refuse";
        buttonTwo.classList.add("link");
        buttonTwo.id = item.id;
        buttonTwo.addEventListener('click', function (){onAnswerPendingRequest("deny", item.id)});
        buttonTwoTdEl.appendChild(buttonTwo);
        tableRow.appendChild(buttonTwoTdEl);

        tableElement.appendChild(tableRow);
    }

    pendingRequestContEl.appendChild(tableElement);
    return pendingRequestContEl;

}

function onLogLoad(adminPageDto) {
    /*lists all admin action logs, last on top in 50 batches*/
    const loadLogsContEl = document.createElement("div");
    loadLogsContEl.id = "reviews";

    const dividerSpanEl = insertDivider("Logs", "divider");
    dividerSpanEl.classList.add("realestatedivider");
    loadLogsContEl.appendChild(dividerSpanEl);

    const tableElement = document.createElement("table");
    const tableHeaderRow = document.createElement("tr");
    const tableHeaderDate = document.createElement("td");
    tableHeaderDate.textContent = "Date";
    tableHeaderRow.appendChild(tableHeaderDate);
    const tableHeaderSender = document.createElement("td");
    tableHeaderSender.textContent = "Admin";
    tableHeaderRow.appendChild(tableHeaderSender);
    const tableHeaderMessageType = document.createElement("td");
    tableHeaderMessageType.textContent = "Action";
    tableHeaderRow.appendChild(tableHeaderMessageType);
    tableElement.appendChild(tableHeaderRow);

    for (let i = 0; i < adminPageDto.allLogs.length; i++){
        const log = adminPageDto.allLogs[i];
        const tableRow = document.createElement("tr");

        const tdDateEl = document.createElement("td");
        tdDateEl.textContent = log.stringDate;
        tableRow.appendChild(tdDateEl);

        const tdSenderEl = document.createElement("td");
        if(log.adminUser) {
            tdSenderEl.textContent = log.admin.name;
            tdSenderEl.id = log.admin.name;
            tdSenderEl.classList.add("link");
            tdSenderEl.addEventListener('click', onProfileLoad);
            tableRow.appendChild(tdSenderEl);
        } else {
            tdSenderEl.textContent = "Removed admin: " + log.adminId;
            tableRow.appendChild(tdSenderEl);
        }

        const tdTypeEl = document.createElement("td");
        tdTypeEl.textContent = log.title;
        tableRow.appendChild(tdTypeEl);

        tableElement.appendChild(tableRow);

    }
    loadLogsContEl.appendChild(tableElement);
    return loadLogsContEl;
}

function onCompanyRealEstateLoad(adminPageDto) {
    /*lists all 'system' owned real estates, every admin has the right to handle (add, edit, delete) these*/
    const companyRealEstatesContEl = document.createElement("div");
    companyRealEstatesContEl.id = "reviews";

    const dividerSpanEl = insertDivider("System real estates", "divider");
    dividerSpanEl.classList.add("realestatedivider");
    companyRealEstatesContEl.appendChild(dividerSpanEl);

    if (adminPageDto.hasRealEstates) {
        const rowEl = document.createElement("div");
        rowEl.classList.add("row");

        for (let j = 0; j < adminPageDto.ownRealEstates.length; j++) {
            const item = adminPageDto.ownRealEstates[j];

            const oneThirdDivEl = document.createElement("div");
            oneThirdDivEl.classList.add("one-third");

            const imgSrc = decodeBase64(item.pic);

            const mainImgEl = document.createElement("img");
            mainImgEl.src = 'data:image/jpg;base64,' + imgSrc;

            const ringImg = document.createElement("img");
            ringImg.src = "img/dark-angled-ring.svg";

            const h3NameEl = document.createElement("h3");
            h3NameEl.textContent = item.name;

            const pEl = document.createElement("p");
            pEl.id = 'country';
            pEl.textContent = item.country;

            const descEl = document.createElement("div");
            descEl.id = "description";
            descEl.textContent = item.description;

            const buttonEl = document.createElement("button");
            buttonEl.textContent = "More";

            const realEstateIdAttr = document.createAttribute('real-estate-id');
            realEstateIdAttr.value = item.id;

            oneThirdDivEl.setAttributeNode(realEstateIdAttr);
            oneThirdDivEl.appendChild(mainImgEl);
            oneThirdDivEl.appendChild(ringImg);
            oneThirdDivEl.appendChild(h3NameEl);
            oneThirdDivEl.appendChild(pEl);
            oneThirdDivEl.appendChild(descEl);
            oneThirdDivEl.appendChild(buttonEl);
            oneThirdDivEl.addEventListener('click', onTileClick);

            rowEl.appendChild(oneThirdDivEl);
            companyRealEstatesContEl.appendChild(rowEl);
        }
    } else {
        newMessage(companyRealEstatesContEl, "message", "No system real estates available.")
    }
    return companyRealEstatesContEl;
}


function onFlaggedCommentsClick(adminPageDto){
    const reviewContainerDivEl = document.createElement("div");
    reviewContainerDivEl.id = "reviews";

    const dividerSpanEl = insertDivider("Reported reviews", "divider");
    dividerSpanEl.classList.add("realestatedivider");
    reviewContainerDivEl.appendChild(dividerSpanEl);

    for(let i = 0; i < adminPageDto.reportedReviews.length; i++){

        const review = adminPageDto.reportedReviews[i]

        const commentDivEl = document.createElement("div");
        commentDivEl.id = "comment";

        const commentTextDivEl = document.createElement("div");
        commentTextDivEl.id = "comment-text";

        const pEl = document.createElement("p");
        pEl.classList.add("datas");
        if(review.hasRealEstate){
            pEl.textContent = review.timeStampString + "   Rate: " + review.userRating;

        } else {
            pEl.textContent = review.timeStampString + "   Rate: " + review.realEstateRating;
        }

        const pTextEl = document.createElement("p");
        pTextEl.textContent = review.review;

        commentTextDivEl.appendChild(pEl);
        commentTextDivEl.appendChild(pTextEl);

        commentDivEl.appendChild(commentTextDivEl);

        const refuseButton = document.createElement("button");
        refuseButton.id = review.id;
        refuseButton.classList.add("comment-button");
        refuseButton.classList.add("link");
        refuseButton.textContent = "Refuse";
        refuseButton.addEventListener('click', onFlagReviewClicked);

        const acceptButton = document.createElement("button");
        acceptButton.id = review.id;
        acceptButton.classList.add("comment-button");
        acceptButton.classList.add("link");
        acceptButton.textContent = "Accept";
        acceptButton.addEventListener('click', onRemoveReportedCommentClick);

        reviewContainerDivEl.appendChild(commentDivEl);
        reviewContainerDivEl.appendChild(refuseButton);
        reviewContainerDivEl.appendChild(acceptButton);

        return reviewContainerDivEl;
    }
}
