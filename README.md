# Cracking The Coding Interview in Clojure

My solution to "Cracking the Coding Interview" (6th Edition) by Gayle Laakmann McDowell. I attempt to do the problem as stated, but often like to add my own functional/immutable/lazy-seq twist to the problem.

## Running the Scripts

I am doing the problems in Clojure using [babashka](https://github.com/borkdude/babashka). [Installation is easy](https://github.com/borkdude/babashka#quickstart) with [babashka](https://github.com/borkdude/babashka) and there are no other external dependencies.


Once installed, run any of the scripts with:

```
> bb <name_of_script.clj>
```

It will print out the result of the unit test (at the bottom of the script). Something like

```
> bb ch8p2_robot_in_a_grid.clj 

Testing user

Ran 1 tests containing 7 assertions.
0 failures, 0 errors.
#inst "2020-05-21T16:55:46.428-00:00"
```

## Recordings

I have recorded every single problem as I worked through it. Currently it is just a giant collection of videos on [Twitch](https://www.twitch.tv/samedhi/). However, I have recorded every video on my personal computer and will split them by problem soon.

[Twitch Playlist of "Cracking the Coding Interview" in Clojure.](https://www.twitch.tv/collections/okilnEUbERYSLQ)

## Iterative Development with `entr`

I usually run the script continuously while I edit it. I use [entr](http://eradman.com/entrproject/) to rerun the script on every save (in less than 1/100th of a second). You can pass to `entr` the last touched script using the following `ls -t | head -n1 | entr -c bb /_`. 

## General Format of Scripts

```clj
#!/usr/bin/env bb
;; NOTE: The above line lets you run this as a script if you make it executable.

;; NOTE: I put the implementation at the top
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

;; NOTE: I then have 1 or more unit test. Note that babashka includes clojure.test for you.
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

;; NOTE: This runs the test as the *output* of the script
(t/run-tests *ns*)
```
