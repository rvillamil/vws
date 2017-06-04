/**
 * Javascript functions
 *
 * https://www.w3schools.com/js/js_ajax_http.asp
 */
function showAlertWindow(text) {
    console.log("showAlertWindow:" + text);
    alert(text);
}

function showModalWindow(text) {
    // Get the modal
    var modal = document.getElementById('modalWindow');
    modal.style.display = "block";
    document.getElementById('modal-text').innerHTML = text;

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
}