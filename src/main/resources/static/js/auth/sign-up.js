const signUpForm = document.getElementById("signUpForm");
const signUpBtn = document.getElementById("signUpButton");
const csrfToken = document.querySelector('meta[name=_csrf]').getAttribute('content');


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

const fd = new FormData(signUpForm);

   const response = await fetch("/sign-up", {
   method : "POST",
   body : fd,
headers: {
  "Content-Type": "application/json",
  "X-CSRF-TOKEN": csrfToken
}

   });
   console.log("response : " + response);
   const data = await response.json();
   console.log("message : " + data.message);

   alert(data.message);

});