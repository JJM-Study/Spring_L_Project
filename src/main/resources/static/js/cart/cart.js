const inputQty = document.querySelector(".qty");


    document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.add-to-cart-btn').forEach(function (btn) {
        btn.addEventListener('click', function() {
            const prodNo = this.dataset.prodno;
            const qty = parseInt(inputQty.value);

               fetch("/cart/add", {
                   method: "POST",
                   headers: {
                       'Content-Type': 'application/json',
                       'X-CSRF-TOKEN': csrfToken
                   },
                   body: JSON.stringify({
                       "prodNo": prodNo,
                       "qty": qty
                   })
               }).then((res) => {
                       return res.json();
               }).then((data) => {
                       debugger;
                       if (data.success) {
                           if (confirm("담기 성공했습니다. 장바구니로 이동하시겠습니까?")) {
                               window.location.href = "/cart/cartlist";
                           }
                       } else {
                           alert("담기 실패 : " + data.message);
                       }
                   }).catch((err) => {
                         alert("에러 발생 : " + err.message);
               });
           });
        });
   });