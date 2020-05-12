#!/usr/bin/env bb

(defn palindrome? [ls]
  (let [c (count ls)
        n (quot c 2)
        [ts ds] (split-at n ls)
        ds (if (odd? c) (rest ds) ds)]
    (= ts (reverse ds))))

(require '[clojure.test :as t])

(t/deftest my-test
  (t/are [expected ls] (= expected (palindrome? ls))
    true []
    true [0]
    true [0 0]
    true [0 1 0]
    true [1 0 1]
    true [0 1 1 0]
    true [0 1 2 1 0]
    false [1 0 0]
    false [1 0 nil]
    false [1 2 3 4]))

(t/run-tests *ns*)
