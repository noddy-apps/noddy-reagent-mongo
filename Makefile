app_name:='noddy-reagent-mongo'
registry:='templecloud'
database-url:='mongodb://127.0.0.1/noddy_facts_dev'

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

run-jar-local:
	java -Ddatabase-url="mongodb://127.0.0.1/noddy_facts_dev" -jar target/uberjar/facts.jar

run-jar:
	java -Ddatabase-url=${database-url}  -jar target/uberjar/facts.jar

docker-build:
	docker build -t ${registry}/${app_name} .

docker-push:
	docker push ${registry}/${app_name}

docker-run:
	docker run -p 3000:3000 --name ${name} ${registry}/${app_name}
