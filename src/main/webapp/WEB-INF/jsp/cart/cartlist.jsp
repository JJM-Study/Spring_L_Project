<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart/cartlist.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<%--<main class="flex-grow-1">--%>
<div class="container mt-4">
<%-- 현재 정렬 상태를 유지하려고 hidden input 사용 --%>
<form id="sortForm" method="get" action="/cart/cartlist">
    <input type="hidden" name="orderColumn" id="orderColumn">
    <input type="hidden" name="orderType" id="orderType">
</form>


<!-- 기존 cartList 출력 부분 -->
<div class="container-fluid">
<table class="w-100 table table-bordered list-table">
    <thead>
        <tr>
            <th class="cart-th"><input type="checkbox" id="selectAll" onClick="toggleAll(this)"></th>
            <th id="cart-th-no">
                <a href="javascript:void(0);" onClick="toggleOrderType('cart_no')">
                    카트번호
                    <c:if test="${orderColumn == 'cart_no'}">
                        <c:choose>
                            <c:when test="${orderType == 'asc'}">▲</c:when>
                            <c:otherwise>▼</c:otherwise>
                        </c:choose>
                    </c:if>
                </a>
            </th>
            <th id="cart-th-name">
                <a href="javascript:void(0);" onClick="toggleOrderType('prod_name')">
                    제품명
                    <c:if test="${orderColumn == 'prod_name'}">
                        <c:choose>
                            <c:when test="${orderType == 'asc'}">▲</c:when>
                            <c:otherwise>▼</c:otherwise>
                        </c:choose>
                    </c:if>
                </a>
            </th>
            <th>
                <a href="javascript:void(0);" onClick="toggleOrderType('prod_type')">
                    상품 종류
                    <c:if test="${orderColumn == 'prod_type'}">
                        <c:choose>
                            <c:when test="${orderType == 'asc'}">▲</c:when>
                                <c:otherwise>▼</c:otherwise>
                                </c:choose>
                    </c:if>
                </a>
            </th>
            <th>
                <a href="javascript:void(0);" onClick="toggleOrderType('qty')">
                    수량
                    <c:if test="${orderColumn == 'qty'}">
                        <c:choose>
                            <c:when test="${orderType == 'asc'}">▲</c:when>
                            <c:otherwise>▼</c:otherwise>
                        </c:choose>
                    </c:if>
                </a>
            </th>
            <th id="cart-th-del" class="cart-th">
               삭제
            </th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${not empty cartList}">
                <c:forEach var="cart" items="${cartList}">
                    <tr>
                        <td class="cart-td"><input type="checkbox" name="cartItem" value="${cart.cartNo}"></td>
                        <td>${cart.cartNo}</td>
                        <td>${cart.prodName}</td>
                        <td>
                            <c:choose>
                                <c:when test="${cart.prodType == 'DIGITAL'}">
                                    다운로드 상품
                                </c:when>
                                <c:when test="${cart.prodType == 'PHYSICAL'}">
                                    배송 상품
                                </c:when>
                                <c:otherwise>
                                    기타 상품
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${cart.qty}</td>
                        <td class="cart-td"><button id="del-cart-${cart.cartNo}" class="del-cart-btn" onClick="delCart(this.value)" value="${cart.cartNo}"><i class="bi bi-trash"></i></button></td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="6" class="text-center">비어있습니다</td>
                </tr>
            </c:otherwise>
        </c:choose>
    </tbody>
</table>

    <input type="button" class="btn btn-primary" onClick="submitOrder()" id="btn_orderSubmit" value="주문하기"></input>

    </div>
</div>
<%--</main>--%>

<script>
    let currentOrderColumn = "${orderColumn}";
    let currentOrderType = "${orderType}";


    async function delCart(cartNo) {
    try {
        const response = await fetch("/cart/delete?cartNo=" + cartNo, {
            method : "POST",
            headers : {
                'X-CSRF-TOKEN': csrfToken
            }
        });

        if(response.ok) {
            const delButton = document.getElementById("del-cart-" + cartNo);
            if (delButton) {
            console.log("tbRow" + delButton);
            const delRow = delButton.closest('tr');
                if(delRow) {
                    const cellCount = delRow.children.length;

                    delRow.innerHTML = '';

                    const msgCell = document.createElement('td');

                    msgCell.setAttribute('colspan', cellCount);

                    msgCell.innerHTML = '삭제 처리되었습니다.';
                    msgCell.style.textAlign = 'center';
                    msgCell.style.color = 'gray';

                    delRow.appendChild(msgCell);

                // alert("삭제 되었습니다.");
                } else {
                    console.log("삭제 열을 찾을 수 없습니다.");
                }
            } else {
                console.log("삭제 버튼을 찾을 수 없습니다.");
            }
        }

   else {

            // 에러 메세지 처리 모듈화 고민.

                   const errorData = await response.json();

                   let errorMessage = "삭제 중 알 수 없는 오류가 발생했습니다.";

                   if (errorData && errorData.message) {
                       errorMessage = errorData.message;
                   } else if (response.statusText) {
                       errorMessage = `오류 발생: ${response.status} ${response.statusText}`;
                   }

                   alert(errorMessage);
               }

     } catch(error) {
        alert("에러 발생 : " + error);
        }
    }

    function toggleOrderType(column) {
        let newOrderType ="asc";
        //if (currentOrderColumn === column && currentOrderType === "asc") {
        //    newOrderType = "desc";
        //}

        if (currentOrderColumn === column) {

            newOrderType = (currentOrderType === 'asc') ? 'desc' : 'asc';

        } else {

                if (column === 'prod_type') {
                     newOrderType = 'desc';
                }

        }

        document.getElementById("orderColumn").value = column;
        document.getElementById("orderType").value = newOrderType;

        document.getElementById("sortForm").submit();
    }

    function toggleAll(source) {
        const checkboxes = document.getElementsByName('cartItem');
        for (let i = 0; i < checkboxes.length; i++) {
            checkboxes[i].checked = source.checked;
        }
    }

    function submitOrder() {
        const selectedItems = [];
        const checkboxes = document.getElementsByName('cartItem');

        for (let i = 0; i < checkboxes.length; i++) {
           if (checkboxes[i].checked) {
            selectedItems.push(checkboxes[i].value);
          }
        }

        if (selectedItems.length === 0) {
            alert("주문할 상품을 선택하세요.");
            return;
        }

        try {
        // debugger;
        $.ajax({
           url : '/order/from-cart',
           method: 'POST',
           'contentType' : 'application/json',
           headers: {
             'X-CSRF-TOKEN' : csrfToken
           },

           data: JSON.stringify(selectedItems),
           success: function(data) {
               debugger;
               console.log("Item_Info :" + data),
               alert(data.message);
               location.href = "/order/result?orderId=" + data.orderId;
           },
           error: function(xhr, status, error) {
                if (xhr.responseJSON) {
                    alert(xhr.responseJSON.message);
                } else {
                    alert("주문 실패");
                }
                console.error("에러 메세지 : " + error);
                }
           })
        } catch (error) {
            console.log("서버 처리 중 에러 : " + error);
        }

    }


</script>

<style>
    .list-table th,
    .list-table td {
        border: 2px solid #333;
    }
</style>

<%--<%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>--%>