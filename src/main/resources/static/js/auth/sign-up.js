const signUpForm = document.getElementById("signUpForm");
const signUpBtn = document.getElementById("signUpButton");
const checkIdBtn = document.getElementById("existId");
const showPwBtns = document.querySelectorAll(".showPwBtn");

//const csrfToken = document.querySelector('meta[name="_csrf"]').content;

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

       const username = document.querySelector('input[name="username"]').value;

       console.log("sign-up test");

       const isValidId = await checkId();

       if(isValidId === undefined) {
            return;
       }

       if(!isValidId) {

            alert(username + "은 이미 존재하는 아이디입니다.");
                return;
       }

        const password = document.querySelector('input[name="password"]').value;
        const passwordCheck = document.querySelector('input[name="check-password"]').value;

        if(password !== passwordCheck) {
            alert("패스워드가 일치하지 않습니다.");
            return;
        }


//       const fd = new FormData(signUpForm);



   try {

       const response = await fetch("/sign-up", {
           method: "POST",
//           body: fd

            headers : {
                'Content-Type' : 'application/json',
                'X-CSRF-TOKEN' : csrfToken
            },

            body : JSON.stringify ({
                "username" : username,
                "password" : password,
//                "check-password" : passwordCheck
            })
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

showPwBtns.forEach(button => {

    button.addEventListener("click", () => {

       const parentLabel = button.closest("label");

       const PasswordInput = parentLabel.querySelector('input');

       if (PasswordInput.type === "password") {
            PasswordInput.type = "text";
       } else {
            PasswordInput.type = "password";
       }
    })

});
//
//showPwBtn.addEventListener("click", () => {
//
//    if (passwdBtn.type === "password") {
//        passwdBtn.type = "text";
//    }
//    else {
//        passwdBtn.type = "password";
//    }
//
//});
//
//showPwBtn.addEventListener("click", () => {
//
//    if (passwdChgBtn.type === "password") {
//        passwdChgBtn.type = "text";
//    }
//    else {
//        passwdChgBtn.type = "password";
//    }
//
//});



checkIdBtn.addEventListener("click", async() => {
    const username = document.querySelector('input[name="username"]').value;
    const isValidId = await checkId();

    console.log("isValidId : " + isValidId);

    if(!isValidId) {
        alert(username + "은 이미 존재하는 아이디입니다.");
    } else {
         alert(username + "는 사용할 수 있는 아이디입니다.");
   }

})

async function checkId() {
    const username = document.querySelector('input[name="username"]').value;
        console.log("check username : " + username);

            const valid = isValidaUsername(username);
            //debugger;
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
//            debugger;
            const result = await response.json();
            console.log("result " + result.username);
            if (result.username === "" || result.username === null || result.username === undefined) {
                 return true;
            } else {
                 return false;
            }
}