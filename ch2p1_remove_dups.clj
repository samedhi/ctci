#!/usr/bin/env bb

(defn remove-dups [ls]
  (:ls
   (reduce
    (fn [{:keys [ls seen] :as m} v]
      (if (contains? seen v)
        m
        (-> m
            (update :seen conj v)
            (update :ls conj v))))
    {:ls [] :seen #{}}
    ls)))

(require '[clojure.test :as t])

(t/deftest my-test
  (t/are [expected ls] (= expected (remove-dups ls))
    [] []
    [1] [1]
    [1 2] [1 1 2]
    [1 2] [1 2 1]
    [1 2 3 4] [1 1 1 1 1 2 2 1 2 3 3 3 3 4 4]))

(t/run-tests *ns*)
