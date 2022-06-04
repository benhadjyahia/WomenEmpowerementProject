package tn.esprit.spring.serviceInterface.courses;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CertificateService {
void createCertificateQr() throws IOException, InterruptedException;
byte[] certif(Long certificateid) throws IOException, InterruptedException;
}
