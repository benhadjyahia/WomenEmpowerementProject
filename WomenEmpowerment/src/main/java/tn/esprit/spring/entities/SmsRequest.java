package tn.esprit.spring.entities;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SmsRequest {
	  @NotBlank
	    private  String phoneNumber; // destination

	    @NotBlank
	    private  String message;

	    public SmsRequest(@JsonProperty("phoneNumber") String phoneNumber,
	                      @JsonProperty("message") String message) {
	        this.phoneNumber = phoneNumber;
	        this.message = message;
	    }
	    

	    public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}


		public void setMessage(String message) {
			this.message = message;
		}


		public String getPhoneNumber() {
	        return phoneNumber;
	    }

	    public String getMessage() {
	        return message;
	    }

	    @Override
	    public String toString() {
	        return "SmsRequest{" +
	                "phoneNumber= ..." + '\'' +
	                ", message='" + message + '\'' +
	                '}';
	    }
}
