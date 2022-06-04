package tn.esprit.spring.service;

import java.util.Date;
import java.util.List;

import tn.esprit.spring.entities.Appointment;
import tn.esprit.spring.entities.SmsRequest;

public interface IAppointment  {
	public Appointment addRdv(Appointment apt , Long serviceId, Long userId, Long expert_id);
	public void updateRdv(Appointment apt ,Long appointmentId );
	public List<Appointment> affichRdv();
	public void deleteAppoitment(Long appointmentId);
	public void NombresCaseSolved();
	Boolean isDisponible(Date date, Long service_id);
	public void sendSms(SmsRequest smsRequest, String numberPhone, String msg);
}
