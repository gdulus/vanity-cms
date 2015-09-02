(ns connections-app.graph
  (:require [connections-app.log :as log]))

(def ^{:private true} sigma-instane)
(def ^{:private true} default-config {"container" "canvas" "settings" {"defaultNodeColor" "#ec5148"}})

(defn- transform-data [data]
  data)

(defn- create-sigma []
  (do
    (if (nil? sigma-instane)
      (do
        (log/info "Creating an instance of SigmaJS object")
        (set! sigma-instane (js/sigma. (clj->js default-config)))))
    sigma-instane))

(defn render [data]
  (let [sigma-data (transform-data data)
        sigma (create-sigma)]
    (do
      (log/debug "Redrawing SigmaJS context with new data")
      (.clear sigma.graph)
      (.read sigma.graph (clj->js sigma-data))
      (.refresh sigma))))
