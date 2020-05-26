#!/usr/bin/env bb

(require '[clojure.test :as t])

(defn weave
  "weave xs and ys in every way while maintaining order of each"
  ([xs ys]
   (weave xs ys []))
  ([xs ys zs]
   (cond
     (and (empty? xs) (empty? ys)) [zs]
     (empty? xs) [(into zs ys)]
     (empty? ys) [(into zs xs)]
     :else
     (let [[x & xrest] xs
           [y & yrest] ys]
       (concat
        (weave xrest ys (conj zs x))
        (weave xs yrest (conj zs y)))))))

(defn bst-sequence
  "sequence of arrays that could have formed this BST node"
  [node]
  {:post [(seq? %)
          (every? (fn [xs] (every? number? xs)) %)]}
  (let [[v l r] node]
    (map
     #(cons v %)
     (cond
       (and (nil? l) (nil? r)) [nil]
       (nil? l) (bst-sequence r)
       (nil? r) (bst-sequence l)
       :else
       (apply
        concat
        (for [l-seq (bst-sequence l)
              r-seq (bst-sequence r)]
          (weave l-seq r-seq)))))))

(t/deftest test
  (t/are [xs ys expected] (= expected (weave xs ys))
    [] [] [[]]
    [1] [] [[1]]
    [] [1] [[1]]
    [1] [2] [[1 2] [2 1]]
    [1 2] [3 4] [[1 2 3 4] [1 3 2 4] [1 3 4 2] [3 1 2 4] [3 1 4 2] [3 4 1 2]])
  (t/are [root expected] (= expected (->> root bst-sequence set))
    [2] #{[2]}
    [2 [1]] #{[2 1]}
    [2 [1] [3]] #{[2 1 3] [2 3 1]}
    [2 nil [3]] #{[2 3]}
    [1 nil [2 nil [3]]] #{[1 2 3]}
    [3 [2 [1]]] #{[3 2 1]}
    [3 [2 [1]] [4]] #{[3 4 2 1] [3 2 1 4] [3 2 4 1]}
    [3 [2 [1]] [4 nil [5]]] #{[3 4 5 2 1] [3 2 1 4 5] [3 4 2 1 5] [3 2 4 1 5] [3 2 4 5 1] [3 4 2 5 1]}))

(t/run-tests *ns*)
