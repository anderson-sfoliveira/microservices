package com.microservices.pagamento.message;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.microservices.pagamento.data.vo.ProdutoVO;
import com.microservices.pagamento.entity.Produto;
import com.microservices.pagamento.repository.ProdutoRepository;

@Component
public class ProdutoReceiveMessage {
	
	public final ProdutoRepository produtoRepository;

	@Autowired
	public ProdutoReceiveMessage(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}
	
	@RabbitListener(queues = "${crud.rabbitmq.queue}")
	public void receive(@Payload ProdutoVO produtoVO) {
		produtoRepository.save(Produto.create(produtoVO));
	}
}
