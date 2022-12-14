package com.alves.financa.controller;

import com.alves.financa.modelo.converter.ReceitaConverter;
import com.alves.financa.modelo.dto.DetalhesReceitaDto;
import com.alves.financa.modelo.dto.ReceitaDto;
import com.alves.financa.controller.form.AtualizarReceitaDtoInput;
import com.alves.financa.controller.form.ReceitaDtoInput;
import com.alves.financa.modelo.Receita;
import com.alves.financa.repository.ReceitasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/receita")
public class ReceitasController {

    @Autowired
    private ReceitasRepository receitasRepository;
    @Autowired
    private ReceitaConverter receitaConverter;


    @GetMapping
    public List<ReceitaDto> listarReceitas(String nomeBusca) {
        List<Receita> listaDeReceitas;
        if (nomeBusca == null){
            listaDeReceitas = receitasRepository.findAll();
        }else {
            listaDeReceitas = receitasRepository.findReceitaByDescricao(nomeBusca);
        }
        return receitaConverter.toReceitaDtoList(listaDeReceitas) ;
    }

    @PostMapping
    public ResponseEntity<ReceitaDto> cadastrarReceitas(@RequestBody @Valid ReceitaDtoInput receitaDtoInput){
        Receita receita = receitaConverter.toReceitaBanco(receitaDtoInput);
        receitasRepository.save(receita);

        return ResponseEntity.ok(receitaConverter.toReceitaDto(receita));
    }
    @GetMapping("/{id}")
    public ResponseEntity<DetalhesReceitaDto> detalhar(@PathVariable Long id) {
        Optional<Receita> receita = receitasRepository.findById(id);
        if (receita.isPresent()) {
            return ResponseEntity.ok(new DetalhesReceitaDto(receita.get()));
        }
        return ResponseEntity.notFound().build();
    }
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ReceitaDto> atualizar(@PathVariable Long id,@RequestBody
    @Valid AtualizarReceitaDtoInput atualizarReceitaDtoInput) {
        Receita receita = atualizarReceitaDtoInput.atualizar(id, receitasRepository);

        return ResponseEntity.ok(receitaConverter.toReceitaDto(receita));

    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> remover(@PathVariable Long id) {
        Optional<Receita> optional = receitasRepository.findById(id);
        if (optional.isPresent()) {
            receitasRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();

    }



}
