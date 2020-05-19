#!/usr/bin/env bb

(require '[clojure.test :as t])

(def robot-in-a-grid
  (memoize
   (fn [grid row column]     (let [row-count (-> grid count)
           column-count (-> grid first count)
           open? (get-in grid [row column])]
       (cond
         (<= row-count row)
         false

         (<= column-count column)
         false

         (false? open?)
         false

         (and (-> row-count dec (= row))
              (-> column-count dec (= column)))
         []

         :default
         (or (when-let [go-down (robot-in-a-grid grid (inc row) column)]
               (cons :down go-down))

             (when-let [go-right (robot-in-a-grid grid row (inc column))]
               (cons :right go-right))

             false))))))

(t/deftest test
  (t/are [vs expected] (= expected (robot-in-a-grid vs 0 0))
    [[true]] []
    [[false]] false
    [[true true]
     [true true]] [:down :right]
    [[true true]
     [true false]] false
    [[false true]
     [true true]] false
    [[true false]
     [true true]] [:down :right]
    [[true true]
     [false true]] [:right :down]))

(t/run-tests *ns*)

(println (java.util.Date.))
