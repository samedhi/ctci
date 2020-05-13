#!/usr/bin/env bb

(require '[clojure.test :as t])

(defn queue []
  {:values {} :min-key 0 :max-key -1})

(defn is-empty [q]
  (-> q :values empty?))

(defn my-peek [q]
  (assert (not (is-empty q)))
  (let [{:keys [min-key values]} q]
    (get values min-key)))

(defn reset-queue-on-empty [q]
  (if (is-empty q)
    (queue)
    q))

(defn my-remove
  [q]
  (assert (not (is-empty q)))
  (let [{:keys [min-key]} q]
    (-> q
        (update :values dissoc min-key)
        (update :min-key inc)
        reset-queue-on-empty)))

(defn my-push [q v]
  (let [{:keys [max-key]} q
        next-key (inc max-key)]
    (-> q
        (update :values assoc next-key v)
        (assoc :max-key next-key)
        reset-queue-on-empty)))

#_(t/deftest queue-test
  (let [zero-queue (queue)
        queue1 (my-push zero-queue 1)
        queue-eq  (reduce my-push zero-queue [1 1 1 1 1])
        queue-asc-from-root (reduce my-push zero-queue [1 2 3 4 5])
        queue-des-from-root (reduce my-push zero-queue [5 4 3 2 1])]
    (t/testing "Check that exceptions are thrown on empty for "
      (t/is (thrown? AssertionError (my-peek zero-queue)))
      (t/is (thrown? AssertionError (my-remove zero-queue))))
    (t/testing "Check non-empty queue functionality for "
      (t/is (= false (is-empty queue1)))
      (t/is (= 1 (my-peek queue1)))
      (t/is (= zero-queue (my-remove queue1))))
    (t/testing "Test the peek functionality for"
      (t/is (= [1 2 3 4 5] (take 5 (map my-peek (iterate my-remove queue-asc-from-root)))))
      (t/is (= [5 4 3 2 1] (take 5 (map my-peek (iterate my-remove queue-des-from-root)))))
      (t/is (= [1 1 1 1 1] (take 5 (map my-peek (iterate my-remove queue-eq))))))))

;; helter shelter

(defn enqueue [shelter pet]
  (let [{current-tick :tick} shelter
        ticked-pet (assoc pet :tick current-tick)]
    (-> shelter
        (update (:kind pet) my-push ticked-pet)
        (update :tick inc))))

(defn dequeue-breed [shelter specie]
  {:pet (-> shelter (get specie) my-peek)
   :shelter (update shelter specie my-remove)})

(defn dequeue-cats [shelter]
  (dequeue-breed shelter :cats))

(defn dequeue-dogs [shelter]
  (dequeue-breed shelter :dogs))

(defn dequeue [shelter]
  (let [tick-cat (try (-> shelter :cats my-peek :tick) (catch AssertionError e))
        tick-dog (try (-> shelter :dogs my-peek :tick) (catch AssertionError e))]
    (cond
      (= tick-cat tick-dog nil) (throw (AssertionError. "No Pets at shelter"))

      (nil? tick-cat) (dequeue-dogs shelter)

      (nil? tick-dog) (dequeue-cats shelter)

      (< tick-cat tick-dog) (dequeue-cats shelter)

      :else (dequeue-dogs shelter))))

(defn shelter []
  {:tick 0 :dogs (queue) :cats (queue)})

(defmacro test-is [$ & body]
  `(do (t/is ~@body)
       ~$))

(t/deftest shelter-test
  (t/is (= (shelter) {:tick 0
                      :dogs {:values {} :min-key 0 :max-key -1}
                      :cats {:values {} :min-key 0 :max-key -1}}))
  (as-> (shelter) $
    (enqueue $ {:kind :cats :name "whiskers-1"})
    (test-is $ (= 1 (:tick $)))
    (test-is $ (= 1 (-> $ :cats :values count)))
    (test-is $ (= "whiskers-1" (-> $ :cats my-peek :name)))
    (dequeue $)
    (:shelter $)
    (test-is $ (= 1 (:tick $)))
    (test-is $ (= 0 (-> $ :cats :values count)))
    (enqueue $ {:kind :cats :name "whiskers-2"})
    (enqueue $ {:kind :cats :name "whiskers-3"})
    (enqueue $ {:kind :dogs :name "fido-1"})
    (dequeue $)
    (test-is $ (= "whiskers-2" (-> $ :pet :name)))
    (:shelter $)
    (dequeue-cats $)
    (test-is $ (= "whiskers-3" (-> $ :pet :name)))
    (:shelter $)
    (test-is $ (-> $ :cats is-empty))
    (dequeue $)
    (test-is $ (= "fido-1" (-> $ :pet :name)))
    (:shelter $)
    (test-is $ (true? (-> $ :dogs is-empty)))
    (test-is $ (= 4 (:tick $)))))

(t/run-tests *ns*)
