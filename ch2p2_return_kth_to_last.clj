#!/usr/bin/env bb

(defn kth-to-last [i ls]
  (drop i ls))

(require '[clojure.test :as t])

(t/deftest my-test
  (t/are [expected i ls] (= expected (kth-to-last i ls))
    [] 0 []
    [] 5 [1 2]
    [1] 0 [1]
    [2 3] 1 [1 2 3]
    [1] 2 [1 2 1]
    [2 2 1 2 3 3 3 3 4 4] 5 [1 1 1 1 1 2 2 1 2 3 3 3 3 4 4]))

(t/run-tests *ns*)
