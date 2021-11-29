$(document).ready(function () {


    let client = document.getElementById('CLIENT');
    let coach = document.getElementById('COACH');

    client.setAttribute("checked", "checked");
    client.checked = true;
    $('#coachContent').hide();


    $('#CLIENT').click(function () {
        client.setAttribute("checked", "checked");
        client.checked = true;

        coach.setAttribute("checked", "");
        coach.removeAttribute("checked");
        coach.checked = false;

        $('#clientContent').show();
        $('#coachContent').hide();
        // $('#CLIENT').prop('checked', true);
        //  $('#COACH').prop('checked', false);
    });
    $('#COACH').click(function () {
        coach.setAttribute("checked", "checked");
        coach.checked = true;

        client.setAttribute("checked", "");
        client.removeAttribute("checked");
        client.checked = false;


        $('#clientContent').hide();
        $('#coachContent').show();
        // $('#COACH').prop('checked', true);
        // $('#CLIENT').prop('checked', false);
    });
});




