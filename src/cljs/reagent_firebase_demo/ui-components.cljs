(ns reagent-firebase-demo.ui-components
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent-firebase-demo.form :as form]))
;; header

(defn- login-section [user]
  (let [username-input (atom "")
        login (fn [] (reset! user @username-input ))]
    (fn []
      [:div 
       [form/text-input username-input {:class "fancy-input"}] 
       [:a {:on-click login} "login"]])))

(defn- logout [user]
  (reset! user nil))

(defn- logged-in-user [user]
  [:div
     [:div "Logged in user: " @user]
     [:a {:on-click (partial logout user)} "logout"]])

(defn header [user]
  (if (= nil @user) 
    [login-section user]
    [logged-in-user user]))

(defn welcome [name]
  [:span  "Welcome " name])
