package tn.esprit.spring.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.nylas.Event;
import com.nylas.RemoteCollection;
import com.nylas.RequestFailedException;

import io.swagger.annotations.Api;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tn.esprit.spring.exceptions.CoursesLimitReached;
import tn.esprit.spring.service.courses.CourseCalendarServiceImpl;

@RestController
@EnableSwagger2
@Api(tags = "Courses Events Management")
@RequestMapping("/CourseEvent")
public class CourseCalendarController {
	@Autowired
	CourseCalendarServiceImpl courseCalendarServiceImpl;
	@PostMapping(path = "addEvent/{courseId}/{eventName}/{hour}/{minutes}/{date}")
	public void addEvent(@PathVariable("courseId")Long courseId,@PathVariable("eventName")String eventName,@PathVariable("hour")int hour,@PathVariable("minutes")int minutes,@PathVariable(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) throws CoursesLimitReached, IOException, RequestFailedException {

		courseCalendarServiceImpl.addEvent(courseId, eventName,hour,minutes,date.toInstant().atZone(ZoneId.of("UTC")).toLocalDate());
}
	@GetMapping(path = "getEvent/{eventId}")
	public Event getEvent(@PathParam(value = "eventId") long eventId) throws IOException, RequestFailedException {
		return courseCalendarServiceImpl.getEvent(eventId);
	}
	@DeleteMapping(path="deleteEvent/{eventId}")
	 public void deleteEvent(Long eventId)throws IOException, RequestFailedException{
		courseCalendarServiceImpl.deleteEvent(eventId);
	}
	@PutMapping(path = "updateEvent/{eventId}/{hour}/{minutes}/{date}")
	public void updateEvent(@PathVariable("eventId")Long eventId,@PathVariable("hour")int hour,@PathVariable("minutes")int minutes,@RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws CoursesLimitReached, IOException, RequestFailedException {
		courseCalendarServiceImpl.updateEventTime(eventId, hour, minutes, date);
}
	@GetMapping(path = "getEvents/{clendarId}")
	public RemoteCollection<Event> getEvents(@PathParam(value = "clendarId") String clendarId) throws IOException, RequestFailedException {
		return courseCalendarServiceImpl.getEvents(clendarId);
	}
	}
