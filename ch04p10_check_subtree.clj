#!/usr/bin/env bb

(require '[clojure.test :as t])
(require '[clojure.zip :as zip])

(defn node->loc
  "wraps a binary vector tree [Value Left Right] into a zipper"
  [node]
  (zip/zipper #(->> % rest (remove nil?) count pos?)
              rest
              (fn [[v] [y z]] (cond
                                (and (nil? y) (nil? z)) [v]
                                (and y (nil? z)) [v y]
                                :else [v y z]))
              node))


(defn check-subtree [t1 t2]
  (->> t1
       node->loc
       (iterate zip/next)
       (take-while (complement zip/end?))
       (filter #(-> % zip/node (= t2)))
       empty?
       not))

(defn height
  "return the total height of a tree (min 1)"
  [loc]
  (->> loc
       (iterate zip/next)
       (take-while (complement zip/end?))
       (map #(-> % zip/path count inc))
       (apply max)))

(t/deftest test
  (t/are [t expected] (= expected (height (node->loc t)))
    [0] 1
    [0 [1] [2]] 2
    [0 [1 nil [2]]] 3)
  (t/are [t1 t2 expected] (= expected (check-subtree t1 t2))
    [0] [1] false
    [1] [1] true
    [1 [2]] [2] true
    [1 nil [2]] [2] true
    [1 [2] [3 nil [4 [5]]]] [4 [5]] true))

(t/run-tests *ns*)
