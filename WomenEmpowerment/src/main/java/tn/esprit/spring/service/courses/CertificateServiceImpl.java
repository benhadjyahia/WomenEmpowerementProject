package tn.esprit.spring.service.courses;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import tn.esprit.spring.entities.Certificate;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repository.CertificateRepository;
import tn.esprit.spring.repository.CourseRepository;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.serviceInterface.courses.CertificateService;
@Service
public class CertificateServiceImpl implements CertificateService {
	@Autowired
	CertificateRepository certificateRepository;
	@Autowired
	CourseRepository courseRepository;
	@Autowired
	UserRepository userRepository;
	
	@Scheduled(cron = "0/10 * * * * *")
	public void createCertificateQr() throws IOException, InterruptedException {
		
		List<Certificate> c = certificateRepository.findAll();
		for (Certificate certificate : c) {
			if(certificate.isAquired()==true && certificate.getCertificateQR()==null) {
				String text=certificate.getUser().getUsername()+"'mail'"+certificate.getUser().getEmail();
				String texttrimmed = text.replaceAll("\\s","");
				
				HttpRequest request = HttpRequest.newBuilder()
						.uri(URI.create("https://codzz-qr-cods.p.rapidapi.com/getQrcode?type=text&value="+texttrimmed+""))
						.header("x-rapidapi-host", "codzz-qr-cods.p.rapidapi.com")
						.header("x-rapidapi-key", "b648c42070msh2f1e24111397e42p1155f4jsn864d7705eee5")
						.method("GET", HttpRequest.BodyPublishers.noBody())
						.build();
				HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
				System.err.println(response.body());
			 certificate.setCertificateQR(response.body().substring(8, 61));
				certificateRepository.saveAndFlush(certificate);
				
			}
		}

		}
public List<Certificate> userCertificate(Long courseId) {
	List<Certificate> certif = certificateRepository.findByCourse(courseId);
	return certif;
	
}
	@Override
	public byte[] certif(Long certificateid) throws IOException, InterruptedException{
		Certificate certif = certificateRepository.findById(certificateid).get();
	HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create("https://yakpdf.p.rapidapi.com/pdf"))
			.header("content-type", "application/json")
			.header("X-RapidAPI-Host", "yakpdf.p.rapidapi.com")
			.header("X-RapidAPI-Key", "b648c42070msh2f1e24111397e42p1155f4jsn864d7705eee5")
			.method("POST", HttpRequest.BodyPublishers.ofString("{\r\n    \"pdf\": {\r\n        \"format\": \"A4\",\r\n        \"printBackground\": true,\r\n        \"scale\": 1\r\n    },\r\n    \"source\": {\r\n        \"html\": \"<!DOCTYPE html><html lang=\\\"en\\\"><head><meta charset=\\\"UTF-8\\\"><meta name=\\\"viewport\\\" content=\\\"width=device-width, initial-scale=1.0\\\"></head><body><div style=\\\"width:800px; height:600px; padding:20px; text-align:center; border: 10px solid #DB7093\\\"><div style=\\\"width:750px; height:550px; padding:20px; text-align:center; border: 5px solid #FFC0CB\\\"><span style=\\\"font-size:50px; font-weight:bold\\\">Certificate of Completion</span><br><br><span style=\\\"font-size:25px\\\"><i>This is to certify that</i></span><br><br><span style=\\\"font-size:30px\\\"><b>"+certif.getUser().getName()+"</b></span><br/><br/><span style=\\\"font-size:25px\\\"><i>has completed the course</i></span> <br/><br/><span style=\\\"font-size:30px\\\"> "+certif.getCourse().getCourseName()+"</span> <br/><br/><br/><br/><br/><br/><span style=\\\"font-size:25px\\\"><i>For "+certif.getCourse().getNbHours()+"hours length</i></span><br><span style=\\\"font-size:25px;float:left\\\">Aquired on : "+certif.getObtainingDate()+"</span><div style=\\\"float:right\\\"><img src=\\\""+certif.getCertificateQR()+"\\\"></div></div></div></body></html>\"\r\n    },\r\n    \"wait\": {\r\n        \"for\": \"navigation\",\r\n        \"timeout\": 250,\r\n        \"waitUntil\": \"load\"\r\n    }\r\n}"))
			.build();
	 HttpResponse<byte[]> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofByteArray());
	 byte[] res = response.body();
	return res;
	}
	public List<Certificate> getCertificates(){
		return certificateRepository.findAll();
	}
	public int getAquiredCertificates() {
		int nb = 0;
		List<Certificate> certificates = certificateRepository.findAll();
		for (Certificate certificate : certificates) {
			if (certificate.isAquired() == true) {
				nb++;
			}
		}
		return nb;
	}
	
}