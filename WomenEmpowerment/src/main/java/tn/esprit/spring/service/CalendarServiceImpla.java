package tn.esprit.spring.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.RequestFailedException;

import tn.esprit.spring.entities.User;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.repository.serviceRepo;


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
public class CalendarServiceImpla {
	@Autowired
	UserRepository ur;
	@Autowired
	serviceRepo sr;
	
	public String createCal(Long idexpert) throws IOException, RequestFailedException {
		User u = ur.findById(idexpert).orElse(null);
		 NylasClient client = new NylasClient();
		  NylasAccount account = client.account("wOIy4UmmlWREVdr9S4pH5kAzRprvax");
		  Calendars calendars = account.calendars();
		  Calendar newCal1 = new Calendar();
		  newCal1.setName(u.getName());
		  newCal1.setDescription("Testing calendar creation");
		  newCal1.setLocation("far, far away");
		  newCal1.setTimezone("America/Los_Angeles");
		  Calendar created = calendars.create(newCal1);
		  
		  
		  u.setExpertCalander(created.getId());
		  ur.save(u);
		  return created.getId();
	}
	
	
	
	
	
 public void postEventExample(Long idservice,Long idExpert) throws IOException, RequestFailedException {
	 tn.esprit.spring.entities.Service s = sr.findById(idservice).orElse(null);
	 User u= ur.findById(idExpert).orElse(null);
  NylasClient client = new NylasClient();
  NylasAccount account = client.account("wOIy4UmmlWREVdr9S4pH5kAzRprvax");
  Calendars calendars = account.calendars();
  Event.When when = null;
  LocalDate today =  LocalDate.of(2022, s.getStartDate().getMonth(), s.getStartDate().getDay());
  LocalDate end =  LocalDate.of(2022, s.getEndDate().getMonth(), s.getEndDate().getDay());
  
  System.out.println("time is " +today);
  when = new Event.Date( );
  when = new Event.Datespan(today, end);
  Instant sixPmUtc = today.atTime(22,44).toInstant(ZoneOffset.UTC);
  when = new Event.Time(sixPmUtc);
  when = new Event.Timespan(sixPmUtc, sixPmUtc.plus(1, ChronoUnit.HOURS));
  
  Event event = new Event(u.getExpertCalander(),when);
  
 event.setWhen(when);
  event.setTitle(s.getJob().toString());
  event.setLocation("Remote");
  event.setDescription("Help meeting");
  event.setBusy(true);
  event.setParticipants(Arrays.asList(new Participant(u.getEmail())));
  account.events().create(event, true);
  
  
 }
 }

  
 