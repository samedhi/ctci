#!/usr/bin/env bb

(defn is-empty [vs]
  (empty? vs))

(defn my-peek [vs]
  (assert (not (is-empty vs)))
  (first vs))

(defn my-remove
  [vs]
  (assert (not (is-empty vs)))
  (-> vs reverse vec pop reverse vec))

(defn my-push [vs v]
  (conj vs v))

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
      (t/is (thrown? AssertionError (my-remove zero-stack))))
    (t/testing "Check non-empty stack functionality for "
      (t/is (= 1 (my-peek stack1)))
      (t/is (= zero-stack (my-remove stack1))))
    (t/testing "Test the peek functionality for"
      (t/is (= [1 2 3 4 5] (take 5 (map my-peek (iterate my-remove stack-asc-from-root)))))
      (t/is (= [5 4 3 2 1] (take 5 (map my-peek (iterate my-remove stack-des-from-root)))))
      (t/is (= [1 1 1 1 1] (take 5 (map my-peek (iterate my-remove stack-eq))))))
    (t/testing "Test underlying implemntation for "
      (t/is (= [1 2 3 4 5] stack-asc-from-root))
      (t/is (= [5 4 3 2 1] stack-des-from-root))
      (t/is (= [1 1 1 1 1] stack-eq))
      (t/is (= [1 2 3] (my-push [1 2] 3)))
      (t/is (= [2 3] (my-remove [1 2 3]))))))

(tests *ns*)
