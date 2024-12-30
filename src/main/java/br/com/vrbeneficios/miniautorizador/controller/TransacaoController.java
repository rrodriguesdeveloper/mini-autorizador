package br.com.vrbeneficios.miniautorizador.controller;

import br.com.vrbeneficios.miniautorizador.dto.TransacaoRequestDTO;
import br.com.vrbeneficios.miniautorizador.exception.CartaoInexistenteException;
import br.com.vrbeneficios.miniautorizador.exception.SaldoInsuficienteException;
import br.com.vrbeneficios.miniautorizador.exception.SenhaInvalidaException;
import br.com.vrbeneficios.miniautorizador.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
class TransacaoController {

    private final TransacaoService transacaoService;

    /**
     * Realizar uma transação.
     *
     * @param transacaoRequestDTO Dados da transação
     * @return String
     */
    @PostMapping
    public ResponseEntity<String> realizarTransacao(@RequestBody TransacaoRequestDTO transacaoRequestDTO) {
        try {
            transacaoService.autorizarTransacao(transacaoRequestDTO);
            return ResponseEntity.ok("OK");
        } catch (CartaoInexistenteException | SenhaInvalidaException | SaldoInsuficienteException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }
}