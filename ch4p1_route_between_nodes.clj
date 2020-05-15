#!/usr/bin/env bb

(require '[clojure.test :as t])

;; Route Between Nodes: Give a directed graph and two nodes (S and E), design
;; an algorithm to find out whether there is a route from S to E.

(def empty-queue clojure.lang.PersistentQueue/EMPTY)

(defn bfs' [{:keys [graph q visited] :as m}]
  (lazy-seq
   (when-not (empty? q)
     (let [path (peek q)
           new-visited (->> path peek (conj visited))
           new-q (pop q)
           vertices (->> path peek graph (remove new-visited) (map #(conj path %)))]
       (cons
        path
        (bfs' {:graph graph
               :q (reduce conj new-q vertices)
               :visited new-visited})) ))))

(defn bfs
  "Return a lazy sequence of of paths from vertex in BF order"
  [graph vertex]
  (when (contains? graph vertex)
    (bfs' {:graph graph
           :q (conj empty-queue [vertex])
           :visited #{}})))

(defn route-between-nodes
  "True if there is a path from vertex s to e, else false"
  [graph s e]
  (->> (bfs graph s)
       (filter #(-> % peek (= e)))
       seq
       some?))

(t/deftest test
  (t/is (nil? (bfs {} 1)))
  (t/are [expected graph] (= expected (bfs graph 1))
    [[1]] {1 #{}}
    [[1]] {1 #{1}}
    [[1] [1 2] [1 3]] {1 [2 3]
                       2 []
                       3 []}
    [[1] [1 2] [1 2 3]] {1 [2]
                         2 [3]
                         3 []}
    [[1] [1 2] [1 2 3]] {1 [2]
                         2 [3]
                         3 []}
    [[1] [1 2] [1 2 3]] {1 [2]
                         2 [3]
                         3 [1]})
  (t/are [expected graph s e] (= expected (route-between-nodes graph s e))
    false {} nil nil
    true  {1 #{}} 1 1
    false {1 #{}} 1 nil
    true  {1 #{2}
           2 #{3}
           3 #{}} 1 3
    false {1 #{2}
           2 #{3}
           3 #{}} 3 1
    false {1 #{2}
           2 #{}
           3 #{2}} 1 3
    true  {1 #{2}
           2 #{1 3}
           3 #{2}} 1 3))

(t/run-tests *ns*)

(println (java.util.Date.))

;; dropped attempt at the bidirectional search

;; (defn route-between-nodes [graph b e]
;;   (println :route-between-nodes [graph b e])
;;   (loop [visited #{}
;;          [b-path :as bs] (bfs graph b)
;;          [e-path :as es] (bfs graph e)]
;;     (let [b (peek b-path)
;;           e (peek e-path)]
;;       (println visited b bs e es)
;;       (if (and (not-empty bs) (not-empty es))
;;         (if (or (contains? visited b)
;;                 (contains? visited e)
;;                 (= b e))
;;           true
;;           (recur (conj visited b e)
;;                  (rest bs)
;;                  (rest es)))
;;         false))))
