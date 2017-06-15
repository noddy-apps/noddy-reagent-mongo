(ns facts.core
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [DELETE GET POST]]))

(defn errors-component
  [errors id]
  (when-let [error (id @errors)]
    [:div.alert.alert-danger (clojure.string/join error)]))


(defn delete-fact!
  [id facts]
  (DELETE "/fact"
          {:headers {"Accept"       "application/transit+json"
                     "x-csrf-token" (.-value (.getElementById js/document "token"))}
           :params {:id id}
           :handler (fn
                      [response]
                      (do
                        ;; (.log js/console (str "DELETE response:" response))
                        (if (:deleted response)
                          (reset! facts (remove (fn [fact] (= id (:id fact))) @facts))
                          facts)))}))

(defn get-facts
  [facts]
  (GET
    "/fact"
    {:headers {"Accept" "application/transit+json"}
     :handler #(reset! facts (vec %))}))

(defn fact-list
  [facts]
  [:ul.content
   (for
     [{:keys [timestamp id side_1 side_2]} @facts]
      ^{:key timestamp}
      [:li
       [:time (.toLocaleString timestamp)]
       [:p side_1]
       [:p side_2]
       [:input.btn.btn-primary {:type :submit
                                :on-click #(delete-fact! id facts)
                                :value "delete"}]
       ])])

(defn save-fact!
  [fields errors facts]
  (POST "/fact"
        {:format :json
         :headers {"Accept"       "application/transit+json"
                   "x-csrf-token" (.-value (.getElementById js/document "token"))}
         :params @fields
         :handler #(do
                     ;(.log js/console (str "response:" %))
                     (swap! facts conj (assoc @fields :timestamp (js/Date.)))
                     (reset! fields {})
                     (reset! errors nil))
         :error-handler #(do
                           (.error js/console (str "error:" %))
                           (reset! errors (get-in % [:response :errors])))}))

(defn fact-form
  [facts]
  (let [fields (atom {})
        errors (atom nil)]
    (fn []
      [:div.content.span12
       [:div.form-group.span12
        [:p "Question:"
         [:input.form-control
          {:type :text
           :name :side_1
           :value (:side_1 @fields)
           :on-change #(swap! fields
                              assoc :side_1 (-> % .-target .-value))}]]
        [errors-component errors :side_1]
        [:p "Answer:"
         [:textarea.form-control
          {:rows 4
           :cols 50
           :name :side_2
           :value (:side_2 @fields)
           :on-change #(swap! fields
                              assoc :side_2 (-> % .-target .-value))}]]
        [errors-component errors :side_2]
        [:input.btn.btn-primary {:type :submit
                                 :on-click #(save-fact! fields errors facts)
                                 :value "add"}]
        [errors-component errors :server-error]]])))


(defn home
  []
  (let [facts (atom nil)]
    (get-facts facts)
    (fn []
      [:div
       [:div.row
         [:div.span12
          [fact-form facts]]]
       [:div.row
        [:div.span12
         [fact-list facts]]]
       ])))

(reagent/render
  [home]
  (.getElementById js/document "content"))
