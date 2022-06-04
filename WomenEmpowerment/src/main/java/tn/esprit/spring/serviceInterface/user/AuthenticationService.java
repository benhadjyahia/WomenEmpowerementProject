package tn.esprit.spring.serviceInterface.user;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import io.jsonwebtoken.io.IOException;
import tn.esprit.spring.entities.PasswordResetToken;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.exceptions.EmailNotExist;
import tn.esprit.spring.exceptions.ResetPasswordException;
import tn.esprit.spring.exceptions.ResetPasswordTokenException;


public interface AuthenticationService
{
    User signInAndReturnJWT(User signInRequest);
	
    PasswordResetToken generatePasswordResetToken(String email) throws EmailNotExist, IOException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, TemplateException, java.io.IOException;
	
	void updatePassword(String token, String newPassword) throws ResetPasswordException, ResetPasswordTokenException;
}
