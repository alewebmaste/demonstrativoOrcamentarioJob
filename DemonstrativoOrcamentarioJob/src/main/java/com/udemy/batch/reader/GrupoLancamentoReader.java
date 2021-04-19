package com.udemy.batch.reader;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.udemy.batch.dominio.GrupoLancamento;

@Component
public class GrupoLancamentoReader
		implements ItemStreamReader<GrupoLancamento>, ResourceAwareItemReaderItemStream<GrupoLancamento> {
	
	@Autowired
	private JdbcCursorItemReader<GrupoLancamento> delegate;	
	private GrupoLancamento lancamentoAtual;

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		delegate.open(executionContext);
		
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		delegate.update(executionContext);
	}

	@Override
	public void close() throws ItemStreamException {
		delegate.close();
	}

	@Override
	public GrupoLancamento read() throws Exception{

		if(lancamentoAtual == null) {
			lancamentoAtual = delegate.read();
		}
		
		GrupoLancamento grupoLancamento = lancamentoAtual;
		
		lancamentoAtual = null;
		
		if(grupoLancamento != null) {
			
			GrupoLancamento proximoLancamento = peak();
			
			while(isLancamentoSelecionado(grupoLancamento,proximoLancamento)) {
				grupoLancamento.getLancamentos().add(lancamentoAtual.getLancamentoTmp());
				proximoLancamento = peak();
			}
			
			grupoLancamento.getLancamentos().add(grupoLancamento.getLancamentoTmp());
			
		}
		
		return grupoLancamento;
	}

	private boolean isLancamentoSelecionado(GrupoLancamento grupoLancamento, GrupoLancamento proximoLancamento) {
		
		return proximoLancamento != null && proximoLancamento.getCodigoNaturezaDespesa().equals(grupoLancamento.getCodigoNaturezaDespesa());
	}

	public GrupoLancamento peak() throws Exception {
		 lancamentoAtual = delegate.read();
		return lancamentoAtual;
	}

	@Override
	public void setResource(Resource resource) {
		// TODO Auto-generated method stub		
	}

}
