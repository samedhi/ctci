#!/usr/bin/env bb

(require '[clojure.test :as t])

(defn build-bit-vector []
  (into [] (take 32001 (repeat false))))

(defn find-duplicates-reduction [xs]
  (reductions
   (fn [acc vs]
     (let [{:keys [duplicate-indicies used-numbers]} acc
           [i n] vs]
       (if (nth used-numbers n)
         (update acc :duplicate-indicies conj i)
         (update acc :used-numbers assoc n true))))
   {:duplicate-indicies []
    :used-numbers (build-bit-vector)}
   (map-indexed vector xs)))

(defn find-duplicates [xs]
  (->> xs
      find-duplicates-reduction
      last
      :duplicate-indicies
      (map xs)))

(t/deftest test
  (t/are [input expected] (= expected (find-duplicates input))
    [] []
    [1] []
    [1 1] [1]
    [1 1 1] [1 1]
    [1 1 1 2 2 1 2 3] [1 1 2 1 2]))

(t/run-tests *ns*)
