#!/usr/bin/env bb

(defn string-rotation [s1 s2]
  (as-> (cycle s2) $
    (take (* 2 (count s1)) $)
    (str/join $)
    (str/includes? $ s1)))

(require '[clojure.test :as t])

(t/deftest my-test
  (t/are [expected s1 s2] (= expected (string-rotation s1 s2))
    true "" ""
    true "waterbottle" "erbottlewat"
    false "yes" "no"))

(t/run-tests *ns*)
