const signUpForm = document.getElementById("signUpForm");
const signUpBtn = document.getElementById("signUpButton");

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