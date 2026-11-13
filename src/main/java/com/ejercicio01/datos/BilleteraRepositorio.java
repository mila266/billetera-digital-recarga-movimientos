package com.ejercicio01.datos;

import com.ejercicio01.modelo.Billetera;
import jakarta.servlet.http.HttpSession;

/**
 * Capa de datos: guarda y recupera la billetera en la sesión HTTP
 */
public class BilleteraRepositorio {

    public static final String ATRIBUTO = "billetera";

    /**
     * Devuelve la billetera de la sesión; si no existe, la crea con S/ 250.00.
     */
    public Billetera obtener(HttpSession session) {
        Billetera billetera = (Billetera) session.getAttribute(ATRIBUTO);
        if (billetera == null) { // 1ra verificación: si ya existe, se devuelve sin hacer esperar a nadie
            synchronized (this) {
                billetera = (Billetera) session.getAttribute(ATRIBUTO);
                if (billetera == null) { // 2da verificación: otra petición pudo crearla mientras esperábamos, evita que se creen dos billeteras y se pierda un movimiento
                    billetera = new Billetera();
                    session.setAttribute(ATRIBUTO, billetera);
                }
            }
        }
        return billetera;
    }
}
