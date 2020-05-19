#!/usr/bin/env bb

;; Word Frequencies: Design a method to find the frequency of occurrences
;; of any given word in a book. What if we were running this algorithm
;; multiple times.

(require '[clojure.test :as t])

(defn rolling-frequencies [words]
  (reductions
   (fn [acc word]
     (update acc word (fnil inc 0)))
   {}
   words))

(defn word-frequencies [text]
  (if-not (str/blank? text)
    (-> (str/split text #"\s+")
        rolling-frequencies
        last)
    {}))

(t/deftest test
  (t/are [text expected] (= expected (word-frequencies text))
    nil {}
    "" {}
    "a" {"a" 1}
    "hello world" {"hello" 1 "world" 1}
    "hello world hello" {"hello" 2 "world" 1}))

(t/run-tests *ns*)

(println (java.util.Date.))
