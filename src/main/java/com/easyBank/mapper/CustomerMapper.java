package com.easyBank.mapper;

import com.easyBank.dto.AccountsDTO;
import com.easyBank.dto.CustomerDto;
import com.easyBank.model.Customer;

public class CustomerMapper {
	
	public static CustomerDto mapToCustomerDtoForRetriveTheData(Customer customer,CustomerDto customerDto) {
		
		
		customerDto.setName(customer.getName());
		customerDto.setEmail(customer.getEmail());
		 customerDto.setMobileNumber(customer.getMobileNumber());
		
		 
	     return customerDto;
	}
	
	 public static Customer mapToCustomerForSavingTheData(CustomerDto customerDto, Customer customer) {
	        customer.setName(customerDto.getName());
	        customer.setEmail(customerDto.getEmail());
	        customer.setMobileNumber(customerDto.getMobileNumber());
	        
	        return customer;
	    }

}
