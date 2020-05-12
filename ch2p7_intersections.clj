#!/usr/bin/env bb

;; I got tired of this nonsense. Not dealing with it as a problem right now 2020/05/11

(defn intersection [ls1 ls2]
  (let [[c1 c2] (map count [ls1 ls2])
        c-max (max c1 c2)
        [ls1' ls2'] (map #(take-last c-max %) [ls1 ls2])]
    (loop [list1 ls1'
           list2 ls2']
      (println :list1 list1 :list2 list2)
      (when (empty? list1)
        (if (identical? list1 list2)
          list1
          (recur
           (rest list1)
           (rest list2)))))))

(require '[clojure.test :as t])

(t/deftest my-test
  (t/testing "That we get nil if no intersection"
    (t/is
     (= nil (intersection (list 1) (list 2)))))
  (t/testing "That intersection is not based on value"
    (t/is
     (= nil (intersection (list 1 2 3) (list 1 2 3)))))
  (t/testing "That intersection is based on reference"
    (let [ls (list 1 2 3)]
      (t/is
       (identical? ls (intersection ls ls)))))
  (let [ls (list 1 2 3)
        sublist (rest ls)]
    (t/is
     (identical? sublist (intersection ls sublist)))
    (t/is
     (identical? sublist (intersection sublist ls))))
  (let [common (list 7 8 9)
        l1 (cons 1 (cons 2 (cons 3 common)))
        l2 (cons 4 (cons 5 (cons 6 common)))]
    (= common (intersection l1 l2))))

(t/run-tests *ns*)
