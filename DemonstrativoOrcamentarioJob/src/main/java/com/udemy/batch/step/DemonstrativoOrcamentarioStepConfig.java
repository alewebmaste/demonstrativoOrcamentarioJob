package com.udemy.batch.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.udemy.batch.dominio.GrupoLancamento;
import com.udemy.batch.reader.GrupoLancamentoReader;
import com.udemy.batch.writer.DemonstrativoOrcamentarioRodape;

@Configuration
public class DemonstrativoOrcamentarioStepConfig {
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Step demonstrativoOrcamentarioStep(GrupoLancamentoReader demonstrativoOrcamentarioHeader,
			MultiResourceItemWriter<GrupoLancamento> demonstrativoOrcamentarioWriter,
			DemonstrativoOrcamentarioRodape rodapeCallback) {
		
		
		return stepBuilderFactory
				.get("demonstrativoOrcamentarioStep")
				.<GrupoLancamento,GrupoLancamento>chunk(1)
				.reader(demonstrativoOrcamentarioHeader)
				.writer(demonstrativoOrcamentarioWriter)
				.listener(rodapeCallback)
				.build();
		
		
	}
	
}
