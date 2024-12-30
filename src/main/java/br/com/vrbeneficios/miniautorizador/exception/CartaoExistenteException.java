package br.com.vrbeneficios.miniautorizador.exception;

import br.com.vrbeneficios.miniautorizador.dto.CartaoResponseDTO;
import com.google.gson.Gson;

public class CartaoExistenteException extends RuntimeException {
    public CartaoExistenteException(CartaoResponseDTO cartaoResponseDTO) {
        super(convertToJson(cartaoResponseDTO));
    }

    private static String convertToJson(CartaoResponseDTO cartaoResponseDTO) {
        return new Gson().toJson(cartaoResponseDTO);
    }
}
