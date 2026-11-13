package com.ejercicio01.negocio;

import com.ejercicio01.modelo.Billetera;
import com.ejercicio01.modelo.Movimiento;
import com.ejercicio01.modelo.TipoMovimiento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Capa de negocio: reglas y validaciones de la billetera.
 * Recibe texto del formulario y devuelve una lista de errores.
 */
public class BilleteraService {

    private static final Pattern TIPO = Pattern.compile("^(RECARGA|GASTO)$");

    // Hasta 8 enteros y máximo 2 decimales
    private static final Pattern MONTO = Pattern.compile("^\\d{1,8}(\\.\\d{1,2})?$");

    /** @return errores encontrados; vacía si el movimiento se registró */
    public List<String> registrar(Billetera billetera, String tipoTexto, String montoTexto) {
        List<String> errores = new ArrayList<>();

        String tipoLimpio = limpiar(tipoTexto);
        if (!TIPO.matcher(tipoLimpio).matches()) {
            errores.add("Seleccione un tipo de movimiento válido (Recarga o Gasto).");
        }

        String montoLimpio = limpiar(montoTexto);
        BigDecimal monto = null;
        if (montoLimpio.isEmpty()) {
            errores.add("Ingrese un monto.");
        } else if (!MONTO.matcher(montoLimpio).matches()) {
            errores.add("El monto debe ser un número válido (solo dígitos, máximo 2 decimales).");
        } else {
            monto = new BigDecimal(montoLimpio).setScale(2);
            if (monto.signum() <= 0) { 
                errores.add("El monto debe ser mayor que cero.");
            }
        }

        if (!errores.isEmpty()) {
            return errores; 
        }

        // La billetera verifica saldo y registra de forma atómica (synchronized)
        Movimiento movimiento = new Movimiento(TipoMovimiento.valueOf(tipoLimpio), monto);
        if (!billetera.registrar(movimiento)) {
            errores.add("Saldo insuficiente. Gasto solicitado: S/ " + monto
                    + " — Saldo disponible: S/ " + billetera.getSaldo());
        }
        return errores;
    }

    private static String limpiar(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
