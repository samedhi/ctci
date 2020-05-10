#!/usr/bin/env bb

;; O(N)
(defn check-permutations [s1 s2]
  (= (frequencies s1) (frequencies s2)))

(require '[clojure.test :as t])

(t/deftest is-unique-test
  (t/are [expected s1 s2] (= expected (check-permutations s1 s2))
    true "" ""
    true "a" "a"
    true "abc" "abc"
    false "a" "b"
    false "a" ""))

(t/run-tests *ns*)
