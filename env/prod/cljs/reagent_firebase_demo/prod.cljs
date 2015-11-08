(ns reagent-firebase-demo.prod
  (:require [reagent-firebase-demo.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
