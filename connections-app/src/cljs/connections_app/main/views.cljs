(ns connections-app.main.views
    (:require [re-frame.core :as re-frame]
              [reagent.core :as reagent]
              [connections-app.events :as events]
              [clojure.string :as string]))

;; ---------------------------------------------------------------

(defn graph-canvas-renderer []
    (fn []
        [:div {:class "col-md-10 fill "}
         [:div {:id "alchemy" :class "droppable"}]]))

(defn graph-canvas-did-mount []
    (js/$ (fn [] (.droppable
                     (js/$ "#alchemy")
                     (clj->js {:drop (fn [event ui] (re-frame/dispatch [:stop-dragg]))})))))

(defn graph-canvas []
    (reagent/create-class {:reagent-render      graph-canvas-renderer
                           :component-did-mount graph-canvas-did-mount}))

;; ---------------------------------------------------------------

(defn nodes-list-item []
    (fn [node]
        [:a {:id       (get node "id")
             :class    "list-group-item draggable"
             :on-click #(re-frame/dispatch [:show-node-details (get node "id")])}
         [:span {:class "glyphicon glyphicon-zoom-in"}]
         (get node "name")]))

(defn new-node-list-item []
    (fn [node-name node-type]
        [:a {:class    "list-group-item add-edit"
             :on-click #(re-frame/dispatch [:add-new-node node-type node-name])}
         [:span {:class "glyphicon glyphicon-plus"}]
         (str "Add as new " (string/lower-case node-type))
         ]))

(defn current-node-edit-item []
    (fn [node-name node-type]
        [:a {:class    "list-group-item edit-button"
             :on-click #(re-frame/dispatch [:add-new-node node-type node-name])}
         [:span {:class "glyphicon glyphicon-pencil"}]
         "Edit current node"
         ]))

(defn current-node-delete-item []
    (fn [node-name node-type]
        [:a {:class    "list-group-item edit-button"
             :on-click #(re-frame/dispatch [:add-new-node node-type node-name])}
         [:span {:class "glyphicon glyphicon-remove"}]
         "Delte current node"
         ]))

(defn nodes-list-did-* []
    (js/$ (fn [] (.draggable
                     (js/$ ".draggable")
                     (clj->js {:helper "clone"
                               :start  #(this-as this (re-frame/dispatch [:start-dragg (.attr (js/$ this) "id")]))})))))

(defn nodes-list-renderer []
    (let [node-list (re-frame/subscribe [:nodes-list])
          node-name (re-frame/subscribe [:node-name])
          node-type (re-frame/subscribe [:node-type])]
        (fn []
            [:div {:class     "list-group"
                   :on-scroll #(re-frame/dispatch [:node-list-scroll (events/extract-targe %)])}
             (if (and (not (string/blank? @node-name)) (not (string/blank? @node-type)))
                 [new-node-list-item @node-name @node-type])
             [current-node-edit-item]
             [current-node-delete-item]
             (for [node @node-list]
                 ^{:key node} [nodes-list-item node])])))

(defn nodes-list []
    (reagent/create-class {:reagent-render       nodes-list-renderer
                           :component-did-mount  nodes-list-did-*
                           :component-did-update nodes-list-did-*}))

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


(defn main-panel []
    (let [loading? (re-frame/subscribe [:loading?])]
        (fn []
            [:div {:class (if @loading? "row fill loader" "row fill")}
             [graph-canvas]
             [navigation]])))

