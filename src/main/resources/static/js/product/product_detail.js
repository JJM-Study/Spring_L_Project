const pBtn = document.querySelector("#pBtn");
const mBtn = document.querySelector("#mBtn");
const pd_inputQty = document.querySelector(".qty")


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