package tn.esprit.spring.service.offer;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.RequestFailedException;

import tn.esprit.spring.entities.Candidacy;
import tn.esprit.spring.entities.Offer;
import tn.esprit.spring.repository.CandidacyRepository;
import tn.esprit.spring.repository.IOfferRepository;
import tn.esprit.spring.serviceInterface.offer.ICalendarService;

import com.nylas.Calendar;
import com.nylas.CalendarQuery;
import com.nylas.Calendars;
import com.nylas.Event;
import com.nylas.EventQuery;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;


import com.nylas.Participant;
import com.nylas.Event.Conferencing;
import com.nylas.Event.Timespan;
@Service
public class CalendarServiceImpl {
	
	@Autowired 
	IOfferRepository offerRepo;
	@Autowired
	CandidacyRepository candidacyRepo; 
	
	public String createCal(long offerId) throws IOException, RequestFailedException {
		 
		Offer o = offerRepo.findById(offerId).get();
		 NylasClient client = new NylasClient();
		  NylasAccount account = client.account("wOIy4UmmlWREVdr9S4pH5kAzRprvax");
		  Calendars calendars = account.calendars();
		  Calendar newCal1 = new Calendar();
		  newCal1.setName(o.getTitle()+" Offer");
		  newCal1.setDescription(o.getDescription());
		  newCal1.setLocation(o.getLocation());
		  newCal1.setTimezone("America/Los_Angeles");
		  Calendar created = calendars.create(newCal1);
		  o.setCalendId(created.getId());
		  offerRepo.saveAndFlush(o);
		  return created.getId();
	}
	
	
	
	
	
 public void postEventExample(Long candidacyId,int hour,int minutes,LocalDate date) throws IOException, RequestFailedException {
	 long offerId= candidacyRepo.getOfferId(candidacyId);
	 Offer o =offerRepo.findById(offerId).get();
	 Candidacy cc = candidacyRepo.findById(candidacyId).orElse(null);
	 String candidacyState="Accepted";
  NylasClient client = new NylasClient();
  NylasAccount account = client.account("wOIy4UmmlWREVdr9S4pH5kAzRprvax");
  //Calendars calendars = account.calendars();
  Event.When when = null;
  //LocalDate today = LocalDate.now();
  when = new Event.Date(date);
  when = new Event.Datespan(date, date.plusDays(1));
  Instant sixPmUtc = date.atTime(hour-1,minutes).toInstant(ZoneOffset.UTC);
  when = new Event.Time(sixPmUtc);
  when = new Event.Timespan(sixPmUtc, sixPmUtc.plus(1, ChronoUnit.HOURS));
  
  Event event = new Event(o.getCalendId(),when);
   
  //
  
  event.setWhen(when);
  event.setTitle(candidacyRepo.getOfferTitle(candidacyId)+" Interview");
  event.setLocation(candidacyRepo.getOfferLocation(candidacyId));
  event.setDescription(candidacyRepo.getOfferDescription(candidacyId));
  event.setBusy(true);
  String mail =candidacyRepo.getCandidateEmail(candidacyId);
  String name= candidacyRepo.getCandidateName(candidacyId);
  event.setParticipants(Arrays.asList(new Participant(mail).name(name)));
  Event.Conferencing conferencing = new Event.Conferencing();
  conferencing.setProvider("Zoom Meeting");

  Event.Conferencing.Details details = new Event.Conferencing.Details();
  details.setMeetingCode("213");
  details.setPassword("xyz");
  details.setUrl("https://zoom.us/j/5033058101?pwd=QmYwdGZmMGo1TmFLdm9WQk9tREhsQT09");
  details.setPhone(Collections.singletonList("+11234567890"));
  conferencing.setDetails(details);

  event.setConferencing(conferencing);
  account.events().create(event, true);
  
  
 }
 }

  
 