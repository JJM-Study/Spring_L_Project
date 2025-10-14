const signUpForm = document.getElementById("signUpForm");
const signUpBtn = document.getElementById("signUpButton");
const checkIdBtn = document.getElementById("existId");
//fetch("/sign-up", {
//    method: "POST",
//    headers: {
//        "Content-Type": "application/json"
//    }
////    body: JSON.stringify(resultData)
//
//}).then(response => {
//    alert(response);
//})

signUpForm.addEventListener("submit", async (e) => {
e.preventDefault();

// const fd = new FormData(signUpForm);

   try {

       console.log("sign-up test");

       const fd = new FormData(signUpForm);


       const response = await fetch("/sign-up", {
           method: "POST",
           body: fd
           //    headers: {
           //   "Content-Type": "application/json",
           //   "X-CSRF-TOKEN": csrfToken
           // }

       });



       console.log("response : " + response);
       const resultData = await response.json();
       console.log("message : " + resultData.message);

       alert(resultData.message);

       if (response.ok) {
           location.href = "/login";
       }

   } catch(error) {
       console.log("서버 요청 중 에러 발생 : " + error);
   }

});


checkIdBtn.addEventListener("click", async() => {

    const username = document.querySelector('input[name="username"]').value;
    console.log("check username : " + username);

    const valid = isValidaUsername(username);

    if(valid === VALIDATION_STATUS.EMPTY_INVALID) {
        alert("아이디를 입력해주세요.");
        return;
    }

    if(valid === VALIDATION_STATUS.LENGTH_INVALID) {
        alert("아이디 길이는 4~12자 이내여야 합니다.");
        return;
    }

    if (valid === VALIDATION_STATUS.FORMAT_INVALID) {
        alert("특수문자는 사용할 수 없습니다.");
        return;
    }



    const response = await fetch("/exist-id", {
    method : "POST",
    headers: {
         'Content-Type': 'application/json',
         'X-CSRF-TOKEN': csrfToken
    },
    body : JSON.stringify({"username" : username})

    })

    console.log("response :" + JSON.stringify(response));

    const result = await response.json();
    console.log("result" + result);
    if (result.isExist) {
        alert(username + "은 이미 존재하는 아이디입니다.");
    } else {
        alert(username + "은 사용 가능한 아이디입니다.");
    }

})