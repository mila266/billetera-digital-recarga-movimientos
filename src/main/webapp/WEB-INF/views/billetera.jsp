<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="en_US"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Mi Billetera Digital</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-body-tertiary">

<nav class="navbar bg-dark" data-bs-theme="dark">
    <div class="container" style="max-width: 900px;">
        <span class="navbar-brand fw-semibold">Mi Billetera Digital</span>
        <span class="badge text-bg-secondary">JSP + Servlet</span>
    </div>
</nav>

<main class="container py-4" style="max-width: 900px;">

    <%-- Errores de validación: solo existen si el POST fue rechazado --%>
    <c:if test="${not empty errores}">
        <div class="alert alert-danger" role="alert">
            <strong>Movimiento rechazado:</strong>
            <ul class="mb-0">
                <c:forEach var="error" items="${errores}">
                    <li><c:out value="${error}"/></li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <%-- Saldo actual --%>
    <div class="row g-3 mb-4">
        <div class="col-12 col-md-6">
            <div class="card shadow-sm h-100">
                <div class="card-body">
                    <p class="text-uppercase text-body-secondary small mb-1">Saldo disponible</p>
                    <p class="saldo text-success fw-bold mb-1">
                        S/ <fmt:formatNumber value="${sessionScope.billetera.saldo}" pattern="#,##0.00"/>
                    </p>
                </div>
            </div>
        </div>
    </div>

    <%-- Historial: c:forEach recorre los movimientos (el más reciente primero) --%>
    <div class="card shadow-sm mb-4">
        <div class="card-header d-flex justify-content-between align-items-center">
            <span>Movimientos recientes</span>
            <span class="badge text-bg-secondary">${sessionScope.billetera.movimientos.size()}</span>
        </div>

        <c:choose>
            <c:when test="${empty sessionScope.billetera.movimientos}">
                <div class="card-body text-body-secondary">Aún no hay movimientos.</div>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-dark">
                        <tr>
                            <th scope="col">Tipo</th>
                            <th scope="col">Monto (S/)</th>
                            <th scope="col">Orden</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="mov" items="${sessionScope.billetera.movimientos}" varStatus="estado">
                            <tr>
                                <td>
                                    <c:choose>
                                        <c:when test="${mov.gasto}">
                                            <span class="badge text-bg-danger px-3 py-2">Gasto</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge text-bg-success px-3 py-2">Recarga</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="fw-semibold ${mov.gasto ? 'text-danger' : 'text-success'}">
                                    ${mov.gasto ? '-' : '+'} <fmt:formatNumber value="${mov.monto}" pattern="#,##0.00"/>
                                </td>
                                <td class="text-body-secondary small">
                                    ${estado.first ? 'Más reciente' : 'Anterior'}
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <%-- Formulario: POST a /billetera. "novalidate" deja la validación al servidor --%>
    <div class="card shadow-sm">
        <div class="card-header">Nuevo movimiento <span class="text-body-secondary small">(POST /billetera)</span></div>
        <div class="card-body">
            <form method="post" action="<c:url value='/billetera'/>" class="row g-3 align-items-end" novalidate>
                <div class="col-12 col-md-4">
                    <label for="tipo" class="form-label">Tipo</label>
                    <select class="form-select" id="tipo" name="tipo">
                        <option value="RECARGA" ${param.tipo == 'RECARGA' ? 'selected' : ''}>Recarga</option>
                        <option value="GASTO" ${param.tipo == 'GASTO' ? 'selected' : ''}>Gasto</option>
                    </select>
                </div>
                <div class="col-12 col-md-4">
                    <label for="monto" class="form-label">Monto (S/)</label>
                    <%-- oninput bloquea letras; el servidor revalida con regex --%>
                    <input type="text" inputmode="decimal" autocomplete="off" maxlength="12"
                           class="form-control ${not empty errores ? 'is-invalid' : ''}"
                           id="monto" name="monto" placeholder="0.00"
                           oninput="this.value = this.value.replace(/[^0-9.\-]/g, '')"
                           value="<c:out value='${param.monto}'/>">
                </div>
                <div class="col-12 col-md-4 d-grid">
                    <button type="submit" class="btn btn-primary">Registrar movimiento</button>
                </div>
            </form>
        </div>
    </div>

</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
