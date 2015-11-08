(ns reagent-firebase-demo.core
  (:require [reagent.core :as reagent :refer [atom]]
			[reagent.session :as session]
			[secretary.core :as secretary :include-macros true]
			[accountant.core :as accountant]
			[reagent-firebase-demo.welcome :as welcome]
			[reagent-firebase-demo.header :as header]
			[reagent-firebase-demo.form :as form] 

			))

;; -------------------------
;; Views




;; State 

(defonce state (atom {:user nil
					  :todo-items []
					  :clicks 0
					  }))


(defn add-one []
  (swap! state update :clicks inc))

(defn remove-one []
  (swap! state update :clicks dec))

(defn number-box [number] 
  ^{:key number}  [:div " number is :  " number])


;; Util 

(defn log [text]
  (.log js/console text))


;; Firebase

(defonce fb-ref (js/Firebase. "https://fb-test321.firebaseio.com/"))

;(.set fb-ref (clj->js @state))

(defn js->clj-keywords [x]
  (js->clj x :keywordize-keys true ))

(.on fb-ref "value" #(log (.val %)))
(.on fb-ref "value" (fn [data]  (->> data .val js->clj-keywords (reset! state))))
(.on fb-ref "value" (fn [data]  (log @state)))

(defn fb-setter [fb-ref]
  (fn [key ref old-value new-value]
	(if (not= old-value new-value)
	  (.set fb-ref (clj->js new-value)))))


(add-watch state :fb-sync (fb-setter fb-ref))


(defonce todo-items (reagent/cursor state [:todo-items]))

(defn remove-todo-item [item]
   (swap! todo-items (partial remove #{item} )))

(defn todo-item [item]
 [:div.todo-item item
	[:a {:on-click (partial remove-todo-item item) } "remove"]
])

(defonce new-item (atom ""))


(defn add-new-item []
  (swap! todo-items conj @new-item)
  (reset! new-item "" ))

;; Home page
(defn home-page []
  [:div 
   [:h2 "Welcome to reagent-firebase-demo"]
   [:div (str @state)]
   [welcome/welcome (str "dear user " (:user @state))]
   [header/header (reagent/cursor state [ :user ])]
   [:div ( :clicks @state)]
   [:a {:on-click add-one } "add" ]
   [:span " | "]
   [:a {:on-click remove-one } "remove" ]
   (map todo-item @todo-items) 
   [form/text-input new-item {:on-enter add-new-item :on-esc (partial reset! new-item "")}]
   [:a {:on-click add-new-item} "ADD"]
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

