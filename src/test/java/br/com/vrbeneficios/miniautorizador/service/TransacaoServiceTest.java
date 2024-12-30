package br.com.vrbeneficios.miniautorizador.service;

import br.com.vrbeneficios.miniautorizador.domain.Cartao;
import br.com.vrbeneficios.miniautorizador.dto.TransacaoRequestDTO;
import br.com.vrbeneficios.miniautorizador.exception.CartaoInexistenteException;
import br.com.vrbeneficios.miniautorizador.exception.SaldoInsuficienteException;
import br.com.vrbeneficios.miniautorizador.exception.SenhaInvalidaException;
import br.com.vrbeneficios.miniautorizador.repository.CartaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @InjectMocks
    private TransacaoService transacaoService;

    @Mock
    private CartaoRepository cartaoRepository;

    private Cartao cartao;

    @BeforeEach
    void setUp() {
        cartao = new Cartao();
        cartao.setNumeroCartao("1234567890123456");
        cartao.setSenhaCartao("senha123");
        cartao.setSaldo(new BigDecimal("500.00"));
    }

    @Test
    void testAutorizarTransacaoSucesso() {
        TransacaoRequestDTO transacaoRequestDTO = new TransacaoRequestDTO("1234567890123456",
                "senha123", new BigDecimal("100.00"));

        when(cartaoRepository.findByIdForUpdate("1234567890123456"))
                .thenReturn(java.util.Optional.of(cartao));

        transacaoService.autorizarTransacao(transacaoRequestDTO);

        verify(cartaoRepository, times(1)).save(cartao);
        assertEquals(new BigDecimal("400.00"), cartao.getSaldo());
    }

    @Test
    void testAutorizarTransacaoSaldoInsuficiente() {
        TransacaoRequestDTO transacaoRequestDTO = new TransacaoRequestDTO("1234567890123456",
                "senha123", new BigDecimal("600.00"));

        when(cartaoRepository.findByIdForUpdate("1234567890123456"))
                .thenReturn(java.util.Optional.of(cartao));

        assertThrows(SaldoInsuficienteException.class, () -> {
            transacaoService.autorizarTransacao(transacaoRequestDTO);
        });
    }

    @Test
    void testAutorizarTransacaoSenhaInvalida() {
        TransacaoRequestDTO transacaoRequestDTO = new TransacaoRequestDTO("1234567890123456",
                "senhaErrada", new BigDecimal("100.00"));

        when(cartaoRepository.findByIdForUpdate("1234567890123456"))
                .thenReturn(java.util.Optional.of(cartao));

        assertThrows(SenhaInvalidaException.class, () -> {
            transacaoService.autorizarTransacao(transacaoRequestDTO);
        });
    }

    @Test
    void testAutorizarTransacaoCartaoInexistente() {
        TransacaoRequestDTO transacaoRequestDTO = new TransacaoRequestDTO("1234567890123456",
                "senha123", new BigDecimal("100.00"));

        when(cartaoRepository.findByIdForUpdate("1234567890123456"))
                .thenReturn(java.util.Optional.empty());

        assertThrows(CartaoInexistenteException.class, () -> {
            transacaoService.autorizarTransacao(transacaoRequestDTO);
        });
    }
}
