package tn.esprit.spring.service.event;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import tn.esprit.spring.entities.Donation;
import tn.esprit.spring.entities.Event;
import tn.esprit.spring.entities.Media;
import tn.esprit.spring.entities.Payment;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.entities.WomenNeedDonation;
import tn.esprit.spring.repository.DonationRepo;
import tn.esprit.spring.repository.EventRepo;
import tn.esprit.spring.repository.PostRepo;

import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.repository.WomenNeedHelpRepo;
import tn.esprit.spring.service.user.ServiceAllEmail;
import tn.esprit.spring.serviceInterface.DonationService;

@Service
public class DonationServiceImpl implements DonationService {

	@Autowired
	DonationRepo donationRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	EventRepo eventRepo;

	

	@Autowired
	WomenNeedHelpRepo womanNeedHelpRepo;
	@Autowired
	CloudinaryService cloud;

	@Autowired
	ServiceAllEmail emailService;

	@Autowired
	PaymentService ps;

	@Override
	public Donation addDonation(Donation donation) {
		donationRepo.save(donation);

		return donation;
	}
	
	
	
	
	
	
	

	@Override
	public Donation addDonation_to_Event(Long idEvent, Long idUser ,Donation donation){
		Event event = eventRepo.findById(idEvent).orElse(null);
		User user = userRepo.findById(idUser).orElse(null);
	
		donation.setEvent(event);
		donation.setDonor(user);
	
		return donationRepo.save(donation);

	}



	@Override
	public void removeDonation(Long idDonation) {
		Donation donation = donationRepo.findById(idDonation).orElse(null);
		donationRepo.delete(donation);

	}

	@Override
	public List<Donation> Get_all_Donation() {

		return donationRepo.findAll();
	}

	@Override
	public void NeedDonnation(Long idEvent) {
		Event e = eventRepo.findById(idEvent).orElse(null);


		for (WomenNeedDonation w : womanNeedHelpRepo.findAll()) {
			if (w.getPriority() == 1) {
				e.setMontantCollecte(e.getMontantCollecte()-w.getMontant_needed());
				//float montant = w.getMontant_needed();
				w.setGetHelp(true);
				//w.setMontantRecu(w.getMontant_needed());
				//w.setMontant_needed(w.getMontant_needed() - montant);
				//System.out.println(w.getMontant_needed()-montant);
				w.setMontantRecu(w.getMontant_needed());
				w.setMontant_needed(0);
				
				eventRepo.flush();
				womanNeedHelpRepo.saveAndFlush(w);
			}
		

		}

	}

	@Override
	public void womenNeedDonation(Long iduser, WomenNeedDonation wnd) {
	
		User user = userRepo.findById(iduser).orElse(null);
		wnd.setUser(user);
		womanNeedHelpRepo.save(wnd);
		
		
	}

	@Override
	public void DeleteWomenNeedDonation(Long idWND) {
		WomenNeedDonation wnd = womanNeedHelpRepo.findById(idWND).orElse(null);
		womanNeedHelpRepo.save(wnd);
		
		
	}








	@Override
	public Donation addDonation(Long idEvent, Long idUser,Donation donation) {
		Set<Event> eventList = new HashSet<Event>();
		Set<Donation> donationList = new HashSet<Donation>();

		User user = userRepo.findById(idUser).orElse(null);
		Event event = eventRepo.findById(idEvent).orElse(null);
		donationList.add(donation);
		user.setDonations(donationList);
		event.setDonations(donationList);
	
		return donationRepo.save(donation);
	}








	@Override
	public Donation addDonationVfinal(Long idEvent, Long idUser, Donation donation) {
	

		Event event = eventRepo.findById(idEvent).orElse(null);
		User user = userRepo.findById(idUser).orElse(null);
	
		donation.setEvent(event);
		donation.setDonor(user);
	
		return donationRepo.save(donation);
	
	}

	
	
	/*@Override
	public Donation addDonation_to_Event(Long idEvent, Long idUser, Payment pi) throws StripeException {
		Donation donation = new Donation();

		Event event = eventRepo.findById(idEvent).orElse(null);
		User user = userRepo.findById(idUser).orElse(null);
		PaymentIntent p = ps.paymentIntent(pi);
		donation.setEvent(event);
		donation.setDonor(user);
		donation.setAmount_forEvent(p.getAmount());
		donation.setCodePayement(p.getId());
		// donation.setDonationDate(p.GET);
			event.setMontantCollecte(event.getMontantCollecte()+p.getAmount());
		 ps.confirm(p.getId());
		return donationRepo.save(donation);

	}*/

	




	

}
