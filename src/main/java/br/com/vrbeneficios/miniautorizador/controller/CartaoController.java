package br.com.vrbeneficios.miniautorizador.controller;

import br.com.vrbeneficios.miniautorizador.dto.CartaoRequestDTO;
import br.com.vrbeneficios.miniautorizador.dto.CartaoResponseDTO;
import br.com.vrbeneficios.miniautorizador.exception.CartaoInexistenteException;
import br.com.vrbeneficios.miniautorizador.service.CartaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
public class CartaoController {

    private final CartaoService cartaoService;

    /**
     * Cria um novo cartão.
     *
     * @param cartaoRequestDTO Dados do cartão a ser criado
     * @return ResponseEntity com o cartão criado
     */
    @PostMapping("/criar")
    public ResponseEntity<CartaoResponseDTO> criarCartao(@RequestBody CartaoRequestDTO cartaoRequestDTO) {
        CartaoResponseDTO response = cartaoService.criarCartao(cartaoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtém o saldo de um cartão específico.
     *
     * @param numeroCartao Número do cartão
     * @return ResponseEntity com o saldo do cartão
     */
    @GetMapping("/{numeroCartao}/saldo")
    public ResponseEntity<BigDecimal> obterSaldo(@PathVariable String numeroCartao) {
        try {
            BigDecimal saldo = cartaoService.obterSaldo(numeroCartao);
            return ResponseEntity.status(HttpStatus.OK).body(saldo);
        } catch (CartaoInexistenteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}