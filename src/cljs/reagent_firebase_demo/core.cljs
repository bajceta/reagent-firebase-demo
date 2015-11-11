(ns reagent-firebase-demo.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]
            [reagent-firebase-demo.ui-components :as ui]
            [reagent-firebase-demo.form :as form] 
            [reagent-firebase-demo.firebase :as firebase] ))
;; State 

(defonce state (atom {:user nil
                      :todo-items []
                      :clicks 0 }))

(firebase/sync state "https://fb-test321.firebaseio.com/")

;; managing the clicks
(defn add-one []
  (swap! state update :clicks inc))

(defn remove-one []
  (swap! state update :clicks dec))

(defn number-box [number] 
  ^{:key number}  [:div " number is :  " number])

;; Util 

(defn log [text]
  (.log js/console text))

;; To-do list 
(defonce todo-items (reagent/cursor state [:todo-items]))

(defn remove-todo-item [item]
  (swap! todo-items (partial remove #{item} )))

(defn todo-item [item]
  [:div.todo-item item
   [:span " click here to remove "]
   [:div.right
   [:a {:on-click (partial remove-todo-item item) } "remove"]]])

(defonce new-item (atom ""))

(defn add-new-item []
  (swap! todo-items conj @new-item)
  (reset! new-item "" ))

;; Home page
(defn home-page []
  [:div 
   [:h2 "Our app state"]
   [:div (str @state)]
   [:h2 "a simple component: "]
   [ui/welcome (str "dear user " (:user @state))]
   [:h2 "another one: "]
   [ui/header (reagent/cursor state [ :user ])]
   [:div 
    [:h2 "Clicks are here below : " ]
    [:div ( :clicks @state)]
    [:a {:on-click add-one } "add" ]
    [:span " | "]
    [:a {:on-click remove-one } "remove" ]]
   [:div 
    [:h2 "Todo items:"]
    (map todo-item @todo-items) 
    [form/text-input new-item {:on-enter add-new-item :on-esc (partial reset! new-item "")}]
    [:a {:on-click add-new-item} "ADD"]]
   ])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))

