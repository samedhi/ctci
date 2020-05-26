#!/usr/bin/env bb

(require '[clojure.test :as t])

#_(defn bst-sequence-rec [root acc]
    (let [[v l r] root
          new-acc (conj acc v)]
      (println (apply str (take (count acc) "  ")) :bst-sequence-rec :root root :acc acc :new-acc new-acc)
      (apply
       concat
       (cond
         (and (nil? l) (nil? r)) [[new-acc]]
         (nil? l) [(bst-sequence-rec r new-acc)]
         (nil? r) [(bst-sequence-rec l new-acc)]
         :else [(bst-sequence-rec l (conj new-acc (first r)))
                (bst-sequence-rec r (conj new-acc (first l)))]))))

(defn bst-sequence-rec
  "sequence of arrays that could have formed this node"
  [node]
  (let [[v l r] node]
    (map
     #(cons v %)
     (cond
       (and (nil? l) (nil? r)) [nil]
       (nil? l) (bst-sequence-rec r)
       (nil? r) (bst-sequence-rec l)
       :else
       (apply
        concat
        (for [l-seq (bst-sequence-rec l)
              r-seq (bst-sequence-rec r)]
          [(into (vec l-seq) r-seq)
           (into (vec r-seq) l-seq)]))))))

(defn bst-sequence [root]
  (bst-sequence-rec root))

(t/deftest test
  (t/are [root expected] (= expected (-> root bst-sequence set))
    [2] #{[2]}
    [2 [1]] #{[2 1]}
    [2 [1] [3]] #{[2 1 3] [2 3 1]}
    [2 nil [3]] #{[2 3]}
    [1 nil [2 nil [3]]] #{[1 2 3]}
    [3 [2 [1]]] #{[3 2 1]}
    [3 [2 [1]] [4]] #{[3 4 2 1] [3 2 1 4] [3 2 4 1]}
    ;; This isn't right, what about [3 4 2 1 5] for instance?
    [3 [2 [1]] [4 nil [5]]] #{[3 4 5 2 1] [3 2 1 4 5] [3 4 2 1 5] [3 2 4 1 5]}
    ))

(t/run-tests *ns*)
