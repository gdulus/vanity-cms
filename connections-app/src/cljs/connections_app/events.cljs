(ns connections-app.events)

(defn extract-value [event]
  (-> event .-target .-value))