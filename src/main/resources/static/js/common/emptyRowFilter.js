/**
 * 테이블 본체(tbody)에 빈 행을 채움.
 * @param {string} selectorOrIdOrClass - CSS 선택자(#id, .class) 또는 순수한 ID/Class 이름
 * @param {number} pageSize - 전체 행 개수 (채워야 할 총 개수)
 */
function emptyRowFilter(tableBody, pageSize) {
    let selector = tableBody.trim();
    let tBody = null;

    // (#, .) 기호가 이미 포함된 완벽한 선택자인 경우
    if (selector.startsWith('#') || selector.startsWith('.')) {
        tBody = document.querySelector(selector);
    }

    // 기호가 없으면, ID(#)로 추측하여 찾기
    if (!tBody) {
        let idSelector = '#' + selector;
        tBody = document.querySelector(idSelector);
    }

    // ID로도 못 찾으면, Class(.)로 추측하여 찾기
    if (!tBody) {
        let classSelector = '.' + selector;
        tBody = document.querySelector(classSelector);
    }

    if(!tBody) return;

    const tableElement = tBody.closest('table'); // 가장 가까운 상위 <table> 요소를 찾습니다.

        if (tableElement) {
            // !important를 포함한 인라인 스타일로 <table>의 테두리를 강제로 제거
            tableElement.style.border = 'none';
            tableElement.style.outline = 'none';

            // border-spacing이 테두리처럼 보이는 문제 방지
            tableElement.style.borderSpacing = '0';
        }

    const currentBodyRow = tBody.rows.length;
    const emptyRowsNeeded = pageSize - currentBodyRow;
    const columnCount = tBody.querySelector('tr')?.cells.length || undefined;

const inlineStyle = `border: none; outline: none; box-shadow: none; background-color: transparent;`;

    if(emptyRowsNeeded > 0) {
        for(i = 0; i < emptyRowsNeeded; i++) {
            const addTr = tBody.insertRow();
            //addTr.innerHTML = `<td colspan="${columnCount}" style="${inlineStyle}">&nbsp;</td>`;
            //addTr.innerHTML = `<td colspan="${columnCount}">&nbsp;</td>`;
            addTr.style.border = 'none';
                        addTr.innerHTML = `<td colspan="${columnCount}" style="${inlineStyle}">&nbsp;</td>`;

            addTr.classList.add('empty-row-placeholder');
        }
    }

}