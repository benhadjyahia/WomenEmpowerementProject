package tn.esprit.spring.controllers;

import tn.esprit.spring.entities.Media;
import tn.esprit.spring.entities.TokenDto;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.exceptions.EmailExist;
import tn.esprit.spring.exceptions.EmailNotExist;
import tn.esprit.spring.exceptions.ResetPasswordException;
import tn.esprit.spring.exceptions.ResetPasswordTokenException;
import tn.esprit.spring.exceptions.UsernameExist;
import tn.esprit.spring.exceptions.UsernameNotExist;
import tn.esprit.spring.repository.MediaRepo;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.serviceInterface.user.AuthenticationService;
import tn.esprit.spring.serviceInterface.user.JwtRefreshTokenService;
import tn.esprit.spring.serviceInterface.user.UserService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;


@RestController
@RequestMapping("api/authentication")//pre-path
public class AuthenticationController
{

	public static String uploadDirectory22 = "C:\\Users\\lenovo\\Desktop\\spring git\\font\\WomenEmpowermentAngular\\src\\assets\\img\\";
	public static String uploadDirectory = System.getProperty("user.dir")+"/uploads/";
	//Static File path
	public static String uploadDirectory2 = "C:\\Users\\SeifD\\Desktop\\WomenEmpowermentAngular\\src\\assets\\img\\";
	
	ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	@Value("${google.clientId}")
	String googleClientId;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;
    
    @Autowired
    MediaRepo mediaRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtRefreshTokenService jwtRefreshTokenService;

    @PostMapping(value="sign-up", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})//api/authentication/sign-up
    public ResponseEntity<User> signUp(@RequestPart("user") String user, @RequestPart("file") MultipartFile file) throws UsernameNotExist, UsernameExist, EmailExist, MessagingException, IOException, io.jsonwebtoken.io.IOException, TemplateException
    {
    	//upload file

 
    	File convertFile = new File(uploadDirectory+file.getOriginalFilename());
    	convertFile.createNewFile();
    	FileOutputStream fout = new FileOutputStream(convertFile);
    	fout.write(file.getBytes());
    	fout.close();
    	Media profilPicture = new Media();
    	profilPicture.setImagenUrl(uploadDirectory2+file.getOriginalFilename());
    	profilPicture = mediaRepository.save(profilPicture);
    	User userData = objectMapper.readValue(user, User.class);
    	userData.setProfilPicture(profilPicture);
    	userData.setProfilPic(file.getOriginalFilename());
    	
    	
    	//////
    	
    	userService.saveUser(userData);
    	return new ResponseEntity<>(userData, HttpStatus.CREATED);
    }
    
   /* 
    @PostMapping("sign-up")
    public ResponseEntity<User> signUp(@RequestBody User user) throws UsernameNotExist, UsernameExist, EmailExist, MessagingException, io.jsonwebtoken.io.IOException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, IOException{
    	userService.saveUser(user);
    	return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    */

    @PostMapping("sign-in")//api/authentication/sign-in
    public ResponseEntity<?> signIn(@RequestBody User user) throws tn.esprit.spring.exceptions.AccountLockedException
    {
    	User u = userRepository.findByUsername(user.getUsername()).orElse(null);
    	if (u != null) {
    		if (!u.isLocked()) {
        		if (u.getLoginAttempts() != 4 && !passwordEncoder.matches(user.getPassword(), u.getPassword())){
        			u.setLoginAttempts(u.getLoginAttempts() + 1);
        			userRepository.save(u);
        			if (u.getLoginAttempts()==4) {
        				u.setLocked(true);
        				userRepository.save(u);
        				throw new tn.esprit.spring.exceptions.AccountLockedException("Your account has been locked, please contact the administration !");
        			}
        			else {
            			return new ResponseEntity<>(authenticationService.signInAndReturnJWT(user), HttpStatus.OK);
        			}

        		}
        		else if (u.getLoginAttempts() != 4 && passwordEncoder.matches(user.getPassword(), u.getPassword())){
        			u.setLoginAttempts(0);
        			userRepository.save(u);
        			return new ResponseEntity<>(authenticationService.signInAndReturnJWT(user), HttpStatus.OK);
        		}
        		else {
        			throw new tn.esprit.spring.exceptions.AccountLockedException("Your account has been locked, please contact the administration !");
        		}
    		}
    		else {
    			throw new tn.esprit.spring.exceptions.AccountLockedException("Your account has been locked, please contact the administration !");
    		}

    	}
    	else {
    		return new ResponseEntity<>(authenticationService.signInAndReturnJWT(user), HttpStatus.OK);
    	}
        
    }

    @PostMapping("refresh-token")//api/authentication/refresh-token?token=
    public ResponseEntity<?> refreshToken(@RequestParam String token)
    {
        return ResponseEntity.ok(jwtRefreshTokenService.generateAccessTokenFromRefreshToken(token));
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<?> generatePasswordResetToken(@RequestParam String email) throws EmailNotExist, io.jsonwebtoken.io.IOException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, IOException {
    	return new ResponseEntity<>(authenticationService.generatePasswordResetToken(email), HttpStatus.OK);
    }
    
    @PostMapping("/reset-password/new")
    public ResponseEntity<?> updatePassword(@RequestParam String token, @RequestBody String newPassword) throws ResetPasswordException, ResetPasswordTokenException{
    	authenticationService.updatePassword(token, newPassword);
    	return new ResponseEntity<>("Your password has been successfully updated !", HttpStatus.OK);
    }
    
    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody TokenDto tokenDto) throws IOException{
    	final NetHttpTransport transport = new NetHttpTransport();
    	final GsonFactory jacksonFactory = GsonFactory.getDefaultInstance();
    	GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
    			.setAudience(Collections.singletonList(googleClientId));
    	final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), tokenDto.getValue());
    	final GoogleIdToken.Payload payload = googleIdToken.getPayload();
    	return new ResponseEntity(payload, HttpStatus.OK);
    }
    
    @PostMapping("/facebook")
    public ResponseEntity<?> loginWithFacebook(@RequestBody TokenDto tokenDto) throws IOException{
    	Facebook facebook = new FacebookTemplate(tokenDto.getValue());
    	final String [] fields = {"email", "picture"};
    	org.springframework.social.facebook.api.User user = facebook.fetchObject("me", org.springframework.social.facebook.api.User.class, fields); 
    	return new ResponseEntity(user, HttpStatus.OK);
    }

}
