(ns facts.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [facts.layout :refer [error-page]]
            [facts.routes.home :refer [home-routes]]
            [compojure.route :as route]
            [facts.env :refer [defaults]]
            [mount.core :as mount]
            [facts.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (-> #'home-routes
        ; tmp disable
        ;(wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
