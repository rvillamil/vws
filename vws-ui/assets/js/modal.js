/**
 * Javascript functions
 *
 * https://www.w3schools.com/js/js_ajax_http.asp
 */
function showAlertWindow(text) {
    console.log("showAlertWindow:" + text);
    alert(text);
}

function showModalWindow(modalHeader, modalText, modalFooter) {
    // Get the modal
    var modal = document.getElementById('modalWindow');
    modal.style.display = "block";

    if (modalHeader != null) {
        document.getElementsByClassName('modal-header')[0].innerHTML = "<span class=\"close\">&times;</span><h3>" + modalHeader + "</h3>"
    }
    document.getElementsByClassName('modal-text')[0].innerHTML = "<p>" + modalText + "</p>";

    if (modalFooter != null) {
        document.getElementsByClassName('modal-footer')[0].innerHTML = "<p>" + modalFooter + "</p>";
    }
    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];
    // When the user clicks on <span> (x), close the modal
    span.onclick = function() {
        modal.style.display = "none";
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
    return modal;
}