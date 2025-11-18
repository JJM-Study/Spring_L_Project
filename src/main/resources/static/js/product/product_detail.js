const pBtn = document.querySelector("#pBtn");
const mBtn = document.querySelector("#mBtn");
let pd_inputQty = document.querySelector(".qty");
const orderBtn = document.querySelector(".btn-ord-now");


let pd_isInLyb = itemData.dataset.isInLyb;
let pd_prodType = itemData.dataset.prodType;

pBtn.addEventListener("click", ()=> {
    let qty = parseInt(pd_inputQty.value) || 0;
    qty++;  // 여기에는 나중에 max 값을 받아오든 해서 할 것.
    pd_inputQty.value = qty;
    calculation();
});

mBtn.addEventListener("click", ()=> {
    let qty = parseInt(pd_inputQty.value) || 0;
    if(qty > 1) qty--;
    pd_inputQty.value = qty;
    calculation();
});

if(orderBtn) {
    orderBtn.addEventListener("click", (event) => {
        const prodNo = event.currentTarget.dataset.prodno;
        const qty = parseInt(pd_inputQty.value);
        console.log("prodNo : " + prodNo);
        console.log("qty : " + qty);

        const priceElement = document.getElementById("total-price").innerText;
        const totalPrice = parseInt(priceElement.replace(/[^0-9]/g, ''));



    //    debugger;
        console.log("totalPrice" + totalPrice);

        fetch("/order/order_prod", {
            method: "POST",
            headers: {
                'content-type': 'application/json',
                'X-CSRF-TOKEN': csrfToken
            },
            body: JSON.stringify({
                "prodNo": prodNo,
                "qty": qty,
                "totalPrice": totalPrice
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

}

pd_inputQty.addEventListener("input", () => {
    calculation();
})

function calculation () {
    //    let tPriceSpan = document.createElement('span');
        let qty = parseInt(pd_inputQty.value) || 0;
        const totalPrice = document.getElementById("total-price");
        const priceElement = document.getElementById("js-price");

        const price = parseInt(priceElement.textContent.replace(/[^0-9]/g, ''));

    //    const price = parseInt(priceElement.textContent);
        console.log(price);
        let tPrice = qty * price;

    //    qtyText = document.createTextNode(tPrice);
        totalPrice.innerText = tPrice.toLocaleString();

    //    totalPrice.appendChild(qtyText);

}

// 나중에 초기 계산 함수 등의 도입을 고려.
window.addEventListener('pageshow', function(event) {
    // 페이지가 처음 로드되거나 새로고침될 때 실행
    //pd_inputQty = document.querySelector(".qty");

    console.log("event.persisted : " + event.persisted);

    console.log("pd_inputQty :" + parseInt(pd_inputQty.value));
    console.log("Loaded 실행 확인");
    calculation();

});

document.addEventListener('DOMContentLoaded', function() {
    const itemData = document.getElementById("itemData");

    if (pd_isInLyb == "true") {
        pBtn.disabled=true;
        mBtn.disabled=true;
        pd_inputQty.disabled=true;
    }
    else if (pd_prodType == "DIGITAL") {
        pBtn.disabled=true;
        mBtn.disabled=true;
        pd_inputQty.disabled=true;
    }

});