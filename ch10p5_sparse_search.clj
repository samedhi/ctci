#!/usr/bin/env bb

(require '[clojure.test :as t])
(require '[clojure.walk :as walk])

;; 

;; Linear
#_(defn sparse-search [xs s]
    (->> (map-indexed vector xs)
         (filter #(-> % second (= s)))
         ffirst))

(declare sparse-search-left-of-i)

(declare sparse-search-right-of-i)

(defn sparse-search [xs s]
  (when-not (empty? xs)
    (let [midpoint (quot (count xs) 2)
          midpoint-value (nth xs midpoint)
          compare-value (compare s midpoint-value)]
      (cond
        (= midpoint-value s)
        midpoint

        (str/blank? midpoint-value)
        (or (sparse-search-left-of-i xs s midpoint)
            (sparse-search-right-of-i xs s midpoint))

        (neg? compare-value)
        (sparse-search-left-of-i xs s midpoint)

        (pos? compare-value)
        (sparse-search-right-of-i xs s midpoint)

        :else (throw (ex-info "WTF" {}))))))

(defn sparse-search-left-of-i [xs s i]
  (sparse-search (subvec xs 0 i) s))

(defn sparse-search-right-of-i [xs s i]
  (when-let [j (sparse-search (subvec xs (inc i)) s)]
    (+ (inc i) j)))

(t/deftest test
  (t/are [xs s i] (= i (sparse-search xs s))
    [] "dog" nil
    ["cat"] "dog" nil
    ["cat"] "cat" 0
    ["" "cat"] "cat" 1
    ["cat" ""] "cat" 0
    ["" "cat" ""] "cat" 1
    ["cat" "" "dog" "lemur" ""] "dog" 2
    (into ["cat"] (take 1000 (repeat ""))) "cat" 0
    (conj (vec (take 1000 (repeat ""))) "cat") "cat" 1000
    ["" "" "cat"] "cat" 2))

(t/run-tests *ns*)

(println (java.util.Date.))
