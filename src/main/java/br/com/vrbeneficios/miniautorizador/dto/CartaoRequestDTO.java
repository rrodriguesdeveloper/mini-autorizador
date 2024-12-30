package br.com.vrbeneficios.miniautorizador.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartaoRequestDTO {

    private String numeroCartao;
    private String senhaCartao;
}
