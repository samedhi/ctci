#!/usr/bin/env bb

(defn contains-zero? [xs]
  (some zero? xs))

(defn zero-all [xs]
  (mapv (fn [_] 0) xs))

(defn zerofy-row [xs]
  (if (contains-zero? xs)
    (zero-all xs)
    xs))

(defn zero-matrix [matrix]
  (let [zeroed-rows (map zerofy-row matrix)
        zeroed-cols (->> (apply map vector matrix)
                         (map zerofy-row)
                         (apply map vector))]
    (mapv (fn [r1 r2] (map min r1 r2)) zeroed-rows zeroed-cols)))

(require '[clojure.test :as t])

(t/deftest my-test
  (t/are [expected matrix] (= expected (zero-matrix matrix))
    [[1]]
    [[1]]

    [[1 2]
     [3 4]]
    [[1 2]
     [3 4]]

    [[1 2 3]
     [4 5 6]
     [7 8 9]]
    [[1 2 3]
     [4 5 6]
     [7 8 9]]

    [[1 2 0]
     [4 5 0]
     [0 0 0]]
    [[1 2 3]
     [4 5 6]
     [7 8 0]]

    [[0 0 0]
     [4 0 6]
     [7 0 9]]
    [[1 0 3]
     [4 5 6]
     [7 8 9]]

    [[1 0 0]
     [0 0 0]
     [0 0 0]]
    [[1 2 3]
     [4 0 6]
     [7 8 0]]))

(t/run-tests *ns*)
