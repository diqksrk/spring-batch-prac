package com.example.springbatchprac;

import com.example.springbatchprac.batch.CustomerByCityQueryProvider;
import com.example.springbatchprac.domain.Customer;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;

public class JpaJob {
    @Bean
	@StepScope
	public JpaPagingItemReader<Customer> customerItemReader(
			EntityManagerFactory entityManagerFactory,
			@Value("#{jobParameters['city']}") String city) {
        CustomerByCityQueryProvider queryProvider = new CustomerByCityQueryProvider();
        queryProvider.setCityName(city);

		return new JpaPagingItemReaderBuilder<Customer>()
				.name("customerItemReader")
				.entityManagerFactory(entityManagerFactory)
                .queryProvider(queryProvider)
				.parameterValues(Collections.singletonMap("city", city))
				.build();
	}
}
