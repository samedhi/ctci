#!/usr/bin/env bb

(require '[clojure.test :as t])
(require '[clojure.walk :as walk])

(defn additive-multiply [x y]
  (let [[x y] [(min x y) (max x y)]]
    (apply
     +
     (for [i (range x)]
       y))))

(defn bits
  "seq of bits with least significant bit first"
  [n]
  (loop [n n
         acc []]
    (if (zero? n)
      (map pos? acc)
      (let [bit (bit-and 1 n)]
        (recur
         (bit-shift-right n 1)
         (conj acc bit))))))

(defn sequential-multiply [x y]
  (let [[x y] [(min x y) (max x y)]
        x-bits (bits x)]
    (:result
     (reduce
      (fn [{:keys [y result] :as m} bit]
        {:result (if bit
                   (+ result y)
                   result)
         :y (bit-shift-left y 1)})
      {:y y :result 0}
      x-bits))))

(t/deftest test
  (t/are [n expected] (= expected (bits n))
    1 [true]
    2 [false true]
    3 [true true]
    11 [true true false true]
    15 [true true true true])
  (t/are [x y z] (= z (sequential-multiply x y))
    1 1 1
    1 2 2
    2 1 2
    3 3 9
    3 7 21 ;; (3,7) = (2,7)+(1,7) -> (1,14)+(1,7) -> (1,21)
    4 15 60 ;; (4,15) -> (2,30) -> (1,60)
    4 17 68 ;; (4,17) -> (2,34) -> (1,68)
    11 13 143
    1024 1024 1048576))

(t/run-tests *ns*)

(println (java.util.Date.))
