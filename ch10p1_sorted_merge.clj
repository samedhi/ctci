#!/usr/bin/env bb

(require '[clojure.test :as t])

;; Sorted Merge: You are given two sorted arrays, A and B, where A has a large
;; enough buffer at the end to hold B. Write a method to merge B into A in
;; sorted order.

(defn sorted-merge [ax bx]
  (loop [[a :as ax] ax
         [b :as bx] bx
         acc []]
    (cond
      (and (empty? ax) (empty? bx)) acc
      (empty? ax) (reduce conj acc bx)
      (empty? bx) (reduce conj acc ax)
      :else (let [a-less-than-b (< a b)]
              (recur
               (if a-less-than-b (rest ax) ax)
               (if a-less-than-b bx (rest bx))
               (conj acc (if a-less-than-b a b)))))))

(defn sorted-merge' [{:keys [acc ax bx]}]
  (cond
    (and (empty? ax) (empty? bx)) acc
    (empty? ax) (reduce conj acc bx)
    (empty? bx) (reduce conj acc ax)
    :else (let [[a & ax-rest] ax
                [b & bx-rest] bx]
            (if (< a b)
              {:acc (conj acc a) :ax ax-rest :bx bx}
              {:acc (conj acc b) :ax ax :bx bx-rest}))))

(defn sorted-merge [ax bx]
  (->> (iterate sorted-merge' {:acc [] :ax ax :bx bx})
       (drop-while map?)
       first))

(t/deftest test
  (t/are [ax bx _ output] (= output (sorted-merge ax bx))
    [] [] -> []
    [1] [] -> [1]
    [] [1] -> [1]
    [1 2 3] [4 5] -> [1 2 3 4 5]
    [1 3 5] [2 4 6] -> [1 2 3 4 5 6]))

(t/run-tests *ns*)

(println (java.util.Date.))
