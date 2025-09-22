const pageBlock = document.getElementById("pages");
let pCount;

document.addEventListener("DOMContentLoaded", () => {

    const btnArray = document.querySelectorAll(".btn-array");

    btnArray.forEach((btn) => {
        btn.addEventListener("click", async (e) => {
        e.preventDefault();

                pageSize = btn.dataset.count;

                try {
                    const response = await fetch("/product/products?pageSize=" + pageSize, {
                    method : "GET",
                });

                if (!response.ok) {
                    throw new Error("에러 : " + response.status);
                }

                console.log("array success : " + pCount);
                location.href = "/product/products?cPage=1"

            } catch (error) {
                console.log("서버 전송 중 에러 :" + error);
            }

        });

    });



});