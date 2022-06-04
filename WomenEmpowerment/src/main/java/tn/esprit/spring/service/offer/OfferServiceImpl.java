package tn.esprit.spring.service.offer;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.spring.entities.Offer;
import tn.esprit.spring.repository.IOfferRepository;
import tn.esprit.spring.serviceInterface.offer.IOfferService;


@Service
public class OfferServiceImpl implements IOfferService {
	
	@Autowired 	
	private IOfferRepository OfferRepo;

	@Override
	public Offer saveOffer(Offer offer) {
		// TODO Auto-generated method stub
		return OfferRepo.save(offer);
	}

	@Override
	public List<Offer> getAllOffers() {
		// TODO Auto-generated method stub
		return OfferRepo.findAll();
	}

	@Override
	public Offer getOfferById(Long offerId) {
		// TODO Auto-generated method stub
		 Optional<Offer> opt = OfferRepo.findById(offerId);
	       if(opt.isPresent()) {
	           return opt.get();
	       } else {
	           return null;
	       }
	}

	@Override
	public void deleteOfferById(Long offerId) {
		// TODO Auto-generated method stub
		OfferRepo.delete(getOfferById(offerId));
	}

	@Override
	public Offer updateOffer(Offer offer) {
		// TODO Auto-generated method stub
		
		OfferRepo.saveAndFlush(offer);
		return offer;
	}

	 public List<Offer> listAll(String keyword) {
	        if (keyword != null) {
	            return OfferRepo.search(keyword);
	        }
	        return OfferRepo.findAll();
	    }
	 

}
