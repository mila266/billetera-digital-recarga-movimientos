package com.ejercicio01.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Billetera {

    public static final BigDecimal SALDO_INICIAL = new BigDecimal("250.00");

    private BigDecimal saldo = SALDO_INICIAL;
    private final List<Movimiento> movimientos = new ArrayList<>();

    public synchronized BigDecimal getSaldo() {
        return saldo;
    }

    /** Copia del historial, el más reciente primero. */
    public synchronized List<Movimiento> getMovimientos() {
        return List.copyOf(movimientos);
    }

    /**
     * Verifica saldo, actualiza y agrega al historial en un solo paso atómico.
     * @return false si es un gasto mayor al saldo (no se modifica nada)
     */
    public synchronized boolean registrar(Movimiento movimiento) {
        if (movimiento.isGasto()) {
            if (movimiento.getMonto().compareTo(saldo) > 0) {
                return false;
            }
            saldo = saldo.subtract(movimiento.getMonto());
        } else {
            saldo = saldo.add(movimiento.getMonto());
        }
        movimientos.add(0, movimiento); // al inicio del historial
        return true;
    }
}
