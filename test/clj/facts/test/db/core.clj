(ns facts.test.db.core
  (:require [facts.db.core :refer [*db*] :as db]
            [luminus-migrations.core :as migrations]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [facts.config :refer [env]]
            [mount.core :as mount]))

;; TODO: Upgrade for mongodb
;;

;; (use-fixtures
;;   :once
;;   (fn [f]
;;     (mount/start
;;       #'facts.config/env
;;       #'facts.db.core/*db*)
;;     (migrations/migrate ["migrate"] (select-keys env [:database-url]))
;;     (f)))


;; (deftest test-users
;;   (jdbc/with-db-transaction [t-conn *db*]
;;     (jdbc/db-set-rollback-only! t-conn)
;;     (is (= 1 (db/create-user!
;;                t-conn
;;                {:id         "1"
;;                 :first_name "Tim"
;;                 :last_name  "Langford"
;;                 :email      "tim.langford@example.com"
;;                 :pass       "pass"
;;                 :admin      true})))
;;     (is (= {:id         "1"
;;             :first_name "Tim"
;;             :last_name  "Langford"
;;             :email      "tim.langford@example.com"
;;             :pass       "pass"
;;             :admin      nil
;;             :last_login nil
;;             :is_active  nil}
;;            (db/get-user t-conn {:id "1"})))))


;; (deftest test-fact
;;   (jdbc/with-db-transaction
;;     [t-conn *db*]
;;     (jdbc/db-set-rollback-only! t-conn)
;;     (let [timestamp (java.util.Date.)]
;;       (is (= 1 (db/save-fact!
;;                  t-conn
;;                 {:user_id   2
;;                  :side_1    "2 x 2"
;;                  :side_2    "4"
;;                  :timestamp timestamp}
;;                  {:connection t-conn})))
;;       (is (= {:user_id    2
;;               :side_1    "2 x 2"
;;               :side_2    "4"
;;               :timestamp timestamp}
;;               (-> (db/get-facts t-conn {})
;;                 (first)
;;                 (select-keys [:user_id :side_1 :side_2 :timestamp])))))))

