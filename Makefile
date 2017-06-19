app_name?=noddy-reagent-mongo
registry?=templecloud
database_url?=mongodb://127.0.0.1/noddy_facts_dev
# database_url?='mongodb://192.168.0.6/noddy_facts_dev'


run:
	lein run

build-cljs:
	lein cljsbuild once

repl:
	lein repl :connect 7000

test:
	lein test

auto-test:
	lein test-refresh

build-uberjar:
	lein uberjar

run-uberjar:
	java -jar target/uberjar/facts.jar -Ddatabase-url=${database_url} 
	
docker-build:
	docker build -t ${registry}/${app_name} .

docker-push:
	docker push ${registry}/${app_name}

docker-run:
	docker run -d \
		--name ${app_name} \
		-p 3000:3000 \
		${registry}/${app_name} \
		-Ddatabase-url=${database_url} -jar /app.jar

docker-stop:
	docker rm -f ${app_name}

docker-logs:
	docker logs -tf ${app_name}

