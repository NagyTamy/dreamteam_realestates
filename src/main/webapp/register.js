function onRegisterClick() {
    removeAllChildren(containerContentDivEl);
    markedNav("Register");

    const regFormDivEl = document.createElement("div");
    regFormDivEl.id = "registration-form";

    const formDiv = document.createElement("form");
    formDiv.setAttribute('onsubmit', "return false;");

    const userNameInputEl = document.createElement("input");
    userNameInputEl.type = "text";
    userNameInputEl.name = "name";
    userNameInputEl.placeholder = "Username";
    formDiv.appendChild(userNameInputEl);

    const eMailInputEl = document.createElement("input");
    eMailInputEl.type = "text";
    eMailInputEl.name = "email";
    eMailInputEl.placeholder = "E-mail";
    formDiv.appendChild(eMailInputEl);

    const passwordInputEl = document.createElement("input");
    passwordInputEl.type = "password";
    passwordInputEl.name = "password";
    passwordInputEl.placeholder = "Password";
    formDiv.appendChild(passwordInputEl);

    const passwordRepeatInputEl = document.createElement("input");
    passwordRepeatInputEl.type = "password";
    passwordRepeatInputEl.name = "password-check";
    passwordRepeatInputEl.placeholder = "Repeat password";
    formDiv.appendChild(passwordRepeatInputEl);

    const logInButton = document.createElement("button");
    logInButton.textContent = "Log in";
    logInButton.addEventListener('click', onLogInClicked);
    formDiv.appendChild(logInButton);

    const regButton = document.createElement("button");
    regButton.textContent = "Register";
    regButton.classList.add("button-right");
    regButton.addEventListener('click', doRegistration);
    formDiv.appendChild(regButton);

    regFormDivEl.appendChild(formDiv);
    containerContentDivEl.appendChild(regFormDivEl);
}

function doRegistration() {
    const userNameInputEl = document.querySelector('input[name="name"]');
    const emailInputEl = document.querySelector('input[name=email]');
    const passwordInputEl = document.querySelector('input[name="password"]');
    const passwordCheckInputEl = document.querySelector('input[name=password-check]')

    const userName = userNameInputEl.value;
    const password = passwordInputEl.value;
    const passwordChk = passwordCheckInputEl.value;
    const eMail = emailInputEl.value;



    const params = new URLSearchParams();
    params.append('name', userName);
    params.append('email', eMail);
    params.append('password', password);
    params.append('passwordChk', passwordChk);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onHomePageLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'register');
    xhr.send(params);
}