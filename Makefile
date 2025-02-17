start:
	docker-compose up -d
	
restart:
	make clear
	make cm
	
clear:
	docker compose down --volumes

cm:
	docker-compose up -d --build
	
config:
	cd src/main/resources

	keytool -genkeypair \
		-alias baeldung \
		-keyalg RSA \
		-keysize 2048 \
		-storetype PKCS12 \
		-keystore baeldung.p12 \
		-validity 3650

