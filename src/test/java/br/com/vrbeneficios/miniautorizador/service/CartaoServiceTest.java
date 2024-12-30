package br.com.vrbeneficios.miniautorizador.service;

import br.com.vrbeneficios.miniautorizador.domain.Cartao;
import br.com.vrbeneficios.miniautorizador.dto.CartaoRequestDTO;
import br.com.vrbeneficios.miniautorizador.dto.CartaoResponseDTO;
import br.com.vrbeneficios.miniautorizador.exception.CartaoExistenteException;
import br.com.vrbeneficios.miniautorizador.exception.NotFoundException;
import br.com.vrbeneficios.miniautorizador.repository.CartaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CartaoServiceTest {

    @InjectMocks
    private CartaoService cartaoService;

    @Mock
    private CartaoRepository cartaoRepository;

    private Cartao cartao;

    private CartaoRequestDTO cartaoRequestDTO;
    private CartaoResponseDTO cartaoResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cartao = new Cartao(1L, "1234567890123456", "1234", new BigDecimal("500.00"), any());

        cartaoRequestDTO = new CartaoRequestDTO("1234567890123456", "1234");
        cartaoResponseDTO = new CartaoResponseDTO("1234567890123456", "1234");
    }

    @Test
    void testCriarCartaoComSucesso() {
        CartaoRequestDTO requestDTO = new CartaoRequestDTO("1234567890", "senha123");

        when(cartaoRepository.findByNumeroCartao(requestDTO.getNumeroCartao())).thenReturn(java.util.Optional.empty());

        when(cartaoRepository.save(Mockito.any())).thenReturn(cartao);

        CartaoResponseDTO retorno = cartaoService.criarCartao(requestDTO);

        assertEquals(requestDTO.getNumeroCartao(), retorno.getNumeroCartao());
    }

    @Test
    void testCriarCartaoExistente() {
        CartaoRequestDTO cartaoRequestDTO = new CartaoRequestDTO("1234567890123456", "senha123");
        Cartao cartaoExistente = new Cartao();
        cartaoExistente.setNumeroCartao("1234567890123456");
        cartaoExistente.setSenhaCartao("senha123");

        when(cartaoRepository.findByNumeroCartao(cartaoRequestDTO.getNumeroCartao()))
                .thenReturn(Optional.of(cartaoExistente));

        assertThrows(CartaoExistenteException.class, () -> cartaoService.criarCartao(cartaoRequestDTO));
    }

    @Test
    void testObterSaldoComSucesso() {
        String numeroCartao = "1234567890";
        BigDecimal saldoEsperado = new BigDecimal("500.00");

        when(cartaoRepository.findByNumeroCartao(numeroCartao)).thenReturn(Optional.of(cartao));

        BigDecimal saldo = cartaoService.obterSaldo(numeroCartao);
        assertEquals(saldoEsperado, saldo);
    }

    @Test
    void testObterSaldoCartaoInexistente() {
        String numeroCartaoInexistente = "1919191919";

        when(cartaoRepository.findByNumeroCartao(numeroCartaoInexistente)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cartaoService.obterSaldo(numeroCartaoInexistente));
    }
}