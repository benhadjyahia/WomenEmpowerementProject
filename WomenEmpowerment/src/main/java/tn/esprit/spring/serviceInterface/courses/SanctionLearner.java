package tn.esprit.spring.serviceInterface.courses;

import org.springframework.stereotype.Service;

import tn.esprit.spring.enumerations.Penality;

@Service
public interface SanctionLearner {
public void Sanction(Long courseId,Long userId,Penality p);
public void PunishmendDecision();
public int userSanctionsByCourse(long userId,long courseId);
}
