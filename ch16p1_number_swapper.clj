#!/usr/bin/env bb

(require '[clojure.test :as t])

(defn number-swapper-arithmetic [[a b]]
  (let [a (- a b)
        b (+ a b)
        a (- b a)]
    [a b]))

(defn number-swapper-bitwise [[a b]]
  (let [a (bit-xor a b)
        b (bit-xor a b)
        a (bit-xor a b)]
    [a b]))

(defn swapem [a b]
  (bit-or (bit-and a (bit-not (bit-xor a b)))
          (bit-and b (bit-xor a b))))

(defn number-swapper [[a b]]
  [(swapem a b)
   (swapem b a)])

(t/deftest test
  (t/are [input expected] (= expected (number-swapper-arithmetic input))
    [14 423423] [423423 14]
    [423423 14] [14 423423]
    [23 7] [7 23])
  (t/are [input expected] (= expected (number-swapper-bitwise input))
    [14 423423] [423423 14]
    [423423 14] [14 423423]
    [23 7] [7 23])
  (t/are [input expected] (= expected (number-swapper input))
    [14 423423] [423423 14]
    [423423 14] [14 423423]
    [23 7] [7 23]))


;; XOR -> gives me the two bits that are different
;; a = (OR (AND a (NOT (XOR a b))) (AND b (XOR a b)))
;; b = (OR (AND b (NOT (XOR a b))) (AND a (XOR a b)))

(t/run-tests *ns*)

(println (java.util.Date.))
