start:
	docker-compose up -d
	
restart:
	make clear
	make cm
	
clear:
	docker compose down --volumes

cm:
	docker-compose up -d --build
	
.PHONY: config	
config:
	@cd ssl && keytool -genkeypair \
		-alias baeldung \
		-keyalg RSA \
		-keysize 2048 \
		-storetype PKCS12 \
		-keystore ssl/baeldung.p12 \
		-validity 3650

