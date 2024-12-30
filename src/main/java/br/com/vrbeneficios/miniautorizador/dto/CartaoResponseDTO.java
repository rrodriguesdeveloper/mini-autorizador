package br.com.vrbeneficios.miniautorizador.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartaoResponseDTO {

    private String numeroCartao;
    private String senha;
}
