package br.com.toppo.donatereq.controllers;


import br.com.toppo.donatereq.dto.RequisicaoFormPedido;
import br.com.toppo.donatereq.models.Pedido;
import br.com.toppo.donatereq.models.StatusPedido;
import br.com.toppo.donatereq.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping(value = "/Pedido")
public class PedidoController {
    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping("")
    public ModelAndView index() {
        List<Pedido> pedidos = this.pedidoRepository.findAll();
        ModelAndView mv = new ModelAndView("pedidos/index");
        mv.addObject("pedidos", pedidos);

        return mv;
    }

    @GetMapping("/new")
    public ModelAndView nnew(RequisicaoFormPedido requisicao) {
        ModelAndView mv = new ModelAndView("pedidos/new");
        mv.addObject("listaStatusPedido", StatusPedido.values());

        return mv;
    }


    @PostMapping("")
    public ModelAndView create(@Valid RequisicaoFormPedido requisicao, BindingResult bindingResult) {
        System.out.println(requisicao);
        if (bindingResult.hasErrors()) {
            System.out.println("\n************* TEM ERROS ***************\n");

            ModelAndView mv = new ModelAndView("Pedido/new");
            mv.addObject("listaStatusPedido", StatusPedido.values());
            return mv;
        }
        else {
            Pedido pedido = requisicao.toPedido();
            this.pedidoRepository.save(pedido);

            return new ModelAndView("redirect:/pedidos/" + pedido.getId());
        }
    }


    @GetMapping("/{id}")
    public ModelAndView show(@PathVariable Long id) {
        Optional<Pedido> optional = this.pedidoRepository.findById(id);

        if (optional.isPresent()) {
            Pedido pedido = optional.get();

            ModelAndView mv = new ModelAndView("pedidos/show");
            mv.addObject("pedido", pedido);

            return mv;
        }
        // não achou um registro na tabela Pedido com o id informado
        else {
            System.out.println("$$$$$$$$$$$ NÃO ACHOU O Pedido DE ID "+ id + " $$$$$$$$$$$");
            return this.retornaErroPedido("SHOW ERROR: Pedido #" + id + " não encontrado no banco!");
        }
    }


    @GetMapping("/{id}/edit")
    public ModelAndView edit(@PathVariable Long id, RequisicaoFormPedido requisicao) {
        Optional<Pedido> optional = this.pedidoRepository.findById(id);

        if (optional.isPresent()) {
            Pedido pedido = optional.get();
            requisicao.fromPedido(pedido);

            ModelAndView mv = new ModelAndView("pedidos/edit");
            mv.addObject("pedidoId", pedido.getId());
            mv.addObject("listaStatusPedido", StatusPedido.values());

            return mv;

        }
        // não achou um registro na tabela Pedido com o id informado
        else {
            System.out.println("$$$$$$$$$$$ NÃO ACHOU O Pedido DE ID "+ id + " $$$$$$$$$$$");
            return this.retornaErroPedido("EDIT ERROR: Pedido #" + id + " não encontrado no banco!");
        }
    }


    @PostMapping("/{id}")
    public ModelAndView update(@PathVariable Long id, @Valid RequisicaoFormPedido requisicao, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("pedidos/edit");
            mv.addObject("pedidoId", id);
            mv.addObject("listaStatusPedido", StatusPedido.values());
            return mv;
        }
        else {
            Optional<Pedido> optional = this.pedidoRepository.findById(id);

            if (optional.isPresent()) {
                Pedido pedido = requisicao.toPedido(optional.get());
                this.pedidoRepository.save(pedido);

                return new ModelAndView("redirect:/pedidos/" + pedido.getId());
            }
            // não achou um registro na tabela Pedido com o id informado
            else {
                System.out.println("############ NÃO ACHOU O Pedido DE ID "+ id + " ############");
                return this.retornaErroPedido("UPDATE ERROR: Pedido #" + id + " não encontrado no banco!");
            }
        }
    }

    @GetMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("redirect:/Pedido");

        try {
            this.pedidoRepository.deleteById(id);
            mv.addObject("mensagem", "Pedido #" + id + " deletado com sucesso!");
            mv.addObject("erro", false);
        }
        catch (EmptyResultDataAccessException e) {
            System.out.println(e);
            mv = this.retornaErroPedido("DELETE ERROR: Pedido #" + id + " não encontrado no banco!");
        }

        return mv;
    }

    private ModelAndView retornaErroPedido(String msg) {
        ModelAndView mv = new ModelAndView("redirect:/pedidos");
        mv.addObject("mensagem", msg);
        mv.addObject("erro", true);
        return mv;
    }
}
