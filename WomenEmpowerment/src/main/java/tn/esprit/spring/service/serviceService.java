package tn.esprit.spring.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.sun.xml.bind.v2.runtime.reflect.Accessor.GetterSetterReflection;

import net.bytebuddy.utility.privilege.GetSystemPropertyAction;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.enumerations.Role;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.repository.serviceRepo;


@Service
public class serviceService implements IService {
	@Autowired
	serviceRepo servrepo;
	@Autowired
	UserRepository userrepo;

	@Override
	public tn.esprit.spring.entities.Service addService(tn.esprit.spring.entities.Service s,Long userId ){
		User u = userrepo.findById(userId).orElse(null);
		
		 
	
			
			s.setJob(u.getJob());
		return servrepo.save(s);
		
	}
	@Override
	public void updateService(tn.esprit.spring.entities.Service s,Long serviceId ){
		tn.esprit.spring.entities.Service serv = servrepo.findById(serviceId).orElse(null);
		
		serv.setJob(s.getJob());
		serv.setStartDate(s.getStartDate());
		serv.setEndDate(s.getEndDate());
		servrepo.flush();
		
		
	}
	@Override
	public List<tn.esprit.spring.entities.Service> affichService( ){
		return servrepo.findAll();
	}
	@Override
	public void deletService(Long serviceId ){
		
		servrepo.deleteById(serviceId);
	}
	@Override
	public List<tn.esprit.spring.entities.Service> recherche(String keyword){
	if(keyword != null){
		return	servrepo.recherche(keyword);
	}else 
		return servrepo.findAll();
	}/*
	@Override
	public List<tn.esprit.spring.entities.Service> getAllBetweenDates(Date start, Date end) {
		
		return servrepo.getAllBetweenDates(start, end) ;
	}*/
	}

//aa
	
