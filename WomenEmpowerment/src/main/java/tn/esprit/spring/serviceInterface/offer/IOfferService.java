package tn.esprit.spring.serviceInterface.offer;

import java.util.List;

import tn.esprit.spring.entities.Offer;

public interface IOfferService {
	public Offer saveOffer(Offer offer);
    public List<Offer> getAllOffers();
    public Offer getOfferById(Long offerId);
    public void deleteOfferById(Long offerId);
    public Offer updateOffer(Offer offer);
    public List<Offer> listAll(String keyword);

 
}

