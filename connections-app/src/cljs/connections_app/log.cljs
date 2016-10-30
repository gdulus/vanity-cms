(ns connections-app.log
  (:require [clojure.string :as string]))

(def ^:private log-level {:debug #(.debug js/console %) :info #(.info js/console %) :error #(.error js/console %)})

(defn- log [level message]
  (if (log-level level)
    ((log-level level) (string/join " " message))))

(defn info [& message]
  (log :info message))

(defn debug [& message]
  (log :debug message))

(defn error [& message]
  (log :error message))