#!/usr/bin/env bb

(require '[clojure.test :as t])

;; Triple Step: A child is running up a staircase with n steps and can hop either 1 step, 2 steps,
;; or 3 steps at a time. Implement a method to count how many ways the child can run up the stairs.


;; A sequence of steps for each step count
(def step-count->steps
  {0 []
   1 [[1]]
   2 [[1 1] [2]]
   3 [[1 1 1] [1 2] [2 1] [3]]})

(defn insert [vs i v]
  (reduce conj (subvec vs 0 i) (cons v (subvec vs i))))

(defn around-insertion [vs v]
  (->> vs
       count
       inc
       (range 0)
       (map #(insert vs % v))))

(def triple-step
  (memoize
   (fn [n]
     (println :triple-step n)
     (if (contains? step-count->steps n)
       (get step-count->steps n)
       (reduce
        into
        #{}
        (for [i [1 2 3]]
          (->> (- n i)
               triple-step
               (mapcat #(around-insertion % i)))))))))

(t/deftest test
  (t/are [input _ output] (= output (count (triple-step input)))
    0 -> 0 ;; ()
    1 -> 1 ;; (1)
    2 -> 2 ;; (1 1) (2)
    3 -> 4 ;; (1 1 1) (1 2) (2 1) (3)
    4 -> 7 ;; (1 1 1 1) (1 2 1) (1 1 2) (2 1 1) (2 2) (1 3) (3 1)
    5 -> 13
    6 -> 24
    7 -> 44
    8 -> 81
    9 -> 149

















    10 -> 274))

(t/run-tests *ns*)

(println (java.util.Date.))
