#!/usr/bin/env bb

(require '[clojure.test :as t])

(defn xs->index-map [xs]
  (into
   {}
   (map-indexed vector xs)))

(defn m+xs->mapped-xs [m xs]
  (mapv m xs))

(defn seq->string [xs]
  (str/join xs))

(def permute-rec
  (memoize
   (fn [acc vs]
     ;; (println (str/join (take (count acc) (repeat "  "))) :permute [acc vs])
     (if (empty? vs)
       [acc]
       (apply
        concat
        (for [[i v] (map-indexed vector vs)
              :let [new-vs (into (subvec vs 0 i) (subvec vs (inc i)))]]
          (permute-rec (conj acc v) new-vs)))))))

(defn permute [vs]
  (permute-rec [] vs))

(defn permutations-with-dups [s]
  (->> s
       vec
       permute
       (map str/join)
       distinct))

(t/deftest test
  (t/are [input expected] (= expected (permute input))
    [] [[]]
    [1] [[1]]
    [1 2] [[1 2] [2 1]]
    [1 2 3] [[1 2 3] [1 3 2] [2 1 3] [2 3 1] [3 1 2] [3 2 1]])
  (t/are [input expected] (= expected (permutations-with-dups input))
    "" [""]
    "11" ["11"]
    "112" ["112" "121" "211"]))

(t/run-tests *ns*)
