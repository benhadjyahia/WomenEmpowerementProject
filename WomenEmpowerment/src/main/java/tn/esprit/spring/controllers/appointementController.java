package tn.esprit.spring.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import springfox.documentation.annotations.ApiIgnore;
import tn.esprit.spring.entities.Appointment;
import tn.esprit.spring.entities.Complaint;
import tn.esprit.spring.security.UserPrincipal;
import tn.esprit.spring.service.IAppointment;
import tn.esprit.spring.service.appointmentService;

@RestController
@RequestMapping("/appointment")
public class appointementController {
@Autowired
IAppointment appserv;
//
@PostMapping("/addrdv/{serviceId}/{userId}/{exId}")
@ResponseBody
public Appointment addRdv(@RequestBody Appointment apt,@PathVariable Long serviceId,@ApiIgnore @AuthenticationPrincipal UserPrincipal u,@PathVariable  Long exId) {
	  Long iduser = u.getId();
	return appserv.addRdv(apt, serviceId, iduser, exId);
}
@PutMapping("/modifier/{appointmentId}")
public void updateRdv(@RequestBody Appointment apt ,@PathVariable Long appointmentId){
	appserv.updateRdv(apt, appointmentId);
}
@GetMapping("/aff")
public List<Appointment> affichRdv(){
	return appserv.affichRdv();
}
@DeleteMapping("deleteRdv/{appointmentId}")
public void deleteAppoitment(@PathVariable Long appointmentId){
	appserv.deleteAppoitment(appointmentId);
}
@PutMapping ("/nbre")
public void NombresCaseSolved() {
	 appserv.NombresCaseSolved();
}
@GetMapping("calendrier/{date}/{service_id}")
public Boolean isDisponible(@PathVariable Date date,@PathVariable Long service_id)
{ return appserv.isDisponible(date, service_id);
}


}
