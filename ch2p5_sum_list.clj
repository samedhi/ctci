#!/usr/bin/env bb

;; INPUT: (7 1 6) + (5 9 2). That is 617 + 295
;; OUTPUT: (2 1 9) eg. 912

(defn sum-lists [n1 n2]
  (let [[c1 c2] (map count [n1 n2])
        c3 (max c1 c2)
        [n3 n4] (map #(take c3 (concat % (repeat 0))) [n1 n2])
        digits (map + n3 n4)
        vec-digits (partition 2 1 [0] (cons 0 digits))]
    (reverse (drop-while zero? (reverse (map (fn [[i j]] (+ (quot i 10) (rem j 10))) vec-digits))))))

(require '[clojure.test :as t])

(t/deftest my-test
  (t/are [expected ls x] (= expected (sum-lists ls x))
    [] [] []
    [] [] [0]
    [] [0] []
    [0 1] [5] [5]
    [2 6] [5 5] [7]
    [5 4 3 2 1] [0 0 3 2 1] [5 4]
    [5 4 3 2 1] [5 4] [0 0 3 2 1]
    [2 1 9] [7 1 6] [5 9 2]))

(t/run-tests *ns*)
