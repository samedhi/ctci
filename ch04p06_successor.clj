#!/usr/bin/env bb

(require '[clojure.test :as t])
(require '[clojure.zip :as zip])

;; [x nil nil] -> Node with value x and no children
;; [x y   nil] -> Node with value x and left child y
;; [x y   z]   -> Node with value x, left child y, right child z
(defn node->loc
  "wraps a binary vector tree [Value Left Right] into a zipper"
  [node]
  (zip/zipper #(some? (or (nth % 1) (nth % 2)))
              rest
              (fn [[v] [y z]] [v y z])
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

(defn mark-as-visited [loc]
  (zip/edit loc (fn [node] (assoc node 0 ::visited))))

(defn next
  "The next node in a binary search tree"
  [loc]
  (let [[x y z :as node] (zip/node loc)
        visited-loc (mark-as-visited loc)]
    (or
     (->> visited-loc
          zip/down
          zip/right
          (iterate zip/down)
          (take-while some?)
          (take-while #(-> % zip/node some?))
          (drop-while #(-> % zip/node first (= ::visited)))
          last)
     (->> visited-loc
          (iterate zip/up)
          (take-while some?)
          (drop-while #(-> % zip/node first (= ::visited)))
          first))))

(defn successors
  "Ordered sequence of values in vector BST"
  [loc]
  (->> loc
       (iterate next)
       (take-while some?)
       (map zip/node)
       (map first)))

(t/deftest test
  (t/are [next-seq start-value root] (= next-seq
                                        (-> root
                                            node->loc
                                            (find-node-with-value start-value)
                                            successors))
    [0] 0 [0 nil nil]

    [1] 1 [1 [0 nil nil] nil]

    [1 2] 1
    [1
     [0 nil nil]
     [2 nil nil]]

    [5 8] 5
    [5
     [2
      [0 nil nil]
      [3 nil nil]]
     [8 nil nil]]

    [2 3 5 8] 2
    [5
     [2
      [0 nil nil]
      [3 nil nil]]
     [8 nil nil]]

    [0 2 3 5 8] 0
    [5
     [2
      [0 nil nil]
      [3 nil nil]]
     [8 nil nil]]

    [3 2 5 8] 3
    [5
     [2
      [0 nil nil]
      [3 nil nil]]
     [8 nil nil]]

    [2 4 6 8 10 20] 2
    [8
     [4 [2 nil nil] [6 nil nil]]
     [10 nil [20 nil nil]]]))

(t/run-tests *ns*)
