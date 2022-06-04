package tn.esprit.spring.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import tn.esprit.spring.entities.Complaint;

public interface IComplaint {
	public Complaint addComplaint(Complaint complaint,Long userId);
	public void updatereclamation(Complaint complaint,Long complaintId);
	public void deletreclamation(Long complaintId);
	public List<Complaint> showclamation();
	public int nb_recl_trait();
	public String GetType(Long id_c);
	public float statCoplaint();
}
