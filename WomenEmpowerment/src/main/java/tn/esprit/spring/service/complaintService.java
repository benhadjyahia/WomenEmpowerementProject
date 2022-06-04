package tn.esprit.spring.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.hibernate.loader.custom.Return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.internal.function.numeric.Max;

import javassist.expr.NewArray;
import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.entities.Complaint;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.repository.complaintRepo;


@Slf4j
@Service
public class complaintService implements IComplaint {
@Autowired
complaintRepo comprepo ;
@Autowired
UserRepository userrepository;

@Override
public Complaint addComplaint(Complaint complaint,Long userId) {

	User u = userrepository.findById(userId).orElse(null);

	
		
		u.getComplaints().add(complaint);
		complaint.setUser(u);
		return comprepo.save(complaint);
		
	} 

@Override
public void updatereclamation(Complaint complaint,Long complaintId){
	Complaint complaint1= comprepo.findById(complaintId).get();
	complaint1.setComplaintTitle(complaint.getComplaintTitle());
	complaint1.setContent(complaint.getContent());
	comprepo.flush();
	
}
@Override
public void deletreclamation(Long complaintId ){
// User usr = userrepository.findById(idUser).get();
	Complaint comp= comprepo.findById(complaintId).get();
	
//	usr.getComplaints().remove(comp);
	comprepo.deleteById(complaintId);
	
	
	
	
}
@Override
public List<Complaint> showclamation(){
	return comprepo.findAll();
}

@Override
public int nb_recl_trait() {
	int x =0;
			for (Complaint complaint : comprepo.findAll()) {
				if (complaint.getIsTreated() == true)
					x++;	
			}
	return x;
}


@Override
public String GetType(Long id_c) {
	Complaint c = comprepo.findById(id_c).orElse(null);
	int d = 0;
	int p = 0;
	int a = 0;
	List<String> Doctor_key = new ArrayList<>();
	Doctor_key.add("medicin");Doctor_key.add("pharmacie");Doctor_key.add("hopital");
	
	List<String> Attorney_key = new ArrayList<>();
	Attorney_key.add("Attorney");Attorney_key.add("loyer");Attorney_key.add("avocat");
	
	List<String> Psychologist_key = new ArrayList<>();
	Psychologist_key.add("psy");Psychologist_key.add("mahboul");Psychologist_key.add("mdaba3");
	
	for (String string : Doctor_key) {
		if (c.getContent().contains(string))
			d++;
		
	}
	for (String string : Psychologist_key) {
		if(c.getContent().contains(string))
			p++;
			
	}
	for (String string : Attorney_key) {
		if(c.getContent().contains(string))
			a++;
			
	}
	
	if (d>p && d>a) return "Doctor";
	else if (a>d && a>p) return "Attorney";
	else return "psy";
	
}

@Override
public float statCoplaint() {
	
	
	int x = 0 ;
	int y = 0 ;
	
	float z ;
	
	
	for (Complaint complaint : comprepo.findAll()) {
		if (complaint.getIsTreated())
			x++;	
	}
	for (Complaint complaint : comprepo.findAll()) {
		
			y++;
	}

	
System.err.println("x = " + x + "\n y = " + y);

z = ((float)x)/((float)y)*100;

return z;


}}
