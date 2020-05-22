#!/usr/bin/env bb

(require '[clojure.test :as t])
(require '[clojure.walk :as walk])

(defn factorial [n]
  (loop [n n
         acc 1N]
    (if (<= n 1)
      acc
      (recur
       (dec n)
       (* acc n)))))

(defn trailing-zeros [n]
  (->> n
       str
       str/reverse
       (take-while #(= \0 %))
       count))

(defn factorial-zeros [n]
  (-> n factorial trailing-zeros))

(t/deftest test
  (t/are [n factorial-value] (= (trailing-zeros factorial-value)
                                (factorial-zeros n))
    0 1
    1 1
    2 2
    3 6
    4 24
    5 120
    6 720
    7 5040
    8 40320
    9 362880
    10 3628800
    11 39916800
    12 479001600
    13 6227020800
    14 87178291200
    15 1307674368000))

(t/run-tests *ns*)

#_(doseq [i (range 100)]
    (println i "->" (factorial-zeros i) "[" (factorial i) "]"))
