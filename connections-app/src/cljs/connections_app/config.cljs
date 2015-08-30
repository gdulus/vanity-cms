(ns connections-app.config)

;;----------------------------------------------
;; Config related functions
;;----------------------------------------------

(def ^{:private true} config
  {:remote {:list {"GROUP"     "http://localhost/group"
                   "CELEBRITY" "http://localhost/celebirty"
                   "ALIAS"     "http://localhost/alias"}}})

(defn get-config [& args]
  (try
    (loop [current config index 0]
      (if (== index (count args))
        current
        (recur (current (nth args index)) (inc index))))
    (catch js/Error e nil)))