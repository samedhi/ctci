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

(defn take-while-inclusive [pred xs]
  (let [[take-xs drop-xs] (split-with pred xs)]
    (concat take-xs (take 1 drop-xs))))

(defn find-node-with-value
  "Uses zip/next to find nodes with 'value in 'loc"
  [loc value]
  (->> loc
       (iterate zip/next)
       (take-while-inclusive (complement zip/end?))
       (filter #(-> % zip/node first (= value)))
       first))

(defn find-common-ancestor'
  [loc1 loc2]
  ;; {:pre [(map #(-> % zip/path count) [loc1 loc2])]}
  (if (apply = (map #(-> % zip/node first) [loc1 loc2]))
    (zip/node loc1)
    (recur (zip/up loc1) (zip/up loc2))))

(defn find-common-ancestor [loc1 loc2]
  (let [[c1 c2] (map #(-> % zip/path count) [loc1 loc2])]
    (apply
     find-common-ancestor'
     (cond
       (< c1 c2)
       [loc1 (first (drop (- c2 c1) (iterate zip/up loc2)))]

       (< c2 c1)
       [(first (drop (- c1 c2) (iterate zip/up loc1))) loc2]

       (= c1 c2)
       [loc1 loc2]))))

(t/deftest test
  (t/are [root-node v1 v2 _ expected] (= expected
                                         (find-common-ancestor
                                          (-> root-node node->loc (find-node-with-value v1))
                                          (-> root-node node->loc (find-node-with-value v2))))
    [1] 1 1 -> [1]
    [1 [2]] 1 2 -> [1 [2]]
    [1 [2]] 2 1 -> [1 [2]]
    [1 [2] [3]] 2 3 -> [1 [2] [3]]
    [1 [2 [3]]] 2 3 -> [2 [3]]
    [1 [2 [4] [5 [6]]] [3]] 4 6 -> [2 [4] [5 [6]]]
    [1 [2 [4] [5]] [3]] 4 5 -> [2 [4] [5]]))

(t/run-tests *ns*)

