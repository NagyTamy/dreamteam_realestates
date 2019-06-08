function createReviewForm(realEstateId) {
    const commentFormDiv = document.createElement("div");
    commentFormDiv.id = "comment-form";
    
    const formDiv = document.createElement("form");
    formDiv.setAttribute('onsubmit', "return false;");
    
    const textareaInputEl = document.createElement("textarea");
    textareaInputEl.name = "comment";
    textareaInputEl.rows = 5;
    textareaInputEl.placeholder = "Type your comment here...";
    formDiv.appendChild(textareaInputEl);
    
    const inputRatingEl = document.createElement("input");
    inputRatingEl.type = "number";
    inputRatingEl.name = "rate";
    inputRatingEl.id = "rating-input";
    inputRatingEl.min = "1";
    inputRatingEl.max = "5";
    inputRatingEl.value ="3";
    formDiv.appendChild(inputRatingEl);

    const realEstateIdEl = document.createElement( "input");
    realEstateIdEl.type = "hidden";
    realEstateIdEl.name = "id";
    realEstateIdEl.value = realEstateId;
    formDiv.appendChild(realEstateIdEl);

    const sendReviewButtonEl = document.createElement("button");
    sendReviewButtonEl.textContent = "Send review";
    sendReviewButtonEl.addEventListener('click', sendReview);
    formDiv.appendChild(sendReviewButtonEl);

    commentFormDiv.appendChild(formDiv);
    return commentFormDiv;
}

function sendReview() {

    const commentContentEl = document.querySelector('textarea');
    const ratingEl = document.querySelector('input[name=rate]');
    const realEstateId = document.querySelector('input[name=id]');

    const content = commentContentEl.value;
    const rating = ratingEl.value;
    const id = realEstateId.value;

    const params = new URLSearchParams();
    params.append('id', id);
    params.append('content', content);
    params.append('rating', rating);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSentReviewResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'review');
    xhr.send(params);

}

function onSentReviewResponse() {
    clearMessages();
    if (this.status === OK) {
        removeAllChildren(containerContentDivEl);
        setDelay(JSON.parse(this.responseText), onLoad(), 5000);
    } else {
        onOtherResponse(containerContentDivEl, this);
    }
}
function onFlagReviewClicked() {

}

function onReviewUpdateClicked(){
    const element = this;
    const id = element.getAttribute('id');

    const params = new URLSearchParams();
    params.append('id', id);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onReviewLoadToUpdate);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'review?' + params.toString());
    xhr.send();
}

function onReviewLoadToUpdate() {
    clearMessages();
    if (this.status === OK) {
        removeAllChildren(containerContentDivEl);
        onLoadReview(JSON.parse(this.responseText));
    } else {
        onOtherResponse(containerContentDivEl, this);
    }
}

function onLoadReview(comment) {
    const commentFormDiv = document.createElement("div");
    commentFormDiv.id = "comment-form";

    const formDiv = document.createElement("form");
    formDiv.setAttribute('onsubmit', "return false;");

    const textareaInputEl = document.createElement("textarea");
    textareaInputEl.name = "comment";
    textareaInputEl.rows = 5;
    textareaInputEl.textContent = comment.review;
    formDiv.appendChild(textareaInputEl);

    const inputRatingEl = document.createElement("input");
    inputRatingEl.type = "number";
    inputRatingEl.name = "rate";
    inputRatingEl.id = "rating-input";
    inputRatingEl.min = "1";
    inputRatingEl.max = "5";
    inputRatingEl.value = comment.realEstateRating;
    formDiv.appendChild(inputRatingEl);

    const realEstateIdEl = document.createElement( "input");
    realEstateIdEl.type = "hidden";
    realEstateIdEl.name = "id";
    realEstateIdEl.value = comment.id;
    formDiv.appendChild(realEstateIdEl);

    const sendReviewButtonEl = document.createElement("button");
    sendReviewButtonEl.textContent = "Update review";
    sendReviewButtonEl.addEventListener('click', sendReviewUpdate);
    formDiv.appendChild(sendReviewButtonEl);

    commentFormDiv.appendChild(formDiv);
    containerContentDivEl.appendChild(commentFormDiv);
}

function sendReviewUpdate(){
    const commentContentEl = document.querySelector('textarea');
    const ratingEl = document.querySelector('input[name=rate]');
    const realEstateId = document.querySelector('input[name=id]');

    const content = commentContentEl.value;
    const rating = ratingEl.value;
    const id = realEstateId.value;

    const params = new URLSearchParams();
    params.append('id', id);
    params.append('content', content);
    params.append('rating', rating);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSentReviewResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('PUT', 'review?' + params.toString());
    xhr.send();
}

function onReviewDeleteClicked() {
    const element = this;
    const id = element.getAttribute("id");

    const params = new URLSearchParams();
    params.append('id', id);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSentReviewResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('DELETE', 'review?' + params.toString());
    xhr.send();
}