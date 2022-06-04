package tn.esprit.spring.controllers;

import java.awt.print.Pageable;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import springfox.documentation.annotations.ApiIgnore;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Donation;
import tn.esprit.spring.entities.Event;
import tn.esprit.spring.entities.Payment;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.entities.WomenNeedDonation;
import tn.esprit.spring.repository.*;
import tn.esprit.spring.security.UserPrincipal;
import tn.esprit.spring.service.event.PaymentService;
import tn.esprit.spring.serviceInterface.DonationService;
import tn.esprit.spring.serviceInterface.EventService;
@RestController
@RequestMapping("/Donation")
public class DonationController {
	@Autowired
	EventService eventService;
	@Autowired
	DonationService donationService;
	
	
	@Autowired
	DonationRepo donationRepo;
	 @Autowired
	    PaymentService paymentService;


	@PostMapping("/paymentintent")
    public ResponseEntity<String> payment(@RequestBody Payment paymentIntentDto) throws StripeException {
        PaymentIntent paymentIntent = paymentService.paymentIntent(paymentIntentDto);
        String paymentStr = paymentIntent.toJson();
        return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
    }
	

	@PostMapping("add-Donation-Event/{idEvent}")
	public Donation addDonation_to_Event(@PathVariable("idEvent") Long idEvent, @AuthenticationPrincipal UserPrincipal u,@RequestBody Donation donation){
		return donationService.addDonation_to_Event(idEvent,u.getId(),donation);
		 
	}
	
	


	
	@DeleteMapping("delete/{idDonation}")
	@ResponseBody
	public void deleteDonation(@PathVariable Long idDonation ){
		donationService.removeDonation(idDonation);
	}
	
	
	
	@GetMapping("/bestDoner")
	public List<Object> bestDonner() {
		//donationRepo.bestDonner();
		
		return 	donationRepo.bestDonner();
		
	}
	
	
	
	
	

	
	@GetMapping("/Get-all-Donation")
	public List<Donation> Get_all_Donation(){
		return donationService.Get_all_Donation();
	}
	


    

	

	@PutMapping("/AffactDonationToWomen/{idEvent}")
	@ResponseBody
	public void AffactDonationToWomen( @PathVariable("idEvent") Long idEvent) {
		 donationService.NeedDonnation(idEvent);
	}
	
	
	
	
	
	  
    @GetMapping("/qrcode/{idDonation}")
	public void takeYourPdfDonation(@PathVariable("idDonation") Long idDonation) throws IOException, InterruptedException {
		
		Donation donation = donationRepo.findById(idDonation).orElse(null);
			//	String text=donation.getCourse().getCourseName()+certificate.getUser().getUsername()+"'mail'"+certificate.getUser().getEmail();
		String text= donation.getEvent().getEventName()+donation.getAmountForEvent()+donation.getCodePayement()+donation.getDonor().getUsername();
				HttpRequest request = HttpRequest.newBuilder()
						.uri(URI.create("https://codzz-qr-cods.p.rapidapi.com/getQrcode?type=text&value="+text+""))
						.header("x-rapidapi-host", "codzz-qr-cods.p.rapidapi.com")
						.header("x-rapidapi-key", "b648c42070msh2f1e24111397e42p1155f4jsn864d7705eee5")
						.method("GET", HttpRequest.BodyPublishers.noBody())
						.build();
				HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
				System.err.println(response.body());
				
				
				
				donation.setQrcode(response.body().substring(8, 61));
				donationRepo.saveAndFlush(donation);
				
				
		}
	@PostMapping(path="facture/{idDonation}")
	public ResponseEntity<byte[]> factureDonation(@PathVariable("idDonation") Long idDonation) throws IOException, InterruptedException{
		Donation don = donationRepo.findById(idDonation).get();
	HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("https://yakpdf.p.rapidapi.com/pdf"))
			.header("content-type", "application/json")
			.header("X-RapidAPI-Host", "yakpdf.p.rapidapi.com")
			.header("X-RapidAPI-Key", "b648c42070msh2f1e24111397e42p1155f4jsn864d7705eee5")
			.method("POST", HttpRequest.BodyPublishers.ofString("{\r\n    \"pdf\": {\r\n        \"format\": \"A4\",\r\n        \"printBackground\": true,\r\n        \"scale\": 1\r\n    },\r\n    \"source\": {\r\n        \"html\": \"<!DOCTYPE html><html lang=\\\"en\\\"><head><meta charset=\\\"UTF-8\\\"><meta name=\\\"viewport\\\" content=\\\"width=device-width, initial-scale=1.0\\\"></head><body><div><center><h2>invoice</h2></center></br><center><h4>"+don.getDonor().getName()+" Thanks for your help</h4><h5>this event will be transmitted for "+don.getEvent().getEventName()+"</h5></center><h6></h6><h6 >amount</h6><h6> "+don.getAmountForEvent()+"</h6><img src=\\\""+don.getQrcode()+"\\\"></body></html>\"\r\n    },\r\n    \"wait\": {\r\n        \"for\": \"navigation\",\r\n        \"timeout\": 250,\r\n        \"waitUntil\": \"load\"\r\n    }\r\n}"))
			.build();
	 HttpResponse<byte[]> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofByteArray());
	 byte[] res = response.body();
	 return ResponseEntity.ok()
             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ordeesr.pdf") 
             .contentType(MediaType.APPLICATION_PDF).body(res);
	}
	
	
	
	@PostMapping(path="addWomenNeedDoantion/{idUser}")
	public void womenNeedDonation(@PathVariable("idUser") Long idUser,@RequestBody WomenNeedDonation wnd) {
		
		donationService.womenNeedDonation(idUser, wnd);
		
	}
	
	@DeleteMapping("deleteWomenNeedDonation/{idDonation}")
	@ResponseBody
	public void DELETEWomenNeedDonation(@PathVariable Long idWomenNeedDonation ){
		donationService.removeDonation(idWomenNeedDonation);
	}
	
	
	@PostMapping(path="addDonationsss/{idEvent}")
	public Donation addDonation(@PathVariable Long idEvent , @AuthenticationPrincipal UserPrincipal u ,@RequestBody Donation donation) {
		return donationService.addDonation(idEvent, u.getId(), donation);
		
		
		
	}
	
	@PostMapping("add-d-Event/{idEvent}")
	public Donation addDonationFinal(Long idEvent, @ApiIgnore @AuthenticationPrincipal UserPrincipal u ,@RequestBody Donation donation) {
		return donationService.addDonationVfinal(idEvent, u.getId(), donation);
		
		
		
	}
	

	}
	
	
	
	
	
	
	
	
	

