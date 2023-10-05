// function UpdateGroup() {
//     const groupName = document.querySelector("#groupName").value;
//     const description = document.querySelector("#description").value;
//
//     const data = {
//         groupName : groupName,
//         description : description
//     }
//
//     $.ajax({
//         type:'PUT',
//         url:`/api/group/${groupId}`,
//         contentType: "application/json",
//         data: JSON.stringify(data),
//         success: function(response) {
//             alert("그룹스터디 수정 성공");
//             window.location.href = `/api/view/groups`;
//         }, error: function(req,status,error) {
//             alert("그룹 생성자만 수정이 가능합니다");
//         }
//     })
// }