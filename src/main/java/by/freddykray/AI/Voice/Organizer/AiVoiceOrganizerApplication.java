package by.freddykray.AI.Voice.Organizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AiVoiceOrganizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiVoiceOrganizerApplication.class, args);
	}

}
