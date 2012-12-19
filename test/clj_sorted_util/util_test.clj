(ns clj-sorted-util.util-test
  (:use clojure.test
        clj-sorted-util.util))

(deftest concat-one-element-collections
  (is (= (concat-sorted [[4] [2]]) [2 4]))
  (is (= (concat-sorted [[2] [4]]) [2 4])))

(deftest concat-two-element-collections
  (is (= (concat-sorted [[3 5] [2 4]]) [2 3 4 5]))
  (is (= (concat-sorted [[2 4] [3 5]]) [2 3 4 5])))

(deftest concat-uneven-number-of-elements
  (is (= (concat-sorted [[1 3 5] [4]]) [1 3 4 5])))

(deftest concat-with-duplicates
  (is (= (concat-sorted [[1 3] [1 3]]) [1 3]))
  (is (= (concat-sorted [[1 3 4] [1 3]]) [1 3 4]))
  (is (= (concat-sorted [[1 3] [1 3 4]]) [1 3 4])))

(deftest other-than-2-colls
  (is (= (concat-sorted [[1 4] [2 3] [0 9]]) [0 1 2 3 4 9]))
  (is (= (concat-sorted [[1 4]]) [1 4]))
  (is (= (concat-sorted []) [])))

(deftest other-order
  (is (= (concat-sorted > [[5 3 1] [4 0]]) [5 4 3 1 0])))

(deftest concat-sorted-by-collections
  (is (= (concat-sorted-by :age [[{:age 2 :who :joe} {:age 20 :who :tom}]
                                 [{:age 1 :who :dan} {:age 22 :who :foo}]])
         [{:age 1 :who :dan} {:age 2 :who :joe} {:age 20 :who :tom} {:age 22 :who :foo}])))
