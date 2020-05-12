#!/usr/bin/env bb

(defn delete-middle-node [ls find]
  (if-let [i (->> (map-indexed vector ls)
                    (filter (fn [[_ v]] (= v find)))
                    ffirst)]
    (let [[ts ds](split-at i ls)]
      (concat ts (rest ds)))
    ls))

(require '[clojure.test :as t])

(t/deftest my-test
  (t/are [expected ls v] (= expected (delete-middle-node ls v))
    [] [] nil
    [1] [1] nil
    [1 2] [1 2] nil
    [1 2] [1 2 3] 3
    [1 1] [1 2 1] 2
    [1 2 1] [1 2 2 1] 2))

(t/run-tests *ns*)
