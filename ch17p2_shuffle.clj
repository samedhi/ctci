#!/usr/bin/env bb

(require '[clojure.test :as t])

(defn shuffle' [{:keys [from to]}]
  (let [i (-> from count rand-int)
        card (nth from i)]
    {:from (into (subvec from 0 i) (subvec from (inc i)))
     :to (conj to card)}))

(defn shuffle [deck]
  ;; (shuffle deck) ;; <- This is the real answer
  (when-not (empty? deck)
    (->> (iterate shuffle' {:from (vec deck) :to []})
         (drop-while #(-> % :from empty? not))
         first
         :to)))

(t/deftest test
  (t/are [deck] (= (set deck) (set (shuffle deck)))
    []
    [1]
    [1 2 3]))

(t/run-tests *ns*)

(println (java.util.Date.))
