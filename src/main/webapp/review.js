function createReviewForm() {
    const commentFormDiv = document.createElement("div");
    commentFormDiv.id = "comment-form";
    
    const formDiv = document.createElement("form");
    
    const textareaInputEl = document.createElement("textarea");
    textareaInputEl.name = "comment";
    textareaInputEl.rows = 5;
    textareaInputEl.placeholder = "Type your comment here...";
    formDiv.appendChild(textareaInputEl);
    
    const inputRatingEl = document.createElement("input");
    inputRatingEl.type = "number";
    inputRatingEl.id = "rating-input";
    inputRatingEl.min = "1";
    inputRatingEl.max = "5";
    formDiv.appendChild(inputRatingEl);
    
    const sendReviewButtonEl = document.createElement("button");
    sendReviewButtonEl.textContent = "Send review";
    sendReviewButtonEl.addEventListener('click', sendReview);
    formDiv.appendChild(sendReviewButtonEl);

    commentFormDiv.appendChild(formDiv);
    return commentFormDiv;
}

function sendReview() {
    
}


function onFlagReviewClicked() {}