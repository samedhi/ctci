(require '[clojure.test :as t])
(require '[clojure.zip :as zip])

(defn next [loc]
  (->> loc
       (iterate zip/next)
       rest
       (drop-while #(-> % zip/node nil?))
       first))

;; (defn insert [node value])

;; (defn find [node value])

;; (defn delete [node value])

;; [nil nil] insert-> [n nil]
;; [nil nil] append-> [nil n]
;; [o nil] insert-> [n o]
;; [nil o] append-> [o n]
;; [o1 o2] insert-> [n nil]
;; [o1 o2] append-> [nil n]


(defn node->loc
  "wraps a binary vector tree [Value Left Right] into a zipper"
  [node]
  (zip/zipper #(->> % rest (remove nil?) count pos?)
              rest
              (fn [[v] [y z :as children]]
                {:pre [(-> children count (<= 2))]}
                [v y z])
              node))

(defn get-random-node [node]
  (->> node
       node->loc
       (iterate next)
       (take-while (complement zip/end?))
       rand-nth
       zip/node))

(t/deftest test
  (t/are [node expected] (contains? expected (first (get-random-node node)))
    [1] #{1}
    [1 [2]] #{1 2}
    [1 nil [2 nil [3]]] #{1 2 3}
    [1 [2] [3]] #{1 2 3})
  (let [node [1 [2 [4]] [3 [5]]]
        size (* 10 1000)
        partition-size (/ size 5)
        [v w x y z :as result] (as-> (map (fn [_] (get-random-node node)) (range size)) $
                                 (group-by identity $)
                                 (reduce-kv (fn [m k v] (assoc m k (count v))) {} $)
                                 (vals $)
                                 (sort $))]
    (t/is
     (= 5 (count result))
     (<= (* partition-size 0.9) v w x y z (* partition-size 1.1)))))

(t/run-tests *ns*)

#_(-> [1 nil [2 nil [3]]]
     node->loc
     next
     zip/down
     (zip/replace [4])
     zip/up
     (zip/edit assoc 0 :x)
     ;; zip/up
     zip/left
     (zip/replace [:y])
     zip/root
     pr-str)
