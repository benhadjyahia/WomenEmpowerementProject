package tn.esprit.spring.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("twilio")
public class TwilioConfiguration {
	private String accountSid;
    private String authToken;
    private String trialNumber;

    public TwilioConfiguration() {

    }



	



	



	public String getAccountSid() {
		return "ACc26b75363b8a3e8397b617e6bd44ae25";
	}



	public void setAccountSid(String accountSid) {
		this.accountSid = accountSid;
	}



	public String getAuthToken() {
		return "57e91d2120cd40e8555d382ee31760b8";
	}



	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}











	public String getTrialNumber() {
		return "+17402475611";
	}











	public void setTrialNumber(String trialNumber) {
		this.trialNumber = trialNumber;
	}











	












	

   
}
