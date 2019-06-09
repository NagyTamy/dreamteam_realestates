function onLoadConversation(listOfMessages, senderName, receiverName) {
    removeAllChildren(profileDiv);

    const convoDivEl = document.createElement("div");
    convoDivEl.id = "convo";

    const messageHistoryDivEl = document.createElement("div");
    messageHistoryDivEl.id = "message-history";
    messageHistoryDivEl.scrollTop = messageHistoryDivEl.scrollHeight - messageHistoryDivEl.clientHeight;

    const dividerSpanEl = insertDivider(receiverName, "divider");
    dividerSpanEl.classList.add("realestatedivider");
    convoDivEl.appendChild(dividerSpanEl);


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
    messageSenderForm.setAttribute('onsubmit', "return false;");

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

    sendMessageButton.addEventListener('click', onSendAnswer);

    messageSenderForm.appendChild(messageContentTextarea);
    messageSenderForm.appendChild(title);
    messageSenderForm.appendChild(history);
    messageSenderForm.appendChild(sender);
    messageSenderForm.appendChild(receiver);
    messageSenderForm.appendChild(sendMessageButton);

    convoDivEl.appendChild(messageHistoryDivEl);
    convoDivEl.appendChild(messageSenderForm);
    profileDiv.appendChild(convoDivEl);
}


function displayTime(tooltipEl) {
    if(getComputedStyle(tooltipEl, null).display === "none"){
        tooltipEl.style.display = "block";
    } else {
        tooltipEl.style.display = "none";
    }

}

function onSendAnswer() {
    const contentEl = document.querySelector('textarea');
    const titleEl = document.querySelector('input[name=title]');
    const previousMessageEl = document.querySelector('input[name=history]');
    const senderNameEl = document.querySelector('input[name=senderName]');
    const receiverNameEl = document.querySelector('input[name=receiverName]');

    const content = contentEl.value;
    const title = titleEl.value;
    const history = previousMessageEl.value;
    const sender = senderNameEl.value;
    const receiver = receiverNameEl.value;

    const params = new URLSearchParams();
    params.append('content', content);
    params.append('title', title);
    params.append('history', history);
    params.append('sender', sender);
    params.append('receiver', receiver);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onMessageSentResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('PUT', 'message?' + params.toString());
    xhr.send();
}

function onMessageSentResponse() {
    clearMessages();
    if (this.status === OK) {
        removeAllChildren(containerContentDivEl);
        setDelay(JSON.parse(this.responseText), onLoad(), 5000);
    } else {
        onOtherResponse(containerContentDivEl, this);
    }
}



function sendPrivateMessageToUser(receiverName) {
    /*loads the message sending page, sender set to logged in user, receiver set to profile owner*/
    const convoContainer = document.createElement("div");
    convoContainer.id = "convo";

    const messageSenderForm = document.createElement("form");
    messageSenderForm.id = "new-message";
    messageSenderForm.setAttribute('onsubmit', "return false;");

    const title = document.createElement("input");
    title.type = 'text';
    title.name = 'title';
    title.placeholder = 'Add title';

    const messageContentTextarea = document.createElement("textarea");
    messageContentTextarea.name = "new-message";
    messageContentTextarea.rows = 2;
    messageContentTextarea.placeholder = "Type your message here...";

    const sendMessageButton = document.createElement("button");
    sendMessageButton.classList.add("button-right");
    sendMessageButton.textContent = "Send";


    const receiver = document.createElement("input");
    receiver.type = 'hidden';
    receiver.name = 'receiverName';
    receiver.value = receiverName;

    sendMessageButton.addEventListener('click', onSendNewMessage);

    messageSenderForm.appendChild(title);
    messageSenderForm.appendChild(messageContentTextarea);
    messageSenderForm.appendChild(receiver);
    messageSenderForm.appendChild(sendMessageButton);

    convoContainer.appendChild(messageSenderForm);

    return convoContainer;
}

function onSendNewMessage() {
    const contentEl = document.querySelector('textarea');
    const titleEl = document.querySelector('input[name=title]');
    const receiverNameEl = document.querySelector('input[name=receiverName]');

    const content = contentEl.value;
    const title = titleEl.value;
    const receiver = receiverNameEl.value;

    const params = new URLSearchParams();
    params.append('content', content);
    params.append('title', title);
    params.append('receiver', receiver);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onMessageSentResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'message');
    xhr.send(params);
}


