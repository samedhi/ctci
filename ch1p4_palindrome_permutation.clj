#!/usr/bin/env bb

;; O(N)
(defn palindrome-permutation [s1]
  (let [sorted-chars (-> s1
                         (str/replace #"[^a-zA-Z]" "")
                         str/lower-case
                         frequencies
                         vals
                         sort)]
    (or (and (->> sorted-chars first #{1 2})
             (->> sorted-chars rest (every? #{2})))
        false)))

(require '[clojure.test :as t])

(t/deftest is-unique-test
  (t/are [expected s] (= expected (palindrome-permutation s))
    true "a"
    true "aba"
    true "abA"
    true "abA "
    true "a bA !"
    false ""
    false "ab"
    false "abc"
    true "Tact Coa"))

(t/run-tests *ns*)
