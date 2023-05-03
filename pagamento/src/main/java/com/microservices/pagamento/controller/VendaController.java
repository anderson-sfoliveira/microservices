package com.microservices.pagamento.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.pagamento.data.vo.VendaVO;
import com.microservices.pagamento.service.VendaService;


@RestController
@RequestMapping("/venda")
public class VendaController {

	@Value("${server.port}")
	String port;
	
	private final VendaService vendaService;
	private final PagedResourcesAssembler<VendaVO> assembler;

	public VendaController(VendaService vendaService, PagedResourcesAssembler<VendaVO> assembler) {
		this.vendaService = vendaService;
		this.assembler = assembler;
	}
	
	@RequestMapping("/testePorta")
	public String testePorta() {
		return port;
	}

	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml", "application/x-yaml" })
	public VendaVO findById(@PathVariable("id") Long id) {
		VendaVO vendaVO = vendaService.findById(id);
		vendaVO.add(linkTo(methodOn(VendaController.class).findById(id)).withSelfRel());
		return vendaVO;
	}

	@GetMapping(produces = { "application/json", "application/xml", "application/x-yaml" })
	public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "12") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {

		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "data"));

		Page<VendaVO> vendas = vendaService.findAll(pageable);

		vendas.stream()
				.forEach(v -> v.add(linkTo(methodOn(VendaController.class).findById(v.getId())).withSelfRel()));

		PagedModel<EntityModel<VendaVO>> pageModel = assembler.toModel(vendas);

		return new ResponseEntity<>(pageModel, HttpStatus.OK);
	}

	@PostMapping(produces = { "application/json", "application/xml", "application/x-yaml" },
				 consumes = { "application/json", "application/xml", "application/x-yaml" })
	public VendaVO create(@RequestBody VendaVO vendaVO) {
		VendaVO vendaVOSalvo = vendaService.create(vendaVO);
		vendaVOSalvo.add(linkTo(methodOn(VendaController.class).findById(vendaVOSalvo.getId())).withSelfRel());
		return vendaVOSalvo;
	}
}
