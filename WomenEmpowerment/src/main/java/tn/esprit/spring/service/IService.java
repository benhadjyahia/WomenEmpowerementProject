package tn.esprit.spring.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.repository.query.Param;

import tn.esprit.spring.entities.Service;

public interface IService {
	public Service addService(tn.esprit.spring.entities.Service s,Long userId );
	public void updateService(tn.esprit.spring.entities.Service s,Long serviceId );
	public List<tn.esprit.spring.entities.Service> affichService( );
	public void deletService(Long serviceId );
	public List<tn.esprit.spring.entities.Service> recherche(String keyword);
//	List<Service> getAllBetweenDates(Date start, Date end);
	
}
