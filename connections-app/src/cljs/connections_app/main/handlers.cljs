(ns connections-app.main.handlers
    (:require [re-frame.core :as re-frame]
              [clojure.string :as string]
              [ajax.core :refer [GET POST]]
              [connections-app.main.db :as db]
              [connections-app.log :as log]
              [connections-app.config :as config]
              [connections-app.graph :as graph]))

;; ---------------------------------------------------------------------

(re-frame/register-handler
    :initialize-db
    (fn [_ _]
        db/default-db))

;; ---------------------------------------------------------------------

(re-frame/register-handler
    :error
    (fn [db event]
        (do (log/error event)
            (assoc-in db [:context :loading?] false))))

;; ---------------------------------------------------------------------

(def ^{:private true} search-timer-instane)

(re-frame/register-handler
    :node-name-changed
    (fn [db event]
        (let [node-name (event 1)]
            (do (log/info "Node name changed" node-name)
                (if-not (nil? search-timer-instane)
                    (js/clearInterval search-timer-instane))
                ;;(set! search-timer-instane (js/setTimeout #(re-frame/dispatch [:render-nodes node-name]) 2000))
                (assoc-in db [:context :node-name] node-name)))))

;; ---------------------------------------------------------------------

(re-frame/register-handler
    :render-nodes
    (fn [db event]
        (let [node-type (event 1)
              response (event 2)]
            (do (log/info "For node" node-type "got data" response)
                (-> db
                    (assoc-in [:context :loading?] false)
                    (assoc-in [:data node-type] response))))))

;; ---------------------------------------------------------------------

(re-frame/register-handler
    :target-node-type-changed
    (fn [db event]
        (let [node-type (event 1)]
            (if (string/blank? node-type)
                ;; empty node seleced
                (do (log/info "Empty node requested")
                    (re-frame/dispatch [:empty-node-type-selected])
                    db)
                ;; not empty node selected, request data from backend
                (let [url (config/get-config :remote :list node-type)]
                    (do (log/info "Requested target node" node-type "and url" url)
                        (GET url {:handler       #(re-frame/dispatch [:render-nodes node-type %1])
                                  :error-handler #(re-frame/dispatch [:error %1])})
                        (-> db
                            (assoc-in [:context :loading?] true)
                            (assoc-in [:context :node-type] node-type)
                            (assoc-in [:context :node-name] ""))))))))

;; ---------------------------------------------------------------------

(re-frame/register-handler
    :node-list-scroll
    (fn [db event]
        (let [target (event 1)]
            (do (log/debug "Got scroll event")
                (log/debug target)
                db))))

;; ---------------------------------------------------------------------

(re-frame/register-handler
    :render-node-details
    (fn [db event]
        (let [node-id (event 1)
              node-data (event 2)]
            (do (log/info "Node" node-id "with data" node-data)
                (graph/render node-data)
                (-> db
                    (assoc-in [:context :loading?] false)
                    (assoc-in [:data :selected-data] node-data))))))

(re-frame/register-handler
    :show-node-details
    (fn [db event]
        (let [node-id (event 1)
              url (config/get-config :remote :details)]
            (do (log/info "Requested node details" node-id)
                (GET url {:params        {:id node-id}
                          :handler       #(re-frame/dispatch [:render-node-details node-id %1])
                          :error-handler #(re-frame/dispatch [:error %1])})
                (-> db
                    (assoc-in [:context :loading?] true)
                    (assoc-in [:context :node-id] node-id))))))


;; ---------------------------------------------------------------------

(re-frame/register-handler
    :graph-node-clicked
    (fn [db event]
        (let [node-id (event 1)
              node-type (event 2)]
            (do (log/info "Graph node" node-type "(" node-id ") selected")
                (re-frame/dispatch [:show-node-details node-id])
                (-> db
                    (assoc-in [:context :node-id] node-id))))))

;; ---------------------------------------------------------------------

(re-frame/register-handler
    :start-dragg
    (fn [db event]
        (let [node-id (event 1)]
            (do (log/info "Starting dragging node" node-id)
                (assoc-in db [:context :dragged-node-id] node-id)))))

(re-frame/register-handler
    :stop-dragg
    (fn [db event]
        (let [selected-node-id (get-in db [:context :node-id])
              dragged-node-id (get-in db [:context :dragged-node-id])]
            (do (log/info "Stopped dragging node" dragged-node-id "into" selected-node-id)
                (assoc-in db [:context :dragged-node-id] nil)))))