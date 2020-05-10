#!/usr/bin/env bb

(defn string-compression [s]
  (let [compressed-s (->> s
                          (partition-by identity)
                          (map (fn [[x :as xs]] [x (count xs)]))
                          flatten
                          (apply str))]
    (if (<= (count s) (count compressed-s))
      s
      compressed-s)))

(require '[clojure.test :as t])

(t/deftest is-unique-test
  (t/are [expected s] (= expected (string-compression s))
    "a2b1c5a3" "aabcccccaaa"
    "" ""
    "a" "a"
    "abc" "abc"
    "a5b1c1" "aaaaabc"
    "a1b1c5" "abccccc"))

(t/run-tests *ns*)
