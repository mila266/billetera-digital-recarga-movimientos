package com.ejercicio01.presentacion;

import com.ejercicio01.datos.BilleteraRepositorio;
import com.ejercicio01.modelo.Billetera;
import com.ejercicio01.negocio.BilleteraService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Controlador de /billetera.
 *   GET  -> muestra saldo, historial y formulario.
 *   POST -> valida y registra un movimiento.
 */
@WebServlet("/billetera")
public class BilleteraServlet extends HttpServlet {

    private BilleteraRepositorio repositorio;
    private BilleteraService service;

    @Override
    public void init() {
        repositorio = new BilleteraRepositorio();
        service = new BilleteraService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        mostrarPagina(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // antes de leer parámetros

        Billetera billetera = repositorio.obtener(request.getSession());
        List<String> errores = service.registrar(
                billetera,
                request.getParameter("tipo"),
                request.getParameter("monto"));

        if (errores.isEmpty()) {
            // Redirige a GET para que F5 no reenvíe el formulario
            response.sendRedirect(request.getContextPath() + "/billetera");
        } else {
            // Misma vista con errores; los datos no se modificaron
            request.setAttribute("errores", errores);
            mostrarPagina(request, response);
        }
    }

    private void mostrarPagina(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        repositorio.obtener(request.getSession()); // crea la billetera con S/ 250.00, si no existe
        request.getRequestDispatcher("/WEB-INF/views/billetera.jsp").forward(request, response);
    }
}
