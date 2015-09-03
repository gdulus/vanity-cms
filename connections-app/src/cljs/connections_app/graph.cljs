(ns connections-app.graph
    (:require [connections-app.log :as log]))

(def ^{:private true} sigma-instane)
(def ^{:private true} node-default-config {"Group"     {:color "rgb(255, 117, 110)" :size 1}
                                           "Celebrity" {:color "rgb(104, 189, 246)" :size 1}
                                           "Alias"     {:color "rgb(109, 206, 158)" :size 1}})
(def ^{:private true} default-config {:container "alchemy"
                                      :settings  {:defaultNodeColor "#ec5148"
                                                  :edgeColor        "#000000"
                                                  :minEdgeSize      0.5
                                                  :maxEdgeSize      0.5
                                                  :sideMargin       1
                                                  :scalingMode      "inside"}})
(defn- get-cofig [node-type node-config]
    (conj (get node-default-config node-type) node-config))

(defn- get-edges [node]
    (if (== (count node) 2)
        (let [soruce-id (str (:id (get node 0)))
              target-id (str (:id (get node 1)))
              edge-id (str soruce-id "-" target-id)]
            {:id edge-id :source soruce-id :target target-id})))

(defn- get-node
    ([id name type idx count]
     (get-cofig type {:id    (str id)
                      :label name
                      :x     (* (* idx 0.3) (Math/cos (* idx 10)))
                      :y     (* (* idx 0.3) (Math/sin (* idx 10)))}))
    ([id name type]
     (get-cofig type {:id    (str id)
                      :label name
                      :type  type
                      :x     0
                      :y     0})))

(defn- get-nodes [idx node count]
    (let [{from-id "fid" from-name "fname" from-label "flabel" to-id "tid" to-name "tname" to-label "tlabel"} node]
        (if (== from-id to-id)
            [(get-node from-id from-name (get from-label 0))]
            [(get-node from-id from-name (get from-label 0)) (get-node to-id to-name (get to-label 0) idx count)])))

(defn- transform-data [data]
    (let [nodes (map-indexed #(get-nodes %1 %2 (count data)) data)
          edges (map get-edges nodes)]
        {:nodes (reduce #(reduce conj %1 %2) #{} nodes)
         :edges (remove nil? edges)}))

(defn- create-sigma []
    (do
        (if (nil? sigma-instane)
            (do
                (log/info "Creating an instance of SigmaJS object")
                (set! sigma-instane (js/sigma. (clj->js default-config)))
                (.bind sigma-instane "clickNode" #(log/info "handling click event" %)))
            sigma-instane)))

(defn render [data]
    (let [sigma-data (clj->js (transform-data data))
          sigma (create-sigma)]
        (do
            (log/debug "Redrawing SigmaJS context with new data")
            (.debug js/console sigma-data)
            (.clear sigma.graph)
            (.read sigma.graph sigma-data)
            (.refresh sigma))))