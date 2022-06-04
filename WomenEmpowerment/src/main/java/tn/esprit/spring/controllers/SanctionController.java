package tn.esprit.spring.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import tn.esprit.spring.enumerations.Penality;
import tn.esprit.spring.service.courses.SanctionLearnerImpl;

@RestController
@Api(tags = "Sanction")
@RequestMapping("api/SanctionLearner")
public class SanctionController {
	@Autowired
	SanctionLearnerImpl sanctionLearnerImpl;
	
	@PostMapping("addSanction/{idUser}/{idCouse}/{pen}")
	public void SanctionLearner(@PathVariable("idUser")Long userId,@PathVariable("idCouse")Long courseId,@PathVariable("pen") Penality p) 
	{
		sanctionLearnerImpl.Sanction(courseId, userId, p);
	}
	
	
}
