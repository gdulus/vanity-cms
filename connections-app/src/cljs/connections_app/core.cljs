(ns connections-app.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [connections-app.main.handlers]
            [connections-app.main.subs]
            [connections-app.main.views :as views]))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
