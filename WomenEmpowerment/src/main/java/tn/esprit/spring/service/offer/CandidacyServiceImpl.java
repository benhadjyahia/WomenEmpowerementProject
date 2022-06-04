package tn.esprit.spring.service.offer;

import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.entities.Candidacy;
import tn.esprit.spring.entities.Offer;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.enumerations.CandidacyState;
import tn.esprit.spring.repository.CandidacyRepository;
import tn.esprit.spring.repository.IOfferRepository;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.service.user.ServiceAllEmail;
import tn.esprit.spring.serviceInterface.offer.ICandidacyService;

@Service
@Slf4j
public class CandidacyServiceImpl implements ICandidacyService {

	@Autowired
	CandidacyRepository CandidacyRepo;
	@Autowired
	UserRepository UserRepo;
	@Autowired
	IOfferRepository OfferRepo;
	@Autowired
	ServiceAllEmail emailservice;
	
	
	
	
	@Override
	public List<Candidacy> getAllCandidacies() {
		// TODO Auto-generated method stub
		CandidacyRepo.findAll();
		return null;
	}
	
	@Override
	public  Candidacy postulerOffre (Long offerId, Long userId) {
		

		Candidacy candidacy =new Candidacy();
		Offer offer = OfferRepo.findById(offerId).orElse(null);
		//System.err.println(offer.getOfferId().toString());
		User user= UserRepo.findById(userId).get();
		//System.err.println(user.getUserId());
		//log.info(user.getEmail());
		candidacy.setOffer(offer);
		candidacy.setCandidate(user);
		user.getCandidacies().add(candidacy);
		candidacy.setCandidacyState(CandidacyState.Unseen);
		
		// TODO Auto-generated method stub
		return CandidacyRepo.save(candidacy);
	   
	}

	
	

	@Override
	public List<String> getMyCandidacy( String keyword) {
		
		 if (keyword != null) {
	            return CandidacyRepo.search(keyword);}
		 return null;	    }

	/*@Override
	public List<Candidacy> getMyFavoriteCandidacy(String keyword) {
		// TODO Auto-generated method stub
		if (keyword != null) {
            return CandidacyRepo.searchFavorite(keyword);}
	 return null;	    }*/

	@Override
	public void HoldCandidacy(Long candidacy_id) throws MessagingException {
		// TODO Auto-generated method stub
		Candidacy cc = CandidacyRepo.findById(candidacy_id).orElse(null);
		cc.setCandidacyState(CandidacyState.Onhold);
		String firstName=CandidacyRepo.getCandidateName(candidacy_id);
		String email=CandidacyRepo.getCandidateEmail(candidacy_id);
		String title=CandidacyRepo.getOfferTitle(candidacy_id);
		String candidacyState="On Hold";
		log.error(firstName+email+title);
		emailservice.sendCandidacyEmail(firstName, title, email, candidacyState);		
		CandidacyRepo.saveAndFlush(cc);
	}

	@Override
	public void RestrainCandidacy(Long candidacy_id) throws MessagingException {
		// TODO Auto-generated method stub
		Candidacy cc = CandidacyRepo.findById(candidacy_id).orElse(null);
		String firstName=CandidacyRepo.getCandidateName(candidacy_id);
		String email=CandidacyRepo.getCandidateEmail(candidacy_id);
		String title=CandidacyRepo.getOfferTitle(candidacy_id);
		String candidacyState="Is Denied";
		log.error(firstName+email+title);
		emailservice.sendCandidacyEmail(firstName, title, email, candidacyState);		
		CandidacyRepo.deleteById(candidacy_id);
		
	}

	@Override
	public void SetFavorite(Long candidacy_id) {
		Candidacy cc = CandidacyRepo.findById(candidacy_id).orElse(null);
		cc.setBookmarked(true);
		CandidacyRepo.save(cc);		
	}

	@Override
	public List<String> getMyFavoriteCandidacy(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}
	}
	
	
	
	

