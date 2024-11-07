start:
	docker-compose up -d
restart:
	make clear
	make start
clear:
	docker compose down --volumes
config:
	cd src/main/resources

	keytool -genkeypair \
		-alias baeldung \
		-keyalg RSA \
		-keysize 2048 \
		-storetype PKCS12 \
		-keystore baeldung.p12 \
		-validity 3650

