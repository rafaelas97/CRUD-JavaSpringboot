package com.example.crud.controller;

import com.example.crud.model.Evento;
import com.example.crud.model.Usuario;
import com.example.crud.dto.EventoDTO;
import com.example.crud.dto.UsuarioIdDTO;
import com.example.crud.repository.EventoRepository;
import com.example.crud.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<EventoDTO>> listarEventos() {
        List<Evento> eventos = eventoRepository.findAll();
        List<EventoDTO> eventoDTOs = eventos.stream().map(evento -> {
           
        List<UsuarioIdDTO> usuarioDTOs = evento.getUsuarios().stream().map(usuario -> {
            return new UsuarioIdDTO(usuario.getId(), usuario.getNome(), usuario.getEmail()); 
        }).collect(Collectors.toList());

        return new EventoDTO(
            evento.getId(),           
            evento.getNome(),        
            evento.getDescricao(),  
            evento.getData().toString(), 
            usuarioDTOs      
        );
    }).collect(Collectors.toList());

    return ResponseEntity.ok(eventoDTOs);
    }

    @PostMapping
    public ResponseEntity<Evento> criarEvento(@RequestBody Evento evento) {
    Evento novoEvento = eventoRepository.save(evento);
    return ResponseEntity.status(HttpStatus.CREATED).body(novoEvento);
    }

    @PostMapping("/{id}/inscricao")
    public ResponseEntity<?> inscreverUsuario(@PathVariable Long id, @RequestBody Map<String, Long> payload) {
        Optional<Evento> eventoOptional = eventoRepository.findById(id);
        if (eventoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Long usuarioId = payload.get("usuarioId");
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioId);
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado");
        }

        Evento evento = eventoOptional.get();
        Usuario usuario = usuarioOptional.get();

        if (!evento.getUsuarios().contains(usuario)) {
            evento.getUsuarios().add(usuario);
        }

        if (!usuario.getEventos().contains(evento)) {
            usuario.getEventos().add(evento);
        }

        usuarioRepository.save(usuario);
        eventoRepository.save(evento);

        return ResponseEntity.ok("Usuário inscrito com sucesso!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evento> atualizarEvento(@PathVariable Long id, @RequestBody Evento eventoAtualizado) {
        Optional<Evento> eventoExistente = eventoRepository.findById(id);
        
        if (!eventoExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
        }

        Evento evento = eventoExistente.get();
        evento.setNome(eventoAtualizado.getNome());
        evento.setDescricao(eventoAtualizado.getDescricao());
        evento.setData(eventoAtualizado.getData());

        eventoRepository.save(evento); 
        return ResponseEntity.ok(evento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarEvento(@PathVariable Long id) {
        if (!eventoRepository.existsById(id)) {
            return ResponseEntity.status(404).body("Evento não encontrado!");
        }
        eventoRepository.deleteById(id);
        return ResponseEntity.status(200).body("Evento deletado com sucesso!");
    }
}
