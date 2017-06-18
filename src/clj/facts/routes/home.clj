(ns facts.routes.home
  (:require [clojure.java.io :as io]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [compojure.core :refer [defroutes GET POST DELETE]]
            [ring.util.http-response :as response]
            [ring.util.response :refer [response status]])
  (:require [facts.db.core :as db]
            [facts.layout :as layout]))

(defn home-page
  [{:keys [flash]}]
  (layout/render
    "home.html"
    (merge
      {:facts (db/get-facts)}
      (select-keys flash [:side_1
                          :side_2
                          :errors]))))

(defn about-page
  []
  (layout/render "about.html"))


;; (defn validate-fact
;;   [params]
;;   (first
;;    (b/validate
;;      msg-fn
;;      params
;;      :side_1  [v/required [v/min-count 1]]
;;      :side_2  [v/required [v/min-count 1]])))
(defn validate-fact
  [params]
  (let [mod_params (-> params
                       (assoc :title (:side_1 params))
                       (assoc :notes (:side_2 params))
                       (dissoc :side_1 :side_2))]
    (first
      (b/validate
        mod_params
        :title [v/required [v/min-count 1]]
        :notes [v/required [v/min-count 1]]))))


(defn save-fact!
  [{:keys [params]}]
  (if-let [errors (validate-fact params)]
    (response/bad-request {:errors errors})
    (try
      (db/save-fact!
        (-> params
          (assoc :timestamp (java.util.Date.))
          (assoc :user_id 1)))
      (response/ok {:status :ok})
      (catch Exception e
        (println "exception: " e)
        (response/internal-server-error
          {:errors {:server-error ["Failed to save fact!"]}})))))

(defn validate-fact-id
  [params]
  (first
    (b/validate
      params
      :id v/required)))

(defn delete-fact!
  [{:keys [params]}]
  (if-let [errors (validate-fact-id params)]
    (-> (response/found "/")
      (assoc :flash (assoc params :errors errors))))
    (db/delete-fact! params)
      (response/ok {:deleted  true
                    :redirect "/"}))


(defn home-page [] (layout/render "home.html"))

(defroutes
  home-routes
  (GET "/" [] (home-page))
  (GET "/fact" [] (response/ok (db/get-facts)))
  (POST "/fact" request (save-fact! request))
  (DELETE "/fact" request (delete-fact! request))
  (GET "/about" [] (about-page)))



