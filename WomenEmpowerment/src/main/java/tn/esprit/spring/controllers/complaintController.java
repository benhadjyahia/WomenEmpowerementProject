package tn.esprit.spring.controllers;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import tn.esprit.spring.entities.Complaint;
import tn.esprit.spring.security.UserPrincipal;
import tn.esprit.spring.service.IComplaint;
import tn.esprit.spring.service.complaintService;

@RestController
@RequestMapping("/complaint")
public class complaintController {
@Autowired
IComplaint compservice;

  @PostMapping("/addReclamation/{userid}")
@ResponseBody
public Complaint addComplaint(@RequestBody Complaint complaint,@ApiIgnore @AuthenticationPrincipal UserPrincipal u){
	  Long iduser = u.getId();
	  return compservice.addComplaint(complaint, iduser);
		
}

@PutMapping("update/{complaintId}")
@ResponseBody
public void updatereclamation(@RequestBody Complaint complaint,@PathVariable Long complaintId){
	compservice.updatereclamation(complaint, complaintId);

}
@DeleteMapping("delet/{complaintId}")
@ResponseBody
public void deletreclamation(@PathVariable Long complaintId ){
	
	compservice.deletreclamation(complaintId);
	
}
@GetMapping("/affichReclamation")

public List<Complaint> showclamation(){
	return compservice.showclamation();
}
@GetMapping("/NBr-REc-Tr")
public int nb_reclmation_traite(){
	return compservice.nb_recl_trait();
}

@GetMapping("/TypeOfComplaint/{id}")
public String nb_reclmation_traite(@PathVariable Long id){
	return compservice.GetType(id);
}
@GetMapping("/PourcentageOfComplaint")
public float statCoplaint(){
return compservice.statCoplaint();

}}

