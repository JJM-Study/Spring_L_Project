const titleSearch = document.querySelector(".search-title");
const titleBtn = document.querySelector(".search-title-btn");



titleBtn.addEventListener("click", () => {
    const search = titleSearch.value;
    console.log(search);

    location.href ="/product/products?title=" + search;

});

////    alert('구현 중...' + search);
//
//
//    await fetch("/product/products?title=" + search, {
//        method : "GET",
//        headers : {
//            'content-type': 'application/json',
//            'X-CSRF-TOKEN' : csrfToken
//        },
//
//    });