(ns connections-app.events)

(defn extract-value [event]
  (-> event .-target .-value))

(defn extract-targe [event]
  (-> event .-target))