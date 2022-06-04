package tn.esprit.spring.serviceInterface;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import com.stripe.exception.StripeException;

import tn.esprit.spring.entities.Donation;
import tn.esprit.spring.entities.Payment;
import tn.esprit.spring.entities.WomenNeedDonation;




public interface DonationService {
	public Donation addDonation(Donation donation);

	public void removeDonation(Long idDonation);
	public Donation addDonation_to_Event(Long idEvent, Long idUser ,Donation donation);
    public List<Donation> Get_all_Donation();
	public void NeedDonnation(Long idEvent);
	public void womenNeedDonation(Long iduser, WomenNeedDonation wnd);
	public void DeleteWomenNeedDonation(Long WND);


	public Donation addDonation(Long idEvent , Long idUser , Donation donation);
	public Donation addDonationVfinal(Long idEvent , Long idUser , Donation donation);
	
	
    
}
