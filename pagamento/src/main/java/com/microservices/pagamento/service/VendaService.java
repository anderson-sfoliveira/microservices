package com.microservices.pagamento.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.microservices.pagamento.data.vo.VendaVO;
import com.microservices.pagamento.entity.ProdutoVenda;
import com.microservices.pagamento.entity.Venda;
import com.microservices.pagamento.exception.ResourceNotFoundException;
import com.microservices.pagamento.repository.ProdutoVendaRepository;
import com.microservices.pagamento.repository.VendaRepository;

@Service
public class VendaService {

	private final VendaRepository vendaRepository;
	private final ProdutoVendaRepository produtoVendaRepository;
	
	@Autowired
	public VendaService(VendaRepository vendaRepository, ProdutoVendaRepository produtoVendaRepository) {
		this.vendaRepository = vendaRepository;
		this.produtoVendaRepository = produtoVendaRepository;
	}
	
	public VendaVO create(VendaVO vendaVO) {
		Venda vendaSalva = vendaRepository.save(Venda.create(vendaVO));
		
		List<ProdutoVenda> produtoVenda = new ArrayList<>();
		vendaVO.getProdutos().forEach(p -> {
			ProdutoVenda pv = ProdutoVenda.create(p);
			pv.setVenda(vendaSalva);
			produtoVenda.add(produtoVendaRepository.save(pv));
		});
		
		vendaSalva.setProdutos(produtoVenda);
		
		return VendaVO.create(vendaSalva);
	}
	
	public Page<VendaVO> findAll(Pageable pageable) {
		var page = vendaRepository.findAll(pageable);
		return page.map(this::convertToVendaVO);
	}

	private VendaVO convertToVendaVO(Venda venda) {
		return VendaVO.create(venda);
	}
	
	public VendaVO findById(Long id) {
		var entity = vendaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		return VendaVO.create(entity);
	}
}
