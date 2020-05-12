#!/usr/bin/env bb

;; INPUT:  3 > 5 > 8 > 5 > 10 > 2 > 1 [partition=5]
;; OUTPUT: 3 > 1 > 2 > 10 > 5 > 5 > 8

(defn partition [ls x]
  (let [{ts true fs false} (group-by #(< % x) ls)]
    (concat ts fs)))

(require '[clojure.test :as t])

(t/deftest my-test
  (t/are [expected ls x] (= expected (partition ls x))
    [] [] 0
    [1] [1] 2
    [1 2] [1 2] -1
    [1 2 3] [1 2 3] 2
    [ 3 2 1 5 8 5 10] [3 5 8 5 10 2 1] 5))

(t/run-tests *ns*)
