#!/usr/bin/env bb

(require '[clojure.test :as t])

(defn parens
  "seq of valid pairs of 'n parentheses"
  ([n]
   (parens n 0 []))
  ([opened closed acc]
   (concat
    (when (and (zero? opened) (zero? closed))
      [acc])
    (when (pos? opened)
      (parens (dec opened) (inc closed) (conj acc true)))
    (when (pos? closed)
      (parens opened (dec closed) (conj acc false))))))

(defn bools->parens [bs]
  (->> bs
       (map {true "(" false ")"})
       str/join))

(t/deftest test
  (t/are [n expected] (= expected (map bools->parens (parens n)))
    0 [""]
    1 ["()"]
    2 ["(())" "()()"]
    3 ["((()))" "(()())" "(())()" "()(())" "()()()"]))

(t/run-tests *ns*)
