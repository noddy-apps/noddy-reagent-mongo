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

run-jar:
	java -Ddatabase-url="mongodb://127.0.0.1/noddy_facts_dev" -jar target/uberjar/facts.jar

docker-build:
	docker build -t templecloud/noddy-reagent-mongo 
	
docker-run:
	docker run -p 3000:3000 --name noddy-reagent-mongo templecloud/noddy-reagent-mongo
	
	.
