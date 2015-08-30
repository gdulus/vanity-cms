(ns connections-app.main.handlers
  (:require [re-frame.core :as re-frame]
            [clojure.string :as string]
            [ajax.core :refer [GET POST]]
            [connections-app.main.db :as db]
            [connections-app.log :as log]
            [connections-app.config :as config]))

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
        (assoc-in db [:loading?] false))))

;; ---------------------------------------------------------------------

(re-frame/register-handler
  :node-name-changed
  (fn [db event]
    (let [node-name (event 1)]
      (do (log/info "Node name changed" node-name)
          (assoc-in db [:context :node-name] node-name)))))

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
              (GET url
                   {:handler       #(re-frame/dispatch [:render-node %1])
                    :error-handler #(re-frame/dispatch [:error %1])})
              (-> db
                  (assoc-in [:loading?] true)
                  (assoc-in [:context :node-type] node-type)
                  (assoc-in [:context :node-name] ""))))))))

;; ---------------------------------------------------------------------