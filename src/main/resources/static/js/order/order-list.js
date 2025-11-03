document.querySelectorAll('.ord-prod').forEach(cell => {
    cell.addEventListener('click', function() {
        this.classList.toggle('expanded');
    });
});

function checkOverflow(element) {
    // scrollWidth > clientWidth 이면 내용이 잘린 것입니다.
    return element.scrollWidth > element.clientWidth + 1;
}

// 클래스 적용 함수
function applyTruncatedClass() {
    document.querySelectorAll('.ord-prod').forEach(cell => {
        if (checkOverflow(cell)) {
            // 텍스트가 잘린 요소에만 클래스 추가
            cell.classList.add('is-truncated');
        } else {
            cell.classList.remove('is-truncated');
        }
    });
}

window.addEventListener('load', applyTruncatedClass);

//document.addEventListener("DOMContentLoaded", () => {
//
//    emptyRowFilter("ordListTbody", pageSize);
//});