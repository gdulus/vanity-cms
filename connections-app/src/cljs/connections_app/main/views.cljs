(ns connections-app.main.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [connections-app.events :as events]
            [connections-app.log :as log]
            [connections-app.graph :as graph]))

;; ---------------------------------------------------------------

(defn graph-canvas []
  (fn []
    [:div {:class "col-md-10 fill"}
     [:div {:id "alchemy"}]]))

;; ---------------------------------------------------------------

(defn nodes-list-item []
  (fn [node]
      [:a {:class    "list-group-item"
           :on-click #(re-frame/dispatch [:show-node-details (get node "id")])} (get node "name")]))

(defn nodes-list-renderer []
  (let [node-list (re-frame/subscribe [:nodes-list])]
    (fn []
      [:div {:class     "list-group"
             :on-scroll #(re-frame/dispatch [:node-list-scroll (events/extract-targe %)])}
       (for [node @node-list]
         ^{:key node} [nodes-list-item node])])))

(defn nodes-list-did-mount []
  (log/debug "Mounting main-panel"))

(defn nodes-list []
  (reagent/create-class {:reagent-render      nodes-list-renderer
                         :component-did-mount nodes-list-did-mount}))

;; ---------------------------------------------------------------

(defn navigation []
  (let [loading? (re-frame/subscribe [:loading?])
        node-name (re-frame/subscribe [:node-name])]
    (fn []
      [:div {:class "col-md-2 fill"}
       [:div {:class "row menu fill"}
        [:div {:class "col-md-12 menu-section fill"}
         [:form {:class "fill"}
          [:select {:class     "form-control"
                    :on-change #(re-frame/dispatch [:target-node-type-changed (events/extract-value %)])}
           [:option {:value ""} "Select type ..."]
           [:option {:value "GROUP"} "Group"]
           [:option {:value "CELEBRITY"} "Celebrity"]
           [:option {:value "ALIAS"} "Alias"]]
          [:input {:class       "form-control"
                   :disabled    @loading?
                   :value       @node-name
                   :placeholder "Start typing ..."
                   :on-change   #(re-frame/dispatch [:node-name-changed (events/extract-value %)])}]
          [nodes-list]]]]])))

;; ---------------------------------------------------------------

(defn- main-panel []
  (let [loading? (re-frame/subscribe [:loading?])]
    (fn []
      [:div {:class (if @loading? "row fill loader" "row fill")}
       [graph-canvas]
       [navigation]])))
