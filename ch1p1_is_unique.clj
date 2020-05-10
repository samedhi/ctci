#!/usr/bin/env bb

;; O(N)
(defn is-unique [s]
  (= (count (set s)) (count s)))

;; O(N(logN))
(defn is-unique-no-new-data-structures [s]
  (if (str/blank? s)
    true
    (apply distinct? (sort s))))

(require '[clojure.test :as t])

(t/deftest is-unique-test
  (t/are [expected s] (= expected (is-unique s) (is-unique-no-new-data-structures s))
    true ""
    true "ab"
    true "afyz"
    false "aa"
    false "aba"))

(t/run-tests *ns*)
