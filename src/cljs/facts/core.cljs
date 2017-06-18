(ns facts.core
  (:require [cljs.pprint :as pp]
            [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [DELETE GET POST]]))

;; data functions

(defn delete-fact!
  [id facts]
  (DELETE "/fact"
          {:headers {"Accept"       "application/transit+json"
                     "x-csrf-token" (.-value (.getElementById js/document "token"))}
           :params {:id id}
           :handler (fn
                      [response]
                      (do
                        (.log js/console (str "DELETE response:" response))
                        (if (:deleted response)
                          (reset! facts (remove (fn [fact] (= id (:id fact))) @facts))
                          facts)))}))

(defn get-facts
  [facts]
  (GET
    "/fact"
    {:headers {"Accept" "application/transit+json"}
     :handler #(reset! facts (vec %))}))


(defn save-fact!
  [fields errors facts]
  (POST "/fact"
        {:format :json
         :headers {"Accept"       "application/transit+json"
                   "x-csrf-token" (.-value (.getElementById js/document "token"))}
         :params @fields
         :handler #(do
                     (.log js/console (str "response:" %))
                     (swap! facts conj (assoc @fields :timestamp (js/Date.)))
                     (reset! fields {})
                     (reset! errors nil))
         :error-handler #(do
                           (.error js/console (str "error:" %))
                           (reset! errors (get-in % [:response :errors])))})
  (get-facts facts))


;; dom generation functions


(defn errors-component
  [errors id]
  (when-let [error (id @errors)]
    [:div.alert.alert-danger (clojure.string/join error)]))

(defn fact-form
  [facts]
  (let [fields (atom {})
        errors (atom nil)]
    (fn []
      [:div.content
       [:div.form-group
        [:p "Title:"
         [:input.form-control
          {:type :text
           :name :side_1
           :value (:side_1 @fields)
           :on-change #(swap! fields
                              assoc :side_1 (-> % .-target .-value))}]]
        ;[errors-component errors :side_1]
        [errors-component errors :title]
        [:p "Note:"
         [:textarea.form-control
          {:rows 4
           :cols 50
           :name :side_2
           :value (:side_2 @fields)
           :on-change #(swap! fields
                              assoc :side_2 (-> % .-target .-value))}]]
        ;[errors-component errors :side_2]
        [errors-component errors :notes]
        [:input.btn.btn-primary {:type :submit
                                 :on-click #(save-fact! fields errors facts)
                                 :value "add"}]
        [errors-component errors :server-error]]])))

(defn fact-list
  [facts]
  [:div.content
   (for
     [{:keys [timestamp id side_1 side_2]} @facts]
      ^{:key timestamp}
      [:row {:style {:padding "0px"}}
       [:div {:style {:border-style "solid"
                      :border-width "1px"
                      :border-color "lightgray"
                      :border-radius "5px"
                      :padding "10px"}}
         [:row
           [:small [:time (.toLocaleString timestamp)]]
           [:input.btn.btn-outline-danger.btn-sm.pull-right {:type :submit
                                                             :on-click #(delete-fact! id facts)
                                                             :value "delete"}]]
         [:p [:b side_1]]
         [:hr]
         [:p side_2]]
       [:br]])])

(defn home
  []
  (let [facts (atom nil)]
    (get-facts facts)
    (fn []
      [:div
       [:div.row
         [:div.col-4.offset-md-3
          [fact-form facts]]]
       [:div.row
        [:div.col-4.offset-md-3
         [fact-list facts]]]])))

;; reagent render function

(reagent/render
  [home]
  (.getElementById js/document "content"))
