document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        events: studies.map(function(study) {
            return {
                title: study.name,
                start: study.startTime,
                end: study.endTime,
                id: study.id
            };
        }),
        eventClick: function(info) {
            openStudyDetails(info.event.id);
        }
    });
    calendar.render();
});

function openStudyDetails(id) {
    $.get('/studies/details/' + id, function(data) {
        $('#study-details-body').html(data);
        $('#edit-button').attr('onclick', 'openEditForm(' + id + ')');
        $('#delete-button').attr('onclick', 'deleteStudy(' + id + ')');
        $('#study-details-modal').model('show');
    });
}

function openEditForm(id) {
    window.location.href = '/studies/edit/' + id;
}

function deleteStudy(id) {
    if (confirm('일정을 삭제하시겠습니까?')) {
        window.location.href = '/studies/delete/' + id;
    }
}
