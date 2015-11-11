(ns reagent-firebase-demo.firebase)

(defn- js->clj-keywords [x]
  (js->clj x :keywordize-keys true ))

(defn- fb-setter [fb-ref]
  (fn [key ref old-value new-value]
    (if (not= old-value new-value)
      (.set fb-ref (clj->js new-value)))))
      ; fb-reb.set(new-value);

(defn sync [synced-atom url]
  (let [firebase-ref (js/Firebase. url)]
    ; var firebase-ref = new Firebase(url);
    (.on firebase-ref "value" (fn [data]  (->> data .val js->clj-keywords (reset! synced-atom))))
    ; firebase-ref.on("value",callback);
    (add-watch synced-atom :fb-sync (fb-setter firebase-ref))))
