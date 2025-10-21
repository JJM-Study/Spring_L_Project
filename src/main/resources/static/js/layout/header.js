const logoutBtn = document.querySelector(".logout");

logoutBtn?.addEventListener("click", (event) => {
    event.preventDefault();

    const logoutForm = document.createElement("form");
    // const csrfInput = document.querySelector('meta[name="_csrf"]');
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfInput = document.createElement("input");


    logoutForm.action="/logout";
    logoutForm.method="POST";

    // csrfInput.type = "hidden";


    document.body.appendChild(logoutForm);
    csrfInput.type="hidden";
    csrfInput.name="_csrf";
    csrfInput.value=csrfToken;
    logoutForm.appendChild(csrfInput);


    logoutForm.submit();

})