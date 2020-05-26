#!/usr/bin/env bb

(require '[clojure.test :as t])

(defn binary-search-comparator [vs comparator]
  (when-not (empty? vs)
    (let [midpoint (quot (count vs) 2)
          mid-v (nth vs midpoint)
          cmp (comparator mid-v)]
      (cond
        (neg? cmp) (binary-search-comparator (subvec vs 0 midpoint) comparator)
        (zero? cmp) midpoint
        (pos? cmp) (+ (inc midpoint)
                      (binary-search-comparator (subvec vs (inc midpoint)) comparator))))))

(defn binary-search 
  "Find index of v in vs where vs is ascending"
  [vs v]
  (binary-search-comparator vs #(compare v %)))

(defn binary-search-matrix 
  "Find the index of the row within range of vss, where vs in vss are ascending"
  [vss v]
  (when-not (or (empty? vss)
                (-> vss first empty?))
    (binary-search-comparator
     vss
     (fn [row]
       (let [f (first row)
             l (peek row)]
         (cond
           (< v f) -1
           (<= f v l) 0
           (< l v) 1))))))

(defn sorted-matrix-search
  "find index of 'v in 'matrix"
  [matrix v]
  (when-let [row-index (binary-search-matrix matrix v)]
    (when-let [col-index (binary-search (nth matrix row-index) v)]
      [row-index col-index])))

(t/deftest test
  (t/are [vs v i] (= i (binary-search vs v))
    [] 0 nil
    [0] 0 0)
  (let [test-vs (vec (range 100))]
    (doseq [i (range 100)]
      (t/is (= i (binary-search test-vs i)))))
  (t/are [vss v i] (= i (binary-search-matrix vss v))
    [] 0 nil
    [[1]] 0 nil
    [[1]] 1 0
    [[1 2 3]
     [4 5 6]
     [7 8 9]] 1 0
    [[1 2 3]
     [4 5 6]
     [7 8 9]] 6 1
    [[1 2 3]
     [4 5 6]
     [7 8 9]] 7 2
    [[1 2 3]
     [4 5 6]
     [7 8 9]] 0 nil)
  (t/are [matrix v expected] (= expected (sorted-matrix-search matrix v))
    [] 0 nil
    [[1]] 0 nil
    [[1]] 1 [0 0]
    [[1 2 3]
     [4 5 6]
     [7 8 9]] 1 [0 0]
    [[1 2 3]
     [4 5 6]
     [7 8 9]] 6 [1 2]
    [[1 2 3]
     [4 5 6]
     [7 8 9]] 7 [2 0]
    [[1 2 3]
     [4 5 6]
     [7 8 9]] 0 nil
    [[1 2 3 4]
     [5 6 7 8]
     [9 10 11 12]] 8 [1 3]))

(t/run-tests *ns*)

