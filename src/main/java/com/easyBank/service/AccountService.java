package com.easyBank.service;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easyBank.constants.AccountsConstants;
import com.easyBank.dto.AccountsDTO;
import com.easyBank.dto.CustomerDto;
import com.easyBank.exception.CustomerAlreadyException;
import com.easyBank.exception.ResourceNotFoundException;
import com.easyBank.mapper.CustomerMapper;
import com.easyBank.model.Accounts;
import com.easyBank.model.Customer;
import com.easyBank.repository.AccountRepository;
import com.easyBank.repository.CustomerRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Pattern;
@Service
public class AccountService {

	
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	AccountRepository accountRepository;
	
		public void createAccount(CustomerDto customerDto) {
		

		Optional<Customer>optionalCustomer=	customerRepository.findByMobileNumber(customerDto.getMobileNumber());
		if(optionalCustomer.isPresent()) {
			throw new CustomerAlreadyException("Customer already created with this mobile number"+customerDto.getMobileNumber());
		}
		else {
		Customer customer=	customerRepository.save(CustomerMapper.mapToCustomerForSavingTheData(customerDto, new Customer()));

		

	
	Accounts accounts=	createNewAccount(customer);
	accountRepository.save(accounts);
		}
		
			
		}

private Accounts createNewAccount(Customer customer) {
    Accounts newAccount = new Accounts();
    newAccount.setCustomerId(customer.getCustomerId());
    long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

    newAccount.setAccountNumber(randomAccNumber);
    newAccount.setAccountType(AccountsConstants.SAVINGS);
    newAccount.setBranchAddress(AccountsConstants.ADDRESS);
   
    return newAccount;
}

public CustomerDto getAccountDetails(String mobileNumber) throws ResourceNotFoundException {
	
Optional<Customer> optionalcustomer=	customerRepository.findByMobileNumber(mobileNumber);
if(optionalcustomer.isEmpty())
{
	throw new ResourceNotFoundException("Resource not found with this mobile number:"+mobileNumber);
	
}
CustomerDto customerDto=new CustomerDto();
Customer customer=  optionalcustomer.get();
customerDto.setName(customer.getName());
customerDto.setMobileNumber(customer.getMobileNumber());
customerDto.setEmail(customer.getEmail());
AccountsDTO accountsDTO=getAccount(customer);
customerDto.setAccountsDTO(accountsDTO);
return customerDto;
}
public AccountsDTO getAccount(Customer customer) throws ResourceNotFoundException {
	
Optional<Accounts>optionalAccounts=	accountRepository.findByCustomerId(customer.getCustomerId());
if(optionalAccounts.isEmpty()) {
	throw new ResourceNotFoundException("Customer is not present with this id"+customer.getCustomerId());
}

AccountsDTO accountsDTO=new AccountsDTO();

 Accounts accounts=optionalAccounts.get();
 accountsDTO.setAccountNumber(accounts.getAccountNumber());
 accountsDTO.setAccountType(accounts.getAccountType());
 accountsDTO.setBranchAddress(accounts.getBranchAddress());

return accountsDTO;
	
}

public boolean updateAccount(CustomerDto customerDto) throws ResourceNotFoundException {
	boolean isUpdated=false;
	AccountsDTO accountsDTO=customerDto.getAccountsDTO();
	System.out.println(accountsDTO);
	Accounts accounts=null;
	if(accountsDTO!=null) {
	accounts=   accountRepository.findById(accountsDTO.getAccountNumber())
		.orElseThrow(()->new ResourceNotFoundException(
		"In this Account number we do not have any details:"+accountsDTO.getAccountNumber()));
	}
	accountsDTO.setAccountNumber(accounts.getAccountNumber());
	accountsDTO.setAccountType(accounts.getAccountType());
	accountsDTO.setBranchAddress(accounts.getBranchAddress());
	
	accountRepository.save(accounts);
	Long id=accounts.getCustomerId();
	Customer customer=null;
	customer=customerRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(
		"In this Id we do not have any details:"+id));
	
	customerDto.setName(customer.getName());
	customerDto.setMobileNumber(customer.getMobileNumber());
	customerDto.setEmail(customer.getEmail());
	
	customerRepository.save(customer);
	isUpdated =true;
	

	return isUpdated ;
}

@Transactional
public boolean deleteAccountsDetails( String mobileNumber) throws ResourceNotFoundException {
Customer customer=	customerRepository.findByMobileNumber(mobileNumber).orElseThrow(()->new ResourceNotFoundException("Customer not found with "
			+ "this Mobile Number : "+mobileNumber));
	accountRepository.deleteByCustomerId(customer.getCustomerId());
	customerRepository.deleteById(customer.getCustomerId());
	return true;
}

}
