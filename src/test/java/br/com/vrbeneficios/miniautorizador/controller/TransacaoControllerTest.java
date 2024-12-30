package br.com.vrbeneficios.miniautorizador.controller;

import br.com.vrbeneficios.miniautorizador.dto.TransacaoRequestDTO;
import br.com.vrbeneficios.miniautorizador.exception.CartaoInexistenteException;
import br.com.vrbeneficios.miniautorizador.exception.SaldoInsuficienteException;
import br.com.vrbeneficios.miniautorizador.exception.SenhaInvalidaException;
import br.com.vrbeneficios.miniautorizador.service.TransacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class TransacaoControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TransacaoController transacaoController;

    @Mock
    private TransacaoService transacaoService;

    private TransacaoRequestDTO transacaoRequestDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transacaoController).build();

        transacaoRequestDTO = new TransacaoRequestDTO("1234567890123456",
                "senha123", new BigDecimal("100.00"));
    }

    @Test
    public void testRealizarTransacao_Sucesso() throws Exception {
        doNothing().when(transacaoService).autorizarTransacao(any());

        mockMvc.perform(post("/transacoes")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(transacaoRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    public void testRealizarTransacao_CartaoInexistente() throws Exception {
        doThrow(new CartaoInexistenteException()).when(transacaoService).autorizarTransacao(any());

        mockMvc.perform(post("/transacoes")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(transacaoRequestDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("CARTAO_INEXISTENTE"));
    }

    @Test
    public void testRealizarTransacao_SenhaInvalida() throws Exception {
        doThrow(new SenhaInvalidaException()).when(transacaoService).autorizarTransacao(any());

        mockMvc.perform(post("/transacoes")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(transacaoRequestDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("SENHA_INVALIDA"));
    }

    @Test
    public void testRealizarTransacao_SaldoInsuficiente() throws Exception {
        doThrow(new SaldoInsuficienteException()).when(transacaoService).autorizarTransacao(any());

        mockMvc.perform(post("/transacoes")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(transacaoRequestDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("SALDO_INSUFICIENTE"));
    }
}
