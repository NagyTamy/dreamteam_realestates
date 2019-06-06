function onLoadConversation(listOfMessages, senderName, receiverName) {
    removeAllChildren(profileDiv);

    const dividerSpanEl = insertDivider(receiverName, "divider");
    dividerSpanEl.classList.add("realestatedivider");
    profileDiv.appendChild(dividerSpanEl);

    const messageHistoryDivEl = document.createElement("div");
    messageHistoryDivEl.id = "message-history";


    for(let i = 0; i < listOfMessages.length; i++){
        if(listOfMessages[i].iAmReceiver){
            const incomingMessageDiv = document.createElement("div");
            incomingMessageDiv.classList.add("incoming");

            const contentPEl = document.createElement("p");
            contentPEl.classList.add("content");
            contentPEl.textContent = listOfMessages[i].message;


            const tooltipTimeEl = document.createElement("p");
            tooltipTimeEl.classList.add("tooltip-incoming");
            tooltipTimeEl.textContent = listOfMessages[i].stringDate;

            contentPEl.addEventListener('click', function () {displayTime(tooltipTimeEl)});

            incomingMessageDiv.appendChild(contentPEl);
            incomingMessageDiv.appendChild(tooltipTimeEl);
            messageHistoryDivEl.appendChild(incomingMessageDiv);
        } else {
            const incomingMessageDiv = document.createElement("div");
            incomingMessageDiv.classList.add("outgoing");

            const contentPEl = document.createElement("p");
            contentPEl.classList.add("content");
            contentPEl.textContent = listOfMessages[i].message;


            const tooltipTimeEl = document.createElement("p");
            tooltipTimeEl.classList.add("tooltip-outgoing");
            tooltipTimeEl.textContent = listOfMessages[i].stringDate;

            contentPEl.addEventListener('click', function () {displayTime(tooltipTimeEl)});

            incomingMessageDiv.appendChild(contentPEl);
            incomingMessageDiv.appendChild(tooltipTimeEl);
            messageHistoryDivEl.appendChild(incomingMessageDiv);
        }
    }

    const messageSenderForm = document.createElement("form");
    messageSenderForm.id = "new-message";

    const messageContentTextarea = document.createElement("textarea");
    messageContentTextarea.name = "new-message";
    messageContentTextarea.rows = 2;
    messageContentTextarea.placeholder = "Type your answer here...";

    const sendMessageButton = document.createElement("button");
    sendMessageButton.classList.add("button-right");
    sendMessageButton.textContent = "Send";

    const title = document.createElement("input");
    title.type = 'hidden';
    title.name = 'title';
    title.value = listOfMessages[0].title;

    const history = document.createElement("input");
    history.type = 'hidden';
    history.name = 'history';
    history.value = listOfMessages[listOfMessages.length-1].id;

    const sender = document.createElement("input");
    sender.type = 'hidden';
    sender.name = 'senderName';
    sender.value = senderName;

    const receiver = document.createElement("input");
    receiver.type = 'hidden';
    receiver.name = 'receiverName';
    receiver.value = receiverName;

    sendMessageButton.addEventListener('click', onSendNewMessage);

    messageSenderForm.appendChild(messageContentTextarea);
    messageSenderForm.appendChild(title);
    messageSenderForm.appendChild(history);
    messageSenderForm.appendChild(sender);
    messageSenderForm.appendChild(receiver);
    messageSenderForm.appendChild(sendMessageButton);

    profileDiv.appendChild(messageHistoryDivEl);
    profileDiv.appendChild(messageSenderForm);
}


function displayTime(tooltipEl) {
    if(getComputedStyle(tooltipEl, null).display === "none"){
        tooltipEl.style.display = "block";
    } else {
        tooltipEl.style.display = "none";
    }

}

/*<div id="container">

    <div id="new-message">
        <textarea name="new-message" rows="2" placeholder="Type your answer here..."></textarea>
        <button class="button-right">Send</button>
    </div>
</div>*/

function onSendNewMessage() {
    
}