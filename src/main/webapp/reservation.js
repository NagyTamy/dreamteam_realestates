function onReservationButtonClicked(){}


function onDeleteReservationButtonClicked(){
    const element = this;
    const id = element.getAttribute("id");

    const params = new URLSearchParams();
    params.append('id', id);

    const xhr = new XMLHttpRequest();
    xhr.addEventListener('load', onSentReviewResponse);
    xhr.addEventListener('error', onNetworkError);
    xhr.open('DELETE', 'reservation?' + params.toString());
    xhr.send();
}