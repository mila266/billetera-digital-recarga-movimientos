package com.ejercicio01.modelo;

import java.math.BigDecimal;

public class Movimiento {

    private final TipoMovimiento tipo;
    private final BigDecimal monto;

    public Movimiento(TipoMovimiento tipo, BigDecimal monto) {
        this.tipo = tipo;
        this.monto = monto;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public boolean isGasto() {
        return tipo == TipoMovimiento.GASTO;
    }
}
