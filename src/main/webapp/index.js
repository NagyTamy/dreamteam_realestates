const OK = 200;
const BAD_REQUEST = 400;
const UNAUTHORIZED = 401;
const NOT_FOUND = 404;
const INTERNAL_SERVER_ERROR = 500;

let navContentDivEl;
let containerContentDivEl;
let headerTextEl;
let headerImgEl;
const brEl = document.createElement("br");
let profileDiv;

function markedAside(id) {
    const contentEls = document.getElementsByClassName('aside-nav');
    console.log(contentEls);
    for (let i = 0; i < contentEls.length; i++) {
        const contentEl = contentEls[i];
        const navId = contentEl.getAttribute("nav-id");
        if (id.includes(navId)) {
            contentEl.classList.add('marked-aside');
        } else {
            contentEl.classList.remove('marked-aside');
        }
    }
}

function markedNav(id) {
    const contentEls = document.getElementsByClassName('main');
    console.log(contentEls);
    for (let i = 0; i < contentEls.length; i++) {
        const contentEl = contentEls[i];
        const navId = contentEl.getAttribute("menulist-id");
        if (id.includes(navId)) {
            contentEl.classList.add('marked-nav');
        } else {
            contentEl.classList.remove('marked-nav');
        }
    }
}

function clearMessages() {
    const messageEls = document.getElementsByClassName('message');
    for (let i = 0; i < messageEls.length; i++) {
        const messageEl = messageEls[i];
        messageEl.remove();
    }
}

function newError(targetEl, message) {
    newMessage(targetEl, 'error', message);
}

function newMessage(targetEl, cssClass, message) {
    clearMessages();

    const pEl = document.createElement('p');
    pEl.classList.add('message');
    pEl.classList.add(cssClass);
    pEl.textContent = message;

    targetEl.appendChild(pEl);
}


function showContents(ids) {
    const contentEls = document.getElementsByClassName('content');
    for (let i = 0; i < contentEls.length; i++) {
        const contentEl = contentEls[i];
        if (ids.includes(contentEl.id)) {
            contentEl.classList.remove('hidden');
        } else {
            contentEl.classList.add('hidden');
        }
    }
}

function removeAllChildren(el) {
    while (el.firstChild) {
        el.removeChild(el.firstChild);
    }
}

function insertDivider(string, className) {
    const dividerDivEl = document.createElement('div');
    dividerDivEl.classList.add(className);
    const dividerSpanEl = document.createElement('span');
    const dividerFourSpanEl = document.createElement('span');
    const dividerSpanThreeEl = document.createElement('span');
    dividerSpanThreeEl.textContent = string;

    dividerDivEl.appendChild(dividerSpanEl);
    dividerDivEl.appendChild(dividerSpanThreeEl);
    dividerDivEl.appendChild(dividerFourSpanEl);

    return dividerDivEl;
}

function onNetworkError(response) {
    document.body.remove();
    const bodyEl = document.createElement('body');
    document.appendChild(bodyEl);
    newError(bodyEl, 'Network error, please try reloaing the page');
}

function onOtherResponse(targetEl, xhr) {
    if (xhr.status === NOT_FOUND) {
        newError(targetEl, 'Not found');
        console.error(xhr);
    } else {
        const json = JSON.parse(xhr.responseText);
        if (xhr.status === INTERNAL_SERVER_ERROR) {
            newError(targetEl, `Server error: ${json.message}`);
        } else if (xhr.status === UNAUTHORIZED || xhr.status === BAD_REQUEST) {
            newError(targetEl, json.message);
        } else {
            newError(targetEl, `Unknown error: ${json.message}`);
        }
    }
}

function onLoad(){
    navContentDivEl = document.getElementById('menu');
    containerContentDivEl = document.getElementById('container');
    profileDiv = document.createElement("div");
    profileDiv.id = "profile";
    headerTextEl = document.getElementById("header-text");
    headerImgEl = document.getElementById("header-image");


    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onHomePageLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('GET', 'home');
    xhr.send();
}

document.addEventListener('DOMContentLoaded', onLoad);