package com.microservices.pagamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservices.pagamento.entity.ProdutoVenda;

@Repository
public interface ProdutoVendaRepository extends JpaRepository<ProdutoVenda, Long> {

}
