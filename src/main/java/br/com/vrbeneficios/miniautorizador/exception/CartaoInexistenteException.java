package br.com.vrbeneficios.miniautorizador.exception;

import br.com.vrbeneficios.miniautorizador.enums.ErroTransacaoEnum;

public class CartaoInexistenteException extends RuntimeException {
    public CartaoInexistenteException() {
        super(ErroTransacaoEnum.CARTAO_INEXISTENTE.name());
    }
}
