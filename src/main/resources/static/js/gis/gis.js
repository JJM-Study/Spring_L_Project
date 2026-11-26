//const lon = document.getElementById('lon').valueAsNumber;
//const lat = document.getElementById('lat').valueAsNumber;

const lat = 0.1;
const lon = 0.3;

const btnNearby = document.getElementById('btn-nearby');



btnNearby.addEventListener("click", ()=> {
    console.log("test");
})


async function nearby() {

    const k = document.getElementById('nn-count').valueAsNumber;

    const response = await fetch("/gis/nearby", {
        method:"POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify ({
            k : k,
            lon : lon,
            lat : lat
        })
    });

    if (!response.ok) {
        throw new Error("공간 검색 실패");
    }

    const data = await response.json();

    console.log(data);

    return data;
}

nearby();