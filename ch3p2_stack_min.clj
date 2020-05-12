#!/usr/bin/env bb

(defn is-empty [stack]
  (empty? stack))

(defn stack-peek [stack]
  (assert (-> stack count pos?))
  (-> stack peek :value))

(defn stack-min [stack]
  (assert (-> stack count pos?))
  (-> stack peek :minimum))

(defn stack-pop
  [stack]
  (assert (-> stack count pos?))
  (pop stack))

(defn stack-push [stack v]
  (try
    (let [minimum (stack-min stack)]
      (conj stack
            {:value v :minimum (min minimum v)}))
    (catch AssertionError e
      [{:value v :minimum v}])))

(require '[clojure.test :as t])

(t/deftest my-test
  (let [zero-stack []
        stack1 (stack-push zero-stack 1)
        stack-eq  (reduce stack-push zero-stack [1 1 1 1 1])
        stack-asc-from-root (reduce stack-push zero-stack [1 2 3 4 5])
        stack-des-from-root (reduce stack-push zero-stack [5 4 3 2 1])]
    (t/testing "Check that exceptions are thrown on empty for "
      (t/is (thrown? AssertionError (stack-peek zero-stack)))
      (t/is (thrown? AssertionError (stack-min zero-stack)))
      (t/is (thrown? AssertionError (stack-pop zero-stack))))
    (t/testing "Check non-empty stack functionality for "
      (t/is (= 1 (stack-peek stack1)))
      (t/is (= 1 (stack-min stack1)))
      (t/is (= [] (stack-pop stack1))))
    (t/testing "Test the min functionality for"
      (t/is (= [1 1 1 1 1] (take 5 (map stack-min (iterate stack-pop stack-asc-from-root)))))
      (t/is (= [1 2 3 4 5] (take 5 (map stack-min (iterate stack-pop stack-des-from-root)))))
      (t/is (= [1 1 1 1 1] (take 5 (map stack-min (iterate stack-pop stack-eq))))))
    (t/testing "Test the peek functionality for"
      (t/is (= [5 4 3 2 1] (take 5 (map stack-peek (iterate stack-pop stack-asc-from-root)))))
      (t/is (= [1 2 3 4 5] (take 5 (map stack-peek (iterate stack-pop stack-des-from-root)))))
      (t/is (= [1 1 1 1 1] (take 5 (map stack-peek (iterate stack-pop stack-eq))))))))

(t/run-tests *ns*)

;; Down a rabbit hole...

;; (defn push [stack-min v]
;;   (let [{:keys [minimum] :as m} (meta stack-min)]
;;     (with-meta
;;       (conj stack-min v)
;;       {:minimum (if minimum (min minimum v) v)})))

;; (defn find-min [new-stack old-min]
;;   (let [eq-mins (filter #(= % old-min) new-stack)]
;;     (if (first eq-mins)
;;       old-min
;;       (apply + new-stack))))

;; (defn pop [stack-min]
;;   (assert (-> stack-min count pos?))
;;   (let [{:keys [minimum] :as m} (meta stack-min)
;;         v (peek stack-min)
;;         new-stack-min (pop stack-min)]
;;     (cond
;;       (zero? new-stack-min)
;;       (with-meta new-stack-min {})

;;       (= v minimum)
;;       (with-meta
;;         new-stack-min
;;         {:minimum (find-min new-stack-min minimum)})

;;       :else new-stack-min)))

;; [{:value <Int> :minimum <Int>}]
