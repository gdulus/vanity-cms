(ns connections-app.main.db)

(def default-db
  {:context {:loading?  false
             :node-type nil
             :node-name nil}
   :data    {"GROUP"     {}
             "CELEBRITY" {}
             "ALIAS"     {}}})
