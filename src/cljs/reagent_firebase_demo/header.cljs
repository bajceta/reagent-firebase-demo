(ns reagent-firebase-demo.header
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent-firebase-demo.form :as form]))


(defn login-section [user]
  (let [username-input (atom "")
        login (fn [] (reset! user @username-input ))]
    (fn []
      [:div 
       [form/text-input username-input {:class "fancy-input"}] 
       [:a {:on-click login} "login"]])))

(defn logout [user]
  (reset! user nil))

(defn header [user]
  (if (not= nil @user) 
    [:div
     [:div "Logged in user: " @user]
     [:a {:on-click (partial logout user)} "logout"]]
    [login-section user]))
