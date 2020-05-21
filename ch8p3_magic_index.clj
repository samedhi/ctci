#!/usr/bin/env bb

(require '[clojure.test :as t])

;; (defn magic-index [xs]
;;   (->> (map vector xs (range))
;;        (partition-by #(apply = %))
;;        (filter #(->> % first (apply =)))
;;        (map #(map first %))))

;; (and (= (first xs) i)
;;      (= (peek xs) (+ i c -1)))


;; QUESTION?: What about 
;; [0 X X X X X 13] -> [0 1 2 3 4 5 13]
;; [1 X X X X X 13] -> [0 1 2 3 4 5 13]
;; [N & NS] -> [1 2 3 4 5 6 13]

;; (defn magic-index' [xs i]
;;   (let [c (count xs)
;;         j (+ i c -1)
;;         f (first xs)
;;         l (peek xs)
;;         value-delta (- l f)
;;         index-delta (- j i)
;;         midpoint (quot c 2)
;;         midpoint-plus-i (+ midpoint i)]
;;     (cond
;;       (and (#{0 1} (count xs))
;;            (not= (first xs) i))
;;       nil

;;       ;; not possible
;;       (< value-delta index-delta)
;;       nil

;;       ;; Items that are incrementing & (-> % first (= i)) KEEP
;;       ;; Items that are incrementing & (-> % first (not= i)) DROP
;;       (= value-delta index-delta)
;;       xs

;;       (< index-delta value-delta)
;;       (concat
;;        (magic-index' (subvec xs 0 midpoint) midpoint-plus-i)
;;        (magic-index' (subvec xs midpoint) midpoint-plus-i)))))

(defn midpoint [xs]
  (quot (count xs) 2))

(defn magic-index? [xs i]
  (= i (nth xs i)))

(defn magic-index' [xs depth offset]
  
  (let [midpoint (quot (count xs) 2)
        f (first xs)
        l (peek xs)]
    ;; GOOD FOR DEBUGGING
    ;; (println (apply str (take depth (repeat "  ")))
    ;;          :magic-index' [xs offset f l])
    (cond
      (and (= f offset)
           (-> xs count dec (+ offset) (= l)))
      xs

      (= (- l f) (-> xs count dec))
      []

      (< offset f)
      []

      :else
      (concat
       (magic-index' (subvec xs 0 midpoint) (inc depth) offset)
       (magic-index' (subvec xs midpoint) (inc depth) (+ midpoint offset))))))

(defn magic-index [xs]
  (if (empty? xs)
    []
    (magic-index' xs 0 0)))

;; It should only return a magic-index, not all of them. Not going to
;; bother with that.
(t/deftest test
  (t/are [xs expected] (= expected (magic-index xs))
    [] []
    [1] []
    [0] [0]
    [0 2] [0]
    [-1 0 2 4 5 6 7 9] [2]
    [-1 0 2 4 5 6 7 9] [2]
    [-1 0 2 3 4 5 6 7 9 10 12] [2 3 4 5 6 7]))

(t/run-tests *ns*)

(println (java.util.Date.))
