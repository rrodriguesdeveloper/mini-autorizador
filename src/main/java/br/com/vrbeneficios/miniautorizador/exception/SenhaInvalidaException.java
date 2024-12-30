package br.com.vrbeneficios.miniautorizador.exception;

import br.com.vrbeneficios.miniautorizador.enums.ErroTransacaoEnum;

public class SenhaInvalidaException extends RuntimeException {
    public SenhaInvalidaException() {
        super(ErroTransacaoEnum.SENHA_INVALIDA.name());
    }
}
