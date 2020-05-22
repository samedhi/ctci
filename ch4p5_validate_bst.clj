#!/usr/bin/env bb

(require '[clojure.test :as t])
(require '[clojure.walk :as walk])

;; Validate BST - Implement a function to check if a binary tree is a
;; binary search tree.

(defn bst-node? [n]
  (let [{:keys [children value]} n
        [left right] children
        [left-value right-value] (map :value [left right])]
    (and (if left  (<= left-value value) true)
         (if right (< value right-value) true))))

(defn validate-bst [node]
  (let [a (atom [])
        fx (fn [node] (swap! a conj node) node)]
    (walk/prewalk fx node)
    (->> @a
         (filter map?)
         (remove bst-node?)
         empty?)))

;; It should only return a magic-index, not all of them. Not going to
;; bother with that.
(t/deftest test
  (t/are [tree expected] (= expected (validate-bst tree))
    {:value 1} true
    {:value 1 :children [{:value 2}]} false
    {:value 2 :children [{:value 1}]} true
    {:value 1 :children [{:value 2} {:value 3}]} false
    {:value 2 :children [{:value 1} {:value 3}]} true
    {:value 1 :children [{:value 1}]} true
    {:value 1 :children [{:value 1} {:value 3}]} true))

(t/run-tests *ns*)

(println (java.util.Date.))
