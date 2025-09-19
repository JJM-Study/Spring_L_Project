let pCount;

document.addEventListener("DOMContentLoaded", () => {

    const btnArray = document.querySelectorAll(".btn-array");


    btnArray.forEach((btn) => {
        btn.addEventListener("click", async (e) => {
        e.preventDefault();

                pCount = btn.dataset.count;

                try {
                    const response = await fetch("/product/products?pCount=" + pCount, {
                    method : "GET",
                });

                if (!response.ok) {
                    throw new Error("에러 : " + response.status);
                }

                console.log("array success : " + pCount);

            } catch (error) {
                console.log("서버 전송 중 에러 :" + error);
            }

        });

    });

});