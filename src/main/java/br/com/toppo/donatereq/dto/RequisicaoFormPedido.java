package br.com.toppo.donatereq.dto;

import br.com.toppo.donatereq.models.Pedido;
import br.com.toppo.donatereq.models.StatusPedido;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

// É uma classe DTO (Data Transfer Object)
public class RequisicaoFormPedido {
    @NotBlank
    @NotNull
    private String nome; // em caso de erro, NotBlank.requisicaoNovoPedido.nome
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal salario;
    private StatusPedido StatusPedido;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public StatusPedido getStatusPedido() {
        return StatusPedido;
    }

    public void setStatusPedido(StatusPedido StatusPedido) {
        this.StatusPedido = StatusPedido;
    }


    public Pedido toPedido() {
        Pedido pedido = new Pedido();
        pedido.setNome(this.nome);
        pedido.setSalario(this.salario);
        pedido.setStatusPedido(this.StatusPedido);

        return pedido;
    }


    public Pedido toPedido(Pedido pedido) {
        pedido.setNome(this.nome);
        pedido.setSalario(this.salario);
        pedido.setStatusPedido(this.StatusPedido);
        return pedido;
    }

    public void fromPedido(Pedido pedido) {
        this.nome = pedido.getNome();
        this.salario = pedido.getSalario();
        this.StatusPedido = pedido.getStatusPedido();
    }

    @Override
    public String toString() {
        return "RequisicaoNovoPedido{" +
                "nome='" + nome + '\'' +
                ", salario=" + salario +
                ", StatusPedido=" + StatusPedido +
                '}';
    }
}
