package com.productos.controllers;

import com.productos.dto.ProductoDTO;
import com.productos.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService service;

    @PostMapping
    public ResponseEntity<EntityModel<ProductoDTO>> crear(@RequestBody ProductoDTO dto) {
        ProductoDTO creado = service.crear(dto);
        EntityModel<ProductoDTO> resource = EntityModel.of(creado,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).obtener(creado.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).listar()).withRel("productos")
        );
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ProductoDTO>>> listar() {
        List<ProductoDTO> productos = service.listar();
        List<EntityModel<ProductoDTO>> productosResource = productos.stream()
                .map(producto -> EntityModel.of(producto,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).obtener(producto.getId())).withSelfRel()
                ))
                .collect(Collectors.toList());
        CollectionModel<EntityModel<ProductoDTO>> collectionModel = CollectionModel.of(productosResource,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).listar()).withSelfRel()
        );
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProductoDTO>> obtener(@PathVariable Integer id) {
        ProductoDTO producto = service.obtenerPorId(id);
        EntityModel<ProductoDTO> resource = EntityModel.of(producto,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).obtener(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).listar()).withRel("productos")
        );
        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProductoDTO>> actualizar(@PathVariable Integer id, @RequestBody ProductoDTO dto) {
        ProductoDTO actualizado = service.actualizar(id, dto);
        EntityModel<ProductoDTO> resource = EntityModel.of(actualizado,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).obtener(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProductoController.class).listar()).withRel("productos")
        );
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}