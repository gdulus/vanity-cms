(ns connections-app.main.views
  (:require [re-frame.core :as re-frame]
            [connections-app.events :as events]
            [connections-app.log :as log]))

(defn graph-canvas []
  (fn []
    [:div {:id "canvas" :class "col-md-10 fill"}]))

(defn nodes-list []
  (let [node-list (re-frame/subscribe [:nodes-list])]
    (fn []
      [:div {:class     "list-group"
             :on-scroll #(log/info "SCROLLL")}
       (for [node @node-list]
         ^{:key node} [:div {:class "list-group-item"} (get node "name")])])))

(defn navigation []
  (let [loading? (re-frame/subscribe [:loading?])
        node-name (re-frame/subscribe [:node-name])]
    (fn []
      [:div {:class "col-md-2"}
       [:div {:class "row menu"}
        [:div {:class "col-md-12 menu-section"}
         [:form
          [:select {:class     "form-control input-sm"
                    :on-change #(re-frame/dispatch [:target-node-type-changed (events/extract-value %)])}
           [:option {:value ""} ""]
           [:option {:value "GROUP"} "group"]
           [:option {:value "CELEBRITY"} "celebrity"]
           [:option {:value "ALIAS"} "alias"]]
          [:input {:class       "form-control input-sm"
                   :disabled    @loading?
                   :value       @node-name
                   :placeholder "Start typing ..."
                   :on-change   #(re-frame/dispatch [:node-name-changed (events/extract-value %)])}]
          [nodes-list]]]]])))

(defn main-panel []
  (let [loading? (re-frame/subscribe [:loading?])]
    (fn []
      [:div {:class (if @loading? "row fill loader" "row fill")}
       [graph-canvas]
       [navigation]])))

