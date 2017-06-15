(ns user
  (:require [mount.core :as mount]
            facts.core))

(defn start []
  (mount/start-without #'facts.core/repl-server))

(defn stop []
  (mount/stop-except #'facts.core/repl-server))

(defn restart []
  (stop)
  (start))


