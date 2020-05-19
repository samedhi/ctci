#!/usr/bin/env bb

(require '[clojure.test :as t])

;; Group Anagrams: Write a method to sort an array of strings so that all
;; the anagrams are next to each other

(defn group-anagrams [anagrams]
  (->> anagrams
       (map (fn [anagram]
              [(frequencies anagram)
               anagram]))
       (group-by first)
       vals
       (apply concat)
       (map second)))

(defn group-results [anagram-sequence]
  (partition-by frequencies anagram-sequence))

(t/deftest test
  (t/are [anagrams expected] (= expected
                                (-> anagrams
                                    group-anagrams
                                    group-results
                                    set))
    [] #{}
    [""] #{[""]}
    ["a"] #{["a"]}
    ["ab" "ba"] #{["ab" "ba"]}
    ["ab" "cd" "ba"] #{["ab" "ba"] ["cd"]}
    ["ab" "cd" "ba" "bag"] #{["ab" "ba"] ["cd"] ["bag"]}))

(t/run-tests *ns*)

(println (java.util.Date.))
