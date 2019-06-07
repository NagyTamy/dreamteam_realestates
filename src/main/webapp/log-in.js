function onLogInClicked(){
    removeAllChildren(containerContentDivEl);
    markedNav("Log in");
    const logInFormDivEl = document.createElement("div");
    logInFormDivEl.id = "log-in-form";

    const formDiv = document.createElement("form");
    formDiv.setAttribute('onsubmit', "return false;");

    const userNameInputEl = document.createElement("input");
    userNameInputEl.type = "text";
    userNameInputEl.name = "name";
    userNameInputEl.placeholder = "Username";
    formDiv.appendChild(userNameInputEl);

    const passwordInputEl = document.createElement("input");
    passwordInputEl.type = "password";
    passwordInputEl.name = "password";
    passwordInputEl.placeholder = "Password";
    formDiv.appendChild(passwordInputEl);

    const logInButton = document.createElement("button");
    logInButton.textContent = "Log in";
    logInButton.addEventListener('click', doLogIn);
    formDiv.appendChild(logInButton);

    const regButton = document.createElement("button");
    regButton.textContent = "Register";
    regButton.classList.add("button-right");
    regButton.addEventListener('click', onRegisterClick);
    formDiv.appendChild(regButton);

    logInFormDivEl.appendChild(formDiv);
    containerContentDivEl.appendChild(logInFormDivEl);
}



function doLogIn(){
    const userNameInputEl = document.querySelector('input[name="name"]');
    const passwordInputEl = document.querySelector('input[name="password"]');

    const userName = userNameInputEl.value;
    const password = passwordInputEl.value;


    const params = new URLSearchParams();
    params.append('name', userName);
    params.append('password', password);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onHomePageLoad);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('POST', 'login');
    xhr.send(params);
}
