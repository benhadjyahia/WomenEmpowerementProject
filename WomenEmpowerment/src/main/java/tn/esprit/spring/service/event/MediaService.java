package tn.esprit.spring.service.event;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Media;
import tn.esprit.spring.repository.MediaRepo;

@Service
@Transactional
public class MediaService {
	 @Autowired
	    MediaRepo mediaRepo;

	    

	    public Optional<Media> getOne(Long id){
	        return mediaRepo.findById(id);
	    }

	    public void save(Media media){
	    	mediaRepo.save(media);
	    }

	    public void delete(Long id){
	    	mediaRepo.deleteById(id);
	    }

	    public boolean exists(Long id){
	        return mediaRepo.existsById(id);
	    }
}
