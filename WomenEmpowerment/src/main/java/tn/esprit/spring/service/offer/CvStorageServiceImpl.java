package tn.esprit.spring.service.offer;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.springframework.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import tn.esprit.spring.entities.Candidacy;
import tn.esprit.spring.entities.CvInfo;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.repository.CandidacyRepository;
import tn.esprit.spring.repository.CvDBRepository;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.serviceInterface.offer.ICvStorageService;
@Service
public class CvStorageServiceImpl implements ICvStorageService {
	@Autowired
	  private CvDBRepository fileDBRepository;
	@Autowired 
	 CandidacyRepository candidacyRepository;
	@Autowired 
	 UserRepository UserRepo;
	@Override
	  public CvInfo store(MultipartFile file,Long userId) throws IOException {
	    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	    CvInfo CvInfo = new CvInfo(fileName, file.getContentType(), file.getBytes());
	   /* Candidacy candidacy = candidacyRepository.findById(candidacyId).get();
	    candidacy.getCv().add(CvInfo);
	    candidacyRepository.flush();*/
	    User user= UserRepo.findById(userId).get();
	    CvInfo.setCandidate(user);
	        
	    return fileDBRepository.save(CvInfo);
	  }
	@Override
	  public CvInfo getFile(String id) {
	    return fileDBRepository.findById(id).get();
	  }
	@Override
	  public Stream<CvInfo> getAllFiles() {
	    return fileDBRepository.findAll().stream();
	  }
	@Override
	  public CvInfo removeFile(String id) {
		   fileDBRepository.deleteById(id);
		   return fileDBRepository.findById(id).get();
	  }
}