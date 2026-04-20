package by.freddykray.AI.Voice.Organizer;

import org.springframework.boot.SpringApplication;

public class TestAiVoiceOrganizerApplication {

	public static void main(String[] args) {
		SpringApplication.from(AiVoiceOrganizerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
