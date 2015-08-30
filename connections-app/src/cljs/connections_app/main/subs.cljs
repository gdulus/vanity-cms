(ns connections-app.main.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))

(re-frame/register-sub
  :loading?
  (fn [db]
    (reaction (-> @db :loading?))))

(re-frame/register-sub
  :node-type
  (fn [db]
    (reaction (-> @db :context :node-type))))

(re-frame/register-sub
  :node-name
  (fn [db]
    (reaction (-> @db :context :node-name))))