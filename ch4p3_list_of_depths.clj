#!/usr/bin/env bb

(require '[clojure.test :as t])
(require '[clojure.zip :as zip])
(require '[clojure.walk :as walk])

;; List of Depths: Given a binary tree, design an algorithm which
;; creates a linked list of all the nodes at each depth.

(defn node->zipper [node]
  (zip/zipper map?
              :children
              (fn [m children] (assoc m :children children))
              node))

(defn all-locs [z]
  (->> (iterate zip/next z)
       ;; (take-while-plus-1 (complement zip/end?))
       (take-while (complement zip/end?))
       ))

(defn list-of-depths [node]
  (when (map? node)
    (->> node
         node->zipper
         all-locs
         (group-by #(-> % zip/path count))
         (map (fn [[k vs]] [k (mapv zip/node vs)]))
         (into (sorted-map))
         vals)))

(defn take-while-plus-1 [predicate xs]
  (let [[take-xs drop-xs] (split-with predicate xs)]
    (concat take-xs (when-not (empty? drop-xs)
                      [(first drop-xs)]))))

(t/deftest test
  (t/are [pred xs expected] (= expected (take-while-plus-1 pred xs))
    even? [] []
    even? [1] [1]
    even? [2] [2]
    even? [2 1] [2 1])
  (t/are [node expected] (= expected (map #(map :id %) (list-of-depths node)))
    nil []
    {:id 1} [[1]]
    {:id 1 :children [{:id 2}]} [[1] [2]]
    {:id 1 :children [{:id 2} {:id 3}]} [[1] [2 3]]
    {:id 1 :children [{:id 2} {:id 3}]} [[1] [2 3]]
    {:id 1 :children [{:id 2} {:id 3}]} [[1] [2 3]]
    {:id 1 :children [{:id 2 :children [{:id 4}]}
                      {:id 3}]}
    [[1] [2 3] [4]]

    {:id 1 :children [{:id 2}
                      {:id 3 :children [{:id 4}]}]}
    [[1] [2 3] [4]]

    {:id 1 :children [{:id 2
                       :children [{:id 3
                                   :children [{:id 4}]}]}]}
    [[1] [2] [3] [4]]))

(t/run-tests *ns*)

(println (java.util.Date.))
