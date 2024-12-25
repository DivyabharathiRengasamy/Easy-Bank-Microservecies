package com.easyBank.constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.easyBank.dto.CustomerDto;
import com.easyBank.dto.ErrorResponseDto;
import com.easyBank.dto.ResponseDto;
import com.easyBank.exception.ResourceNotFoundException;
import com.easyBank.model.Customer;
import com.easyBank.service.AccountService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

/**
 * @author Eazy Bytes
 */


@RestController
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class AccountsController {
	
@Autowired   
private AccountService accountService;

    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
    	accountService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }
    
 
    
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
        @ApiResponse(responseCode = "404", description = "HTTP Status Resource Not Found")
    })
    @GetMapping("/getAccountDetails")
    public ResponseEntity<CustomerDto>getAccountDetails(@RequestParam  
    		@Pattern(regexp = "\\d{10}",message = "invalid mobile number") 
    		String mobileNumber) throws ResourceNotFoundException
    {
    CustomerDto customerDto=	accountService.getAccountDetails(mobileNumber);
    	return new ResponseEntity<CustomerDto>(customerDto, HttpStatus.OK);
    	
    }
    
    
    
    @ApiResponses({
    	@ApiResponse(responseCode = "200",description = "Http status ok"),
    	   @ApiResponse(responseCode = "404", description = "HTTP Status Resource Not Found"),
    	  @ApiResponse(responseCode = "417",description = "Expectation Failed")
    })
    
    @PutMapping("/updateAccountsDetails")
    public ResponseEntity<ResponseDto>updateAccountsDetails(CustomerDto customerDto) throws ResourceNotFoundException{
    	
    boolean isUpdated=	accountService.updateAccount(customerDto);
    	if(isUpdated) {
    	return ResponseEntity.status(HttpStatus.OK)
    			.body(new ResponseDto(AccountsConstants.STATUS_200, 
    			AccountsConstants.MESSAGE_200));
    }
    	else {
    		return ResponseEntity
    				.status(HttpStatus.EXPECTATION_FAILED)
    				.body(new ResponseDto(AccountsConstants.STATUS_417,AccountsConstants.MESSAGE_417_UPDATE));
    	}
    }
    
    @ApiResponses({
    	@ApiResponse (responseCode = "200",description ="Http status ok"),
    	@ApiResponse(responseCode = "404",description ="HTTP Status Resource Not Found"),
    	  @ApiResponse(responseCode = "500",description = "Internal server error")
    })
    
    @DeleteMapping("/deleteAccount")
    public ResponseEntity<ResponseDto>deleteAccountsDetails(@RequestParam 
    		@Pattern(regexp ="(^$|[0-9]{10})",message = "mobile number should be 10 digits" )String mobileNumber ) throws ResourceNotFoundException{
    boolean isDeleted =	accountService.deleteAccountsDetails(mobileNumber);
    if(isDeleted) {
    	return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(AccountsConstants.STATUS_200,AccountsConstants.MESSAGE_200));
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(AccountsConstants.STATUS_417,AccountsConstants.MESSAGE_417_DELETE));
    	
    }
   
    public String  getWeather() {
    	String url="";
    	RestTemplate restTemplate=new RestTemplate();
    String response=	restTemplate.getForObject(url, String.class);
	return response;
    }
    
    

}