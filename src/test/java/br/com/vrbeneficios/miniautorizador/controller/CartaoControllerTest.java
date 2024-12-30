package br.com.vrbeneficios.miniautorizador.controller;

import br.com.vrbeneficios.miniautorizador.dto.CartaoRequestDTO;
import br.com.vrbeneficios.miniautorizador.dto.CartaoResponseDTO;
import br.com.vrbeneficios.miniautorizador.exception.CartaoExistenteException;
import br.com.vrbeneficios.miniautorizador.service.CartaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CartaoControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CartaoController cartaoController;

    @Mock
    private CartaoService cartaoService;

    private CartaoRequestDTO cartaoRequestDTO;
    private CartaoResponseDTO cartaoResponseDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartaoController).build();

        cartaoRequestDTO = new CartaoRequestDTO("1234567890123456", "1234");
        cartaoResponseDTO = new CartaoResponseDTO("1234567890123456", "1234");
    }

    @Test
    void testCriarCartaoComSucesso() throws Exception {
        CartaoRequestDTO requestDTO = new CartaoRequestDTO("1234567890", "senha123");
        CartaoResponseDTO responseDTO = new CartaoResponseDTO("1234567890", "senha123");

        when(cartaoService.criarCartao(requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(post("/cartoes/criar")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testObterSaldoCartaoSucesso() throws Exception {
        String numeroCartao = "1234567890123456";
        BigDecimal saldo = new BigDecimal("500.00");

        when(cartaoService.obterSaldo(cartaoRequestDTO.getNumeroCartao())).thenReturn(saldo);

        mockMvc.perform(get("/cartoes/{numeroCartao}/saldo", numeroCartao))
                .andExpect(status().isOk())
                .andExpect(content().string("500.00"));
    }

    @Test
    void testCriarCartaoCartaoJaExistente() throws Exception {
        String numeroCartao = "1234567890123456";

        doThrow(new CartaoExistenteException(cartaoResponseDTO)).when(cartaoService).criarCartao(cartaoRequestDTO);

        mockMvc.perform(post("/cartoes/criar")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(numeroCartao)))
                .andExpect(status().isBadRequest());
    }
}