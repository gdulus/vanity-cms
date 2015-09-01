(ns connections-app.config)

;;----------------------------------------------
;; Config related functions
;;----------------------------------------------

(def ^{:private true} config
  {:remote {:list {"GROUP"     "http://vanity-assembly/groups"
                   "CELEBRITY" "http://vanity-assembly/celebrities"
                   "ALIAS"     "http://vanity-assembly/aliases"}}})

(defn get-config [& args]
  (try
    (loop [current config index 0]
      (if (== index (count args))
        current
        (recur (current (nth args index)) (inc index))))
    (catch js/Error e nil)))