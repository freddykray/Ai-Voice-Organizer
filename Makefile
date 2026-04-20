WHISPER_MODEL=$(HOME)/whisper-models/ggml-base.bin
WHISPER_HOST=127.0.0.1
WHISPER_PORT=8081

start-stt:
	whisper-server -m $(WHISPER_MODEL) -l ru --host $(WHISPER_HOST) --port $(WHISPER_PORT) --convert

start-app:
	./mvnw spring-boot:run

start-all:
	@echo "Сначала запусти STT в отдельной вкладке: make start-stt"
	@echo "Потом запусти приложение: make start-app"

test-stt:
	curl -X POST http://$(WHISPER_HOST):$(WHISPER_PORT)/inference \
	  -F "file=@/Users/freddykray/Downloads/voicetest.wav"