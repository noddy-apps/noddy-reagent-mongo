(ns facts.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [facts.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[facts started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[facts has shut down successfully]=-"))
   :middleware wrap-dev})
