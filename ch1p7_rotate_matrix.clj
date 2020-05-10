#!/usr/bin/env bb

(defn rotate-matrix [matrix]
  (->> (apply map vector matrix)
       (mapv reverse)))

(require '[clojure.test :as t])

(t/deftest is-unique-test
  (t/are [expected matrix] (= expected (rotate-matrix matrix))
    [[1]]
    [[1]]

    [[3 1]
     [4 2]]
    [[1 2]
     [3 4]]

    [[7 4 1]
     [8 5 2]
     [9 6 3]]
    [[1 2 3]
     [4 5 6]
     [7 8 9]]

    [[13 9  5 1]
     [14 10 6 2]
     [15 11 7 3]
     [16 12 8 4]]
    [[1   2  3  4]
     [5   6  7  8]
     [9  10 11 12]
     [13 14 15 16]]
    ))

(t/run-tests *ns*)
