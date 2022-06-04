package tn.esprit.spring.serviceInterface;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Donation;
import tn.esprit.spring.entities.Event;
import tn.esprit.spring.entities.PostComment;
import tn.esprit.spring.entities.SmsRequest;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.entities.eventComment;
import tn.esprit.spring.enumerations.EventType;
import tn.esprit.spring.exceptions.CourseNotExist;
import tn.esprit.spring.exceptions.CourseOwnerShip;

public interface EventService {

	public void removeEvent(Long IdEvent);

	public List<Event> Get_all_Event();

	public Long findUserDonationsById(Long id);

	public ResponseEntity<?> Participer_event(Long userid, Long eventId) throws MessagingException ,IOException, InterruptedException ;

	public void cancelparticipation(Long userid, Long eventId);

	public String TargetAtrbut(Long eventId);

	public Event affecte_place_event_byavie(Long id_event);

	public void invite_participants(Event t) throws MessagingException;
	public Donation addDonation_to_Event(Long idEvent, Long idUser ,Donation donation);
	void sendSms(SmsRequest smsRequest, String nb, String msg);
    public ResponseEntity<?> getAllComment();
	public ResponseEntity<?> addComment_to_Event(eventComment eventcomment, Long idEvent, Long idUser);
	public ResponseEntity<?> Update_Comment(eventComment eventcomment, Long idEventCom, Long idUser);
	public ResponseEntity<?> Delete_eventCom(Long idEventCom, Long idUser);
	public ResponseEntity<?> createEventbyUser(Long idUser, MultipartFile multipartFile, String EventName,
			String description, Date createAt,Date StartAt, Date endAt, int maxPlace, float targetDonation,
			String address,String latitude,String lang) throws MessagingException, IOException;

	public Event displayEvent(Long idEvent);
	//new
	public ResponseEntity<?> create(Long idUser ,Event event) throws IOException;
	

	public List<Long> findEventYear();

	public ResponseEntity<?> updateImageForEvent(Long idmedia, MultipartFile multipartFile) throws IOException;

	public ResponseEntity<?> deleteImageForEvent(@PathVariable("idMedia") Long id) throws IOException;

	public ResponseEntity<?> addImageForDocumentOfEvent(Long idEvent, Set<MultipartFile> multipartFile)
			throws IOException;

	// ---------------pagination-------------//
	public Page<Event> findEventWithPaginationAndSorting(int offset, int pageSize, String field);
	 public ResponseEntity<?> addressMapss(Long  idEvent) throws IOException, InterruptedException;
	 
	 
	 public Event deletEvent(Long idUser, Long idEvent) throws CourseNotExist, CourseOwnerShip ;
	
	 
		public Event editEvent(Event e,Long idEvent,Long idUser) throws CourseNotExist,CourseOwnerShip;
	 
	
		public ResponseEntity<?> addImageForEvent2002(MultipartFile image,Long idEvent) throws IOException;

}
