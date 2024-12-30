package br.com.vrbeneficios.miniautorizador.service;

import br.com.vrbeneficios.miniautorizador.domain.Cartao;
import br.com.vrbeneficios.miniautorizador.dto.CartaoRequestDTO;
import br.com.vrbeneficios.miniautorizador.dto.CartaoResponseDTO;
import br.com.vrbeneficios.miniautorizador.exception.CartaoExistenteException;
import br.com.vrbeneficios.miniautorizador.exception.NotFoundException;
import br.com.vrbeneficios.miniautorizador.repository.CartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartaoService {

    private final CartaoRepository cartaoRepository;

    @Transactional
    public CartaoResponseDTO criarCartao(CartaoRequestDTO cartaoRequestDTO) {
        cartaoRepository.findByNumeroCartao(cartaoRequestDTO.getNumeroCartao())
                .ifPresent(cartao -> {
                    throw new CartaoExistenteException(new CartaoResponseDTO(
                            cartao.getNumeroCartao(), cartao.getSenhaCartao()));
                });

        Cartao novoCartao = new Cartao();
        novoCartao.setNumeroCartao(cartaoRequestDTO.getNumeroCartao());
        novoCartao.setSenhaCartao(cartaoRequestDTO.getSenhaCartao());
        novoCartao.setSaldo(new BigDecimal("500.00"));

        cartaoRepository.save(novoCartao);

        return new CartaoResponseDTO(novoCartao.getNumeroCartao(), novoCartao.getSenhaCartao());
    }

    @Transactional(readOnly = true)
    public BigDecimal obterSaldo(String numeroCartao) {
        return cartaoRepository.findByNumeroCartao(numeroCartao)
                .map(Cartao::getSaldo)
                .orElseThrow(NotFoundException::new);
    }
}
