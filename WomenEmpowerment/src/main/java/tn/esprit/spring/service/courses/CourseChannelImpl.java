package tn.esprit.spring.service.courses;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.exceptions.CourseOwnerShip;
import tn.esprit.spring.repository.CourseRepository;
import tn.esprit.spring.repository.NotificationRepository;
import tn.esprit.spring.repository.UserRepository;
import tn.esprit.spring.serviceInterface.courses.CourseLiveService;
@Service
public class CourseChannelImpl implements CourseLiveService {
@Autowired
CourseRepository courseRepository;
@Autowired
UserRepository userRepository;
@Autowired
NotificationRepository notificationRepository;
	@Override
	public String createchannel(long courseId,long userId) throws IOException, InterruptedException {
		Course cour = courseRepository.findById(courseId).get();
		User user = userRepository.findById(userId).get();
		if(user.getCreatedCourses().contains(cour)) {
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://api.hesp.live/channels"))
			    .header("Accept", "application/json")
			    .header("Authorization","Basic NjRmYTBjMzQtYTNhMy00OWZmLTk1OTAtZjBhMmNjZjQzYjc4OjI2aUg0OUNRR3V3VzZzNjRhS0FkVWZtbA==")
			    .header("Content-Type", "application/json")
			    .method("POST", HttpRequest.BodyPublishers.ofString("{\"ingestLocation\":\"europe-west\",\"metadata\":{\"name\":\""+cour.getCourseName()+"\"},\"ingestConfig\":{\"abr\":true,\"fps\":30,\"aspectRatio\":\"1080p\",\"preset\":\"default\"}}"))
			    .build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body().substring(14, 39));
			System.out.println(response.body());
			cour.setChannelId(response.body().substring(14, 39));
			courseRepository.saveAndFlush(cour);
			int index =response.body().toString().indexOf("streamKey")+12;
			int index2 = index+36;
			cour.setStreamKey(response.body().substring(index,index2));
			courseRepository.save(cour);
			return response.body().substring(14, 39);
		
		}
		else 
			return "You aren't the owner of this course";
		}

	

	@Override
	public void startChannel(long courseId,long userId) throws IOException, InterruptedException {
		Course cour = courseRepository.findById(courseId).get();
		User user = userRepository.findById(userId).get();
		if(user.getCreatedCourses().contains(cour)) {
			HttpRequest request = HttpRequest.newBuilder()
				    .uri(URI.create("https://api.hesp.live/channels/"+cour.getChannelId()+"/start"))
				    .header("Accept", "application/json")
				    .header("Authorization", "Basic NjRmYTBjMzQtYTNhMy00OWZmLTk1OTAtZjBhMmNjZjQzYjc4OjI2aUg0OUNRR3V3VzZzNjRhS0FkVWZtbA==")
				    .header("Content-Type", "application/json")
				    .method("POST", HttpRequest.BodyPublishers.noBody())
				    .build();
				HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
				System.out.println(response.body());
				
		}
		else {
			System.out.println("you aren't the owner of the channel");
		}

	}


	
	@Override
	public void stopChannel(long courseId, long userId) throws IOException, InterruptedException {
		Course cour = courseRepository.findById(courseId).get();
		User user = userRepository.findById(userId).get();
		if(user.getCreatedCourses().contains(cour)) {
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://api.hesp.live/channels/"+cour.getChannelId() +"/stop"))
			    .header("Accept", "application/json")
			    .header("Authorization", "Basic NjRmYTBjMzQtYTNhMy00OWZmLTk1OTAtZjBhMmNjZjQzYjc4OjI2aUg0OUNRR3V3VzZzNjRhS0FkVWZtbA==")
			    .method("POST", HttpRequest.BodyPublishers.noBody())
			    .build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
		}
		else {
			System.out.println("No permission");
		}
	}



	@Override
	public void deleteChannel(long courseId, long userId) throws IOException, InterruptedException, CourseOwnerShip {
		Course cour = courseRepository.findById(courseId).get();
		User user = userRepository.findById(userId).get();
		if(user.getCreatedCourses().contains(cour)) {
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://api.hesp.live/channels/"+cour.getChannelId()))
			    .header("Accept", "application/json")
			    .header("Authorization", "Basic NjRmYTBjMzQtYTNhMy00OWZmLTk1OTAtZjBhMmNjZjQzYjc4OjI2aUg0OUNRR3V3VzZzNjRhS0FkVWZtbA==")
			    .method("DELETE", HttpRequest.BodyPublishers.noBody())
			    .build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
			cour.setChannelId(null);
			courseRepository.saveAndFlush(cour);
		}
		else {
			throw new CourseOwnerShip("You aren not the owner of the course");
		}
	}



	@Override
	public HttpResponse<String> getChannelStatus(long courseId) throws IOException, InterruptedException {
		Course cour = courseRepository.findById(courseId).get();
		{
		HttpRequest request = HttpRequest.newBuilder()
			    .uri(URI.create("https://api.hesp.live/channels/"+cour.getChannelId() +"/status"))
			    .header("Accept", "application/json")
			    .header("Authorization", "Basic NjRmYTBjMzQtYTNhMy00OWZmLTk1OTAtZjBhMmNjZjQzYjc4OjI2aUg0OUNRR3V3VzZzNjRhS0FkVWZtbA==")
			    .method("GET", HttpRequest.BodyPublishers.noBody())
			    .build();
			HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
			return response;
		}
		
	}

}
