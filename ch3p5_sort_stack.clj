#!/usr/bin/env bb

(defn is-empty [vs]
  (empty? vs))

(defn my-peek [vs]
  (assert (not (is-empty vs)))
  (peek vs))

(defn my-pop
  [vs]
  (assert (not (is-empty vs)))
  (pop vs))4

(defn my-push [vs v]
  ;; This is the obvious solution
  #_(vec (sort-by #(* -1 %) (conj vs v)))
  (let [smaller-vs (->> vs rseq (take-while #(< % v)) reverse)
        c (count smaller-vs)]
    (apply
     conj
     (subvec vs 0 (- (count vs) c))
     v
     smaller-vs)))

;; [6 5 3 2 1] <- 4
;; [6 5 3] [2 1] 4
;; -> [6 5 4 3 2 1]

(defn queue-stack []
  [])

(require '[clojure.test :as t])

(t/deftest my-test
  (let [zero-stack (queue-stack)
        stack1 (my-push zero-stack 1)
        stack-eq  (reduce my-push zero-stack [1 1 1 1 1])
        stack-asc-from-root (reduce my-push zero-stack [1 2 3 4 5])
        stack-des-from-root (reduce my-push zero-stack [5 4 3 2 1])]
    (t/testing "Check that exceptions are thrown on empty for "
      (t/is (thrown? AssertionError (my-peek zero-stack)))
      (t/is (thrown? AssertionError (my-pop zero-stack))))
    (t/testing "Check non-empty stack functionality for "
      (t/is (= 1 (my-peek stack1)))
      (t/is (= zero-stack (my-pop stack1))))
    (t/testing "Test the peek functionality for"
      (t/is (= [1 2 3 4 5] (take 5 (map my-peek (iterate my-pop stack-asc-from-root)))))
      (t/is (= [1 2 3 4 5] (take 5 (map my-peek (iterate my-pop stack-des-from-root)))))
      (t/is (= [1 1 1 1 1] (take 5 (map my-peek (iterate my-pop stack-eq))))))
    (t/testing "Test underlying implemntation for "
      (t/is (= [5 4 3 2 1] stack-asc-from-root))
      (t/is (= [5 4 3 2 1] stack-des-from-root))
      (t/is (= [1 1 1 1 1] stack-eq))
      (t/is (= [3 2 1] (my-push [2 1] 3)))
      (t/is (= [3 2] (my-pop [3 2 1]))))))

(t/run-tests *ns*)
