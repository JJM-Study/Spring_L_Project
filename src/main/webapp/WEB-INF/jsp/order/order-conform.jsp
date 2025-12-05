<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ include file="/WEB-INF/views/common/header.jsp" %>--%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/order/order-conform.css">

    <section class="container mt-4">

        <div>
            <h2>주문 완료</h2>
            <dl>
                <dt>주문번호</dt>
                <dd>${orderInfo.orderNo}</dd>
                <dt>결제 상태</dt>
                <dd>${orderInfo.orderStatus}</dd>
                <dt>결제 방식</dt>
                <%-- <dd>${orderInfo.paymentMethod}</dd> --%>
                <dd>결제 생략</dd>
                <dt>총 결제 금액</dt>
                <dd>${orderInfo.totalPrice}</dd>

            </dl>
        </div>

        <div id="links">
            <button type="button" class="btn btn-primary" onclick="location.href='/order/ordlist'">주문 리스트 확인</button>
            <button type="button" class="btn btn-primary" onclick="location.href='/library/my'">내 서재로 가기</button>
        <%-- <a href="/order/ordlist">주문 리스트 확인</a> --%>
        <%-- <a href="/library/my">내 서재로 가기</a> --%>
        </div>

        <div>
            <h3>상품 정보</h3>
            <table>
                <thread>
                    <th class="th-img">이미지</th>
                    <th class="th-prod-name">상품명</th>
                    <th>상품 가격</th>
                    <th>주문 수량</th>
                    <th>상품 타입</th>
                </thread>
                <tbody>
                    <c:forEach var="item" items="${orderInfo.ordInfoProdList}">
                        <tr class="prod-info-row">
                            <td class="td-img">
                              <a href="/product/detail/${item.prodNo}">
                                <img src="${item.mainImgPath}" class="prod-img">
                              </a>
                            </td>
                            <td class="td-prod-name">
                               <a href="/product/detail/${item.prodNo}">
                                    ${item.prodName}
                               </a>
                            </td>
                            <td class="td-price">${item.price}</td>
                            <td class="td-ord-qty">${item.orderQty}</td>
                            <td>${item.prodType}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>



        <%-- <img src="${orderInfo.orderInfoProductDtos[0].imageList[0].imageUrl}" /> --%>
    </section>


<%--<%@ include file="/WEB-INF/views/common/footer.jsp" %>--%>