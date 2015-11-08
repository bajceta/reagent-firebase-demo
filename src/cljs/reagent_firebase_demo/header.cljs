(ns reagent-firebase-demo.header
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [reagent-firebase-demo.form :as form]
            [accountant.core :as accountant]))

(defn welcome [username]
  [:div username])

(defonce username (atom ""))

(defn login [user]
  (let [login (fn [] (reset! user @username ))]
    [:div 
     ;[form/text-input username ] 
     [form/text-input username {:class "fancy-input"}] 
     [:a {:on-click login} "login"]]))


(defn header [user]
  (if (not= nil @user) [welcome @user]
    [login user]))
