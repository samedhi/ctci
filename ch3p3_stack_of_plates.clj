#!/usr/bin/env bb

(def capacity 3)

(defn is-empty [sstack]
  (= sstack [[]]))

(defn stack-peek [sstack]
  (assert (not (is-empty sstack)))
  (-> sstack peek peek))

(defn stack-pop
  [sstack]
  (assert (not (is-empty sstack)))
  (let [new-last-stack (-> sstack peek pop)]
    (cond
      (and (-> sstack count (= 1))
           (-> sstack peek count (= 1)))
      [[]]

      (empty? new-last-stack)
      (pop sstack)

      :else (conj (pop sstack) new-last-stack))))

(defn stack-pop-at [sstack i]
  (assert (not (is-empty sstack)))
  (if (-> sstack (nth i) count (= 1))
    (into
     (stack-pop (subvec sstack 0 (inc i)))
     (subvec sstack (inc i)))
    (update sstack i pop)))

(defn stack-push [sstack v]
  (if (-> sstack peek count (= capacity))
    (conj sstack [v])
    (update sstack (-> sstack count dec) conj v)))

(defn stack-of-plates []
  [[]])

(require '[clojure.test :as t])

(t/deftest my-test
  (let [zero-stack (stack-of-plates)
        stack1 (stack-push zero-stack 1)
        stack-eq  (reduce stack-push zero-stack [1 1 1 1 1])
        stack-asc-from-root (reduce stack-push zero-stack [1 2 3 4 5])
        stack-des-from-root (reduce stack-push zero-stack [5 4 3 2 1])]
    (t/testing "Check that exceptions are thrown on empty for "
      (t/is (thrown? AssertionError (stack-peek zero-stack)))
      (t/is (thrown? AssertionError (stack-pop zero-stack))))
    (t/testing "Check non-empty stack functionality for "
      (t/is (= 1 (stack-peek stack1)))
      (t/is (= zero-stack (stack-pop stack1))))
    (t/testing "Test the peek functionality for"
      (t/is (= [5 4 3 2 1] (take 5 (map stack-peek (iterate stack-pop stack-asc-from-root)))))
      (t/is (= [1 2 3 4 5] (take 5 (map stack-peek (iterate stack-pop stack-des-from-root)))))
      (t/is (= [1 1 1 1 1] (take 5 (map stack-peek (iterate stack-pop stack-eq))))))
    (t/testing "Test underlying implemntation for "
      (t/is (= [[1 2 3] [4 5]] stack-asc-from-root))
      (t/is (= [[5 4 3] [2 1]] stack-des-from-root))
      (t/is (= [[1 1 1] [1 1]] stack-eq)))
    (t/testing "Test pop-at for "
      (t/is (= [[1 2] [4 5]] (stack-pop-at stack-asc-from-root 0)))
      (t/is (= [[5 4 3]] (stack-pop-at [[5 4 3] [2]] 1)))
      (t/is (= [[5 4 3] [1]] (stack-pop-at [[5 4 3] [2] [1]] 1)))
      (t/is (= [[]] (stack-pop-at [[1]] 0))))))

(t/run-tests *ns*)
