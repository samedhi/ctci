#!/usr/bin/env bb

;; Minimal Tree: Given a sorted (increasing order) array with unique integer
;; elements, write an algorithm to create a binary search tree with
;; minimal height.

(require '[clojure.test :as t])

;; Binary Search Tree (BST): A binary search tree is a binary tree in which every
;; node fits a specific ordering property:
;;   all left descendents <= n <= all right descendents

(defn midpoint [vs]
  (-> vs count (quot 2)))

(defn separate-at-i [vs i]
  [(subvec vs 0 i)
   (nth vs i)
   (subvec vs (inc i))])

(defn minimal-tree
  "Convert sequence of integers to BST"
  [vs]
  (case (count vs)
    0 nil
    1 {:value (nth vs 0)}
    2 {:value (nth vs 1)
       :children [(minimal-tree (pop vs))]}
    (let [i (midpoint vs)
          [left v right] (separate-at-i vs i)]
      {:value v
       :children [(minimal-tree left)
                  (minimal-tree right)]})))

(defn nested-vector->tree [vs]
  (when-not (empty? vs)
    (let [[v left right] vs]
      (merge
       {:value v}
       (when (or left right)
         {:children (->> [(nested-vector->tree left)
                          (nested-vector->tree right)]
                         (remove nil?)
                         vec)})))))

(t/deftest test
  (t/are [vs expected] (= (nested-vector->tree expected) (minimal-tree vs))
    [] nil
    [1] [1]
    [1 2] [2 [1]]
    [1 2 3] [2 [1]
               [3]]
    [1 2 3 4] [3 [2 [1]]
                    [4]]
    [1 2 3 4 5] [3 [2 [1]]
                      [5 [4]]]
    [1 2 3 4 5 6] [4 [2 [1] [3]]
                     [6 [5]]]
    [1 2 3 4 5 6 7] [4 [2 [1] [3]]
                       [6 [5] [7]]]))

(t/run-tests *ns*)

(println (java.util.Date.))
