(ns reagent-firebase-demo.welcome
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]))

(defn welcome [name ]
  [:h1 "Welcome " name])
