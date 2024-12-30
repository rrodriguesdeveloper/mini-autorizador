package br.com.vrbeneficios.miniautorizador.exception;

import br.com.vrbeneficios.miniautorizador.enums.ErroTransacaoEnum;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException() {
        super(ErroTransacaoEnum.SALDO_INSUFICIENTE.name());
    }
}
