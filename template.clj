#!/usr/bin/env bb

(require '[clojure.test :as t])

(defn fx-name [xs]
  )

(t/deftest test
  (t/are [input expected] (= expected (fx-name input))
    [] nil))

(t/run-tests *ns*)

