package tn.esprit.spring.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nylas.RequestFailedException;

import springfox.documentation.annotations.ApiIgnore;
import tn.esprit.spring.entities.Service;
import tn.esprit.spring.security.UserPrincipal;
import tn.esprit.spring.service.CalendarServiceImpla;
import tn.esprit.spring.service.IService;
import tn.esprit.spring.service.serviceService;

@RestController
@RequestMapping("/service")
public class serviceController {
	
	@Autowired
	CalendarServiceImpla cs;
	
	
	@Autowired
	IService servserv;
	@PostMapping("/addService/{userId}")
	@ResponseBody
	public Service addService(@RequestBody tn.esprit.spring.entities.Service s,@ApiIgnore @AuthenticationPrincipal UserPrincipal u){
		 Long iduser = u.getId();
		return servserv.addService(s, iduser);
	}
@PutMapping("/updateService/{serviceId}")
@ResponseBody
public void updateService(@RequestBody tn.esprit.spring.entities.Service s,@PathVariable Long serviceId ){
	servserv.updateService(s, serviceId);
}
@GetMapping("/affichge")

public List<Service> affichService( ){
	return servserv.affichService();
	
}
@DeleteMapping("delete/{serviceId}")
@ResponseBody
public void deletService(@PathVariable Long serviceId ){
	servserv.deletService(serviceId);
}
@GetMapping("recherche")
public List<tn.esprit.spring.entities.Service> recherche(String keyword){
	return servserv.recherche(keyword);
}

@GetMapping("/Createcalande/{idUser}")	
String createtexpcalander(@PathVariable Long idUser) throws IOException, RequestFailedException{
	return cs.createCal(idUser);
}

@GetMapping("/create-event-calander/{idUser}/{idService}")	
void createevntcalander(@PathVariable Long idUser,@PathVariable Long idService) throws IOException, RequestFailedException{
	 cs.postEventExample(idService, idUser);
}
}
/*
@GetMapping("/filtredate")	
List<Service> getAllBetweenDates(@RequestBody LocalDate start, @RequestBody LocalDate end){
	return servserv.getAllBetweenDates(null, null);
}*/


