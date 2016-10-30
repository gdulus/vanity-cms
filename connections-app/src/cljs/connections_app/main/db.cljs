(ns connections-app.main.db)

(def default-db
    {:context {:loading?        false
               :node-type       nil
               :node-name       nil
               :node-id         nil
               :dragged-node-id nil}
     :data    {"GROUP"        {}
               "CELEBRITY"    {}
               "ALIAS"        {}
               :selected-data {}}})
