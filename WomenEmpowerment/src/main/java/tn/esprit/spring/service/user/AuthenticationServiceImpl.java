package tn.esprit.spring.service.user;

import tn.esprit.spring.entities.PasswordResetToken;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.exceptions.EmailNotExist;
import tn.esprit.spring.exceptions.PasswordValidException;
import tn.esprit.spring.exceptions.ResetPasswordException;
import tn.esprit.spring.exceptions.ResetPasswordTokenException;
import tn.esprit.spring.repository.PasswordResetTokenRepository;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.security.UserPrincipal;
import tn.esprit.spring.security.jwt.JwtProvider;
import tn.esprit.spring.serviceInterface.user.AuthenticationService;
import tn.esprit.spring.serviceInterface.user.JwtRefreshTokenService;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;

import org.cryptacular.bean.EncodingHashBean;
import org.cryptacular.spec.CodecSpec;
import org.cryptacular.spec.DigestSpec;
import org.passay.CharacterRule;
import org.passay.DigestHistoryRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.MessageResolver;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.PropertiesMessageResolver;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import lombok.SneakyThrows;
import net.bytebuddy.utility.RandomString;


@Service
public class AuthenticationServiceImpl implements AuthenticationService
{
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired 
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtRefreshTokenService jwtRefreshTokenService;
    
    @Autowired
    ServiceAllEmail emailService;

    @Override
    public User signInAndReturnJWT(User signInRequest)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String jwt = jwtProvider.generateToken(userPrincipal);

        User signInUser = userPrincipal.getUser();
        signInUser.setAccessToken(jwt);
        signInUser.setRefreshToken(jwtRefreshTokenService.createRefreshToken(signInUser.getUserId()).getTokenId());

        return signInUser;
    }


	@Override
	public PasswordResetToken generatePasswordResetToken(String email) throws EmailNotExist, io.jsonwebtoken.io.IOException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, IOException {
		User user = userRepository.findByEmail(email).orElse(null);
		if (user!=null) {
			PasswordResetToken token = new PasswordResetToken();
			LocalDateTime nowDate = LocalDateTime.now();
			token.setCreateDate(nowDate);
			String tokenValue = RandomString.make(45);
			token.setUserId(user.getUserId());
			token.setToken(tokenValue);
			token.setExprirationDate(nowDate.plusMinutes(15));
			
			passwordResetTokenRepository.save(token);
			try {
				emailService.sendNewResetPasswordMail(token.getToken(), email);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return token;
		} 
		else {
			throw new EmailNotExist("Could not find any user related to the email : " + email);

		}
	
	}


	@Override
	public void updatePassword(String token, String newPassword) throws ResetPasswordException, ResetPasswordTokenException{
		PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
		User u = userRepository.findById(resetToken.getUserId()).orElse(null);
		if (token != null ) {
			
			if ( resetToken.getExprirationDate().isAfter(LocalDateTime.now() ) ) {
				
				isValid(newPassword);
				String encodedPassword = passwordEncoder.encode(newPassword);
				u.setPassword(encodedPassword);
				userRepository.save(u);
				passwordResetTokenRepository.delete(resetToken);
			}
			
			else {
				throw new ResetPasswordTokenException("Reset Password Request has expired ! !");
			}

		}
		else {
			throw new ResetPasswordException("Error processing Reset Password Request !");
		}
		
	}
	
	 @SneakyThrows
	 public boolean isValid(String password) {
		 String messageTemplate = null;
		 Properties props = new Properties();
		 InputStream inputStream = getClass().getClassLoader().getResourceAsStream("passay.properties");
		 try {
			 props.load(inputStream);
		 } catch (IOException e) {
			 e.printStackTrace();
	     	}
		 MessageResolver resolver = new PropertiesMessageResolver(props);


		 List<PasswordData.Reference> history = Arrays.asList(
				 // Password=P@ssword1
				 new PasswordData.HistoricalReference(
	                        "SHA256",
	                        "j93vuQDT5ZpZ5L9FxSfeh87zznS3CM8govlLNHU8GRWG/9LjUhtbFp7Jp1Z4yS7t"),

	                // Password=P@ssword2
				 new PasswordData.HistoricalReference(
	                        "SHA256",
	                        "mhR+BHzcQXt2fOUWCy4f903AHA6LzNYKlSOQ7r9np02G/9LjUhtbFp7Jp1Z4yS7t"),

	                // Password=P@ssword3
				 new PasswordData.HistoricalReference(
	                        "SHA256",
	                        "BDr/pEo1eMmJoeP6gRKh6QMmiGAyGcddvfAHH+VJ05iG/9LjUhtbFp7Jp1Z4yS7t")
	        );
	        EncodingHashBean hasher = new EncodingHashBean(
	                new CodecSpec("Base64"), // Handles base64 encoding
	                new DigestSpec("SHA256"), // Digest algorithm
	                1, // Number of hash rounds
	                false); // Salted hash == false

	        PasswordValidator validator = new PasswordValidator(resolver, Arrays.asList(

	                // length between 8 and 16 characters
	                new LengthRule(8, 16),

	                // at least one upper-case character
	                new CharacterRule(EnglishCharacterData.UpperCase, 1),

	                // at least one lower-case character
	                new CharacterRule(EnglishCharacterData.LowerCase, 1),

	                // at least one digit character
	                new CharacterRule(EnglishCharacterData.Digit, 1),

	                // at least one symbol (special character)
	                new CharacterRule(EnglishCharacterData.Special, 1),


	                // no whitespace
	                new WhitespaceRule(),

	                // rejects passwords that contain a sequence of >= 3 characters alphabetical  (e.g. abc, ABC )
	                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, false),
	                // rejects passwords that contain a sequence of >= 3 characters numerical   (e.g. 123)
	                new IllegalSequenceRule(EnglishSequenceData.Numerical, 3, false)

	                ,new DigestHistoryRule(hasher)

	               ));

	        RuleResult result = validator.validate(new PasswordData(password));


	        PasswordData data = new PasswordData("P@ssword1", password);//"P@ssword1");
	        data.setPasswordReferences(history);
	        RuleResult result2 = validator.validate(data);


	        if (result.isValid() ) {
	            return true;
	        }
	        try {
	            if (result.isValid()==false) {
	                List<String> messages = validator.getMessages(result);

	                messageTemplate = String.join(",", messages);

	                System.out.println("Invalid Password: " + validator.getMessages(result));
	                }
	               } finally {
	            throw new PasswordValidException(messageTemplate);

	        }

	    }
    

}
