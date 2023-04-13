package com.example.springbatchprac;

import com.example.springbatchprac.domain.Customer;
import com.example.springbatchprac.domain.CustomerService;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.context.annotation.Bean;

public class ExistingServiceJob {
    @Bean
	public ItemReaderAdapter<Customer> customerItemReader(CustomerService customerService) {
		ItemReaderAdapter<Customer> adapter = new ItemReaderAdapter<>();

		adapter.setTargetObject(customerService);
		adapter.setTargetMethod("getCustomer");

		return adapter;
	}
}
