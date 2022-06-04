package tn.esprit.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tn.esprit.spring.entities.Appointment;
@Repository
public interface appointmentRepo extends JpaRepository<Appointment, Long> {
	@Query(nativeQuery = true,value = "SELECT a.case_solved from appointment a where a.case_solved=1 ")
	public int NombresCaseSolved();
	@Query(nativeQuery = true,value = "SELECT service_service_id,appointment_id,appointment.user_user_id FROM users_created_services INNER join service ON users_created_services.created_services_service_id=service.service_id INNER JOIN appointment ON appointment.service_service_id=service.service_id;")
	public int setCaseSolved(); 
}
