package br.com.vrbeneficios.miniautorizador.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transacao")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cartao", nullable = false)
    private String numeroCartao;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "data_hora", nullable = false)
    private java.time.LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;
}
