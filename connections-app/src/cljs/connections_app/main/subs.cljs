(ns connections-app.main.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))

(re-frame/register-sub
  :loading?
  (fn [db]
    (reaction (get-in @db [:context :loading?]))))

(re-frame/register-sub
  :node-name
  (fn [db]
    (reaction (get-in @db [:context :node-name]))))

(re-frame/register-sub
  :node-type
  (fn [db]
    (reaction (get-in @db [:context :node-type]))))

(re-frame/register-sub
  :nodes-list
  (fn [db]
    (let [node-type (reaction (get-in @db [:context :node-type]))]
      (reaction (get-in @db [:data @node-type "data"])))))
