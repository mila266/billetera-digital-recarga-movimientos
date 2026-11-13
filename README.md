# Billetera Digital: Recarga y Movimientos

Aplicación web en **Java puro (Servlet + JSP)** que simula una billetera electrónica al estilo de Yape o Plin, simplificada con fines académicos. Permite consultar el saldo, registrar recargas y gastos, y ver el historial de movimientos.

> Curso: Soluciones Web y Aplicaciones Distribuidas — Ingeniería de Sistemas Computacionales
> Evaluación T1 · Caso 1 · Grupo 3

---

## Problemática

Se requiere una billetera digital sencilla que muestre el saldo disponible y el historial de movimientos, y que permita registrar **recargas** y **gastos** desde un formulario HTML.

La vista se genera en el servidor y los datos se mantienen **en memoria, dentro de la sesión HTTP**. No se usan bases de datos, autenticación, APIs externas ni frameworks. Al iniciar, la billetera tiene un saldo de **S/ 250.00** y el historial está vacío.

## Requisitos

### Funcionalidad de la billetera

1. **Recarga:** agrega un monto positivo al saldo.
2. **Gasto:** descuenta un monto positivo, siempre que sea menor o igual al saldo disponible.
3. Los montos vacíos, no numéricos, iguales o menores que cero se rechazan.
4. Cada movimiento válido actualiza el saldo y se agrega **al inicio** del historial, mostrando su tipo y monto.
5. Un movimiento inválido no modifica el saldo ni el historial, y muestra un mensaje comprensible.

### Requisitos funcionales

1. Un **Servlet controlador** responde a `GET` para mostrar el saldo y el historial, y a `POST` para procesar un nuevo movimiento.
2. Los datos de la billetera se mantienen en **`HttpSession`** y permanecen al actualizar la página durante la sesión.
3. Una **vista JSP** muestra el saldo y el historial con **JSTL** (`<c:forEach>`), sin scriptlets.
4. Un **formulario con Bootstrap** incluye el tipo (Recarga/Gasto), el monto y el botón de registro.
5. El Servlet valida el monto y el saldo disponible. Si hay un error, lo muestra en la misma vista sin modificar los datos; si la operación es válida, actualiza el saldo y el historial.

**Extensión opcional implementada:** Post-Redirect-Get (PRG) con `sendRedirect()` después de un `POST` exitoso, para que al actualizar la página no se reenvíe el formulario.

---

## Ejecución

### 1. Generar el WAR

```bash
# Windows
mvnw.cmd clean package

# Linux / macOS
./mvnw clean package
```

Esto genera `target/ejercicio01grupo3.war`. 

### 2. Desplegar en Tomcat 10.1

Con la extensión *Community Server Connectors* de VS Code:

1. Iniciar el servidor Tomcat (**Start Server**).
2. Clic derecho sobre `target/ejercicio01grupo3.war` → **Run on Server**.

### 3. Abrir la aplicación

```
http://localhost:8080/ejercicio01grupo3/
```

---

## Pruebas funcionales

Cada prueba parte del saldo inicial de S/ 250.00 con el historial vacío.

| # | Acción | Resultado esperado |
|---|---|---|
| 1 | Abrir la aplicación | Saldo S/ 250.00; historial vacío |
| 2 | Registrar una recarga de S/ 100.00 | Saldo S/ 350.00; la recarga aparece en el historial |
| 3 | Registrar un gasto de S/ 50.00 | Saldo S/ 300.00; el gasto aparece antes que la recarga |
| 4 | Intentar un gasto de S/ 400.00 | Se rechaza con "Saldo insuficiente"; el saldo sigue en S/ 300.00 |
| 5 | Ingresar cero, un negativo o un monto vacío | Se rechaza; el saldo y el historial no cambian |
