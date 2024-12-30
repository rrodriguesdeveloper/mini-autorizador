package br.com.vrbeneficios.miniautorizador.service;

import br.com.vrbeneficios.miniautorizador.domain.Cartao;
import br.com.vrbeneficios.miniautorizador.dto.TransacaoRequestDTO;
import br.com.vrbeneficios.miniautorizador.exception.CartaoInexistenteException;
import br.com.vrbeneficios.miniautorizador.exception.SaldoInsuficienteException;
import br.com.vrbeneficios.miniautorizador.exception.SenhaInvalidaException;
import br.com.vrbeneficios.miniautorizador.repository.CartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final CartaoRepository cartaoRepository;

    @Transactional
    public void autorizarTransacao(TransacaoRequestDTO transacaoRequestDTO) {
        Cartao cartao = cartaoRepository.findByIdForUpdate(transacaoRequestDTO.getNumeroCartao())
                .orElseThrow(CartaoInexistenteException::new);
        validarSenha(cartao, transacaoRequestDTO.getSenhaCartao());
        validarSaldo(cartao, transacaoRequestDTO.getValor());
        debitarSaldo(cartao, transacaoRequestDTO.getValor());
        cartaoRepository.save(cartao);
    }

    private Cartao validarSenha(Cartao cartao, String senhaCartao) {
        return Optional.of(cartao)
                .filter(c -> c.getSenhaCartao().equals(senhaCartao))
                .orElseThrow(SenhaInvalidaException::new);
    }

    private Cartao validarSaldo(Cartao cartao, BigDecimal valor) {
        return Optional.ofNullable(cartao)
                .filter(c -> c.getSaldo().compareTo(valor) >= 0)
                .orElseThrow(SaldoInsuficienteException::new);
    }

    private void debitarSaldo(Cartao cartao, BigDecimal valor) {
        cartao.setSaldo(cartao.getSaldo().subtract(valor));
    }
}

