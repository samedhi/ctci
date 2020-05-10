#!/usr/bin/env bb

(defn one-away [s1 s2]
  (let [alphabet (set/union (set s1) (set s2))
        shared-map (zipmap alphabet (repeat 0))
        f1 (merge shared-map (frequencies s1))
        f2 (merge shared-map (frequencies s2))
        deltas (->> (merge-with - f1 f2)
                    vals
                    (remove zero?))]
    (and (-> deltas count (<= 2))
         (->> deltas (apply +) (contains? #{-1 0 1})))))

(require '[clojure.test :as t])

(t/deftest is-unique-test
  (t/are [expected s1 s2] (= expected (one-away s1 s2))
    true "" ""
    true "identity" "identity"
    true "pale" "ple"
    true "pales" "pale"
    true "pale" "bale"
    false "pales" "bake"))

(t/run-tests *ns*)
