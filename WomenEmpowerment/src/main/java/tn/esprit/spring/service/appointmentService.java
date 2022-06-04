package tn.esprit.spring.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tn.esprit.spring.entities.Appointment;
import tn.esprit.spring.entities.SmsRequest;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.enumerations.Job;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.repository.appointmentRepo;
import tn.esprit.spring.repository.serviceRepo;
import tn.esprit.spring.service.event.TwilioSmsSender;
import tn.esprit.spring.serviceInterface.SmsSender;


@Service
public class appointmentService implements IAppointment {
@Autowired
appointmentRepo apprepo;
@Autowired
serviceRepo serrepo;
@Autowired
UserRepository userrepo;
private final SmsSender smsSender;
public appointmentService(@Qualifier("twilio") TwilioSmsSender smsSender) {
	this.smsSender = smsSender;
}

@Override
public Appointment addRdv(Appointment apt, Long serviceId, Long userId, Long expert_id) {
//{
	//SmsRequest smsrequest = new SmsRequest(null, null);
tn.esprit.spring.entities.Service s = serrepo.findById(serviceId).orElse(null);
	User user = userrepo.findById(userId).orElse(null);
	User expert = userrepo.findById(expert_id).orElse(null);
	

	apt.setService(s);
	  apt.setUser(user);
	 apt.setExpert(expert);
      //twilio
	 // sendSms(smsrequest, user.getPhoneNumber(), apt.getAppointmentDate()+ " : YOUR APPOINTEMENT IS VALID FOR THIS DATE");	
	//if (apt.getAppointmentDate().before(s.getEndDate()) && apt.getAppointmentDate().after(s.getStartDate())){
		
	return	apprepo.save(apt);
		
	//}
}



@Override
public void updateRdv(Appointment apt,  Long appointmentId ) {
	
	Appointment app = apprepo.findById(appointmentId).orElse(null); 
	
	
	

		app.setAppointmentDate(apt.getAppointmentDate());
		app.setService(apt.getService());
		app.setUser(apt.getUser());
		apprepo.flush();
		
	
}
@Override
public List<Appointment> affichRdv() {
	return apprepo.findAll();
}
@Override
public void deleteAppoitment(Long appointmentId) {
	apprepo.deleteById(appointmentId);
	
}
@Override
public void NombresCaseSolved() {
	for (User u : userrepo.findAll()) {
		u.setNbCasesSolved( (long) 0);
		
	}
	for (Appointment a : apprepo.findAll()) {
		if (a.getCaseSolved()==true) {
			
		a.getExpert().setNbCasesSolved(a.getExpert().getNbCasesSolved()+1);
		userrepo.save(a.getExpert());
		}
		
	}
} 
@Override
public Boolean isDisponible(Date date ,Long service_id) {
tn.esprit.spring.entities.Service s = serrepo.findById(service_id).orElse(null);
if (date.before(s.getEndDate()) && date.after(s.getStartDate())) {
	return true;
}
else  
	return false;

}

@Override
public void sendSms(SmsRequest smsRequest, String numberPhone, String msg) {
	smsRequest.setPhoneNumber(numberPhone);
	smsRequest.setMessage(msg);
	smsSender.sendSms(smsRequest);

	
} 

}
