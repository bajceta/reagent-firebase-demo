(ns reagent-firebase-demo.form
  (:require [reagent.core :as reagent :refer [atom]]
			[reagent.session :as session]
			[secretary.core :as secretary :include-macros true]
			[accountant.core :as accountant]))

(defn text-input [value-atom attributes]
  (let [{on-enter :on-enter on-esc :on-esc} attributes] 
	[:input (merge attributes  {:type "text"
								:value @value-atom
								:on-change #(reset! value-atom (-> % .-target .-value)) 
								:on-key-down #(case (.-which %)
												  13 (when on-enter (on-enter))
												  27 (when on-esc (on-esc))
												  nil)
								}) ] )  )
