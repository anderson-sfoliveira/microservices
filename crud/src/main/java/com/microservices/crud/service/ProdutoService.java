package com.microservices.crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.microservices.crud.data.vo.ProdutoVO;
import com.microservices.crud.entity.Produto;
import com.microservices.crud.exception.ResourceNotFoundException;
import com.microservices.crud.message.ProdutoSendMessage;
import com.microservices.crud.repository.ProdutoRepository;

@Service
public class ProdutoService {

	private final ProdutoRepository produtoRepository;
	private final ProdutoSendMessage produtoSendMessage;
	
	@Autowired
	public ProdutoService(ProdutoRepository produtoRepository, ProdutoSendMessage produtoSendMessage) {
		this.produtoRepository = produtoRepository;
		this.produtoSendMessage = produtoSendMessage;
	}
	
	public ProdutoVO create(ProdutoVO produtoVO) {
		ProdutoVO produtoVOsalvo = ProdutoVO.create(produtoRepository.save(Produto.create(produtoVO)));
		produtoSendMessage.sendMessage(produtoVOsalvo);
		return produtoVOsalvo;
	}
	
	public Page<ProdutoVO> findAll(Pageable pageable) {
		var page = produtoRepository.findAll(pageable);
		return page.map(this::convertToProdutoVO);
	}

	private ProdutoVO convertToProdutoVO(Produto produto) {
		return ProdutoVO.create(produto);
	}
	
	public ProdutoVO findById(Long id) {
		var entity = produtoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		return ProdutoVO.create(entity);
	}
	
	public ProdutoVO update(ProdutoVO produtoVO) {
		produtoRepository.findById(produtoVO.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		return ProdutoVO.create(produtoRepository.save(Produto.create(produtoVO)));
	}
	
	public void delete(Long id) {
		var entity = produtoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		produtoRepository.delete(entity);
	}
}
