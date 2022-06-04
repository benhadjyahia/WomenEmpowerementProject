package tn.esprit.spring.service.courses;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nylas.Calendar;
import com.nylas.Calendars;
import com.nylas.Event;
import com.nylas.EventQuery;
import com.nylas.Events;
import com.nylas.NylasAccount;
import com.nylas.NylasClient;
import com.nylas.Participant;
import com.nylas.RemoteCollection;
import com.nylas.RequestFailedException;

import tn.esprit.spring.entities.Certificate;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.CourseCalEvent;
import tn.esprit.spring.repository.CertificateRepository;
import tn.esprit.spring.repository.CourseCalEventRepository;
import tn.esprit.spring.repository.CourseRepository;
@Service
public class CourseCalendarServiceImpl {
	@Autowired
	CourseRepository courseRepository;
	@Autowired
	CertificateRepository certificateRepository;
	@Autowired
	CourseCalEventRepository courseCalEventRepository;
	public String createCal(long courseId) throws IOException, RequestFailedException {
		 Course c =courseRepository.findById(courseId).get();
		 NylasClient client = new NylasClient();
		  NylasAccount account = client.account("e2vn6wCeGBU9v6TfnxsfWEvsTDPw0I");
		  Calendars calendars = account.calendars();
		  Calendar newCal1 = new Calendar();
		  newCal1.setName(c.getCourseName());
		  newCal1.setDescription(c.getDomain().toString());
		  newCal1.setLocation("Remote");
		  newCal1.setTimezone("America/Los_Angeles");
		  Calendar created = calendars.create(newCal1);
		 c.setCalendarId(created.getId());
		 courseRepository.save(c);
		  return created.getId();
		  
	}
	
	
	 public void addEvent(Long courseId,String eventName,int hour,int minutes,LocalDate date) throws IOException, RequestFailedException {
		 Course c =courseRepository.findById(courseId).get();
		 List<Certificate> certif = certificateRepository.findByCourse(courseId);
		  NylasClient client = new NylasClient();
		  NylasAccount account = client.account("e2vn6wCeGBU9v6TfnxsfWEvsTDPw0I");
		 // Calendars calendars = account.calendars();
		  Event.When when = null;
		 // LocalDate today = LocalDate.now();
		  when = new Event.Date(date);
		  when = new Event.Datespan(date, date.plusDays(1));
		  Instant sixPmUtc = date.atTime(hour-1,minutes).toInstant(ZoneOffset.UTC);
		  when = new Event.Time(sixPmUtc);
		  when = new Event.Timespan(sixPmUtc, sixPmUtc.plus(1, ChronoUnit.HOURS));
		  
		  Event event = new Event(c.getCalendarId(),when);
		  
		 event.setWhen(when);
		  event.setTitle(eventName);
		  event.setLocation("Remote");
		  event.setDescription("Visit the course on the website Live section");
		  event.setBusy(true);
		  
		  List<Participant> participant = new ArrayList<Participant>();
		  for (Certificate certificate : certif) {
			 Participant p = new Participant(certificate.getUser().getEmail());
			 participant.add(p);
		}
		 
		  for(int i =0 ; i<participant.size();i++) {
			  event.setParticipants(participant);
		  }
		  
		 
		  CourseCalEvent courseEvent = new CourseCalEvent();
		  courseEvent.setEventName(eventName);
		  courseEvent.setCourse(c);
		  courseEvent.setEventOnCalId( account.events().create(event, true).getId());
		  courseCalEventRepository.save(courseEvent);
		 
		  
		 }
	 public Event getEvent(Long eventId) throws IOException, RequestFailedException {
		CourseCalEvent courseEvent =  courseCalEventRepository.findById(eventId).get();
		 NylasClient nylas = new NylasClient();
	        NylasAccount account = nylas.account("e2vn6wCeGBU9v6TfnxsfWEvsTDPw0I");
	        Events events = account.events();
	        return  events.get(courseEvent.getEventOnCalId());
	 }
	 public void deleteEvent(long eventId)throws IOException, RequestFailedException {
		 CourseCalEvent courseEvent =  courseCalEventRepository.findById(eventId).get();
		 NylasClient nylas = new NylasClient();
	     NylasAccount account = nylas.account("e2vn6wCeGBU9v6TfnxsfWEvsTDPw0I");
		 account.events().delete(courseEvent.getEventOnCalId(), true);
		 courseCalEventRepository.delete(courseEvent);
	 }
	 public void updateEventTime(long eventId,int hour,int minutes,LocalDate date) throws IOException, RequestFailedException {
		 CourseCalEvent courseEvent =  courseCalEventRepository.findById(eventId).get();
		 NylasClient nylas = new NylasClient();
	     NylasAccount account = nylas.account("e2vn6wCeGBU9v6TfnxsfWEvsTDPw0I");
	     Event event = account.events().get(courseEvent.getEventOnCalId());
	     Event.When when = null;
		 // LocalDate today = LocalDate.now();
		  when = new Event.Date(date);
		  when = new Event.Datespan(date, date.plusDays(1));
		  Instant sixPmUtc = date.atTime(hour-1,minutes).toInstant(ZoneOffset.UTC);
		  when = new Event.Time(sixPmUtc);
		  when = new Event.Timespan(sixPmUtc, sixPmUtc.plus(1, ChronoUnit.HOURS));
		  event.setWhen(when);
		  event.setLocation("Remote");
		  event.setDescription("Visit the course on the website Live section");
		  event.setBusy(true);
		  account.events().update(event, true);
	     
	 }
	 public RemoteCollection<Event> getEvents(String calendarId) throws IOException, RequestFailedException{
		 NylasClient client = new NylasClient();
		    NylasAccount account = client.account("e2vn6wCeGBU9v6TfnxsfWEvsTDPw0I");
		    return account.events().list(new EventQuery().calendarId(calendarId));
		    	   
	 }
	 
	 
}
