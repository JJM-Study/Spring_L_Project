const pBtn = document.querySelector("#pBtn");
const mBtn = document.querySelector("#mBtn");
const pd_inputQty = document.querySelector(".qty");
const orderBtn = document.querySelector(".btn-ord-now");

pBtn.addEventListener("click", ()=> {
    let qty = parseInt(pd_inputQty.value) || 0;
    qty++;  // 여기에는 나중에 max 값을 받아오든 해서 할 것.
    pd_inputQty.value = qty;
});

mBtn.addEventListener("click", ()=> {
    let qty = parseInt(pd_inputQty.value) || 0;
    if(qty > 0) qty--;
    pd_inputQty.value = qty;
});

orderBtn.addEventListener("click", (event) => {
    const prodNo = event.currentTarget.dataset.prodno;
    const qty = parseInt(pd_inputQty.value);
    fetch("/order/order_prod", {
        method: "POST",
        headers: {
            'content-type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify({
            "prodNo": prodNo,
            "qty": qty
            })
        }).then((res) => {
            return res.json();
        }).then((data) => {
            if(data.success) {
                location.href = "/order/result?orderId=" + data.orderId;
            } else {
                alert("주문 실패");
            }
        })
    })
