#!/usr/bin/env bb

(require '[clojure.test :as t])

(defn fx-name [xs]
  0)

(t/deftest test
  (t/are [xs expected] (= expected (fx-name input))
    [] nil))

(t/run-tests *ns*)

