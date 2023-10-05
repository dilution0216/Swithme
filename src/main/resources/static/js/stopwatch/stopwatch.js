import { makeTime, makeSec, switchDisplay, refreshText, recordText } from "./modules/modules.js"
const btn_start = document.querySelector("#stopwatch-start")
const btn_pause = document.querySelector("#stopwatch-pause")
const btn_record = document.querySelector("#stopwatch-record")
const btn_restart = document.querySelector("#stopwatch-restart")
const div_record = document.querySelector("#stopwatch-records")
let clock, clockInterval

btn_start.addEventListener("click", () => {
    clock = document.querySelector("#stopwatch-clock")
    const _ = clock.innerText.split(':')
    let minn = _[0],
        secc = _[1]

    let curSec = makeSec(minn, secc)
    clockInterval = setInterval(() => {
        curSec += 1
        clock.innerText = makeTime(curSec)
    }, 1000)
    btn_start.innerText = "계속"
    btn_restart.hidden = false
    switchDisplay([btn_start, btn_pause, btn_record])
})

btn_pause.addEventListener("click", () => {
    clearInterval(clockInterval)
    switchDisplay([btn_start, btn_pause, btn_record])
})

// btn_record.addEventListener("click", () => {
//     const recTime = document.querySelector("#stopwatch-clock").innerText
//     recordText(recTime, div_record)
// })

btn_record.addEventListener("click", () => {
    const recTime = document.querySelector("#stopwatch-clock").innerText;
    const recordDto = { recordedTime: recTime };

    fetch('/api/record', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(recordDto),
    })
        .then(response => {
            if (response.ok) {
                // 성공적으로 저장되었을 때의 처리
                refreshText(document.querySelector("#stopwatch-clock"), div_record);
                clearInterval(clockInterval);
                btn_start.innerText = "시작";
                btn_start.hidden = false;
                btn_pause.hidden = true;
                btn_record.hidden = true;
                btn_restart.hidden = true;

                // 오늘의 누적 시간을 다시 가져와서 업데이트
                return fetch('/api/record/today');  // 이 부분 추가
            } else {
                // 오류 처리
                throw new Error('Network response was not ok');
            }
        })
        .then(response => response.json())  // 이 부분 추가
        .then(minutes => {  // 이 부분 추가
            const displayTime = formatMinutesToDisplayTime(minutes);
            document.getElementById('display-today-time').textContent = displayTime;

            return fetch('/api/record/total');
        })
        .then(response => response.json()) // 이 부분 추가
        .then(totalMinutes => { // 이 부분 추가
            const displayTime = formatMinutesToDisplayTime(totalMinutes);
            document.getElementById('display-total-time').textContent = displayTime;  // 'display-total-time'는 총 누적 시간을 표시하는 요소의 ID여야 합니다.
        })


        .catch(error => {
            // 네트워크 오류 처리
            console.error('Fetch error:', error);
        });
});


btn_restart.addEventListener("click", () => {
    refreshText(document.querySelector("#stopwatch-clock"), div_record)
    clearInterval(clockInterval)
    btn_start.innerText = "시작"
    btn_start.hidden = false
    btn_pause.hidden = true
    btn_record.hidden = true
    btn_restart.hidden = true
})



// 페이지가 로드되면 오늘의 누적 시간을 가져옵니다.
document.addEventListener('DOMContentLoaded', function() {
    fetch('/api/record/today')
        .then(response => response.json())
        .then(minutes => {
            const displayTime = formatMinutesToDisplayTime(minutes);
            document.getElementById('display-today-time').textContent = displayTime;
        })
        .catch(error => console.error("Error fetching today's accumulated time:", error));
});

// 페이지가 로드되면 누적 시간을 가져옵니다.
document.addEventListener('DOMContentLoaded', function() {
    fetch('/api/record/total')
        .then(response => response.json())
        .then(minutes => {
            const displayTime = formatMinutesToDisplayTime(minutes);
            document.getElementById('display-total-time').textContent = displayTime;
        })
        .catch(error => console.error("Error fetching total's accumulated time:", error));
});



// 분을 "00:00" 형식으로 포맷팅하는 함수
function formatMinutesToDisplayTime(minutes) {
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return `${hours.toString().padStart(2, '0')}:${mins.toString().padStart(2, '0')}`;
}


// 왼쪽 세로 네비게이션 버튼표시
window.addEventListener("load", function() {

// 네비게이션 항목을 선택
    const navItems = document.querySelectorAll(".app-sidebar-link");
    for (let i = 0; i < navItems.length; i++) {
        let element = navItems.item(i);
        element.classList.remove('active')

    }

    const nav = document.querySelector(".nav-stopwatch");
    nav.classList.add('active')
});