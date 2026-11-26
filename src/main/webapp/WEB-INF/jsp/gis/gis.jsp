<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<section class="container mt-4">
    <p>GIS</p>

    <button type="button" id="btn-nearby">
        input
    </button>

    <div class="div-input-coordi">
        <label for="nn-count">검색 개수</label>
        <input type="number" id="nn-count" min="1" step="1" value="5">
    </div>

    <div class="div-map-container">
    </div>

</section>


<script src="/js/gis/gis.js"></script>