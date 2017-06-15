(ns facts.db.core
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer :all]
            [mount.core :refer [defstate]]
            [facts.config :refer [env]])
  (:import org.bson.types.ObjectId))

(def col "facts")

(defstate db
  :start (:db db*))


(defn- handle_id
  [fact]
  (-> fact
    (assoc :id (str (:_id fact)))
    (dissoc :_id)))


(defn get-fact
  [id]
  (-> (mc/find-one-as-map db col {:_id (ObjectId. id)})
      (handle_id)))


(defn get-facts
  []
  (->> (mc/find-maps db col)
       (map handle_id)))


(defn save-fact!
  [fact]
  (mc/insert db col fact))


(defn delete-fact!
  [identifiable]
  (println "trjl> id :" identifiable)
  (let [res (mc/remove-by-id db col (ObjectId. (:id identifiable)))]
    (println "trjl> res: " res)))

