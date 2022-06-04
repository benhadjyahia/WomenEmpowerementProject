package tn.esprit.spring.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.nylas.RequestFailedException;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import tn.esprit.spring.entities.Candidacy;
import tn.esprit.spring.entities.CvInfo;
import tn.esprit.spring.entities.Offer;
import tn.esprit.spring.repository.CandidacyRepository;
import tn.esprit.spring.repository.IOfferRepository;
import tn.esprit.spring.security.UserPrincipal;
import tn.esprit.spring.service.offer.CalendarServiceImpl;
import tn.esprit.spring.serviceInterface.offer.ICandidacyService;
import tn.esprit.spring.serviceInterface.offer.ICvStorageService;
import tn.esprit.spring.serviceInterface.offer.IOfferService;
@RestController
@CrossOrigin("http://localhost:8087")
@RequestMapping("/offer")
public class OfferRestContrller {
	
	@Autowired
	IOfferService OfferService;
	@Autowired
	IOfferRepository OfferRepo ;
	@Autowired
	CandidacyRepository candidacyRepo;
	@Autowired
	ICandidacyService CandidacyService;
	@Autowired
	CalendarServiceImpl userAccount;
	 @Autowired
	  ICvStorageService storageService;
	
	@ApiOperation(value = "Récupérer la liste des Offres")
	@GetMapping("/retrieve-all-Offers")
	@ResponseBody
	public List<Offer> getOffers() {
		return OfferService.getAllOffers();
	}
	
	
	
	@GetMapping("/retrieve-Offer-By-Id/{id}")
	@ResponseBody
	public Offer getOfferById(@PathVariable(value="id") Long offerId) {
		return OfferService.getOfferById(offerId);
	}

	@PostMapping("/add-Offer")
	@ResponseBody
	public  void saveOffer( @RequestBody Offer offer /*, long offerId*/) throws IOException, RequestFailedException {
		  OfferService.saveOffer(offer);
		  //userAccount.createCal(offerId);
	}
	
	@DeleteMapping("/delete-offer/{id}")
	@ResponseBody
	public void deleteOffer (@PathVariable(value="id") Long offerId ) {
		OfferService.deleteOfferById(offerId);
	}
	
	@PutMapping ("/update-Offer")
	public void updateOffer ( @RequestBody Offer offer) {
		 		OfferService.updateOffer(offer);
	}
	
	
	/*@GetMapping("/list/{keyword}")
	@ResponseBody
    public void viewHomePage( @PathVariable(value="keyword") String keyword) {
        List<Offer> listOffers = OfferService.listAll(keyword);
        
    }*/
	
	@GetMapping("/FilterOffer")
    public List <Offer> FilterSearch( @Param("keyword") String keyword) {
        return OfferService.listAll(keyword);
      
    }
	
	@GetMapping("/get-ALL-Candidacies")
    public List <Candidacy> getAllCandidacies( ) {
        return candidacyRepo.findAll();
        		//CandidacyService.getAllCandidacies();
      
    }
	
	@PostMapping("/Apply-Offer/{userId}/{offerId}")
	@ResponseBody
	void ApplyOffer(@RequestBody Candidacy candidacy,@PathVariable("userId") Long userId,@PathVariable("offerId") Long offerId) {
		CandidacyService.postulerOffre( offerId, userId);
	}
	/*
	@PostMapping("/upload/{candidacyId}")
	  public ResponseEntity<ResponseMessage> uploadCv(@RequestPart("cv") MultipartFile cv,@PathVariable("candidacyId")Long candidacyId) {
	    String message = "";
	    try {
	      storageService.store(cv,candidacyId);
	      message = "Uploaded the file successfully: " + cv.getOriginalFilename();
	      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	    } catch (Exception e) {
	      message = "Could not upload the Cv: " + cv.getOriginalFilename() + "!";
	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	    }
	  }*/
	
	@PutMapping ("/Set-Favorite/{id}")
	@ResponseBody
	public void SetFavorite ( @PathVariable(value="id") Long candidacy_id) {
		CandidacyService.SetFavorite(candidacy_id);
	}
	
	
	@GetMapping("/listMyCandidacy")
    public List <Candidacy>  listMyCandidacy( @ApiIgnore @AuthenticationPrincipal UserPrincipal u) {
		Long userId = u.getId();
		String keyword = userId.toString();
        return (List<Candidacy>) candidacyRepo.mycand(keyword);
        		
        		// CandidacyService.getMyCandidacy(keyword);*/
	}
	
	@GetMapping("/listMyFavoriteCandidacy")
    public List <Candidacy>  listMyFavoriteCandidacy(  @ApiIgnore @AuthenticationPrincipal UserPrincipal u) {
		Long userId = u.getId();
		String keyword = userId.toString();
        return (List<Candidacy>) candidacyRepo.searchFavorite(keyword);
        		//CandidacyService.getMyFavoriteCandidacy(keyword);
	}
	
	

	@PutMapping ("/Hold-Cnadidacy/{id}")
	@ResponseBody
	public void HoldCandidacy ( @PathVariable(value="id") Long candidacy_id) throws MessagingException {
		CandidacyService.HoldCandidacy(candidacy_id);
	}
	
	@DeleteMapping ("/Restrain-Cnadidacy/{id}")
	@ResponseBody
	public void RestrainCandidacy ( @PathVariable(value="id") Long candidacy_id) throws MessagingException {
		CandidacyService.RestrainCandidacy(candidacy_id);
	}
	@PostMapping("/Accept-and-add-interview/{candidacyId}/{hour}/{minutes}/{date}")
	@ResponseBody
	public  void interview(Long candidacyId, @PathVariable("hour")int hour,@PathVariable("minutes")int minutes,@RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws IOException, RequestFailedException {
		userAccount.postEventExample( candidacyId,hour,minutes,date);
	}
	/*@PostMapping("/add-even")
	@ResponseBody
	public  String createcal(long offerId) throws IOException, RequestFailedException {
		return userAccount.createCal(offerId);
	}*/
	/*
	@PostMapping("/upload")
	  public ResponseEntity<ResponseMessage> uploadFile(@RequestPart("file") MultipartFile file) {
	    String message = "";
	    try {
	      storageService.save(file); 	
	      message = "Uploaded the file successfully: " + file.getOriginalFilename();
	      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	    } catch (Exception e) {
	      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
	      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
	    }
	  }
	  @GetMapping("/files")
	  public ResponseEntity<List<CvInfo>> getListFiles() {
	    List<CvInfo> fileInfos = storageService.loadAll().map(path -> {
	      String filename = path.getFileName().toString();
	      String url = MvcUriComponentsBuilder
	          .fromMethodName(OfferRestContrller.class, "getFile", path.getFileName().toString()).build().toString();
	      return new CvInfo(filename, url);
	    }).collect(Collectors.toList());
	    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	  }
	  @GetMapping("/files/{filename:.+}")
	  @ResponseBody
	  public ResponseEntity<Resource> getFile(@PathVariable String filename) {
	    Resource file = storageService.load(filename);
	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	  }*/
	}


