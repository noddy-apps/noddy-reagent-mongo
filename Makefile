app_name?=noddy-reagent-mongo
registry?=templecloud
database_url?=mongodb://127.0.0.1/noddy_facts_dev

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

build-jar:
	lein uberjar

# 'mongodb://192.168.0.6/noddy_facts_dev'
run-jar:
	java -jar target/uberjar/facts.jar -Ddatabase-url=${database_url} 
	
docker-build:
	docker build -t ${registry}/${app_name} .

docker-push:
	docker push ${registry}/${app_name}

docker-run:
	docker run -p 3000:3000 --name ${app_name} ${registry}/${app_name}
